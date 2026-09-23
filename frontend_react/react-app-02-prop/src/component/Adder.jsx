import { useState } from "react";

export default function Adder() {
  const [num1, setNum1] = useState(0);
  const [num2, setNum2] = useState(0);
  const [result, setResult] = useState(0);

  const plusFunc = () => {
    setResult(Number(num1) + Number(num2));
  };

  return (
    <div>
      <input
        type="number"
        value={num1}
        onChange={(e) => {
          setNum1(e.target.value);
        }}
      />
      <button type="button" onClick={plusFunc}>
        +
      </button>
      <input
        type="number"
        value={num2}
        onChange={(e) => {
          setNum2(e.target.value);
        }}
      />
      <span>= {result} </span>
    </div>
  );
}
