const urlParams = new URLSearchParams(window.location.search);
const paymentKey = urlParams.get("paymentKey");
const orderId = urlParams.get("orderId");
const amount = urlParams.get("amount");

async function confirm() {
    const requestData = {
        paymentKey: paymentKey,
        orderId: orderId,
        amount: amount,
    };
    const response = await fetch("/api/v2/payments/success", {
        method: "POST",
        body: JSON.stringify(requestData),
    });

    const json = await response.json();

    if (!response.ok) {
        window.location.href = `/api/v2/page/payment/fail?message=${json.message}&code=${json.code}`;
    }

    setTimeout(() => {
        window.location.replace("/api/v2/page/profile");
    }, 3000);
}
confirm();
