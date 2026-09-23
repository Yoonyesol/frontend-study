import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

export default function StudentList() {
  const [students, setStudents] = useState([]);
  const backendDomain = import.meta.env.VITE_BACKEND_DOMAIN;

  useEffect(() => {
    fetch(`${backendDomain}/students`)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setStudents(data);
      });
  }, [backendDomain]);

  return (
    <div>
      <h2>학생 목록</h2>
      <div style={{ marginBottom: "10px" }}>
        <Link to="/create_student">학생 등록</Link>
      </div>
      <table border="1">
        <caption className="title"> StudentList </caption>
        <thead>
          <tr>
            <th>ID</th>
            <th>이름</th>
            <th>학년</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student) => (
            <tr key={student.id}>
              <td>{student.id} </td>
              <td>
                <Link to={"/students/" + student.id}>{student.name}</Link>
              </td>
              <td>{student.grade} </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
