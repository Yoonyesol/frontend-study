import { useState } from 'react'
import { callAdminApi, logout, tokenStore } from './api.js'

export default function HomePage({ user, onLogout }) {
  const isAdmin = user.roles.includes('ROLE_ADMIN')
  const [adminResult, setAdminResult] = useState(null)

  const handleLogout = () => {
    logout()
      .then(() => onLogout())
  }

  // 같은 버튼을 kim(일반)과 lee(관리자)로 눌러 403 / 200을 비교한다.
  const handleAdminCall = () => {
    callAdminApi()
      .then((result) => setAdminResult(result))
      .catch(() => setAdminResult({ status: '오류', body: '서버에 연결할 수 없습니다.' }))
  }

  return (
    <section className="card">
      <h1>{user.memNm}님, 환영합니다</h1>
      <p className="muted">아이디: {user.memId}</p>
      <p>
        권한: <span className={isAdmin ? 'badge admin' : 'badge'}>{isAdmin ? '관리자' : '일반 사용자'}</span>
      </p>

      <details className="token">
        <summary>저장된 토큰 보기</summary>
        <code>{tokenStore.get()}</code>
        <p className="muted">jwt.io에 붙여 넣으면 Payload(sub, roles, exp)를 읽을 수 있습니다.</p>
      </details>

      <button type="button" className="secondary" onClick={handleAdminCall}>
        관리자 API 호출
      </button>
      {adminResult && (
        <p className={adminResult.status === 200 ? 'ok' : 'error'}>
          {adminResult.status} {adminResult.status === 403 ? '권한 없음' : adminResult.body}
        </p>
      )}

      <button type="button" onClick={handleLogout}>로그아웃</button>
    </section>
  )
}
