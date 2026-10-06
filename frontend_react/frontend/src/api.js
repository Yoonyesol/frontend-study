// Spring Security와 통신하는 함수 모음 (fetch().then() 방식)
// fetch는 같은 출처 요청에 쿠키(JSESSIONID, XSRF-TOKEN)를 자동으로 함께 보낸다.
// 각 함수는 Promise를 return 하므로, 호출하는 쪽에서도 .then() / .catch()로 이어 쓴다.

function getCookie(name) {
  const match = document.cookie.match(new RegExp('(?:^|; )' + name + '=([^;]*)'))
  return match ? decodeURIComponent(match[1]) : null
}

// POST 요청 전에 CSRF 토큰을 준비한다.
// 1) /api/csrf 호출 → 서버가 XSRF-TOKEN 쿠키를 내려줌
// 2) 쿠키 값을 X-XSRF-TOKEN 헤더에 담아 보냄 → 서버가 쿠키와 헤더를 비교
// 로그인 성공 시 토큰이 새로 바뀌므로 매번 다시 받아 온다.
function csrfHeader() {
  return fetch('/api/csrf')
    .then(() => ({ 'X-XSRF-TOKEN': getCookie('XSRF-TOKEN') ?? '' }))
}

// 로그인: Spring Security 폼 로그인은 JSON이 아니라 form-urlencoded로 받는다.
export function login(memId, password) {
  return csrfHeader()
    .then((header) =>
      fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          ...header,
        },
        body: new URLSearchParams({ memId, password }),
      }),
    )
    .then((res) => {
      if (!res.ok) {
        throw new Error('아이디 또는 비밀번호가 올바르지 않습니다.')
      }
    })
}

// 현재 로그인한 사용자 조회: 로그인 전이면 null
export function fetchMe() {
  return fetch('/api/me')
    .then((res) => {
      if (res.status === 401) return null
      if (!res.ok) throw new Error('사용자 정보를 불러오지 못했습니다.')
      return res.json()          // res.json()도 Promise → 다음 .then()으로 결과 전달
    })
}

// 로그아웃: 반드시 POST + CSRF 헤더
export function logout() {
  return csrfHeader()
    .then((header) =>
      fetch('/api/logout', {
        method: 'POST',
        headers: header,
      }),
    )
}
