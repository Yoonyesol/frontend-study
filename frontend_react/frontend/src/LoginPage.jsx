import { useState } from 'react'
import { login } from './api.js'

export default function LoginPage({ onLogin }) {
  const [memId, setMemId] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    login(memId, password)
      .then(() => onLogin())        // 로그인 성공 → /api/me로 사용자 정보 다시 조회
      .catch((err) => {             // 로그인 실패(401) 또는 네트워크 오류
        setError(err.message)
        setPassword('')
      })
      .finally(() => setLoading(false))
  }

  return (
    <form className="card" onSubmit={handleSubmit}>
      <h1>로그인</h1>

      <label>
        아이디
        <input
          value={memId}
          onChange={(e) => setMemId(e.target.value)}
          autoComplete="username"
          autoFocus
          required
        />
      </label>

      <label>
        비밀번호
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          autoComplete="current-password"
          required
        />
      </label>

      {error && <p className="error">{error}</p>}

      <button type="submit" disabled={loading}>
        {loading ? '로그인 중…' : '로그인'}
      </button>

      <p className="muted">실습 계정: kim / 1234 (일반), lee / 1234 (관리자)</p>
    </form>
  )
}
