// 배너 이미지 조회 함수
function fetchBannerImages() {
    $.ajax({
        url: `/api/v2/images/banner/1`,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            const bannerListDiv = $('#banner-list');
            bannerListDiv.empty(); // 기존 목록 초기화
            data.forEach(function (banner) {
                const bannerDiv = $('<div></div>').addClass('banner-item');
                bannerDiv.html(`
                    <img src="${banner.imageUrl}" alt="배너 이미지" class="img-fluid" style="max-width: 200px;">
                    <button class="btn btn-danger mt-2" onclick="deleteBanner(${banner.imageId})">삭제</button>
                `);
                bannerListDiv.append(bannerDiv);
            });
        },
        error: function (xhr, status, error) {
            console.error('배너 이미지 조회 실패:', error);
        }
    });
}

// 배너 이미지 삭제 함수
function deleteBanner(bannerId) {
    if (confirm('정말로 이 배너 이미지를 삭제하시겠습니까?')) {
        $.ajax({
            url: `/api/v2/images/${bannerId}`,
            method: 'DELETE',
            dataType: 'json',
            success: function (data) {
                alert('배너 이미지 삭제 성공');
                fetchBannerImages(); // 이미지 삭제 후 리스트 다시 조회
            },
            error: function (xhr, status, error) {
                alert('배너 이미지 삭제 실패');
                console.error(error);
            }
        });
    }
}

// 배너 이미지 업로드
$(document).ready(function () {
    const uploadBtn = $('#upload-btn');
    if (uploadBtn.length) {
        uploadBtn.on('click', function () {
            const bannerInput = $('#banner-images')[0];
            const files = bannerInput.files;

            // 파일 입력 확인
            if (!files || files.length === 0) {
                alert('업로드할 이미지를 선택해주세요.');
                return;
            }

            const formData = new FormData();
            Array.from(files).forEach(file => formData.append('files', file)); // 'files'로 추가

            uploadBtn.prop('disabled', true); // 버튼 비활성화
            $('#upload-result').html('<p class="text-info">업로드 중...</p>');

            $.ajax({
                url: `/api/v2/images/banner/1`,
                method: 'POST',
                processData: false,
                contentType: false,
                data: formData,
                success: function (data) {
                    $('#upload-result').html(`<p class="text-success">업로드 성공: ${data.message || '배너 이미지가 업로드되었습니다.'}</p>`);
                    fetchBannerImages(); // 업로드 후 배너 이미지 목록 갱신
                },
                error: function (xhr) {
                    const errorMessage = xhr.responseJSON?.message || `HTTP ${xhr.status}: ${xhr.statusText}`;
                    $('#upload-result').html(`<p class="text-danger">업로드 실패: ${errorMessage}</p>`);
                },
                complete: function () {
                    uploadBtn.prop('disabled', false); // 버튼 활성화
                }
            });
        });
    }

    // 페이지 로딩 시 배너 이미지 목록 조회
    fetchBannerImages();
});
