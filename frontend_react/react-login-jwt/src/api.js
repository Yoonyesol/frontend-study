// Spring Security(JWT)와 통신하는 함수 모음 (fetch().then() 방식)
//
// 세션 방식과 달라진 점
// - 서버가 쿠키를 쓰지 않으므로 CSRF 토큰(/api/csrf, X-XSRF-TOKEN)이 필요 없다.
// - 로그인 응답으로 받은 accessToken을 브라우저에 저장하고,
//   이후 모든 요청의 Authorization 헤더에 "Bearer <토큰>"으로 직접 붙인다.
// - 로그아웃은 서버 호출 없이 저장한 토큰을 지우는 것으로 끝난다.

const TOKEN_KEY = 'accessToken'

// 토큰 저장소: 실습에서는 localStorage 사용
// (localStorage는 JavaScript로 읽을 수 있어 XSS에 취약하다. 운영에서는 수명을 짧게 두거나
//  Refresh Token을 HttpOnly 쿠키로 분리하는 방식을 검토한다.)
export const tokenStore = {
  get: () => localStorage.getItem(TOKEN_KEY),
  set: (token) => localStorage.setItem(TOKEN_KEY, token),
  clear: () => localStorage.removeItem(TOKEN_KEY),
}

// 토큰이 있으면 Authorization 헤더를 붙여 요청하는 공통 함수
function authFetch(url, options = {}) {
  const token = tokenStore.get()
  const headers = { ...(options.headers ?? {}) }
  if (token) {
    headers.Authorization = `Bearer ${token}`
  }
  return fetch(url, { ...options, headers })
}

// 로그인: JWT 로그인 API는 컨트롤러(@RequestBody)가 받으므로 JSON으로 보낸다.
// 서버의 LoginRequest(username, password)와 필드 이름을 맞춘다.
export function login(memId, password) {
  return fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: memId, password }),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error('아이디 또는 비밀번호가 올바르지 않습니다.')
      }
      return res.json()                       // { accessToken: "eyJ..." }
    })
    .then((data) => tokenStore.set(data.accessToken))
}

// 현재 로그인한 사용자 조회: 토큰이 없거나 만료·위조되었으면 null
export function fetchMe() {
  if (!tokenStore.get()) {
    return Promise.resolve(null)              // 토큰이 없으면 서버에 묻지 않음
  }
  return authFetch('/api/me')
    .then((res) => {
      if (res.status === 401) {               // 만료된 토큰 → 지우고 로그인 화면으로
        tokenStore.clear()
        return null
      }
      if (!res.ok) throw new Error('사용자 정보를 불러오지 못했습니다.')
      return res.json()
    })
}

// 관리자 API 호출: 권한에 따라 200 / 403, 토큰 문제면 401
export function callAdminApi() {
  return authFetch('/api/admin/hello')
    .then((res) => res.text().then((body) => ({ status: res.status, body })))
}

// 로그아웃: 서버에 세션이 없으므로 클라이언트의 토큰만 지운다.
// 다른 함수들과 사용법을 맞추려고 Promise를 반환한다.
export function logout() {
  tokenStore.clear()
  return Promise.resolve()
}
