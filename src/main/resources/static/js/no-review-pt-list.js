// PT 데이터를 가져오는 함수
function loadPTs() {
    $.ajax({
        url: `/api/v1/pt/completedPTs`,
        type: "GET",
        success: function (response) {
            renderPTs(response);
        },
        error: function (xhr, status, error) {
            console.error("PT 데이터를 가져오는 중 오류 발생:", xhr.responseText || error);
            alert("PT 데이터를 불러오는 데 실패했습니다. 다시 시도해주세요.");
        },
    });
}
// 리뷰 버튼 클릭 이벤트 핸들러
function handleReviewButton(event, ptId) {
    event.stopPropagation(); // 클릭 이벤트 전파를 막음
    window.location.href = `/api/v1/page/reviewCreate/${ptId}`;
}
// PT 데이터를 HTML에 표시하는 함수
function renderPTs(pts) {
    const ptListContainer = $('#pt-list');
    ptListContainer.empty(); // 기존 항목들 지우기

    if (!pts || pts.length === 0) {
        ptListContainer.append("<p>리뷰를 등록할 완료된 피티가 없습니다.</p>");
        return;
    }

    pts.forEach(pt => {
        const ptItem = $(`
                    <div class="pt-item" onclick="window.location.href='/api/v1/page/ptRead/${pt.ptId}'">
                        <img alt="썸네일 이미지" id="thumbnail-${pt.ptId}">
                        <div class="pt-details">
                            <h5>${pt.title}</h5>
                            <p class="pt-specialization">전문 분야: ${pt.specialization}</p>
                            <p class="pt-date">예약 날짜: ${new Date(pt.scheduledDate).toLocaleString()}</p>
                        </div>
                        <div class="pt-price-status">
                            <p class="pt-price">${pt.price.toLocaleString()}원</p>
                            <p class="pt-status">${pt.ptStatus}</p>
                            ${pt.ptStatus === '완료됨' ? `
                                <button class="review-button" onclick="handleReviewButton(event, '${pt.ptId}')">후기 쓰기</button>
                            ` : ''}
                        </div>
                    </div>
                `);
        ptListContainer.append(ptItem);
        loadThumbnail(pt.ptId); // 썸네일 이미지 로드
    });
}

// PT 썸네일 이미지를 로드하는 함수
function loadThumbnail(ptId) {
    $.ajax({
        url: `/api/v2/images/pt/${ptId}/thumbnail`,
        type: "GET",
        success: function (response) {
            const imgElement = $(`#thumbnail-${ptId}`);
            if (response.imageUrl) {
                imgElement.attr("src", response.imageUrl);
            }
        },
        error: function (xhr, status, error) {
            const imgElement = $(`#thumbnail-${ptId}`);
            console.error(`썸네일 이미지를 가져오는 중 오류 발생 (PT ID: ${ptId}):`, error);
            imgElement.attr("src", "/path/to/default-image.jpg");
        }
    });
}

// 페이지가 로드될 때 자동으로 PT 정보를 가져오기
$(document).ready(function() {
    loadPTs();  // PT 목록을 가져오는 함수 호출
});