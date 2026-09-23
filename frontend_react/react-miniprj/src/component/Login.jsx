import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login({ setLoginUser }) {
  const [id, setId] = useState("");
  const [pw, setPw] = useState("");

  const nav = useNavigate();
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;

  const login = () => {
    fetch(`${backendDomain}/mem`)
      .then((res) => res.json())
      .then((data) => {
        // json-server의 숫자 파싱 버그를 방지하기 위해 자바스크립트에서 직접 검색
        const user = Array.isArray(data)
          ? data.find((m) => String(m.mem_id) === String(id))
          : null;
        const savedPw = user?.pwd ?? user?.pw;

        if (user && String(savedPw) === String(pw)) {
          setLoginUser(user);
          nav("/board");
        } else {
          alert("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
      });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    login();
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
            value={id}
            onChange={(e) => setId(e.target.value)}
          />
          <input
            required
            type="password"
            value={pw}
            onChange={(e) => setPw(e.target.value)}
          />
        </div>
        <div>
          <button type="submit">로그인</button>
          <button type="button" onClick={() => nav("/register")}>
            회원가입
          </button>
        </div>
      </form>
    </div>
  );
}
