$(document).ready(function () {
    const auth = getToken();

    if (auth !== undefined && auth !== '') { //토큰이 존재 즉 로그인중
        console.log('auth:', auth);
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

});
function getToken() {
    let auth = Cookies.get('AccessToken');

    if (auth === undefined) {
        return '';
    }

    return auth;
}

