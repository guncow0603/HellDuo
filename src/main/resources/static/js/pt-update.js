$(document).ready(function() {
    const ptId = window.location.pathname.split("/").pop(); // URL에서 ptId 추출
    // PT 정보를 수정하기 위한 버튼 클릭 이벤트 처리
    $('#update-btn').on('click', function() {
        const title = $('#title').val();
        const specialization = $('#specialization').val() === "" ? null : $('#specialization').val();  // 빈 문자열을 null로 처리
        const scheduledDate = $('#scheduledDate').val();
        const price = $('#price').val();
        const description = $('#description').val();

        // PUT 요청을 통해 서버에 데이터 전송
        $.ajax({
            url: `/api/v1/pt/${ptId}`, // PT ID를 URL에 포함
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                title: title,
                specialization: specialization,
                scheduledDate: scheduledDate,
                price: price,
                description: description
            }),
            success: function(res) {
                alert(res.msg);
                // 백틱을 사용하여 ptId 포함한 URL로 리다이렉트
                window.location.href = `/api/v1/page/ptRead/${ptId}`; // PT 상세 페이지로 리다이렉트
            },
            error: function(error) {
                alert('PT 정보 업데이트에 실패했습니다.');
            }
        });
    });
});