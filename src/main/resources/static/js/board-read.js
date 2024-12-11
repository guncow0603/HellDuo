// 특정 게시글 조회 함수
async function getBoardById(boardId) {
    try {
        // API 요청을 통해 특정 게시글 정보를 가져옵니다
        const response = await fetch(`/api/v1/board/${boardId}`);
        if (response.ok) {
            const board = await response.json();  // JSON 데이터로 변환

            // 게시글 정보를 HTML로 표시
            const boardDetails = document.getElementById('board-details');
            boardDetails.innerHTML = `
                    <div class="board-content">
                        <h2 class="board-title">${board.title}</h2>
                        <p><strong>내용:</strong> ${board.content}</p>
                        <p><strong>좋아요 수:</strong> ${board.boardLikeCount}</p>
                    </div>
                `;
        } else {
            alert('게시글을 불러오는 데 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('게시글을 불러오는 데 실패했습니다.');
    }
}

// 페이지 로드 시 특정 게시글 조회 (예: ID = 1)
window.onload = function() {
    const boardId =  window.location.pathname.split("/").pop(); // URL에서 ptId 추출
    getBoardById(boardId);
};