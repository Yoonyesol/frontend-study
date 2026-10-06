# springSecurity — JWT 버전 (모듈 4)

Spring Boot 4.1 · Spring Security 7 · Oracle(tb_mem) · JWT(jjwt 0.12.6)

## 실행 순서

1. SQL Developer에서 `src/main/resources/schema-oracle.sql` 실행 (tb_mem 생성 + kim/lee 계정)
2. `application.yml`의 DB 접속 정보 확인 (서비스명 `XEPDB1`, 계정 scott)
3. IntelliJ에서 프로젝트 열기 → Gradle 새로고침 → `SpringSecurityApplication` 실행
4. React 화면: `react-login-jwt/frontend`에서 `npm install` → `npm run dev` → http://localhost:5173

Gradle Wrapper(`gradlew`)는 포함하지 않았습니다. IntelliJ가 자동으로 Gradle을 맞춰 주며,
기존 Initializr 프로젝트가 있다면 `src` 폴더와 `build.gradle`의 dependencies만 옮겨도 됩니다.

## 패키지 구조

```text
com.springfw.springSecurity
├─ SpringSecurityApplication
├─ config/   SecurityConfig            JWT 필터 체인, PasswordEncoder, RoleHierarchy
├─ auth/     JwtProvider               토큰 발급·검증
│            JwtAuthenticationFilter   매 요청 토큰 검사 → SecurityContext
│            AuthController            POST /api/auth/login, GET /api/me
├─ member/   MemberRepository
│  ├─ entity/     Member              tb_mem 매핑
│  ├─ service/    CustomUserDetailsService, MemberService
│  └─ controller/ MemberController(/signup), MemberApiController(@PreAuthorize), SignupRequest
├─ api/      HelloController(/api/hello), AdminController(/api/admin/hello)
└─ common/   GlobalExceptionHandler    409 중복 아이디, 400 입력값 오류
```

## API

| 메서드 | 경로 | 토큰 | 결과 |
| --- | --- | --- | --- |
| POST | /signup | 불필요 | 201, 아이디 중복 409 |
| POST | /api/auth/login | 불필요 | 200 `{accessToken}`, 실패 401 |
| GET | /api/me | 필요 | 200 `{memId, memNm, roles}` |
| GET | /api/hello | 필요 | 200 |
| GET | /api/members/{memId} | 필요 | 본인 또는 관리자 200, 그 외 403 |
| GET | /api/members | 필요 | 관리자 200, 일반 403 |
| GET | /api/admin/hello | 필요 | 관리자 200, 일반 403 |

## 테스트 (HTTPie)

```bash
http :8080/api/hello                                            # 401 (토큰 없음)
http POST :8080/api/auth/login username=kim password=1234       # 토큰 발급
http :8080/api/hello "Authorization:Bearer <kim 토큰>"           # 200
http :8080/api/admin/hello "Authorization:Bearer <kim 토큰>"     # 403
http :8080/api/members/lee "Authorization:Bearer <kim 토큰>"     # 403 (본인 아님)
http POST :8080/api/auth/login username=lee password=1234
http :8080/api/admin/hello "Authorization:Bearer <lee 토큰>"     # 200
http POST :8080/signup memId=park memNm=박지성 password=1234      # 201
```

## 자주 막히는 곳

| 증상 | 원인 |
| --- | --- |
| 시작 시 `WeakKeyException` | `jwt.secret`이 32바이트 미만 |
| 시작 시 `Failed to configure a DataSource` | yml에서 `datasource`가 `spring:` 아래가 아님 (들여쓰기) |
| 로그인 시 `There is no PasswordEncoder mapped for the id "null"` | DB 비밀번호에 `{noop}`/`{bcrypt}` 접두어 없음 |
| 회원가입 시 `ORA-12899` | `pwd` 컬럼이 100자보다 짧음 |
| 토큰을 넣었는데 항상 401 | `Bearer ` 뒤 공백 누락, 토큰 만료, 서버 재시작 후 비밀키 변경 |
