const boardId = window.location.pathname.split("/").pop(); // 게시글 ID 추출

// 게시글 조회
async function getBoardById(boardId) {
    try {
        const response = await fetch(`/api/v1/board/${boardId}`);
        if (response.ok) {
            const board = await response.json();
            renderBoardDetails(board);
            fetchBoardImages(boardId); // 이미지 호출 추가
        } else {
            alert('게시글을 불러오는 데 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('게시글을 불러오는 중 오류가 발생했습니다.');
    }
}

// 게시글 상세 내용 렌더링
function renderBoardDetails(board) {
    const boardDetails = document.getElementById('board-details');
    boardDetails.innerHTML = `
        <div class="board-content">
            <h2 class="board-title">${board.title}</h2>
            <p><strong>내용:</strong> ${board.content}</p>
            <div id="board-images" class="mt-4">
                <div id="image-container" class="row"></div>
            </div>
            <button class="btn btn-primary" id="likeBoardBtn" type="button">
                Likes: ${board.boardLikeCount}
            </button>
        </div>
    `;
}

// 이미지 조회 및 렌더링
function fetchBoardImages(boardId) {
    $.ajax({
        url: `/api/v1/boards/${boardId}/images`,
        type: 'GET',
        success: function (imageList) {
            renderImages(imageList);
        },
        error: function (error) {
            console.error("이미지 조회 오류:", error);
            alert("이미지를 불러오는 중 오류가 발생했습니다.");
        }
    });
}

function renderImages(imageList) {
    const container = $('#image-container');
    container.empty();

    if (!imageList.length) {
        container.append('<p>첨부된 이미지가 없습니다.</p>');
        return;
    }

    imageList.forEach(image => {
        container.append(`
            <div class="col-md-4 mb-3">
                <img src="${image.imageUrl}" class="img-fluid rounded" alt="게시판 이미지">
            </div>
        `);
    });
}

// 댓글 목록 조회
function loadComments(boardId) {
    $.ajax({
        url: `/api/v1/comment/${boardId}`,
        type: 'GET',
        success: function (comments) {
            renderComments(comments);
        },
        error: function () {
            alert('댓글을 불러오는 데 실패했습니다.');
        }
    });
}

// 댓글 렌더링
function renderComments(comments) {
    const commentList = $('#comment-list');
    commentList.empty();
    comments.forEach(comment => {
        commentList.append(`
            <div class="comment-item" id="comment-${comment.id}">
                <div class="user">${comment.userNickname}</div>
                <div class="content">${comment.content}</div>
                <button class="btn btn-sm btn-primary" onclick="editComment(${comment.commentId}, '${comment.content}')">수정</button>
                <button class="btn btn-sm btn-danger" onclick="deleteComment(${comment.commentId})">삭제</button>
            </div>
        `);
    });
}

// 댓글 수정
function editComment(commentId, currentContent) {
    const newContent = prompt('댓글을 수정하세요:', currentContent);
    if (newContent && newContent !== currentContent) {
        $.ajax({
            url: `/api/v1/comment/${commentId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ content: newContent }),
            success: function () {
                alert('댓글이 수정되었습니다.');
                loadComments(boardId);
            },
            error: function () {
                alert('댓글 수정에 실패했습니다.');
            }
        });
    }
}

// 댓글 삭제
function deleteComment(commentId) {
    if (confirm('정말 삭제하시겠습니까?')) {
        $.ajax({
            url: `/api/v1/comment/${commentId}`,
            type: 'DELETE',
            success: function () {
                alert('댓글이 삭제되었습니다.');
                loadComments(boardId);
            },
            error: function () {
                alert('댓글 삭제에 실패했습니다.');
            }
        });
    }
}

// 게시글 좋아요
function likeBoard() {
    $.ajax({
        url: `/api/v1/boardLike/${boardId}`,
        method: 'POST',
        success: function () {
            getBoardById(boardId); // 게시글 재조회
        },
        error: function (res) {
            const messages = JSON.parse(res.responseText).messages || "좋아요 실패";
            alert(messages);
        }
    });
}

// 문서 로드 시 실행
$(document).ready(function () {
    getBoardById(boardId);
    loadComments(boardId);

    // 좋아요 버튼 이벤트
    $(document).on('click', '#likeBoardBtn', likeBoard);

    // 댓글 작성
    $('#comment-submit').click(function () {
        const content = $('#comment-input').val().trim();
        if (content) {
            $.ajax({
                url: '/api/v1/comment',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ content, boardId }),
                success: function (response) {
                    alert(response.msg);
                    $('#comment-input').val('');
                    loadComments(boardId);
                },
                error: function () {
                    alert('댓글 작성에 실패했습니다.');
                }
            });
        } else {
            alert('댓글을 입력해주세요.');
        }
    });

    // 게시글 삭제
    $('#delete-board-button').click(function (e) {
        e.preventDefault();
        if (confirm("게시글을 삭제하시겠습니까?")) {
            $.ajax({
                url: `/api/v1/board/${boardId}`,
                type: 'DELETE',
                success: function (res) {
                    alert(res.msg);
                    window.location.href = '/api/v1/page/boardList';
                },
                error: function (res) {
                    const messages = JSON.parse(res.responseText).messages || "삭제 실패";
                    alert(messages);
                }
            });
        }
    });

    // 게시글 수정 페이지로 이동
    $('#update-board-button').click(() => {
        window.location.href = `/api/v1/page/boardUpdate/${boardId}`;
    });
});