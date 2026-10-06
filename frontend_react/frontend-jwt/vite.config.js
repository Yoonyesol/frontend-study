import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// /api 로 시작하는 요청을 Spring Boot(8080)로 전달하는 개발용 프록시
// 브라우저 입장에서는 같은 출처(localhost:5173)로 요청하므로 CORS 설정이 필요 없고,
// 세션 쿠키(JSESSIONID)와 CSRF 쿠키(XSRF-TOKEN)도 그대로 주고받을 수 있다.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
