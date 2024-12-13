$(document).ready(function() {
    let trainerId;
    const auth = getToken();
    const role = getUserRole();

    // 로그인 인증 및 역할에 따른 화면 표시
    if (auth !== undefined && auth !== '') {
        if(role === 'TRAINER') {
            $('#profile-w').hide();
            $('#profile-h').hide();
            $('#profile-n').hide();
            $('#profile-b').show();
            $('#profile-s').show();
            $('#profile-e').show();
            $('#profile-c').show();
            $('#profile-r').show();
        } else if(role === 'USER') {
            $('#profile-w').show();
            $('#profile-h').show();
            $('#profile-n').show();
            $('#profile-b').hide();
            $('#profile-s').hide();
            $('#profile-e').hide();
            $('#profile-c').hide();
            $('#profile-r').hide();
        }
    }

    // 사용자 역할에 따른 프로필 정보 요청
    if (role === "USER") {
        $.ajax({
            url: "/api/v1/users",  // 사용자 정보 API
            method: "GET",
            success: function(res) {
                // 사용자 정보 업데이트
                $("#profile-email").text(res.email);
                $("#profile-name").text(res.name);
                $("#profile-gender").text(res.gender);
                $("#profile-age").text(res.age);
                $("#profile-phone").text(res.phoneNumber);
                $("#profile-nickname").text(res.nickname);
                $("#profile-weight").text(res.weight + "kg");
                $("#profile-height").text(res.height + "cm");
            },
            fail: function(res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            }
        });
    } else if (role === "TRAINER") {
        // 트레이너의 프로필 정보 가져오기
        $.ajax({
            url: "/api/v1/users/trainer",  // 트레이너 정보 API
            method: "GET",
            success: function(res) {
                trainerId = res.id;  // 트레이너 ID 설정
                // 트레이너 정보 업데이트
                $("#profile-email").text(res.email);
                $("#profile-name").text(res.name);
                $("#profile-gender").text(res.gender);
                $("#profile-age").text(res.age);
                $("#profile-phone").text(res.phoneNumber);
                $("#profile-specialization").text(res.specialization);
                $("#profile-experience").text(res.experience);
                $("#profile-certifications").text(res.certifications);
                $("#profile-bio").text(res.bio);
                $("#profile-rating").text(res.rating);

                const certification = $('#certification');
                // 트레이너의 자격증 이미지 조회 API 호출 (trainerId가 설정된 후에 호출)
                $.ajax({
                    url: `/api/v1/userImage/certifications/${trainerId}`,  // trainerId를 URL에 포함
                    method: 'GET',
                    success: function(res) {
                        const certificationList = $('#certification-list');
                        certificationList.empty(); // 기존 목록 비우기
                            res.forEach(cert => {
                                const li = $('<li></li>').addClass('certification-item'); // 새로운 li 요소 생성
                                const imageWrapper = $('<div></div>').addClass('image-wrapper');  // 이미지와 삭제 버튼을 감싸는 div

                                const img = $('<img>');   // 새로운 img 요소 생성
                                img.attr('src', cert.imageUrl);  // 이미지 URL 설정
                                img.attr('alt', 'Certification Image');  // alt 속성 설정

                                const deleteButton = $('<button></button>')
                                    .addClass('btn btn-danger btn-sm delete-button')
                                    .text('삭제')
                                    .on('click', function() {
                                        deleteCertificationImage(cert.id);  // 클릭된 자격증의 ID를 삭제 함수에 전달
                                    });

                                imageWrapper.append(deleteButton);  // 이미지 위에 삭제 버튼 추가
                                imageWrapper.append(img);  // imageWrapper에 이미지 추가
                                li.append(imageWrapper);  // li에 imageWrapper 추가
                                certificationList.append(li);  // ul에 li 추가
                            });
                    },
                    error: function (xhr) {
                        // 서버 오류가 404일 때
                        if (xhr.status === 404) {
                            certification.append('업로드된 자격증 이미지가 없습니다.');
                        }
                    }
                });
            },
            fail: function(res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages);
            }
        });
    } else {
        alert("잘못된 역할입니다.");
    }

    // 프로필 이미지 조회 API 호출
    $.ajax({
        url: '/api/v1/userImage/profile',
        method: 'GET',
    })
        .done(function(res) {
            $('#profile-image').attr('src', res.imageUrl);
        })
        .fail(function(res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages;
            alert(messages);
        });

    // 업로드 버튼 클릭 시 실행되는 함수
    $('#certification-upload-form').on('submit', function(event) {
        event.preventDefault(); // 폼 기본 제출 방지

        const formData = new FormData();
        const files = $('#certification-file-upload')[0].files;

        // 선택된 파일이 있으면 FormData에 파일을 추가
        if (files.length > 0) {
            for (let i = 0; i < files.length; i++) {
                formData.append('files', files[i]);
            }

            // AJAX를 통해 서버로 파일 업로드
            $.ajax({
                url: '/api/v1/userImage/certifications', // 서버의 업로드 API
                method: 'POST',
                data: formData,
                processData: false,  // 파일을 자동으로 처리하지 않도록 설정
                contentType: false,  // 컨텐츠 타입을 자동으로 설정하지 않음
                success: function(res) {
                    alert(res.msg);
                    window.location.href = '/api/v1/page/profile'; // 성공 시 리디렉션
                    // 업로드 후 필요한 추가 작업을 이곳에서 처리할 수 있습니다.
                },
                error: function(xhr, status, error) {
                    alert("자격증 이미지 업로드 실패: " + xhr.responseText);
                }
            });
        } else {
            alert("업로드할 파일을 선택해 주세요.");
        }
    });

    // 자격증 이미지 삭제 함수
    function deleteCertificationImage(certId) {
        $.ajax({
            url: `/api/v1/userImage/certifications/${certId}`,  // 삭제 API 호출
            method: 'DELETE',
            success: function(res) {
                alert(res.msg);
                // 삭제 후 리스트 새로고침
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("자격증 이미지 삭제 실패: " + xhr.responseText);
            }
        });
    }

    $(document).ready(function () {
        // 포인트 조회 API 호출
        $.ajax({
            url: '/api/v1/point',
            method: 'GET',
            success: function (res) {
                // 성공적으로 데이터를 가져오면 화면에 표시
                var formattedPoint = res.point.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                $('#profile-point-value').text(formattedPoint + ' P');
            }
        });
    });

});

