const boardId = window.location.pathname.split("/").pop(); // 게시글 ID 추출
var userId;
// 게시글 조회
async function getBoardById(boardId) {
    try {
        const response = await fetch(`/api/v2/board/${boardId}`);
        if (response.ok) {
            const board = await response.json(); // BoardReadRes 객체
            renderBoardDetails(board);
            renderComments(board.commentList); // 댓글 렌더링
            fetchBoardImages(boardId); // 이미지 호출 추가
        } else {
            alert('게시글을 불러오는 데 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('게시글을 불러오는 중 오류가 발생했습니다.');
    }
}

// 게시글 렌더링
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
    userId = board.userId;
}

// 댓글 렌더링
function renderComments(commentList) {
    const commentListContainer = document.getElementById('comment-list');
    commentListContainer.innerHTML = ''; // 이전 댓글 목록 초기화

    if (commentList.length === 0) {
        commentListContainer.innerHTML = '<p>댓글이 없습니다.</p>';
    } else {
        commentList.forEach(comment => {
            const commentItem = document.createElement('div');
            commentItem.classList.add('comment-item');
            commentItem.innerHTML = `
                <div class="user">${comment.userNickname}</div>
                <div class="content">${comment.content}</div>
                <button class="btn btn-warning edit-comment-btn" data-comment-id="${comment.commentId}">수정</button>
                <button class="btn btn-danger delete-comment-btn" data-comment-id="${comment.commentId}">삭제</button>
            `;
            commentListContainer.appendChild(commentItem);
        });
    }
}

// 댓글 수정 및 삭제 처리
document.addEventListener('click', function (e) {
    if (e.target.classList.contains('edit-comment-btn')) {
        const commentId = e.target.getAttribute('data-comment-id');
        const commentContent = e.target.previousElementSibling.innerText;
        editComment(commentId, commentContent);
    }

    if (e.target.classList.contains('delete-comment-btn')) {
        const commentId = e.target.getAttribute('data-comment-id');
        deleteComment(commentId);
    }
});

// 이미지 조회 및 렌더링
function fetchBoardImages(boardId) {
    $.ajax({
        url: `/api/v2/images/board/${boardId}`,
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



// 댓글 수정
function editComment(commentId, currentContent) {
    const newContent = prompt('댓글을 수정하세요:', currentContent);
    if (newContent && newContent !== currentContent) {
        $.ajax({
            url: `/api/v2/comment/${commentId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ content: newContent })
        })
            .done(function (res) {
                alert(res.msg);
                getBoardById(boardId);
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            });
    }
}

// 댓글 삭제
function deleteComment(commentId) {
    if (confirm('정말 삭제하시겠습니까?')) {
        $.ajax({
            url: `/api/v2/comment/${commentId}`,
            type: 'DELETE'
        })
            .done(function (res) {
                alert(res.msg);
                getBoardById(boardId);
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            });
    }
}

// 게시글 좋아요
function likeBoard() {
    $.ajax({
        url: `/api/v2/boardLike/${boardId}`,
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

    // 좋아요 버튼 이벤트
    $(document).on('click', '#likeBoardBtn', likeBoard);

    // 댓글 작성
    $('#comment-submit').click(function () {
        const content = $('#comment-input').val().trim();
        if (content) {
            $.ajax({
                url: '/api/v2/comment',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ content, boardId }),
                success: function (response) {
                    alert(response.msg);
                    $('#comment-input').val('');
                    getBoardById(boardId);
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
                url: `/api/v2/board/${boardId}`,
                type: 'DELETE',
                success: function (res) {
                    alert(res.msg);
                    window.location.href = '/api/v2/page/boardList';
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
        window.location.href = `/api/v2/page/boardUpdate/${boardId}`;
    });
    document.getElementById('report').addEventListener('click', () => {
        window.location.href = `/api/v2/page/reportCreate/${userId}`;
    });
});