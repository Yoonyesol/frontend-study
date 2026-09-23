import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CommentList from "./CommentList";

export default function BoardDetail({ loginUser }) {
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;
  const params = useParams();
  const [board, setBoard] = useState([]);
  const nav = useNavigate();

  useEffect(() => {
    fetch(`${backendDomain}/boards/${params.id}`)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setBoard(data);
      });
  }, [backendDomain, params.id]);

  const deleteBoard = (id) => {
    fetch(`${backendDomain}/boards/${id}`, {
      method: "DELETE",
    })
      .then((res) => res.json())
      .then(() => {
        alert("삭제 성공");
        nav("/board");
      })
      .catch((e) => alert("삭제실패"));
  };

  return (
    <div>
      <section>
        <div>제목: {board.title}</div>
        <div>작성자: {board.mem_id}</div>
        <div>작성일: {board.reg_dtm}</div>
        <div>수정일: {board.mod_dtm}</div>
        <hr></hr>
        <div>{board.text}</div>
        {loginUser && board.mem_id === loginUser.mem_id && (
          <div>
            <button
              onClick={() =>
                nav(`/board/edit/${params.id}`, {
                  state: {
                    boardData: board,
                    isModify: true,
                  },
                })
              }
            >
              수정
            </button>
            <button onClick={() => deleteBoard(params.id)}>삭제</button>
          </div>
        )}
      </section>

      <CommentList boardId={params.id} loginUser={loginUser} />

      <section>
        <button className="go-to-list-btn" onClick={() => nav("/board")}>
          목록으로
        </button>
      </section>
    </div>
  );
}
