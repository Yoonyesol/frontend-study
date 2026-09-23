import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const [mem_id, setMemId] = useState("");
  const [pw, setPw] = useState("");

  const nav = useNavigate();
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;

  const join = () => {
    fetch(`${backendDomain}/mem`)
      .then((res) => res.json())
      .then((data) => {
        const isDuplicate =
          Array.isArray(data) &&
          data.some(
            (m) => m.mem_id && String(m.mem_id).trim() === mem_id.trim()
          );

        if (isDuplicate) {
          alert("이미 회원가입된 아이디입니다.");
          return;
        }

        fetch(`${backendDomain}/mem`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ mem_id: mem_id.trim(), pwd: pw }),
        })
          .then((res) => res.json())
          .then(() => {
            alert("회원가입에 성공하셨습니다. 로그인해주세요.");
            nav("/");
          })
          .catch(() => {
            alert("회원가입 실패");
          });
      })
      .catch(() => {
        alert("회원가입 처리 중 오류가 발생했습니다.");
      });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!mem_id.trim()) {
      alert("아이디를 입력해주세요.");
      return;
    }
    if (!pw.trim()) {
      alert("비밀번호를 입력해주세요.");
      return;
    }
    join();
  };

  return (
    <div>
      <form
        onSubmit={handleSubmit}
        style={{ display: "flex", flexDirection: "column" }}
      >
        <div>
          <input
            required
            type="text"
            value={mem_id}
            onChange={(e) => setMemId(e.target.value)}
          />
          <input
            required
            type="password"
            value={pw}
            onChange={(e) => setPw(e.target.value)}
          />
        </div>
        <div>
          <button type="button" onClick={() => nav("/")}>
            로그인
          </button>
          <button type="submit">회원가입</button>
        </div>
      </form>
    </div>
  );
}
