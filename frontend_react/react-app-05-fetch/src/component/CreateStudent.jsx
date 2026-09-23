import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function CreateStudent() {
  const [name, setName] = useState("");
  const [grade, setGrade] = useState("");
  const navigate = useNavigate();
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;

  const onSubmit = (e) => {
    e.preventDefault();

    // Json 서버는 id 값이 랜덤으로 생성되므로 id는 전송하지 않음
    fetch(`${backendDomain}/students`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: name,
        grade: Number(grade),
      }),
    }).then((res) => {
      if (res.ok) {
        alert("등록이 완료되었습니다.");
        // 등록 완료 후 전체 학생 목록 화면으로 이동
        navigate("/");
      }
    });
  };

  return (
    <div>
      <h2>학생 등록</h2>
      <form onSubmit={onSubmit}>
        <div style={{ marginBottom: "10px" }}>
          <label>이름: </label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        <div style={{ marginBottom: "10px" }}>
          <label>학년: </label>
          <input
            type="number"
            value={grade}
            onChange={(e) => setGrade(e.target.value)}
            required
          />
        </div>
        <button type="submit">등록</button>{" "}
        <Link to="/">취소</Link>
      </form>
    </div>
  );
}
