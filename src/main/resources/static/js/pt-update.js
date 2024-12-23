$(document).ready(function() {
    const ptId = window.location.pathname.split("/").pop(); // URL에서 ptId 추출

    // PT 정보를 수정하기 위한 버튼 클릭 이벤트 처리
    $('#update-btn').click(function(e) {
        e.preventDefault();  // 기본 폼 제출 방지

        const title = $('#title').val() || null;
        const specialization = $('#specialization').val() || null;
        const scheduledDate = $('#scheduledDate').val() || null;
        const price = $('#price').val() || null;
        const description = $('#description').val() || null;
        const latitude = $('#latitude').val() ? parseFloat($('#latitude').val()) : null;
        const longitude = $('#longitude').val() ? parseFloat($('#longitude').val()) : null;


        // PUT 요청을 통해 서버에 데이터 전송
        $.ajax({
            url: `/api/v2/pt/${ptId}`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                title: title,
                specialization: specialization,
                scheduledDate: scheduledDate,
                price: price,
                description: description,
                latitude: latitude,
                longitude: longitude
            })
        })
            .done(function (res) {
                alert(res.msg);
                window.location.replace(`/api/v2/page/ptRead/${ptId}`);
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            });
    });

    // 카카오 맵 초기화
    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 기본 위치
        level: 3
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);

    const marker = new kakao.maps.Marker({
        position: map.getCenter()
    });
    marker.setMap(map);

    // 검색 버튼 클릭
    $('#searchButton').on('click', function () {
        const query = $('#searchInput').val();
        if (query.length >= 2) {
            const places = new kakao.maps.services.Places();
            places.keywordSearch(query, function (data, status) {
                if (status === kakao.maps.services.Status.OK) {
                    displaySearchResults(data);
                } else {
                    alert('검색 결과가 없습니다.');
                }
            });
        } else {
            alert('검색어를 두 글자 이상 입력해주세요.');
        }
    });

    // 검색 결과 표시
    function displaySearchResults(data) {
        const searchResults = $('#searchResults');
        searchResults.empty();

        data.slice(0, 10).forEach(function (place) {
            const resultItem = $('<div></div>')
                .addClass('result-item')
                .text(place.place_name)
                .on('click', function () {
                    selectPlace(place);
                });
            searchResults.append(resultItem);
        });
    }

    // 장소 선택
    function selectPlace(place) {
        const latLng = new kakao.maps.LatLng(place.y, place.x);
        map.panTo(latLng);
        marker.setPosition(latLng);

        // 위치 입력란에 값 설정
        $('#latitude').val(place.y);
        $('#longitude').val(place.x);
        $('#searchResults').empty();
    }
});