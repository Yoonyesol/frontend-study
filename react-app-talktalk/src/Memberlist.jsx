import { useEffect, useState } from 'react';

const MEM_CD_LABEL = { 0: '일반 회원', 1: '관리자' };

export default function MemberList() {
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;
  const [members, setMembers] = useState([]);   // 회원 목록
  const [loading, setLoading] = useState(true); // 로딩 중 여부
  const [error, setError] = useState('');       // 오류 메시지

  useEffect(() => {
    // GET /api/members  (vite proxy → http://localhost:8080)
    fetch(`${backendDomain}/api/members`)
      .then((res) => {
        // fetch 는 404, 500 이어도 예외를 던지지 않음 → 직접 확인
        if (!res.ok) {
          throw new Error(`목록을 불러오지 못했습니다. (${res.status})`);
        }
        return res.json(); // Promise 를 return 해야 다음 then 이 결과를 받음
      })
      .then((data) => {
        setMembers(data); // Response[] 배열
      })
      .catch((err) => {
        // 네트워크 자체가 끊기면 TypeError('Failed to fetch')
        // (개발 중 Spring 서버만 꺼져 있으면 vite proxy 가 500 을 돌려줌 → 위 res.ok 에서 처리)
        setError(err instanceof TypeError ? '서버에 연결할 수 없습니다.' : err.message);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []); // [] → 처음 화면에 나타날 때 한 번만 실행

  if (loading) return <p className="message">불러오는 중…</p>;
  if (error) return <p className="message error">{error}</p>;

  return (
    <div className="container">
      <h1>회원 목록</h1>

      {members.length === 0 ? (
        <p className="message">등록된 회원이 없습니다.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>아이디</th>
              <th>이름</th>
              <th>구분</th>
              <th>프로필</th>
            </tr>
          </thead>
          <tbody>
            {members.map((m) => (
              <tr key={m.memId}>
                <td>{m.memId}</td>
                <td>{m.memNm}</td>
                <td>{MEM_CD_LABEL[m.memCd] ?? m.memCd}</td>
                <td>
                  {m.profileImg
                    ? <img src={m.profileImg} alt={m.memNm} className="avatar" />
                    : '-'}
                </td>
              </tr>
            ))}
            
          </tbody>
        </table>
        
      )}

      <p className="count">총 {members.length}명</p>
    </div>
  );
}
