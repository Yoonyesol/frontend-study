import { useEffect } from "react";

export default function useEffectEx1() {
  useEffect(() => {
    console.log("화면 렌더링 시마다 실행");
  }, []);

  return <div></div>;
}
