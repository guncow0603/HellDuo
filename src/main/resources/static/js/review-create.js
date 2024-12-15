$(document).ready(function () {
    const ptId = window.location.pathname.split("/").pop();
    $('#createReviewForm').submit(function (e) {
        e.preventDefault();

        const title = $('#title').val();      // 제목 필드 값
        const content = $('#content').val();  // 내용 필드 값
        const  rating = $('#rating').val();

        // 서버로 비동기 요청 전송
        $.ajax({
            url: `/api/v1/review/${ptId}`,  // 요청 URL
            type: 'POST',          // HTTP 메서드
            contentType: 'application/json', // 전송 데이터 타입
            data: JSON.stringify({
                title: title,
                content: content,
                rating: rating
            }),
        })
            .done(function (res) {
                alert(res.msg); // 서버에서 반환한 메시지 알림
                if ($('#files')[0].files.length > 0) {
                    uploadReviewImages(res.reviewId); // 이미지 업로드 함수 호출
                } else {
                    window.location.href = '/api/v1/page/reviewList'; // 파일 없으면 바로 리다이렉트
                }
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            });
    });
});

// 이미지 업로드 함수
function uploadReviewImages(reviewId) {
    // FormData 객체 생성
    const formData = new FormData();
    const files = $('#files')[0].files; // 파일 입력 필드의 파일 가져오기

    // FormData에 파일 데이터 추가
    for (let i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
    }

    // AJAX 요청 (POST 방식)
    $.ajax({
        url: `/api/v1/reviews/${reviewId}/images`, // 적절한 URL 경로로 수정
        type: "POST",
        data: formData,
        processData: false, // 파일 전송 시 필수 설정
        contentType: false, // 파일 전송 시 필수 설정
        success: function (response) {
            window.location.href = "/api/v1/page/reviewList"; // 성공 시 리다이렉트 경로
        },
        error: function (error) {
            console.error("Error:", error);
            alert("이미지 업로드에 실패했습니다.");
        }
    });
}