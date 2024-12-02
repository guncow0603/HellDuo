$(document).ready(function() {
    // URL에서 ptId를 가져옵니다.
    const ptId = window.location.pathname.split("/").pop(); // URL에서 ptId 추출

    $('#update-btn').on('click', function() {
        window.location.href = `/api/v1/page/ptUpdate/${ptId}`;
    });

    $.ajax({
        url: `/api/v1/pt/${ptId}`, // PT ID를 URL에 포함
        method: 'GET',
        success: function(res) {
            // 서버에서 받아온 데이터를 HTML 요소에 동적으로 삽입
            $('#pt-title').text(`PT 제목: ${res.title}`);
            $('#pt-description').text(`설명: ${res.description}`);
            $('#pt-specialization').text(`전문 분야: ${res.specialization}`);
            $('#pt-trainer').text(`트레이너: ${res.trainerName}`);
            $('#pt-user').text(`예약자: ${res.userName}`);
            $('#pt-scheduledDate').text(`예약 시간: ${new Date(res.scheduledDate).toLocaleString()}`);
            $('#pt-price').text(`PT 비용: ${res.price} 원`);
            $('#pt-status').text(`상태: ${res.status}`);
        },
        error: function() {
            alert('PT 정보를 불러오는 데 실패했습니다.');
        }
    })

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