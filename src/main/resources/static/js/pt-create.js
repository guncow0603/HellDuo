$(document).ready(function () {
    $('#ptForm').on('submit', function (event) {
        event.preventDefault();

        // 폼 데이터 수집
        const formData = {
            title: $('#title').val(),
            specialization: $('#specialization').val(),
            scheduledDate: $('#scheduledDate').val(),
            price: parseInt($('#price').val(), 10),
            description: $('#description').val(),
        };

        // AJAX 호출
        $.ajax({
            url: '/api/v1/pt',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (res) {
                alert(res.msg);
                window.location.href = '/api/v1/page/ptList';
            },
            error: function (xhr) {
                $('#message').text(`PT 생성 실패`);
            },
        });
    });
});