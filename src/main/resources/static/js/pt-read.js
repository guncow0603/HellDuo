$(document).ready(function() {
    const auth = getToken();

// 로그인 인증 및 역할에 따른 화면 표시
    if (auth !== undefined && auth !== '') {
        const role = getUserRole();
        if (role === 'TRAINER') {
            $('#delete-btn').show();
            $('#update-btn').show();
            $('#reserve-btn').hide();
        } else {
            $('#delete-btn').hide();
            $('#update-btn').hide();
            $('#reserve-btn').show();
        }
    } else {
        $('#delete-btn').hide();
        $('#update-btn').hide();
        $('#reserve-btn').hide();
        $('#chat-user').hide();
    }
    // URL에서 ptId를 가져옵니다.
    const ptId = window.location.pathname.split("/").pop(); // URL에서 ptId 추출
    var trainerId;
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
            trainerId=res.trainerId;
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
    var chatTrybutton = document.getElementById('chat-user');
    chatTrybutton.innerText = "채팅걸기";
    chatTrybutton.addEventListener('click', function () {
        chatTry(trainerId);
    });

    $('#reserve-btn').on('click', function() {
        // jQuery AJAX 요청
        $.ajax({
            url: `/api/v1/pt/${ptId}`,  // 요청할 URL
            type: 'PATCH',               // HTTP 메서드
            contentType: 'application/json'  // 요청 본문 타입
        })
            .done(function (res) {
                alert(res.msg);
                window.location.href = `/api/v1/page/ptRead/${ptId}`;
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            });
    });

});




function chatTry(userId) {

    const data = {
        receiverId: userId
    }
    $.ajax({
        type: "POST",
        url: `/api/v1/chats/rooms`,
        contentType: "application/json",
        data: JSON.stringify(data),
    })
        .done(function (res) {
            console.log(res.id);
            window.location.href ='/api/v1/chats/rooms/' + res.id +'/front';
        })
        .fail(function (res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages;
            alert(messages);
        });

}
