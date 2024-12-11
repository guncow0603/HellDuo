document.addEventListener("DOMContentLoaded", function () {
    // 폼 제출 이벤트 핸들러
    document.getElementById("createBoardForm").addEventListener("submit", function (e) {
        e.preventDefault();  // 폼 제출 기본 동작을 막음

        const boardId = window.location.pathname.split("/").pop();  // URL에서 boardId 추출
        const title = document.getElementById("title").value.trim();  // 제목
        const content = document.getElementById("content").value.trim();  // 내용

        if (!title || !content) {
            alert("제목과 내용을 모두 입력해주세요.");
            return;
        }

        // 서버로 보낼 데이터
        const requestData = {
            title: title,
            content: content
        };

        // PUT 요청 보내기
        fetch(`/api/v1/board/${boardId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestData)  // 요청 데이터를 JSON 형식으로 변환
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("게시글 수정에 실패했습니다.");
                }
                return response.json();  // 서버에서 응답받은 JSON 데이터 처리
            })
            .then(data => {
                alert("게시글이 성공적으로 수정되었습니다.");
                window.location.href = `/api/v1/page/boardRead/${boardId}`;  // 수정 후 해당 게시글 페이지로 이동
            })
            .catch(error => {
                console.error(error);
                alert("게시글 수정 중 오류가 발생했습니다.");
            });
    });

    // 페이지 로드 시 게시글 정보 불러오기
    const boardId = window.location.pathname.split("/").pop();  // boardId 추출

    // 게시글 조회
    fetch(`/api/v1/board/${boardId}`)
        .then(response => response.json())
        .then(board => {
            document.getElementById("title").value = board.title;  // 제목
            document.getElementById("content").value = board.content;  // 내용
        })
        .catch(error => {
            console.error(error);
            alert("게시글 정보를 불러오는 데 실패했습니다.");
        });
});