$(document).ready(function() {
    // 페이지가 로드될 때 리뷰 데이터를 가져오기 위한 AJAX 요청
    $.ajax({
        url: '/api/v1/review',  // 서버 API 엔드포인트
        method: 'GET',
        success: function(response) {
            // 응답을 받은 후 리스트를 생성하여 HTML에 추가
            var reviewsList = $('#reviews-list'); // 올바른 ID
            response.forEach(function(review) {
                reviewsList.append(
                    '<div class="review-item">' +  // 스타일링된 클래스 사용
                    '<a href="/api/v1/page/reviewRead/' + review.reviewId + '">' +
                    '<h3>' + review.title + '</h3>' +
                    '<p>' + review.content + '</p>' +
                    '<p>평점: ' + review.rating + ' ⭐</p>' +
                    '</a>' +
                    '</div>'
                );
            });
        },
        error: function() {
            alert('리뷰를 불러오는 데 실패했습니다.');
        }
    });
});