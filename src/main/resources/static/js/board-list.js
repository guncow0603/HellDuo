// 게시글 목록 조회 (GET 요청)
async function loadBoards() {
    try {
        const response = await fetch('/api/v1/board');
        const boards = await response.json();

        const boardListContainer = document.getElementById('board-list');
        boardListContainer.innerHTML = '';  // 기존 내용 초기화

        if (boards.length === 0) {
            boardListContainer.innerHTML = '<p class="no-boards">등록된 게시글이 없습니다.</p>';
        } else {
            boards.forEach(board => {
                const boardItem = document.createElement('div');
                boardItem.classList.add('board-item');

                // board-item 전체를 링크로 감싸기
                const link = document.createElement('a');
                link.href = `/api/v1/page/boardRead/${board.boardId}`;
                link.style.textDecoration = 'none'; // 링크 스타일 제거
                link.style.color = 'inherit'; // 텍스트 색상 그대로

                // 링크 안에 게시글 정보 넣기
                link.innerHTML = `
                        <h3>${board.title}</h3>
                        <p>좋아요 수: ${board.boardLikeCount}</p>
                    `;

                // board-item을 링크로 감싸기
                boardItem.appendChild(link);
                boardListContainer.appendChild(boardItem);
            });
        }
    } catch (error) {
        console.error('Error:', error);
        alert('게시글을 불러오는 데 실패했습니다.');
    }
}

// 페이지 로드 시 게시글 목록 조회
window.onload = loadBoards;