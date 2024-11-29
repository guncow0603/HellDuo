// DOMContentLoaded 이벤트를 사용하여 DOM이 완전히 로드된 후 스크립트 실행
document.addEventListener("DOMContentLoaded", function() {
    // URL에서 파라미터를 추출합니다.
    const urlParams = new URLSearchParams(window.location.search);

    // HTML 요소를 가져옵니다.
    const codeElement = document.getElementById("code");
    const messageElement = document.getElementById("message");

    // 에러 코드와 메시지가 없을 경우 기본값을 설정
    const errorCode = urlParams.get("code") || "알 수 없는 에러";
    const errorMessage = urlParams.get("message") || "자세한 정보를 확인할 수 없습니다.";

    // 에러 코드와 메시지를 화면에 표시
    codeElement.textContent = "에러코드: " + errorCode;
    messageElement.textContent = "실패 사유: " + errorMessage;

    // 3초 후 프로필 페이지로 리디렉션
    setTimeout(() => {
        window.location.replace("/api/v1/page/profile");
    }, 3000);
});