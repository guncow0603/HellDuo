function onSignup() {
    const email = $('#email').val();
    const password = $('#password').val();
    const passwordConfirm = $('#passwordConfirm').val();
    const name = $('#name').val();
    const gender = $('#gender').val();
    const age = $('#age').val();
    const phoneNumber = $('#phoneNumber').val();
    const specialization = $('#specialization').val();
    const experience = $('#experience').val();
    const certifications = $('#certifications').val();
    const bio = $('#bio').val();


    // 요청할 데이터 준비
    const data = {
        email: email,
        password: password,
        passwordConfirm: passwordConfirm,
        name: name,
        gender: gender,
        age: age,
        phoneNumber: phoneNumber,
        specialization: specialization,
        experience: experience,
        certifications: certifications,
        bio: bio
    };

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: `/api/v1/users/trainerSignup`,
        contentType: "application/json",
        data: JSON.stringify(data),
    })
        .done(function (res) {
            // 백엔드에서 전송한 메시지를 알림으로 표시
            alert(res.message);
            window.location.href = '/api/v1/page/login'; // 로그인 페이지로 리디렉션
        })
        .fail(function (res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages;
            alert(messages);
        });
}