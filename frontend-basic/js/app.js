const title = document.querySelector("#title");
/* document(이 문서) id가 title인 요소를 찿기 */
const button = document.querySelector("#btn");

button.addEventListener("click", function () {
    title.textContent = "버튼을 클릭했습니다!";
});