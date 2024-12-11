const boardId = window.location.pathname.split("/").pop();  // boardId 추출
// 특정 게시글 조회 함수
async function getBoardById(boardId) {
    try {
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

// 댓글 목록 조회 함수
function loadComments(boardId) {
    $.ajax({
        url: `/api/v1/comment/${boardId}`,
        type: 'GET',
        contentType: 'application/json',
        success: function(response) {
            $('#comment-list').empty();  // 기존 댓글 삭제
            response.forEach(function(comment) {
                $('#comment-list').append(`
                    <div class="comment-item" id="comment-${comment.id}">
                        <div class="user">${comment.userNickname}</div>
                        <div class="content">${comment.content}</div>
                        <button class="btn btn-sm btn-primary" onclick="editComment(${comment.commentId}, '${comment.content}')">수정</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteComment(${comment.commentId})">삭제</button>
                    </div>
                `);
            });
        },
        error: function(error) {
            console.error('댓글을 불러오는 데 실패했습니다', error);
        }
    });
}

// 댓글 수정 함수
function editComment(commentId, currentContent) {
    const newContent = prompt('댓글을 수정하세요:', currentContent);
    if (newContent && newContent !== currentContent) {
        $.ajax({
            url: `/api/v1/comment/${commentId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ content: newContent }),
            success: function(response) {
                alert('댓글이 수정되었습니다.');
                loadComments(boardId);  // 댓글 목록 새로고침
            },
            error: function(error) {
                alert('댓글 수정에 실패했습니다.');
            }
        });
    }
}

// 댓글 삭제 함수
function deleteComment(commentId) {
    if (confirm('정말 삭제하시겠습니까?')) {
        $.ajax({
            url: `/api/v1/comment/${commentId}`,
            type: 'DELETE',
            success: function(response) {
                alert('댓글이 삭제되었습니다.');
                loadComments(boardId);  // 댓글 목록 새로고침
            },
            error: function(error) {
                alert('댓글 삭제에 실패했습니다.');
            }
        });
    }
}

$(document).ready(function() {

    // 특정 게시글 조회 함수 호출
    getBoardById(boardId);

    // 댓글 작성 요청
    $('#comment-submit').click(function() {
        const content = $('#comment-input').val().trim();
        if (content) {
            $.ajax({
                url: '/api/v1/comment',  // 댓글 작성 API
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    content: content,
                    boardId: boardId
                }),
                success: function(response) {
                    alert(response.msg);  // 서버에서 응답한 메시지 출력
                    $('#comment-input').val('');  // 댓글 작성 후 입력 필드 비우기
                    loadComments(boardId);  // 댓글 목록 새로고침
                },
                error: function(error) {
                    alert('댓글 작성에 실패했습니다.');
                }
            });
        } else {
            alert('댓글을 입력해주세요.');
        }
    });

    // 페이지 로드 시 댓글 목록 조회
    loadComments(boardId);
});