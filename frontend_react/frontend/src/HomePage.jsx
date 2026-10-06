import { logout } from './api.js'

export default function HomePage({ user, onLogout }) {
  const isAdmin = user.roles.includes('ROLE_ADMIN')

  const handleLogout = () => {
    logout()
      .then(() => onLogout())
      .catch((err) => console.error('로그아웃 실패', err))
  }

  return (
    <section className="card">
      <h1>{user.memNm}님, 환영합니다</h1>
      <p className="muted">아이디: {user.memId}</p>
      <p>
        권한: <span className={isAdmin ? 'badge admin' : 'badge'}>{isAdmin ? '관리자' : '일반 사용자'}</span>
      </p>
      <button type="button" onClick={handleLogout}>로그아웃</button>
    </section>
  )
}
