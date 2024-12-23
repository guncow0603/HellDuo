function loadPTs(keyword = '', page = 1, size = 10, sortBy = 'createdAt', isAsc = false, category = '') {
    $.ajax({
        url: `/api/v2/pt/search?keyword=${keyword}&page=${page}&size=${size}&sortBy=${sortBy}&isAsc=${isAsc}&category=${category}`,
        type: "GET",
        success: function (response) {
            renderPTs(response.content, response.totalPages, response.number + 1); // 응답에서 content, totalPages, number 가져옴
        },
        error: function (xhr, status, error) {
            console.error("PT 데이터를 가져오는 중 오류 발생:", xhr.responseText || error);
        },
    });
}

function renderPagination(totalPages, currentPage) {
    const paginationContainer = $('#pagination');
    paginationContainer.empty();

    if (totalPages <= 1) return; // 페이지가 하나 이하일 경우 페이지네이션 숨김

    // 이전 페이지 버튼
    if (currentPage > 1) {
        paginationContainer.append(`<li class="page-item"><a class="page-link" href="#" onclick="loadPTs('', ${currentPage - 1})">‹</a></li>`);
    }

    // 페이지 번호들
    for (let i = 1; i <= totalPages; i++) {
        paginationContainer.append(`
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="loadPTs('', ${i})">${i}</a>
            </li>
        `);
    }

    // 다음 페이지 버튼
    if (currentPage < totalPages) {
        paginationContainer.append(`<li class="page-item"><a class="page-link" href="#" onclick="loadPTs('', ${currentPage + 1})">›</a></li>`);
    }
}

function renderPTs(pts, totalPages, currentPage) {
    const ptListContainer = $('#pt-list');
    ptListContainer.empty();

    if (!pts || pts.length === 0) {
        ptListContainer.append("<p>등록된 PT가 없습니다.</p>");
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
        </div>
    </div>
`);

        ptListContainer.append(ptItem);
        loadThumbnail(pt.ptId); // 이미지 로드
    });

    renderPagination(totalPages, currentPage); // 페이지네이션 처리
}

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
            $(`#thumbnail-${ptId}`).attr("src", "/path/to/default-image.jpg");
        }
    });
}

// 검색 버튼 클릭 이벤트 핸들러
$(document).on("click", "#search-btn", function () {
    const keyword = $("#search-keyword").val();
    const category = $("#search-category").val(); // 카테고리 값 추가
    loadPTs(keyword, 1, 10, 'createdAt', false, category); // 검색 조건을 기반으로 PT 데이터를 로드
});

// 페이지 로드 시 초기 데이터 로드
$(document).ready(function () {
    loadPTs(); // 초기에는 검색 조건 없이 전체 데이터 로드
});