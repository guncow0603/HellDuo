$(document).ready(function() {
    const auth = getToken();

    // 로그인 인증 및 역할에 따른 화면 표시
    if (auth !== undefined && auth !== '') {
        const role = getUserRole();
        if (role === 'TRAINER') {
            $('#delete-btn').show();
            $('#update-btn').show();
            $('#reserve-btn').hide();
        } else {
            $('#delete-btn').hide();
            $('#update-btn').hide();
            $('#reserve-btn').show();
        }
    } else {
        $('#delete-btn').hide();
        $('#update-btn').hide();
        $('#reserve-btn').hide();
        $('#chat-user').hide();
    }

    // URL에서 ptId를 가져옵니다.
    const ptId = window.location.pathname.split("/").pop(); // URL에서 ptId 추출
    var trainerId;

    // PT 정보 가져오기
    $.ajax({
        url: `/api/v1/pt/${ptId}`, // PT ID를 URL에 포함
        method: 'GET',
        success: function(res) {
            // 서버에서 받아온 데이터를 HTML 요소에 동적으로 삽입
            $('#pt-title').text(`PT 제목: ${res.title}`);
            $('#pt-description').text(`설명: ${res.description}`);
            $('#pt-specialization').text(`전문 분야: ${res.specialization}`);
            $('#pt-trainer').text(`트레이너: ${res.trainerName}`);
            $('#pt-user').text(`예약자: ${res.userName}`);
            $('#pt-scheduledDate').text(`예약 시간: ${new Date(res.scheduledDate).toLocaleString()}`);
            $('#pt-price').text(`PT 비용: ${res.price} 원`);
            $('#pt-status').text(`상태: ${res.status}`);
            trainerId=res.trainerId;

            // 위도와 경도 정보 받아서 카카오 맵 표시
            const latitude = res.latitude;  // 위도
            const longitude = res.longitude;  // 경도
            $('#pt-location').text(`위도: ${latitude}, 경도: ${longitude}`);

            // 카카오맵의 Geocoder를 사용하여 주소 변환
            const geocoder = new kakao.maps.services.Geocoder();
            geocoder.coord2Address(longitude, latitude, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    const address = result[0].address.address_name; // 주소 이름
                    $('#pt-location').text(`주소: ${address}`); // 주소 표시

                    // 카카오맵 표시
                    const mapContainer = document.getElementById('map'); // 지도를 표시할 div

                    // 지도 옵션 설정
                    const mapOption = {
                        center: new kakao.maps.LatLng(latitude, longitude), // 지도 중심 좌표
                        level: 3 // 지도의 확대 레벨
                    };

                    // 지도 생성
                    const map = new kakao.maps.Map(mapContainer, mapOption);

                    // 마커 생성
                    const marker = new kakao.maps.Marker({
                        position: new kakao.maps.LatLng(latitude, longitude), // 마커 위치
                        map: map // 마커를 지도에 표시
                    });

                    // 마커에 인포윈도우 표시
                    const infowindow = new kakao.maps.InfoWindow({
                        content: `<div style="padding:5px;">${address}</div>` // 마커 위에 표시될 내용
                    });
                    infowindow.open(map, marker);
                } else {
                    alert("주소를 찾을 수 없습니다.");
                }
            });
            // 사용자 위치 및 거리 계산 함수 호출
            getUserLocationAndCalculateDistance(latitude, longitude);

            // PT 이미지 조회 API 호출
            if (ptId !== undefined) {
                $.ajax({
                    url: `/api/v1/userImage/pt/${ptId}`,  // 트레이너 ID에 맞춰서 API 호출
                    method: 'GET',
                    success: function (response) {
                        const ptImages = response;  // PT 이미지 리스트 (response)

                        // 이미지를 HTML에 삽입
                        if (ptImages.length > 0) {
                            let imageGallery = $('#image-gallery'); // 이미지 갤러리 영역
                            ptImages.forEach(function (image) {
                                // 이미지 HTML 생성
                                const imageElement = `<img src="${image.imageUrl}" alt="PT 이미지" class="pt-image"/>`;
                                imageGallery.append(imageElement);  // 이미지 추가
                            });
                        } else {
                            alert("PT 이미지가 없습니다.");
                        }
                    },
                    error: function () {
                        alert("이미지 조회에 실패했습니다.");
                    }
                });
            }
        },
        error: function() {
            alert('PT 정보를 불러오는 데 실패했습니다.');
        }
    });

    // 수정 버튼 클릭 시 PT 수정 페이지로 이동
    $('#update-btn').on('click', function() {
        window.location.href = `/api/v1/page/ptUpdate/${ptId}`;
    });

    // 삭제 버튼 클릭 시 PT 삭제 요청
    $('#delete-btn').on('click', function() {
        if (confirm('정말로 PT를 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/v1/pt/${ptId}`, // DELETE 요청
                method: 'DELETE',
                success: function(res) {
                    alert(res.msg);
                    window.location.href = '/api/v1/page/ptList';
                },
                error: function() {
                    alert('PT 삭제에 실패했습니다.');
                }
            });
        }
    });

    // 채팅 버튼 클릭 시 채팅 시작
    var chatTrybutton = document.getElementById('chat-user');
    chatTrybutton.innerText = "채팅걸기";
    chatTrybutton.addEventListener('click', function () {
        chatTry(trainerId);
    });

    // 예약 버튼 클릭 시 PT 예약 요청
    $('#reserve-btn').on('click', function() {
        $.ajax({
            url: `/api/v1/pt/${ptId}`,  // 요청할 URL
            type: 'PATCH',               // HTTP 메서드
            contentType: 'application/json'  // 요청 본문 타입
        })
            .done(function (res) {
                alert(res.msg);
                window.location.href = `/api/v1/page/ptList`;
            })
            .fail(function (res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            });
    });

    // 트레이너 프로필 데이터를 가져오기
    function fetchTrainerProfile(trainerId) {
        fetch(`/api/v1/users/trainer/${trainerId}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('트레이너 프로필 조회 실패');
                }
                return response.json();
            })
            .then(data => renderTrainerProfile(data))
            .catch(error => console.error('Error:', error));
    }

    // 트레이너 프로필을 화면에 렌더링
    function renderTrainerProfile(profile) {
        document.getElementById('trainer-image').src = profile.image || "https://via.placeholder.com/150";
        document.getElementById('trainer-name').textContent = `이름: ${profile.name}`;
        document.getElementById('trainer-specialization').textContent = `전문 분야: ${profile.specialization}`;
        document.getElementById('trainer-description').textContent = `소개: ${profile.bio}`;
        document.getElementById('trainer-contact').textContent = `연락처: ${profile.phoneNumber}`;
        document.getElementById('trainer-age').textContent = `나이: ${profile.age}`;
        document.getElementById('trainer-gender').textContent = `성별: ${profile.gender}`;
        document.getElementById('trainer-experience').textContent = `경력: ${profile.experience}년`;
        document.getElementById('trainer-certifications').textContent = `자격증: ${profile.certifications}`;
    }

    // 프로필 표시/숨김을 토글
    function toggleTrainerProfile() {
        const trainerProfileElement = document.getElementById('trainer-profile');

        if (trainerProfileElement.style.display === 'none' || trainerProfileElement.style.display === '') {
            // 프로필 표시 (데이터 가져오기 및 렌더링)
            fetchTrainerProfile(trainerId);
            trainerProfileElement.style.display = 'block';
        } else {
            // 프로필 숨기기
            trainerProfileElement.style.display = 'none';
        }
    }

    document.getElementById('view-trainer-profile-btn').addEventListener('click', toggleTrainerProfile);
});

// 사용자 위치 및 PT 위치로 거리 계산
function getUserLocationAndCalculateDistance(ptLatitude, ptLongitude) {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            const userLatitude = position.coords.latitude;
            const userLongitude = position.coords.longitude;

            // 두 지점 간의 거리 계산
            const distance = calculateDistance(userLatitude, userLongitude, ptLatitude, ptLongitude);
            const distanceInKm = distance.toFixed(2); // 소수점 2자리까지 표시

            // 거리 출력
            document.getElementById('distance').innerText = `현재 위치와 PT 장소의 거리: ${distanceInKm} km`;

        }, function() {
            alert("위치 정보를 가져올 수 없습니다.");
        });
    } else {
        alert("이 브라우저는 위치 정보를 지원하지 않습니다.");
    }
}

// 두 좌표 간의 거리 계산 함수
function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // 지구 반지름 (단위: km)
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;

    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    const distance = R * c; // 단위: km
    return distance;
}