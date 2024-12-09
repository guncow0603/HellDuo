// PT 데이터를 서버에서 가져오는 함수
function loadPTs(keyword = "", category = "", sortBy = "createdAt", isAsc = false) {
    $.ajax({
        url: "/api/v1/pt/search",
        type: "GET",
        data: { keyword, category, sortBy, isAsc },
        success: function (response) {
            renderPTs(response);
        },
        error: function (xhr, status, error) {
            console.error("PT 데이터를 가져오는 중 오류 발생:", error);
        },
    });
}

// PT 데이터를 화면에 렌더링하는 함수
function renderPTs(ptList) {
    const ptListContainer = $("#pt-list");
    ptListContainer.empty(); // 기존 내용을 비움

    if (!ptList || ptList.length === 0) {
        ptListContainer.append("<p>등록된 PT가 없습니다.</p>");
        return;
    }

    ptList.forEach(pt => {
        const ptElement = `
                <div class="pt-item" style="cursor: pointer;" onclick="window.location.href='/api/v1/page/ptRead/${pt.ptId}'">
                    <h5>${pt.title}</h5>
                    <p>전문 분야: ${pt.specialization}</p>
                    <p>가격: ${pt.price} 원</p>
                    <p>일정: ${new Date(pt.scheduledDate).toLocaleString()}</p>
                    <p>상태: <span class="pt-status">${pt.ptStatus}</span></p>
                </div>
            `;
        ptListContainer.append(ptElement); // PT 목록 추가
    });
}

// 페이지 로드 시 실행
$(document).ready(function () {
    const auth = getToken();

    // 로그인 인증 및 역할에 따른 화면 표시
    if (auth) {
        const role = getUserRole();
        if (role === "TRAINER") {
            $("#pt-create-button").show();
        } else {
            $("#pt-create-button").hide();
        }
    } else {
        $("#pt-create-button").hide();
    }

    // PT 리스트 초기 로드
    loadPTs();

    // 검색 버튼 클릭 이벤트 핸들러
    $(document).on("click", "#search-btn", function () {
        const keyword = $("#search-keyword").val();
        const category = $("#search-category").val();
        loadPTs(keyword, category); // 검색 조건을 기반으로 PT 데이터 로드
    });
});