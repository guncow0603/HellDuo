$(document).ready(function () {
    const auth = getToken();

    // 로그인 상태 확인 및 UI 처리
    if (auth === undefined || auth === '') { // 로그인하지 않은 상태일 때
        $('#logout-button').hide();
        $('#login-button').show();
        $('#my-page').hide();
        $('#header-chat-list').hide();
        $('#banner').hide();


        $('#review-create-button').hide();
        $('#chat-user').hide();
        $('#pt-button').hide();
        $('#update-btn').hide();
        $('#delete-btn').hide();
        $('#complete-pt-btn').hide();
        $('#pt-create-button').hide();
        loadUserHeader();
    } else { // 토큰이 존재 즉 로그인 중일 때
        const role = getUserRole();
        $('#logout-button').show();
        $('#login-button').hide();

        $('#header-chat-list').show();

        if (role === 'ADMIN') {
            $('#review-create-button').hide();
            $('#chat-user').show();
            $('#reserve-btn').hide();
            $('#update-btn').hide();
            $('#delete-btn').show();
            $('#complete-pt-btn').hide();
            $('#pt-create-button').hide();
            loadAdminHeader();
        } else if(role === 'USER') {
            $('#review-create-button').show();
            $('#chat-user').show();
            $('#update-btn').hide();
            $('#delete-btn').hide();
            $('#complete-pt-btn').hide();
            $('#pt-create-button').hide();
            loadUserHeader();
        }else if(role === 'TRAINER'){
            $('#review-create-button').hide();
            $('#chat-user').show();
            $('#reserve-btn').hide();
            $('#update-btn').show();
            $('#delete-btn').show();
            $('#pt-create-button').show();
            loadUserHeader();
        }

        // 알림 이벤트 수신
        let eventSource = new EventSource(
            `${window.location.protocol}//${window.location.host}/api/v1/notifications/subscribe`
        );
        console.log('EventSource 연결 확인');

        eventSource.addEventListener("createChatRoom", function (event) {
            console.log("새로운 채팅방 생성:", event.data);
            alertBadge();
        });
    }

    // 로그인하지 않은 상태일 때도 일반 사용자 헤더 로드
    loadUserHeader();

    // 로그아웃 버튼 클릭 이벤트
    $('#logout-button').click(function () {
        $.ajax({
            type: 'POST',
            url: '/api/v1/users/logout',
        })
            .done(function (res) {
                alert(res.msg);
                window.location.href = '/api/v1/page/index';
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
                window.location.href = '/api/v1/page/index';
            });
    });
});

// 토큰 가져오기
function getToken() {
    let auth = Cookies.get('AccessToken');
    return auth === undefined ? '' : auth;
}

// 사용자 역할 가져오기
function getUserRole() {
    let role;
    $.ajax({
        url: '/api/v1/users/role',
        method: 'GET',
        dataType: 'json',
        async: false,
        success: function (data) {
            role = data;
        },
        error: function () {
            alert('알 수 없는 오류 발생');
        }
    });
    return role;
}

// 관리자용 헤더 로드
function loadAdminHeader() {
    if (!$("#header").hasClass("loaded")) {
        $("#header").load("/adminHeader.html", function (response, status, xhr) {
            if (status === "error") {
                console.error("헤더 로드 실패:", xhr.status, xhr.statusText);
            } else {
                $("#header").addClass("loaded");
            }
        });
    }
}

// 일반 사용자용 헤더 로드
function loadUserHeader() {
    if (!$("#header").hasClass("loaded")) {
        $("#header").load("/header.html", function (response, status, xhr) {
            if (status === "error") {
                console.error("헤더 로드 실패:", xhr.status, xhr.statusText);
            } else {
                $("#header").addClass("loaded");
            }
        });
    }
}

// 알림 뱃지 표시
function alertBadge() {
    var headerChatlist = document.getElementById('header-chat-list');
    const newSpan = document.createElement('span');
    newSpan.className = 'position-absolute top-0 start-100 translate-middle p-2 bg-danger border border-light rounded-circle';
    const innerSpan = document.createElement('span');
    innerSpan.className = 'visually-hidden';
    innerSpan.textContent = 'New alerts';
    newSpan.appendChild(innerSpan);
    headerChatlist.appendChild(newSpan);
}