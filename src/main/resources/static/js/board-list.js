// 게시글 목록 조회 (GET 요청)
async function loadBoards(keyword = '') {
    try {
        const response = await fetch(`/api/v1/board/search?keyword=${keyword}`);
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


// 검색 버튼 클릭 이벤트 핸들러
$(document).on("click", "#search-btn", function () {
    const keyword = $("#search-keyword").val();
    loadBoards(keyword); // 검색 조건을 기반으로 Board 데이터를 로드
});
// 페이지 로드 시 초기 데이터 로드
$(document).ready(function () {
    loadBoards(); // 초기에는 검색 조건 없이 전체 데이터 로드
});

// 리뷰 데이터 가져오기
async function loadReviews() {
    try {
        const response = await fetch(`/api/v1/review`);
        const reviews = await response.json();

        const boardListContainer = document.getElementById('board-list');
        boardListContainer.innerHTML = ''; // 기존 내용 초기화

        if (reviews.length === 0) {
            boardListContainer.innerHTML = '<p class="no-boards">등록된 리뷰가 없습니다.</p>';
        } else {
            reviews.forEach(review => {
                const reviewItem = document.createElement('div');
                reviewItem.classList.add('board-item');

                // 리뷰 내용을 표시
                reviewItem.innerHTML = `
                    <h3>${review.title}</h3>
                    <p>${review.content}</p>
                    <p>평점: ${review.rating}</p>
                `;

                boardListContainer.appendChild(reviewItem);
            });
        }
    } catch (error) {
        console.error('Error:', error);
        alert('리뷰를 불러오는 데 실패했습니다.');
    }
}

// 리뷰 조회 버튼 클릭 이벤트 핸들러
$(document).on("click", "#review-button", function () {
    loadReviews(); // 리뷰 데이터 로드
});
$(document).on("click", "#board-button", function () {
    loadBoards(); // 리뷰 데이터 로드
});