$(document).ready(function () {
    // 배너 이미지 조회 함수 호출
    fetchBannerImages();
});

// 배너 이미지 조회 함수
function fetchBannerImages() {
    fetch('/api/v1/userImage/banner', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => response.ok ? response.json() : Promise.reject('배너 이미지 조회 실패'))
        .then(data => updateBannerImages(data))
        .catch(error => console.error('이미지를 불러오는 데 실패:', error));
}

// 배너 이미지 업데이트 함수
function updateBannerImages(data) {
    const bannerContainer = document.getElementById('banner-container');
    if (data.length === 0) {
        bannerContainer.innerHTML = `<p class="text-center text-muted">등록된 배너 이미지가 없습니다.</p>`;
        return;
    }
    data.forEach((banner, index) => {
        const bannerElement = document.createElement('div');
        bannerElement.className = index === 0 ? 'carousel-item active' : 'carousel-item';
        bannerElement.innerHTML = `<img src="${banner.imageUrl}" class="d-block w-100" alt="배너 이미지">`;
        bannerContainer.appendChild(bannerElement);
    });
}