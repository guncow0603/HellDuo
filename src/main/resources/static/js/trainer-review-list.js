
// API를 호출하여 트레이너 정보를 로드하는 함수
const trainerId = window.location.pathname.split("/").filter(Boolean).pop(); // URL에서 trainerId 추출

function loadReviews() {
    $.ajax({
        url: `/api/v1/review/trainer/${trainerId}`,
        type: "GET",
        success: function (response) {
            renderReviews(response);
        },
        error: function (xhr, status, error) {
            console.error("Review 데이터를 가져오는 중 오류 발생:", xhr.responseText || error);
            $('#reviews-list').html("<p>리뷰 데이터를 불러오는 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>");
        },
    });
}

function renderReviews(reviews) {
    const reviewListContainer = $('#reviews-list');
    reviewListContainer.empty();

    if (!reviews || reviews.length === 0) {
        reviewListContainer.append("<p>등록된 리뷰가 없습니다.</p>");
        return;
    }

    reviews.forEach(review => {
        const reviewItem = $(`
            <div class="review-item" style="cursor: pointer;" onclick="window.location.href='/api/v1/page/reviewRead/${review.reviewId}'">
                <img alt="썸네일 이미지" id="thumbnail-${review.reviewId}" 
                    style="width: 150px; height: 150px; border-radius: 5px; margin-right: 20px;">
                
                <div class="review-details">
                    <h5>${review.title}</h5>
                    <p class="review-rating">평점: ${review.rating}⭐</p>
                </div>
            </div>
        `);

        reviewListContainer.append(reviewItem);
        loadThumbnail(review.reviewId); // 이미지 로드
    });
}

function loadThumbnail(reviewId) {
    $.ajax({
        url: `/api/v1/reviews/${reviewId}/images/thumbnail`,
        type: "GET",
        success: function (response) {
            if (response.imageUrl) {
                $(`#thumbnail-${reviewId}`).attr("src", response.imageUrl);
            } else {
                console.error("이미지 URL이 응답에 포함되어 있지 않습니다.");
                $(`#thumbnail-${reviewId}`).attr("src", "/path/to/default-image.jpg");
            }
        },
        error: function (xhr, status, error) {
            console.error(`썸네일 이미지를 가져오는 중 오류 발생 (Review ID: ${reviewId}):`, error);
            $(`#thumbnail-${reviewId}`).attr("src", "/path/to/default-image.jpg");
        }
    });
}

// 페이지 로드 시 초기 데이터 로드
$(document).ready(function () {
    loadReviews();
});
