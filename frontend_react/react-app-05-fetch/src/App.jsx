import { BrowserRouter, Route, Routes } from "react-router-dom";
import StudentList from "./component/StudentList";
import CreateStudent from "./component/CreateStudent";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<StudentList />} />
          <Route path="/create_student" element={<CreateStudent />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