// 프로필 이미지 업로드 처리
document.addEventListener("DOMContentLoaded", function () {
    const uploadButton = document.getElementById("upload-button");
    uploadButton.addEventListener("click", function () {
        const fileInput = document.getElementById("profile-image-upload");
        const file = fileInput.files[0];

        if (file) {
            const formData = new FormData();
            formData.append("file", file);

            $.ajax({
                url: '/api/v1/userImage/profile',
                method: 'POST',
                data: formData,
                processData: false, // FormData로 데이터를 보내기 위해 false로 설정
                contentType: false  // Content-Type을 자동으로 설정하도록 false
            })
                .done(function (res) {
                    alert(res.msg);
                    window.location.href = '/api/v1/page/profile'; // 성공 시 리디렉션
                })
                .fail(function (res) {
                    const jsonObject = JSON.parse(res.responseText);
                    const messages = jsonObject.messages;
                    alert(messages);
                });
        } else {
            alert("업로드할 파일을 선택해 주세요.");
        }
    });
});

function confirmWithdrawal() {
    if (confirm("정말로 탈퇴하시겠습니까?")) {
        // 모달을 띄웁니다.
        document.getElementById('passwordModal').style.display = 'block';
    }
}

// 비밀번호 입력 후 확인 버튼 클릭
function submitPassword() {
    const password = document.getElementById('passwordInput').value;
    if (password) {
        $.ajax({
            url: '/api/v1/users/withdrawal',
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                password: password
            }),
            success: function(res) {
                alert(res.msg); // 성공 메시지 표시
                window.location.href = '/api/v1/page/index'; // 성공 시 리디렉션
            },
            error: function(res) {
                const jsonObject = JSON.parse(res.responseText);
                const messages = jsonObject.messages;
                alert(messages); // 에러 메시지 표시
            }
        });
        closeModal(); // 모달 닫기
    } else {
        alert("비밀번호를 입력해주세요.");
    }
}

// 모달 닫기
function closeModal() {
    document.getElementById('passwordModal').style.display = 'none';
}