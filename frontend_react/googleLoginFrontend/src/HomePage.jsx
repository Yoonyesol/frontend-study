import { useState } from 'react'
import { callAdminApi, logout, tokenStore } from './api.js'

export default function HomePage({ user, onLogout }) {
  const isAdmin = user.roles.includes('ROLE_ADMIN')
  const isGoogle = user.loginType === 'GOOGLE'
  const hasPhoto = typeof user.profileImg === 'string' && user.profileImg.startsWith('https://')
  const [adminResult, setAdminResult] = useState(null)

  const handleLogout = () => {
    logout()
      .then(() => onLogout())
  }

  const handleAdminCall = () => {
    callAdminApi()
      .then((result) => setAdminResult(result))
      .catch(() => setAdminResult({ status: '오류', body: '서버에 연결할 수 없습니다.' }))
  }

  return (
    <section className="card">
      <div className="profile">
        {hasPhoto
          ? <img src={user.profileImg} alt="" referrerPolicy="no-referrer" />
          : <div className="avatar">{user.memNm.slice(0, 1)}</div>}
        <div>
          <h1>{user.memNm}님, 환영합니다</h1>
          <p className="muted">아이디: {user.memId}</p>
        </div>
      </div>

      <p>
        <span className={isAdmin ? 'badge admin' : 'badge'}>{isAdmin ? '관리자' : '일반 사용자'}</span>{' '}
        <span className="badge">{isGoogle ? 'Google 로그인' : '아이디 로그인'}</span>
      </p>

      <details className="token">
        <summary>저장된 토큰 보기</summary>
        <code>{tokenStore.get()}</code>
        <p className="muted">Google 로그인이어도 우리 서버가 발급한 JWT입니다. jwt.io에서 sub를 확인해 보세요.</p>
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
