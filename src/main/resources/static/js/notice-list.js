$(document).ready(function () {

    // 공지사항 목록 가져오기
    function getNoticeList() {
        $.ajax({
            url: '/api/v2/admin/notice',  // GET 요청
            type: 'GET',
            success: function (response) {
                const noticeList = response || [];
                const $list = $('#notice-list');
                $list.empty();  // 기존 목록 비우기

                if (noticeList.length === 0) {
                    $list.append('<li class="list-group-item">등록된 공지사항이 없습니다.</li>');
                } else {
                    noticeList.forEach(function (notice) {
                        $list.append(`
                                <li class="list-group-item notice-item" data-id="${notice.noticeId}">
                                    <a href="javascript:void(0);" class="notice-title">
                                        ${notice.title}
                                    </a>
                                </li>
                            `);
                    });
                }
            },
            error: function () {
                alert('공지사항 목록을 가져오는 데 실패했습니다.');
            }
        });
    }

    // 공지사항 클릭 시 해당 페이지로 이동
    $(document).on('click', '.notice-item', function () {
        const noticeId = $(this).data('id');
        window.location.href = `/api/v2/page/notice/${noticeId}`;  // 실제 페이지 경로로 수정
    });

    // 페이지 로드 시 공지사항 목록을 불러오기
    getNoticeList();
});