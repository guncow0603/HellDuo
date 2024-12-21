// URL 경로에서 리뷰 ID 추출
const reviewId = window.location.pathname.split("/").pop();

// 리뷰 조회 함수
async function getReviewById(reviewId) {
    try {
        const response = await fetch(`/api/v1/review/${reviewId}`);
        if (!response.ok) {
            throw new Error('리뷰를 불러오는 데 실패했습니다.');
        }
        const review = await response.json();
        renderReviewDetails(review);
        fetchReviewImages(reviewId); // 이미지 조회 호출
    } catch (error) {
        console.error('리뷰 조회 오류:', error);
        alert('리뷰를 불러오는 중 오류가 발생했습니다.');
    }
}

// 리뷰 상세 내용 렌더링
function renderReviewDetails(review) {
    const reviewDetails = document.getElementById('review-details');
    reviewDetails.innerHTML = `
        <div class="review-content">
            <h2 class="review-title">${review.title}</h2>
            <p><strong>내용:</strong> ${review.content}</p>
            <p><strong>평점:</strong> ${review.rating} ⭐</p>
            <div id="review-images" class="mt-4">
                <div id="image-container" class="row"></div>
            </div>
        </div>
    `;
}

// 이미지 조회 함수 (AJAX 사용)
function fetchReviewImages(reviewId) {
    $.ajax({
        url: `/api/v2/images/review/${reviewId}`, // URL 일관성 유지
        method: 'GET',
        success: function (imageList) {
            renderImages(imageList);
        },
        error: function (xhr, status, error) {
            console.error("이미지 조회 오류:", error);
            alert("이미지를 불러오는 중 오류가 발생했습니다.");
        }
    });
}

// 이미지 렌더링 함수
function renderImages(imageList) {
    const container = $('#image-container');
    container.empty();

    if (!imageList || imageList.length === 0) {
        container.append('<p class="text-muted">첨부된 이미지가 없습니다.</p>');
        return;
    }

    imageList.forEach(image => {
        container.append(`
            <div class="col-md-4 mb-3">
                <img src="${image.imageUrl}" class="img-fluid rounded shadow-sm" alt="리뷰 이미지">
            </div>
        `);
    });
}

// 문서 로드 시 리뷰 조회 실행
$(document).ready(function () {
    if (reviewId) { // 리뷰 ID가 존재하는지 확인
        getReviewById(reviewId);
    } else {
        alert("유효하지 않은 리뷰 ID입니다.");
    }
});