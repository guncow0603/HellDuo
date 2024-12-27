// 선택된 PT 상태를 저장하는 변수
let selectedStatus = "SCHEDULED"; // 기본 상태를 SCHEDULED로 설정

// PT 데이터를 서버에서 가져오는 함수
function loadPTs() {
    $.ajax({
        url: `/api/v2/pt/myPt?status=${selectedStatus}`, // 선택한 상태를 쿼리 매개변수로 전달
        type: "GET",
        success: function (response) {
            renderPTs(response); // 서버에서 받은 데이터를 화면에 표시
        },
        error: function (xhr, status, error) {
            console.error("PT 데이터를 가져오는 중 오류 발생:", error);
        },
    });
}
// PT 항목들을 화면에 렌더링하는 함수
function renderPTs(pts) {
    const ptListContainer = $('#pt-list');
    ptListContainer.empty(); // 기존의 PT 목록을 초기화

    if (pts.length === 0) {
        ptListContainer.append("<p>조회된 PT가 없습니다.</p>");
        return;
    }

    pts.forEach(pt => {
        const ptItem = $(`
    <div class="pt-item" style="cursor: pointer;" onclick="window.location.href='/api/v2/page/ptRead/${pt.ptId}'">
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
            ${pt.ptStatus === '완료됨' ? `
            <button class="review-button" onclick="handleReviewButton(event, '${pt.ptId}')">리뷰 쓰기</button>
            ` : ''}
        </div>
    </div>
`);
        ptListContainer.append(ptItem);
        loadThumbnail(pt.ptId); // 이미지 로드
    });
}

// 리뷰 버튼 클릭 이벤트 핸들러
function handleReviewButton(event, ptId) {
    event.stopPropagation(); // 클릭 이벤트 전파를 막음
    window.location.href = `/api/v2/page/reviewCreate/${ptId}`;
}

// 썸네일 이미지를 불러오는 함수
function loadThumbnail(ptId) {
    $.ajax({
        url: `/api/v2/images/pt/${ptId}/thumbnail`,
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
        }
    });
}

// PT 상태를 변경하는 함수
function changeStatus(status) {
    selectedStatus = status; // 선택한 상태를 업데이트
    loadPTs(); // 상태에 따라 PT 목록 다시 불러오기
}

// 페이지 로드 시 PT 목록을 자동으로 불러오기
$(document).ready(function () {
    loadPTs();

    // 상태 변경 버튼에 클릭 이벤트 추가
    $(".status-filter").click(function () {
        const status = $(this).data("status"); // 버튼의 데이터 속성에서 상태 가져오기
        changeStatus(status);
    });
});