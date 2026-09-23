import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function BoardList() {
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;
  const [boards, setBoards] = useState([]);
  const nav = useNavigate();

  useEffect(() => {
    fetch(`${backendDomain}/boards`)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setBoards(data);
      });
  }, [backendDomain]);

  return (
    <div>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "12px",
        }}
      >
        <h2>게시판 목록</h2>
        <button onClick={() => nav("/board/edit")}>글쓰기</button>
      </div>

      <table>
        <thead>
          <tr>
            <th>글번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {boards.map((it, idx) => (
            <tr
              key={it.id}
              onClick={() => nav(`/board/${it.id}`)}
              style={{ cursor: "pointer" }}
            >
              <td>{idx + 1}</td>
              <td>{it.title}</td>
              <td>{it.mem_id}</td>
              <td>{it.reg_dtm}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
