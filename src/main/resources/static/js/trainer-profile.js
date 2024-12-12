const trainerId = window.location.pathname.split("/").pop();

function fetchTrainerImage() {
    const imageUrlApi = `/api/v1/userImage/profile/${trainerId}`;
    fetch(imageUrlApi)
        .then(response => {
            if (!response.ok) {
                throw new Error("이미지를 가져오는 데 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            const imageUrl = data.imageUrl;
            document.getElementById("trainer-image").src = imageUrl;
        })
        .catch(error => console.error("이미지 로드 오류:", error));
}

async function fetchTrainerProfile() {
    const profileApi = `/api/v1/users/trainer/${trainerId}`;
    try {
        const response = await fetch(profileApi);
        if (!response.ok) {
            throw new Error("프로필 정보를 가져오는 데 실패했습니다.");
        }
        const data = await response.json();
        document.getElementById("trainer-name").textContent = data.name;
        document.getElementById("trainer-email").textContent = data.email;
        document.getElementById("trainer-phone").textContent = data.phoneNumber;
        document.getElementById("trainer-gender").textContent = data.gender;
        document.getElementById("trainer-age").textContent = data.age;
        document.getElementById("trainer-specialization").textContent = data.specialization;
        document.getElementById("trainer-experience").textContent = `${data.experience}년`;
        document.getElementById("trainer-bio").textContent = data.bio;
        document.getElementById("trainer-certifications").textContent = data.certifications.join(", ");

    } catch (error) {
        console.error("프로필 로드 오류:", error);
    }
}

$.ajax({
    url: `/api/v1/userImage/certifications/${trainerId}`,
    method: 'GET',
    success: function(res) {
        const certificationList = $('#certification-list');
        certificationList.empty();
        res.forEach(cert => {
            const li = $('<li></li>').addClass('certification-item');
            const imageWrapper = $('<div></div>').addClass('image-wrapper');
            const img = $('<img>');
            img.attr('src', cert.imageUrl);
            img.attr('alt', 'Certification Image');
            imageWrapper.append(img);
            li.append(imageWrapper);
            certificationList.append(li);
        });
    },
    error: function(xhr) {
        if (xhr.status === 404) {
            $('#certification-list').append('업로드된 자격증 이미지가 없습니다.');
        }
    }
});

document.addEventListener("DOMContentLoaded", () => {
    fetchTrainerImage();
    fetchTrainerProfile();
});