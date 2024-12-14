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
                    $('#notice-detail').html(`
                            <h2 class="notice-title">제목 : ${notice.title}</h2>
                            <div class="notice-content">내용 : ${notice.content}</div>
                        `);
                    $('#delete-button').show();  // 삭제 버튼 표시
                    $('#update-button').show();  // 수정 버튼 표시
                } else {
                    $('#notice-detail').html('<p>해당 공지사항을 찾을 수 없습니다.</p>');
                }
            },
            error: function () {
                alert('공지사항을 가져오는 데 실패했습니다.');
            }
        });
    }

    // 공지사항 삭제 요청
    function deleteNotice() {
        $.ajax({
            url: `/api/v1/admin/notice/${noticeId}`,  // DELETE 요청
            type: 'DELETE',
        })
            .done(function (res) {
                alert(res.msg);
                window.location.href = '/api/v1/page/noticeList';  // 삭제 후 목록 페이지로 이동
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages); // 서버에서 제공된 오류 메시지 출력

            });
    }

    // 공지사항 수정 페이지로 이동
    function goToUpdatePage() {
        window.location.href = `/api/v1/page/noticeUpdate/${noticeId}`;  // 공지사항 수정 페이지로 이동
    }

    // 삭제 버튼 클릭 이벤트
    $('#delete-button').click(function () {
        if (confirm("정말 이 공지사항을 삭제하시겠습니까?")) {
            deleteNotice();
        }
    });

    // 수정 버튼 클릭 이벤트
    $('#update-button').click(function () {
        goToUpdatePage();
    });

    // 페이지 로드 시 공지사항 상세 내용을 불러오기
    getNoticeDetail();
});