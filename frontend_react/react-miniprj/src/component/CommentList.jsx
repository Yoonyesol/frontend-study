import { useEffect, useState } from "react";

export default function CommentList({ boardId, loginUser }) {
  const [replys, setReplys] = useState([]);
  const [isEditing, setIsEditing] = useState(null);
  const [reply, setReply] = useState("");
  const [newReply, setNewReply] = useState("");

  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;

  const fetchReplies = () => {
    if (!boardId) return;

    fetch(`${backendDomain}/reply`)
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setReplys(data.filter((r) => String(r.board_id) === String(boardId)));
        }
      })
      .catch(() => {});
  };

  useEffect(() => {
    fetchReplies();
  }, [backendDomain, boardId]);

  const handleAddReply = (e) => {
    e.preventDefault();
    if (!loginUser) {
      alert("로그인이 필요합니다.");
      return;
    }
    if (!newReply.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    const commentData = {
      board_id: boardId,
      mem_id: loginUser.mem_id,
      reply: newReply,
      del_flg: "N",
      reg_dtm: new Date().toISOString().slice(0, 10),
    };

    fetch(`${backendDomain}/reply`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(commentData),
    })
      .then((res) => res.json())
      .then((data) => {
        alert("댓글이 등록되었습니다.");
        setNewReply("");
        setReplys((prev) => [...prev, data]);
      })
      .catch((e) => alert("댓글 등록 실패"));
  };

  const startEdit = (item) => {
    setIsEditing(item.id);
    setReply(item.reply);
  };

  const saveEdit = (id) => {
    fetch(`${backendDomain}/reply/${id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        reply,
      }),
    })
      .then((res) => res.json())
      .then((data) => {
        alert("수정 성공");
        setIsEditing(null);
        // 댓글 목록 갱신
        setReplys((prev) =>
          prev.map((r) => (r.id === id ? { ...r, reply } : r)),
        );
      })
      .catch((e) => alert("수정 실패"));
  };

  const deleteReply = (id) => {
    fetch(`${backendDomain}/reply/${id}`, {
      method: "DELETE",
    })
      .then((res) => res.json())
      .then(() => {
        alert("삭제 성공");
        setReplys((prev) => prev.filter((r) => r.id !== id));
      })
      .catch((e) => alert("삭제실패"));
  };

  return (
    <div>
      <hr />
      <div>
        <h3>댓글</h3>
        {loginUser ? (
          <form onSubmit={handleAddReply}>
            <input
              required
              type="text"
              placeholder="댓글을 입력하세요"
              value={newReply}
              onChange={(e) => setNewReply(e.target.value)}
            />
            <button type="submit">댓글 등록</button>
          </form>
        ) : (
          <div>댓글을 작성하려면 로그인이 필요합니다.</div>
        )}
      </div>

      {Array.isArray(replys) &&
        replys.map((it) => (
          <div key={it.id}>
            <hr />
            <div>{it.mem_id}</div>
            {loginUser &&
            it.mem_id === loginUser.mem_id &&
            isEditing === it.id ? (
              <form
                onSubmit={(e) => {
                  e.preventDefault();
                  saveEdit(it.id);
                }}
              >
                <input
                  required
                  type="text"
                  value={reply}
                  onChange={(e) => setReply(e.target.value)}
                />
                <button type="submit">확인</button>
                <button type="button" onClick={() => setIsEditing(null)}>
                  취소
                </button>
              </form>
            ) : (
              <div>{it.reply}</div>
            )}
            <div>{it.reg_dtm}</div>
            {loginUser && it.mem_id === loginUser.mem_id && !isEditing && (
              <div>
                <button type="button" onClick={() => startEdit(it)}>
                  수정
                </button>
                <button type="button" onClick={() => deleteReply(it.id)}>
                  삭제
                </button>
              </div>
            )}
          </div>
        ))}
    </div>
  );
}
