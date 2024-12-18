$(document).ready(function () {
    // 폼 제출 이벤트 핸들러
    $("#createBoardForm").on("submit", function (e) {
        e.preventDefault(); // 폼 제출 기본 동작 방지

        const boardId = window.location.pathname.split("/").pop(); // URL에서 boardId 추출
        const title = $("#title").val().trim(); // 제목
        const content = $("#content").val().trim(); // 내용

        // 유효성 검사
        if (!title || !content) {
            alert("제목과 내용을 모두 입력해주세요.");
            return;
        }

        // 서버로 보낼 데이터
        const requestData = {
            title: title,
            content: content
        };

        // AJAX를 사용하여 PUT 요청 보내기
        $.ajax({
            url: `/api/v1/board/${boardId}`,
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(requestData) // 데이터를 JSON 형식으로 변환
        })
            .done(function (res) {
                alert(res.msg); // 성공 메시지 출력
                window.location.href = `/api/v1/page/boardRead/${boardId}`; // 수정 후 해당 게시글 페이지로 이동
            })
            .fail(function (res) {
                    const jsonObject = JSON.parse(res.responseText);
                    const messages = jsonObject.messages || "게시글 수정에 실패했습니다.";
                    alert(messages); // 서버에서 제공된 오류 메시지 출력

            });
    });

    // 페이지 로드 시 게시글 정보 불러오기
    const boardId = window.location.pathname.split("/").pop(); // boardId 추출

    $.ajax({
        url: `/api/v1/board/${boardId}`,
        method: "GET"
    })
        .done(function (board) {
            $("#title").val(board.title); // 제목
            $("#content").val(board.content); // 내용
        })
        .fail(function (res) {
            console.error(res.responseText);
            alert("게시글 정보를 불러오는 데 실패했습니다.");
        });
});