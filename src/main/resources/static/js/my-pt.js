// 선택된 PT 상태를 저장하는 변수
let selectedStatus = "SCHEDULED"; // 기본 상태를 SCHEDULED로 설정

// PT 데이터를 서버에서 가져오는 함수
function loadPTs() {
    $.ajax({
        url: `/api/v1/pt/myPt?status=${selectedStatus}`, // 선택한 상태를 쿼리 매개변수로 전달
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