import { useEffect, useState } from 'react'
import { fetchMe, handleOAuthCallback } from './api.js'
import LoginPage from './LoginPage.jsx'
import HomePage from './HomePage.jsx'

export default function App() {
  const [user, setUser] = useState(null)
  const [checking, setChecking] = useState(true)
  const [oauthError, setOauthError] = useState(null)

  // 저장된 토큰으로 사용자 정보를 불러온다. (새로고침해도 로그인 유지)
  const loadUser = () => {
    return fetchMe()
      .then((me) => setUser(me))
      .finally(() => setChecking(false))
  }

  useEffect(() => {
    // 1) Google 로그인에서 돌아온 경우: 주소의 #token=... 을 저장
    const { error } = handleOAuthCallback()
    if (error) setOauthError(error)

    // 2) 토큰이 있으면 /api/me 호출 (아이디 로그인·Google 로그인 모두 같은 흐름)
    loadUser().catch((err) => console.error(err))
  }, [])

  if (checking) {
    return <main className="page"><p className="muted">로그인 상태 확인 중…</p></main>
  }

  return (
    <main className="page">
      {user
        ? <HomePage user={user} onLogout={() => setUser(null)} />
        : <LoginPage onLogin={loadUser} initialError={oauthError} />}
    </main>
  )
}
