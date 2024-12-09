$(document).ready(function () {
    const auth = getToken();

    if (auth === undefined || auth === '') { // 로그인하지 않은 상태일 때
        // 로그인하지 않았을 때 표시할 요소들
        $('#logout-button').hide();
        $('#login-button').show();
        $('#my-page').hide();
        $('#header-chat-list').hide();
        $('#banner').hide();
    } else { // 토큰이 존재 즉 로그인 중일 때
        const role = getUserRole();
        // 로그인 후 표시할 요소들
        $('#logout-button').show();
        $('#login-button').hide();
        $('#my-page').show();
        $('#header-chat-list').show();

        // 권한에 따른 배너 표시 여부
        if (role === 'ADMIN') {
            $('#banner').show();
        } else {
            $('#banner').hide();
        }

        // 알림 이벤트 수신
        let eventSource = new EventSource(
            `${window.location.protocol}//${window.location.host}/api/v1/notifications/subscribe`
        );
        console.log('EventSource 연결 확인');

        eventSource.addEventListener("createChatRoom", function (event) {
            console.log("새로운 채팅방 생성:", event.data);
            let message = event.data;
            // 알림 기능을 처리하는 함수 호출
            alertBadge();
        });
    }


    // 로그아웃 버튼 클릭 이벤트
    $('#logout-button').click(function () {
        $.ajax({
            type: 'POST',
            url: '/api/v1/users/logout',
        })
        .done(function (res) {
            alert(res.msg)
            window.location.href = '/api/v1/page/index';
        })
        .fail(function (res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages;
            alert(messages);
            window.location.href = host + '/api/v1/page/index';
        });
    });



});
function getToken() {
    let auth = Cookies.get('AccessToken');

    if (auth === undefined) {
        return '';
    }

    return auth;
}

function getUserRole() {
    let role;
    $.ajax({
        url: '/api/v1/users/role',
        method: 'GET',
        dataType: 'json',
        async: false, // 동기적으로 설정
        success: function (data) {
            role = data;
        },
        error: function (error) {
            alert('알 수 없는 오류 발생');
        }
    });
    return role;
}

$(document).ready(function () {
    if (!$("#header").hasClass("loaded")) {
        $("#header").load("/header.html", function (response, status, xhr) {
            if (status === "error") {
                console.error("헤더 로드 실패:", xhr.status, xhr.statusText);
            } else {
                $("#header").addClass("loaded"); // 중복 로드 방지
            }
        });
    }
});

function alertBadge() {

    var headerChatlist = document.getElementById('header-chat-list');
    const newSpan = document.createElement('span');
    newSpan.className = 'position-absolute top-0 start-100 translate-middle p-2 bg-danger border border-light rounded-circle';
    console.log(newSpan);
    const innerSpan = document.createElement('span');
    innerSpan.className = 'visually-hidden';
    innerSpan.textContent = 'New alerts';
    headerChatlist.appendChild(newSpan);
    console.log(headerChatlist);
}
