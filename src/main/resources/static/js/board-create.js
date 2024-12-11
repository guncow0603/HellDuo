document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('createBoardForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;

        // 게시글 데이터를 서버로 전송
        const response = await fetch('/api/v1/board', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: title,
                content: content
            })
        });

        if (response.ok) {
            alert('게시글이 작성되었습니다.');
            window.location.href = '/api/v1/page/boardList'; // 게시글 목록 페이지로 리다이렉트
        } else {
            alert('게시글 작성에 실패했습니다.');
        }
    });
});