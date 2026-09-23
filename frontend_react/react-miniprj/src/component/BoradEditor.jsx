import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

export default function BoradEditor({ loginUser }) {
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;
  const nav = useNavigate();
  const location = useLocation();

  const isModify = location.state?.isModify;
  const boardData = location.state?.boardData;

  const [board, setBoard] = useState({
    title: isModify ? boardData.title : "",
    text: isModify ? boardData.text : "",
    mem_id: loginUser.mem_id,
    count: isModify ? boardData.count : 0,
    reg_dtm: isModify
      ? boardData.reg_dtm
      : new Date().toISOString().slice(0, 10),
    mod_dtm: isModify ? new Date().toISOString().slice(0, 10) : "",
  });

  const saveData = () => {
    fetch(`${backendDomain}/boards`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(board),
    })
      .then((res) => res.json())
      .then(() => {
        alert("등록 성공");
        nav("/board");
      })
      .catch((e) => alert("등록 실패"));
  };

  const patchData = () => {
    fetch(`${backendDomain}/boards/${boardData.id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(board),
    })
      .then((res) => res.json())
      .then(() => {
        alert("수정 성공");
        nav(`/board/${boardData.id}`);
      })
      .catch((e) => alert("수정 실패"));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isModify) {
      patchData();
    } else {
      saveData();
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <span>제목: </span>
          <input
            required
            type="text"
            value={board.title}
            onChange={(e) => {
              setBoard({ ...board, title: e.target.value });
            }}
          />
        </div>
        <div>
          <span>내용: </span>
          <input
            required
            type="text"
            value={board.text}
            onChange={(e) => {
              setBoard({ ...board, text: e.target.value });
            }}
          />
        </div>
        <div>
          <button type="submit">
            {isModify ? "수정" : "등록"}
          </button>
          <button type="button" onClick={() => nav(-1)}>
            취소
          </button>
        </div>
      </form>

      <div>
        <button type="button" onClick={() => nav("/board")}>
          목록으로
        </button>
      </div>
    </div>
  );
}
