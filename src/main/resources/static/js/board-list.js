function renderBoards(boards) {
    const boardListContainer = $('#board-list');
    boardListContainer.empty();

    if (!boards || boards.content.length === 0) {
        boardListContainer.append("<p>등록된 Board가 없습니다.</p>");
        return;
    }

    // Board 항목 렌더링
    boards.content.forEach(board => {
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

    // 페이지네이션 처리 (필요한 경우 구현)
    renderPagination(boards.totalPages, boards.number + 1);
}

function renderPagination(totalPages, currentPage) {
    const paginationContainer = $('#pagination');
    paginationContainer.empty();

    if (totalPages <= 1) return; // 페이지가 하나 이하일 경우 페이지네이션 숨김

    // 이전 페이지 버튼
    if (currentPage > 1) {
        paginationContainer.append(`<li class="page-item"><a class="page-link" href="#" onclick="loadBoards('', ${currentPage - 1})">‹</a></li>`);
    }

    // 페이지 번호들
    for (let i = 1; i <= totalPages; i++) {
        paginationContainer.append(`
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="loadBoards('', ${i})">${i}</a>
            </li>
        `);
    }

    // 다음 페이지 버튼
    if (currentPage < totalPages) {
        paginationContainer.append(`<li class="page-item"><a class="page-link" href="#" onclick="loadBoards('', ${currentPage + 1})">›</a></li>`);
    }
}
function loadBoards(keyword = '', page = 1, size = 10, sortBy = 'createdAt', isAsc = false) {
    $.ajax({
        url: `/api/v2/board/search?keyword=${keyword}&page=${page}&size=${size}&sortBy=${sortBy}&isAsc=${isAsc}`,
        type: "GET",
        success: function (response) {
            renderBoards(response);
        },
        error: function (xhr, status, error) {
            console.error("Board 데이터를 가져오는 중 오류 발생:", xhr.responseText || error);
        },
    });
}


function loadThumbnail(boardId) {
    $.ajax({
        url: `/api/v2/images/board/${boardId}/thumbnail`,
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
    const page = 1; // 기본적으로 첫 페이지로 설정
    const size = 10; // 기본 페이지 크기
    const sortBy = "createdAt"; // 기본 정렬 기준
    const isAsc = false; // 기본 내림차순
    loadBoards(keyword, page, size, sortBy, isAsc); // 검색 조건을 기반으로 Board 데이터를 로드
});

// 페이지 로드 시 초기 데이터 로드
$(document).ready(function () {
    loadBoards(); // 초기에는 검색 조건 없이 전체 데이터 로드
});