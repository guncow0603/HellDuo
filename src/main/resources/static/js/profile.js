$(document).ready(function() {
    const auth = getToken();
    const role = getUserRole();
    if (auth !== undefined && auth !== '') {
        if(role === 'TRAINER') {
            $('#profile-w').hide();
            $('#profile-h').hide();
            $('#profile-n').hide();
            $('#profile-b').show();
            $('#profile-s').show();
            $('#profile-e').show();
            $('#profile-c').show();
        }else if(role === 'USER'){
            $('#profile-w').show();
            $('#profile-h').show();
            $('#profile-n').show();
            $('#profile-b').hide();
            $('#profile-s').hide();
            $('#profile-e').hide();
            $('#profile-c').hide();
        }
    }

    if (role === "USER") {
        // 일반 유저의 프로필 정보 가져오기
        $.ajax({
            url: "/api/v1/users",  // 사용자 정보 API
            method: "GET",
            success: function(res) {
                // 사용자 정보 업데이트
                $("#profile-email").text(res.email);
                $("#profile-name").text(res.name);
                $("#profile-gender").text(res.gender);
                $("#profile-age").text(res.age);
                $("#profile-phone").text(res.phoneNumber);
                $("#profile-nickname").text(res.nickname);
                $("#profile-weight").text(res.weight + "kg");
                $("#profile-height").text(res.height + "cm");
            },
            fail: function(res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            }
        });
    } else if (role === "TRAINER") {
        // 트레이너의 프로필 정보 가져오기
        $.ajax({
            url: "/api/v1/users/trainer",  // 트레이너 정보 API
            method: "GET",
            success: function(res) {
                // 트레이너 정보 업데이트
                $("#profile-email").text(res.email);
                $("#profile-name").text(res.name);
                $("#profile-gender").text(res.gender);
                $("#profile-age").text(res.age);
                $("#profile-phone").text(res.phoneNumber);
                $("#profile-specialization").text(res.specialization);
                $("#profile-experience").text(res.experience);
                $("#profile-certifications").text(res.certifications);
                $("#profile-bio").text(res.bio);
            },
            fail: function(res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            }
        });
    } else {
        alert("잘못된 역할입니다.");
    }
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

// 프로필 이미지 업로드 처리
document.addEventListener("DOMContentLoaded", function () {
    const uploadButton = document.getElementById("upload-button");
    uploadButton.addEventListener("click", function () {
        const fileInput = document.getElementById("profile-image-upload");
        const file = fileInput.files[0];

        if (file) {
            const formData = new FormData();
            formData.append("file", file);

            $.ajax({
                url: '/api/v1/userImage/profile',
                method: 'POST',
                data: formData,
                processData: false, // FormData로 데이터를 보내기 위해 false로 설정
                contentType: false  // Content-Type을 자동으로 설정하도록 false
            })
                .done(function (res) {
                    alert(res.msg);
                    window.location.href = '/api/v1/page/profile'; // 성공 시 리디렉션
                })
                .fail(function (res) {
                    const jsonObject = JSON.parse(res.responseText);
                    const messages = jsonObject.messages;
                    alert(messages);
                });
        } else {
            alert("업로드할 파일을 선택해 주세요.");
        }
    });
});