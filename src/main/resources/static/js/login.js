$(document).ready(function () {
    // 토큰 삭제
    Cookies.remove('AccessToken', {path: '/'});
    Cookies.remove('RefreshToken', {path: '/'});
});

let href = location.href;

const host = 'http://' + window.location.host;
console.log(href);
console.log(host);

function onLogin() {
    let email = $('#email').val();
    let password = $('#password').val();

    $.ajax({
        type: "POST",
        url: `/api/v2/users/login`,
        contentType: "application/json",
        data: JSON.stringify({email: email, password: password}),
    })
        .done(function (res) {
            alert(res.msg);
            window.location.href = host + '';
        })
        .fail(function (res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages;
            alert(messages);
            window.location.href = host + '';
        });
}