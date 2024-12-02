// 페이지 로드 시 실행
$(document).ready(function() {


    const role = getUserRole();
    const auth = getToken();

    // 로그인 인증 및 역할에 따른 화면 표시
    if (auth !== undefined && auth !== '') {
        if (role === 'TRAINER') {
            $('#pt-create').show();
        } else if (role === 'USER') {
            $('#pt-create').hide();
        }
    }

    loadPTs();
});

// PT 목록을 불러오는 함수
$(document).ready(function() {
    loadPTs();  // PT 리스트를 로드하는 함수

    // PT 리스트가 제대로 표시될 수 있도록 위치를 확인
    function loadPTs() {
        $.ajax({
            url: '/api/v1/pt',  // API 호출
            method: 'GET',
            success: function(response) {
                const ptListContainer = $('#pt-list');
                ptListContainer.empty(); // 기존 목록 비우기

                if (response && response.length > 0) {
                    response.forEach(pt => {
                        const ptElement = `
                            <div class="pt-item">
                                <h5>${pt.title}</h5>
                                <p>전문 분야: ${pt.specialization}</p>
                                <p>가격: ${pt.price} 원</p>
                                <p>일정: ${new Date(pt.scheduledDate).toLocaleString()}</p>
                                <p>상태: <span class="pt-status">${pt.ptStatus}</span></p>
                            </div>
                        `;
                        ptListContainer.append(ptElement);  // PT 목록 추가
                    });
                } else {
                    ptListContainer.append('<p>등록된 피티가 없습니다.</p>');
                }
            },
            error: function(error) {
                alert('피티 목록을 불러오는 데 실패했습니다.');
                console.error(error);
            }
        });
    }
});