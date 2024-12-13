$(document).ready(function () {
    // PT 생성 폼 제출
    $('#ptForm').on('submit', function (event) {
        event.preventDefault();

        // 좌표 가져오기
        const latitude = parseFloat($('#latitude').val());
        const longitude = parseFloat($('#longitude').val());

        if (!isValidLocation(latitude, longitude)) {
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
            address: $('#address').val(),
            latitude: latitude,
            longitude: longitude
        };

        if (!isValidFormData(formData)) {
            alert('모든 필수 항목을 입력해주세요.');
            return;
        }

        // AJAX 요청
        $.ajax({
            url: '/api/v1/pt',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (res) {
                // PT 생성 성공 시 이미지 업로드 요청
                uploadPTImages(res.ptId, function (uploadSuccess) {
                    if (uploadSuccess) {
                        alert("PT 생성 및 이미지 업로드 성공!");
                        window.location.href = '/api/v1/page/ptList';
                    } else {
                        // 이미지 업로드 실패 시 PT 생성 취소 요청
                        cancelPTCreation(res.ptId);
                    }
                });
            },
            error: function (xhr) {
                alert('PT 생성 실패: ' + xhr.responseText);
            }
        });
    });

    // 좌표 유효성 검사
    function isValidLocation(latitude, longitude) {
        return !isNaN(latitude) && !isNaN(longitude);
    }

    // 폼 데이터 유효성 검사
    function isValidFormData(formData) {
        return formData.title && formData.specialization && formData.scheduledDate &&
            formData.price && formData.description && formData.address;
    }

    // PT 이미지 업로드 처리
    function uploadPTImages(ptId, callback) {
        const formData = new FormData();
        const files = $('#pt-files')[0].files;

        if (files.length === 0) {
            alert("업로드할 파일을 선택해 주세요.");
            callback(false);
            return;
        }

        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        // AJAX를 통해 서버로 파일 업로드
        $.ajax({
            url: `/api/v1/userImage/pt/${ptId}`,
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                callback(true);
            },
            error: function (xhr) {
                alert("이미지 업로드 실패: " + xhr.responseText);
                callback(false);
            }
        });
    }

    // PT 생성 취소 요청
    function cancelPTCreation(ptId) {
        $.ajax({
            url: `/api/v1/pt/${ptId}`,
            method: 'DELETE',
            success: function () {
                alert('이미지 업로드 실패로 인해 PT 생성이 취소되었습니다.');
            },
            error: function (xhr) {
                alert('PT 생성 취소 실패: ' + xhr.responseText);
            }
        });
    }
    // 카카오 맵 초기화
    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);

    const marker = new kakao.maps.Marker({
        position: map.getCenter()
    });
    marker.setMap(map);

    // 검색 버튼 클릭
    $('#searchButton').on('click', function () {
        const query = $('#searchInput').val().trim();
        if (query.length >= 2) {
            searchPlaces(query);
        } else {
            alert('검색어를 두 글자 이상 입력해주세요.');
        }
    });

    // 장소 검색
    function searchPlaces(query) {
        const places = new kakao.maps.services.Places();
        places.keywordSearch(query, function (data, status) {
            if (status === kakao.maps.services.Status.OK) {
                displaySearchResults(data);
            } else {
                alert('검색 결과가 없습니다.');
            }
        });
    }

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
        $('#address').val(place.address_name);
        $('#searchResults').empty();
    }
});