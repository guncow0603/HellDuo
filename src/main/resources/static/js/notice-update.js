$(document).ready(function () {

    // URL에서 noticeId를 추출
    const noticeId = window.location.pathname.split('/').pop();

    // 공지사항 상세 내용 가져오기
    function getNoticeDetail() {
        $.ajax({
            url: `/api/v1/admin/notice/${noticeId}`,  // GET 요청
            type: 'GET',
            success: function (response) {
                const notice = response || {};
                if (notice) {
                    $('#title').val(notice.title);  // 제목 필드에 공지사항 제목 설정
                    $('#content').val(notice.content);  // 내용 필드에 공지사항 내용 설정
                } else {
                    alert('해당 공지사항을 찾을 수 없습니다.');
                }
            },
            error: function () {
                alert('공지사항을 가져오는 데 실패했습니다.');
            }
        });
    }

    // 공지사항 수정 요청
    function updateNotice(req) {
        $.ajax({
            url: `/api/v1/admin/notice/${noticeId}`,  // PUT 요청
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(req),
        })
            .done(function (res) {
                alert(res.msg);
                window.location.href = `/api/v1/page/notice/${noticeId}`;  // 수정 후 상세 페이지로 이동
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages); // 서버에서 제공된 오류 메시지 출력

            });
    }

    // 폼 제출 시 공지사항 수정 요청
    $('#update-form').submit(function (event) {
        event.preventDefault();

        const req = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        // 공지사항 수정 요청
        updateNotice(req);
    });

    // 페이지 로드 시 공지사항 상세 내용을 불러오기
    getNoticeDetail();
});