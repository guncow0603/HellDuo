$(document).ready(function () {
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

// 폼 제출 처리
    $("#update-profile-form").on("submit", function (e) {
        e.preventDefault();
        if (role === "USER") {
        // 수정 가능한 데이터만 추출
        const formData = {
            phoneNumber: $("#phone").val(),
            age: $("#age").val(),
            nickname: $("#nickname").val(),
            weight: $("#weight").val(),
            height: $("#height").val(),
        };

        $.ajax({
            url: "/api/v1/users",
            method: "PUT",
            contentType: "application/json",  // JSON 형식으로 전송
            dataType: "json",  // 서버에서 JSON 응답을 기대
            data: JSON.stringify(formData),  // 데이터를 JSON 문자열로 변환
        }) .done(function (res) {
            alert(res.msg);
            window.location.href = '/api/v1/page/profile';
        })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
                window.location.href = '/api/v1/page/profile-edit';
            });
        }else if (role === "TRAINER"){
            // 수정 가능한 데이터만 추출
            const formData = {
                phoneNumber: $("#phone").val(),
                age: $("#age").val(),
                bio: $("#bio").val(),
                specialization: $("#specialization").val(),
                experience: $("#experience").val(),
                certifications: $("#certifications").val(),
            };

            $.ajax({
                url: "/api/v1/users/trainer",
                method: "PUT",
                contentType: "application/json",  // JSON 형식으로 전송
                dataType: "json",  // 서버에서 JSON 응답을 기대
                data: JSON.stringify(formData),  // 데이터를 JSON 문자열로 변환
            }) .done(function (res) {
                alert(res.msg);
                window.location.href = '/api/v1/page/profile';
            })
                .fail(function (res) {
                    const jsonObject = JSON.parse(res.responseText);
                    const messages = jsonObject.messages;
                    alert(messages);
                    window.location.href = '/api/v1/page/profile-edit';
                });
        }
    });
});