// 배너 이미지 조회 함수
function fetchBannerImages() {
    fetch('/api/v1/userImage/banner')
        .then(response => response.json())
        .then(data => {
            const bannerListDiv = document.getElementById('banner-list');
            bannerListDiv.innerHTML = ''; // 기존 목록 초기화
            data.forEach(banner => {
                const bannerDiv = document.createElement('div');
                bannerDiv.classList.add('banner-item');
                bannerDiv.innerHTML = `
                    <img src="${banner.imageUrl}" alt="배너 이미지" class="img-fluid" style="max-width: 200px;">
                    <button class="btn btn-danger mt-2" onclick="deleteBanner(${banner.id})">삭제</button>
                `;
                bannerListDiv.appendChild(bannerDiv);
            });
        })
        .catch(error => console.error('배너 이미지 조회 실패:', error));
}

// 배너 이미지 삭제 함수
function deleteBanner(bannerId) {
    if (confirm('정말로 이 배너 이미지를 삭제하시겠습니까?')) {
        fetch(`/api/v1/userImage/banner/${bannerId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => response.json())
            .then(data => {
                alert('배너 이미지 삭제 성공');
                fetchBannerImages(); // 이미지 삭제 후 리스트 다시 조회
            })
            .catch(error => {
                alert('배너 이미지 삭제 실패');
                console.error(error);
            });
    }
}

// 배너 이미지 업로드
document.addEventListener('DOMContentLoaded', function () {
    const uploadBtn = document.getElementById('upload-btn');
    if (uploadBtn) {
        uploadBtn.addEventListener('click', function () {
            const bannerInput = document.getElementById('banner-images');
            const files = bannerInput.files;

            // 파일 입력 확인
            if (!files || files.length === 0) {
                alert('업로드할 이미지를 선택해주세요.');
                return;
            }

            const formData = new FormData();
            for (const file of files) {
                formData.append('banners', file);
            }

            fetch("/api/v1/userImage/banner", {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        return response.json().then(err => {
                            throw new Error(err.message || '이미지 업로드 실패');
                        });
                    }
                })
                .then(data => {
                    const resultDiv = document.getElementById('upload-result');
                    resultDiv.innerHTML = `<p class="text-success">업로드 성공: ${data.message || '배너 이미지가 업로드되었습니다.'}</p>`;
                    fetchBannerImages(); // 업로드 후 배너 이미지 목록 갱신
                })
                .catch(error => {
                    console.error(error);
                    document.getElementById('upload-result').innerHTML = `<p class="text-danger">업로드 실패: ${error.message}</p>`;
                });
        });
    }

    // 페이지 로딩 시 배너 이미지 목록 조회
    fetchBannerImages();
});