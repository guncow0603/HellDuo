$(document).ready(function() {
    // 페이지가 로드될 때 리뷰 데이터를 가져오기 위한 AJAX 요청
    $.ajax({
        url: '/api/v1/review',  // GET 요청을 보낼 URL
        method: 'GET',
        success: function(response) {
            // 응답을 받은 후 리스트를 생성하여 HTML에 추가
            var reviewsList = $('#reviews-list');
            response.forEach(function(review) {
                // 각 리뷰에 대해 카드 형식으로 스타일링하여 추가
                reviewsList.append(
                    '<div class="review-card">' +
                    '<div class="review-title">' + review.title + '</div>' +
                    '<div class="review-content">' + review.content + '</div>' +
                    '<div class="review-rating">Rating: ' + review.rating + ' ⭐</div>' +
                    '</div>'
                );
            });
        },
        error: function() {
            alert('리뷰를 불러오는 데 실패했습니다.');
        }
    });
});