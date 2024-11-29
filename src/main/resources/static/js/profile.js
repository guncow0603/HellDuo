$(document).ready(function() {
    // 사용자 역할 확인 (로그인한 사용자 정보 또는 JWT 등의 방법으로 역할을 확인할 수 있음)
    $.ajax({
        url: "/api/v1/users/role",  // 예시: 현재 로그인한 사용자의 역할 확인 API
        method: "GET",
        success: function() {
            // 'role'에 따라 분기 처리
            if (getUserRole() === "USER") {
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
            } else if (getUserRole() === "TRAINER") {
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
        },
        fail: function(res) {
            alert("사용자 역할을 확인하는데 실패했습니다.");
        }
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