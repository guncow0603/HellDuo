const ptId = window.location.pathname.split("/").pop();

function getProfileImage(userId) {
    $.ajax({
        url: `/api/v2/images/profile/${userId}`,
        method: 'GET',
    })
        .done(function(res) {
            if (res.length > 0) {
                $('#user-image').attr('src', res[0].imageUrl);
            }
        })
        .fail(function(res) {
            const jsonObject = JSON.parse(res.responseText);
            const messages = jsonObject.messages;
            alert(messages);
        });
}

async function fetchUserProfile() {
    const profileApi = `/api/v1/users/pt/${ptId}`;
    try {
        const response = await fetch(profileApi);
        if (!response.ok) {
            throw new Error("프로필 정보를 가져오는 데 실패했습니다.");
        }
        const data = await response.json();
        document.getElementById("user-name").textContent = data.name;
        document.getElementById("user-email").textContent = data.email;
        document.getElementById("user-phone").textContent = data.phoneNumber;
        document.getElementById("user-gender").textContent = data.gender;
        document.getElementById("user-age").textContent = data.age;
        document.getElementById("user-nickname").textContent = data.nickname;
        document.getElementById("user-weight").textContent = data.weight
        document.getElementById("user-height").textContent = data.height;
        getProfileImage(data.userId);

    } catch (error) {
        console.error("프로필 로드 오류:", error);
    }
}
document.addEventListener("DOMContentLoaded", () => {

    fetchUserProfile();
});