document.addEventListener("DOMContentLoaded", function () {
    // URL에서 ptId 추출
    const ptId = window.location.pathname.split("/").pop();

    // 리뷰 제출 폼 이벤트 리스너 추가
    document.getElementById("review-form").addEventListener("submit", function (event) {
        event.preventDefault(); // 폼의 기본 동작인 페이지 리로드를 막음

        // 입력값 가져오기
        const title = document.getElementById("title").value;
        const content = document.getElementById("content").value;
        const rating = document.getElementById("rating").value;

        // 리뷰 데이터 객체 생성
        const reviewData = {
            title: title,
            content: content,
            rating: parseInt(rating), // 평점은 정수로 변환
        };

        // $.ajax()를 사용하여 비동기 요청
        $.ajax({
            url: `/api/v1/review/${ptId}`,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(reviewData),
        })
            .done(function (res) {
                // 성공적으로 응답을 받았을 때
                document.getElementById("alert").textContent = res.msg || "리뷰가 성공적으로 작성되었습니다!";
                document.getElementById("alert").classList.add("show");
                document.getElementById("error-alert").classList.remove("show");

                // 폼 초기화
                document.getElementById("review-form").reset();

                // 2초 후 페이지 뒤로 이동
                setTimeout(function () {
                    window.history.back();
                }, 2000);
            })
            .fail(function (res) {
                const result = JSON.parse(res.responseText); // 실패 메시지 파싱


                document.getElementById("error-alert").textContent = result.messages;
                document.getElementById("error-alert").classList.add("show");
                document.getElementById("alert").classList.remove("show");
            });
    });
});