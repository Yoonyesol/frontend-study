import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// /api 로 시작하는 요청을 Spring Boot(8080)로 전달하는 개발용 프록시
// 브라우저 입장에서는 같은 출처(localhost:5173)로 요청하므로 CORS 설정이 필요 없다.
// (프록시 없이 8080을 직접 호출하려면 서버에 CORS 설정과 Authorization 헤더 허용이 필요)
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
