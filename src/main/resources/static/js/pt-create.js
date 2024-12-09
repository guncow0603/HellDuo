$(document).ready(function () {
    // PT 생성 폼 제출
    $('#ptForm').on('submit', function (event) {
        event.preventDefault();

        // 좌표 가져오기
        const latitude = parseFloat($('#latitude').val());
        const longitude = parseFloat($('#longitude').val());

        if (isNaN(latitude) || isNaN(longitude)) {
            alert('유효한 위치 정보를 입력해주세요.');
            return;
        }

        // 폼 데이터 수집
        const formData = {
            title: $('#title').val(),
            specialization: $('#specialization').val(),
            scheduledDate: $('#scheduledDate').val(),
            price: parseInt($('#price').val(), 10),
            description: $('#description').val(),
            address: $('#address').val(), // 주소도 폼 데이터에 추가
            latitude: latitude,
            longitude: longitude
        };

        // AJAX 요청
        $.ajax({
            url: '/api/v1/pt',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (res) {
                alert(res.msg);
                // PT 생성 후 이미지 업로드 요청
                uploadPTImages(res.ptId); // PT 생성 후 ID를 이용해 이미지 업로드
            },
            error: function (xhr) {
                $('#message').text('PT 생성 실패').addClass('error');
            }
        });
    });

    // PT 이미지 업로드 처리
    function uploadPTImages(ptId) {
        const formData = new FormData();
        const files = $('#pt-files')[0].files;

        // 선택된 파일이 있으면 FormData에 파일을 추가
        if (files.length > 0) {
            for (let i = 0; i < files.length; i++) {
                formData.append('files', files[i]);
            }

            // AJAX를 통해 서버로 파일 업로드
            $.ajax({
                url: `/api/v1/userImage/pt/${ptId}`, // PT ID를 포함한 URL로 요청
                method: 'POST',
                data: formData,
                processData: false,  // 파일을 자동으로 처리하지 않도록 설정
                contentType: false,  // 컨텐츠 타입을 자동으로 설정하지 않음
                success: function(res) {
                    alert(res.msg);
                    window.location.href = '/api/v1/page/ptList'; // 성공 시 리디렉션
                },
                error: function(xhr, status, error) {
                    alert("이미지 업로드 실패: " + xhr.responseText);
                }
            });
        } else {
            alert("업로드할 파일을 선택해 주세요.");
        }
    }

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
        $('#address').val(place.address_name); // 주소 입력란에 주소 설정
        $('#searchResults').empty();
    }
});