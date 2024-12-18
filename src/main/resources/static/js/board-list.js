function loadBoards(keyword = '') {
    $.ajax({
        url: `/api/v1/board/search?keyword=${keyword}`,
        type: "GET",
        success: function (response) {
            renderBoards(response);
        },
        error: function (xhr, status, error) {
            console.error("Board 데이터를 가져오는 중 오류 발생:", xhr.responseText || error);
        },
    });
}
function renderBoards(boards) {
    const boardListContainer = $('#board-list');
    boardListContainer.empty();

    if (!boards || boards.length === 0) {
        boardListContainer.append("<p>등록된 Board가 없습니다.</p>");
        return;
    }

    boards.forEach(board => {
        const boardItem = $(`
    <div class="board-item" style="cursor: pointer;" onclick="window.location.href='/api/v1/page/boardRead/${board.boardId}'">
        <img alt="썸네일 이미지" id="thumbnail-${board.boardId}" 
             style="width: 150px; height: 150px; border-radius: 5px; margin-right: 20px;">
        
        <div class="board-details">
            <h5>${board.title}</h5>
            <p class="board-like-count">좋아요 수 : ${board.boardLikeCount}</p>
        </div>
    </div>
`);

        boardListContainer.append(boardItem);
        loadThumbnail(board.boardId); // 이미지 로드
    });
}

function loadThumbnail(boardId) {
    $.ajax({
        url: `/api/v1/boards/${boardId}/images/thumbnail`,
        type: "GET",
        success: function (response) {
            if (response.imageUrl) {
                $(`#thumbnail-${boardId}`).attr("src", response.imageUrl);
            } else {
                console.error("이미지 URL이 응답에 포함되어 있지 않습니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error(`썸네일 이미지를 가져오는 중 오류 발생 (Board ID: ${boardId}):`, error);
            $(`#thumbnail-${boardId}`).attr("src", "/path/to/default-image.jpg");
        }
    });
}

// 검색 버튼 클릭 이벤트 핸들러
$(document).on("click", "#search-btn", function () {
    const keyword = $("#search-keyword").val();
    const category = $("#search-category").val();
    loadBoards(keyword); // 검색 조건을 기반으로 Board 데이터를 로드
});
// 페이지 로드 시 초기 데이터 로드
$(document).ready(function () {
    loadBoards(); // 초기에는 검색 조건 없이 전체 데이터 로드
});