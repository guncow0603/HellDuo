$(document).ready(function () {
    // 폼 제출 시
    $('#notice-form').on('submit', function (event) {
        event.preventDefault();  // 폼 기본 제출 방지

        const noticeData = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        // 폼이 비어있는지 확인
        if (!noticeData.title || !noticeData.content) {
            alert('제목과 내용을 모두 입력해주세요.');
            return;
        }

        // AJAX 요청
        $.ajax({
            url: '/api/v2/admin/notice',  // 서버의 /notice 엔드포인트
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(noticeData)
        })
            .done(function (res) {
                alert(res.msg);
                window.location.href = '/api/v2/page/noticeList';
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages); // 서버에서 제공된 오류 메시지 출력

            });
    });
});