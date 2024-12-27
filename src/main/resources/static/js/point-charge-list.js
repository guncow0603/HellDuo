document.addEventListener("DOMContentLoaded", function () {
    // API 호출 URL
    const apiUrl = "/api/v2/point/charge";

    // HTML 요소
    const tableBody = document.getElementById("charge-point-table-body");
    const noDataMessage = document.getElementById("no-data-message");

    // API 호출
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error("API 호출 실패");
            }
            return response.json();
        })
        .then(data => {
            // 데이터를 테이블에 삽입
            if (data.length > 0) {
                data.forEach(item => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                                <td>${new Date(item.time).toLocaleString()}</td>
                                <td>${item.amount.toLocaleString()} P</td>
                                <td>${item.orderId || "없음"}</td>
                            `;
                    tableBody.appendChild(row);
                });
            } else {
                noDataMessage.style.display = "block";
            }
        })
        .catch(error => {
            console.error("에러 발생:", error);
            noDataMessage.textContent = "충전 내역을 불러오는 중 오류가 발생했습니다.";
            noDataMessage.style.display = "block";
        });
});