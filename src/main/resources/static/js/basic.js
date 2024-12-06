$(document).ready(function () {
    const auth = getToken();

    if (auth !== undefined && auth !== '') { //토큰이 존재 즉 로그인중
        $('#logout-button').show();
        $('#login-button').hide();
        $('#my-page').show();
    } else {
        $('#logout-button').hide();
        $('#login-button').show();
        $('#my-page').hide();
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

    let eventSource = new EventSource(
        window.location.protocol + '//' + window.location.host + '/api/v1/notifications/subscribe');
    console.log('확인');

    eventSource.addEventListener("createChatRoom", function (event) {
        console.log(event);
        let message = event.data;
        alert(message);
        alertBadge();
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
