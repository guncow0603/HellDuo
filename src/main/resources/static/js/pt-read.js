$(document).ready(function() {
    // URL에서 ptId를 가져옵니다.
    const ptId = window.location.pathname.split("/").pop(); // URL에서 ptId 추출

    // 서버에서 PT 정보를 가져오는 API 호출
    $.ajax({
        url: `/api/v1/pt/${ptId}`,  // 백엔드 API URL
        method: 'GET',
        success: function(response) {
            // 서버에서 반환된 데이터를 화면에 표시
            $('#pt-trainer').text(`트레이너: ${response.trainerName}`);
            $('#pt-user').text(`예약자: ${response.userName}`);
            $('#pt-scheduledDate').text(`예약 시간: ${new Date(response.scheduledDate).toLocaleString()}`);
            $('#pt-price').text(`PT 비용: ${response.price}원`);
            $('#pt-description').text(`세션 설명: ${response.description}`);
            $('#pt-status').text(`상태: ${response.status}`);
        },
        error: function(error) {
            // 오류 처리
            alert('PT 정보를 불러오는 데 실패했습니다.');
        }
    });

    // 삭제 버튼 클릭 시 PT 삭제 요청
    $('#delete-btn').on('click', function() {
        if (confirm('정말로 PT를 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/v1/pt/${ptId}`, // DELETE 요청
                method: 'DELETE',
                success: function(res) {
                    alert(res.msg);
                    window.location.href = '/api/v1/page/ptList';
                },
                error: function(error) {
                    alert('PT 삭제에 실패했습니다.');
                }
            });
        }
    });
});