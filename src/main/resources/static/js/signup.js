document.getElementById('adminCheck').addEventListener('change', function () {
    var adminTokenInput = document.getElementById('adminTokenInput');
    adminTokenInput.style.display = this.checked ? 'block' : 'none';
});

function onSignup() {
    const email = $('#email').val();
    const password = $('#password').val();
    const passwordConfirm = $('#passwordConfirm').val();
    const name = $('#name').val();
    const gender = $('#gender').val();
    const age = parseInt($('#age').val());
    const phoneNumber = $('#phoneNumber').val();
    const nickname = $('#nickname').val();
    const weight = parseFloat($('#weight').val());
    const height = parseFloat($('#height').val());
    const admin = $('#adminCheck').prop('checked');
    const adminToken = $('#adminToken').val();

    $.ajax({
        type: "POST",
        url: `/api/v1/users/signup`,
        contentType: "application/json",
        data: JSON.stringify({
            email: email,
            password: password,
            passwordConfirm: passwordConfirm,
            name: name,
            gender: gender,
            age: age,
            phoneNumber: phoneNumber,
            nickname: nickname,
            weight: weight,
            height: height,
            admin: admin,
            adminToken: adminToken
        }),
    })
        .done(function (res) {
            // 백엔드에서 전송한 메시지를 알림으로 표시
            alert(res.message);
            window.location.href = '/api/v1/users/login';
        })
        .fail(function (res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages.join('\n');
            alert(messages);
        });
}