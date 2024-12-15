
// API를 호출하여 트레이너 정보를 로드하는 함수
const trainerId = window.location.pathname.split("/").filter(Boolean).pop(); // URL에서 trainerId 추출

async function loadTrainerReviews() {
    try {
        const response = await fetch(`/api/v1/review/trainer/${trainerId}`);
        if (!response.ok) {
            throw new Error('리뷰 데이터를 불러오는 중 오류가 발생했습니다.');
        }

        const reviews = await response.json();

        // 리뷰 목록을 렌더링
        const reviewList = document.getElementById('review-list');
        reviewList.innerHTML = ''; // 기존 리뷰 목록 초기화

        if (reviews.length === 0) {
            reviewList.innerHTML = '<li class="list-group-item text-muted">등록된 리뷰가 없습니다.</li>';
            return;
        }

        reviews.forEach(review => {
            const reviewItem = document.createElement('li');
            reviewItem.className = 'list-group-item';
            reviewItem.innerHTML = `
                    <strong>제목</strong>: ${review.title || "내용 없음"}
                    <strong>내용</strong>: ${review.content || "내용 없음"}
                    <div class="text-muted small">평점: ${review.rating || "0"}점</div>
                `;
            reviewList.appendChild(reviewItem);
        });
    } catch (error) {
        console.error(error);
        const reviewList = document.getElementById('review-list');
        reviewList.innerHTML = '<li class="list-group-item text-danger">리뷰를 불러오는 데 실패했습니다. 나중에 다시 시도해주세요.</li>';
    }
}

// 페이지가 로드되면 리뷰를 불러옴
document.addEventListener('DOMContentLoaded', loadTrainerReviews);