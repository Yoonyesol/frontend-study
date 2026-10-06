import { useEffect, useState } from 'react'
import { fetchMe } from './api.js'
import LoginPage from './LoginPage.jsx'
import HomePage from './HomePage.jsx'

export default function App() {
  const [user, setUser] = useState(null)
  const [checking, setChecking] = useState(true)

  // 새로고침해도 세션이 살아 있으면 로그인 상태를 유지한다.
  // Promise를 return 해서 LoginPage에서도 .then()으로 이어 쓸 수 있게 한다.
  const loadUser = () => {
    return fetchMe()
      .then((me) => setUser(me))
      .finally(() => setChecking(false))
  }

  useEffect(() => {
    loadUser().catch((err) => console.error(err))
  }, [])

  if (checking) {
    return <main className="page"><p className="muted">로그인 상태 확인 중…</p></main>
  }

  return (
    <main className="page">
      {user
        ? <HomePage user={user} onLogout={() => setUser(null)} />
        : <LoginPage onLogin={loadUser} />}
    </main>
  )
}
