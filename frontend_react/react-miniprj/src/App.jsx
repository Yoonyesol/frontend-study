import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./App.css";
import BoardList from "./component/BoardList";
import BoardDetail from "./component/BoardDetail";
import { useState } from "react";
import Login from "./component/Login";
import BoradEditor from "./component/BoradEditor";
import Register from "./component/Register";

function App() {
  const [loginUser, setLoginUser] = useState(null);
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login setLoginUser={setLoginUser} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/board" element={<BoardList />} />
        <Route
          path="/board/edit"
          element={<BoradEditor loginUser={loginUser} />}
        />
        <Route
          path="/board/:id"
          element={<BoardDetail loginUser={loginUser} />}
        />
        <Route
          path="/board/edit/:id"
          element={<BoradEditor loginUser={loginUser} />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
