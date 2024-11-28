$(document).ready(function() {
    // API에서 사용자 정보를 가져오는 함수
    $.ajax({
        url: "/api/v1/users",  // 사용자 정보 API
        method: "GET",
        success: function(res) {
            // API 응답을 받아 각 필드에 값을 업데이트
            $("#profile-email").text(res.email);
            $("#profile-name").text(res.name);
            $("#profile-gender").text(res.gender);
            $("#profile-age").text(res.age);
            $("#profile-phone").text(res.phoneNumber);
            $("#profile-nickname").text(res.nickname);
            $("#profile-weight").text(res.weight + "kg");
            $("#profile-height").text(res.height + "cm");
        }
    }).fail(function (res) {
        const jsonObject = JSON.parse(res.responseText);
        const messages = jsonObject.messages;
        alert(messages);
    });
});

$(document).ready(function() {
    // 프로필 이미지 조회 API 호출
    $.ajax({
        url: '/api/v1/userImage/profile',
        method: 'GET',
    }) .done(function (res) {
        $('#profile-image').attr('src', res.imageUrl);
    })
        .fail(function (res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages;
            alert(messages);
        });
});