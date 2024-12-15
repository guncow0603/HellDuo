const ptId = window.location.pathname.split("/").pop();

function fetchUserImage(userId) {
    const imageUrlApi = `/api/v1/userImage/profile/${userId}`;
    fetch(imageUrlApi)
        .then(response => {
            if (!response.ok) {
                throw new Error("이미지를 가져오는 데 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            const imageUrl = data.imageUrl;
            document.getElementById("user-image").src = imageUrl;
        })
        .catch(error => console.error("이미지 로드 오류:", error));
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
        fetchUserImage(data.id);

    } catch (error) {
        console.error("프로필 로드 오류:", error);
    }
}
document.addEventListener("DOMContentLoaded", () => {

    fetchUserProfile();
});