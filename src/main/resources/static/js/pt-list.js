function loadPTs() {
    $.ajax({
        url: "/api/v1/pt/myPt",
        type: "GET",
        success: function (response) {
            renderPTs(response);
        },
        error: function (xhr, status, error) {
            console.error("PT 데이터를 가져오는 중 오류 발생:", xhr.responseText || error);
        },
    });
}

function renderPTs(pts) {
    const ptListContainer = $('#pt-list');
    ptListContainer.empty();

    if (!pts || pts.length === 0) {
        ptListContainer.append("<p>등록된 PT가 없습니다.</p>");
        return;
    }

    pts.forEach(pt => {
        const ptItem = $(`
    <div class="pt-item" style="cursor: pointer;" onclick="window.location.href='/api/v1/page/ptRead/${pt.ptId}'">
        <img alt="썸네일 이미지" id="thumbnail-${pt.ptId}" 
             style="width: 150px; height: 150px; border-radius: 5px; margin-right: 20px;">
        
        <div class="pt-details">
            <h5>${pt.title}</h5>
            <p class="pt-specialization">전문 분야: ${pt.specialization}</p>
            <p class="pt-date">예약 날짜: ${new Date(pt.scheduledDate).toLocaleString()}</p>
        </div>
        
        <div class="pt-price-status">
            <p class="pt-price">${pt.price.toLocaleString()}원</p>
            <p class="pt-status">${pt.ptStatus}</p>
        </div>
    </div>
`);

        ptListContainer.append(ptItem);
        loadThumbnail(pt.ptId); // 이미지 로드
    });
}

function loadThumbnail(ptId) {
    $.ajax({
        url: `/api/v1/userImage/pt/thumbnail/${ptId}`,
        type: "GET",
        success: function (response) {
            if (response.imageUrl) {
                $(`#thumbnail-${ptId}`).attr("src", response.imageUrl);
            } else {
                console.error("이미지 URL이 응답에 포함되어 있지 않습니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error(`썸네일 이미지를 가져오는 중 오류 발생 (PT ID: ${ptId}):`, error);
            $(`#thumbnail-${ptId}`).attr("src", "/path/to/default-image.jpg");
        }
    });
}

$(document).ready(function () {
    loadPTs();
});