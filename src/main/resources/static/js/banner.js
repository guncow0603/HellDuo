document.getElementById('upload-btn').addEventListener('click', function () {
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

    fetch("/api/v1/userImage/banner", { // URL 수정 필요
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(err => { throw new Error(err.message || '이미지 업로드 실패'); });
            }
        })
        .then(data => {
            const resultDiv = document.getElementById('upload-result');
            resultDiv.innerHTML = `<p class="text-success">업로드 성공: ${JSON.stringify(data)}</p>`;
        })
        .catch(error => {
            console.error(error);
            document.getElementById('upload-result').innerHTML = `<p class="text-danger">업로드 실패: ${error.message}</p>`;
        });
});