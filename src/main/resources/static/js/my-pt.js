// PT 데이터를 서버에서 가져오는 함수
function loadPTs() {
    $.ajax({
        url: "/api/v1/pt/myPt",  // PT 목록을 가져올 API 경로
        type: "GET",
        success: function (response) {
            renderPTs(response);  // 서버에서 받은 데이터를 화면에 표시
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

    pts.forEach(pt => {
        const ptItem = $(`
                    <div class="pt-item" style="cursor: pointer;" onclick="window.location.href='/api/v1/page/ptRead/${pt.ptId}'">
                        <div>
                            <h5>${pt.title}</h5>
                            <p class="pt-specialization">전문 분야: ${pt.specialization}</p>
                            <p class="pt-date">예약 날짜: ${new Date(pt.scheduledDate).toLocaleString()}</p>
                        </div>
                        <div>
                            <p class="pt-price">${pt.price.toLocaleString()}원</p>
                            <p class="pt-status">${pt.ptStatus}</p>
                        </div>
                    </div>
                `);

        ptListContainer.append(ptItem);
    });
}

// 페이지 로드 시 PT 목록을 자동으로 불러오기
$(document).ready(function () {
    loadPTs();
});