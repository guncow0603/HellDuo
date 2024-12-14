$(document).ready(function () {
    const auth = getToken();

    // 로그인 상태 확인 및 UI 처리
    if (auth === undefined || auth === '') {
        $('#logout-button').hide();
        $('#login-button').show();
    } else { // 토큰이 존재 즉 로그인 중일 때
        $('#logout-button').show();
        $('#login-button').hide();
        const role = getUserRole();
        if (role === 'ADMIN') {
            loadAdminHeader()
            $('#board-create-button').hide();
            $('#review-create-button').hide();
            $('#reserve-btn').hide();
            $('#update-btn').hide();
            $('#delete-btn').show();
            $('#complete-pt-btn').hide();
            $('#pt-create-button').hide();
        }
    }

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