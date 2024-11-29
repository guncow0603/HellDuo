$(document).ready(function () {
    $("#pay-button").on("click", function () {
        pay('카드');
    });
    $('input[name="chargeAmount"]').change(function () {
        if ($(this).attr('id') === 'customAmountRadio') {
            $('#customAmount').prop('disabled', false);
        } else {
            $('#customAmount').prop('disabled', true);
        }
    });

    $('#customAmount').on('input', function () {
        const value = $(this).val();
        if (!/^\d+$/.test(value)) {
            $(this).val(value.replace(/\D/g, '')); // 숫자만 허용
        }
    });
});

let key;
let tossPayments;

$.ajax({
    url: `/api/v1/payments/getKey`,
    type: "GET",
    success: function (data) {
        if (!data) {
            console.error("TossPayments 초기화 실패: key가 없습니다.");
            return;
        }
        key = data;
        tossPayments = TossPayments(key);
    },
    error: function (jqXHR, textStatus) {
        console.error("API 키 가져오기 실패:", textStatus);
    }
});

function getRequest(callback) {
    let amount;
    if ($('#customAmountRadio').is(':checked')) {
        amount = $('#customAmount').val();
    } else {
        amount = $('input[name="chargeAmount"]:checked').val();
    }

    if (!amount || parseInt(amount) <= 0) {
        errorAlert('충전 금액을 올바르게 입력하세요.');
        return;
    }

    $.ajax({
        url: `/api/v1/payments`,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            amount: amount,
            orderName: "포인트 충전"
        }),
        success: function (data) {
            if (!data || !data.request) {
                errorAlert('결제 요청 데이터가 없습니다.');
                return;
            }
            callback(data.request);
        },
        error: function (jqXHR, textStatus) {
            console.error("결제 요청 실패:", textStatus);
        }
    });
}

function pay(method) {
    if (!method || typeof method !== 'string') {
        console.error("결제 방식이 유효하지 않습니다.");
        return;
    }

    getRequest(function (json) {
        tossPayments.requestPayment(method, json)
            .catch(function (error) {
                if (error.code === "USER_CANCEL") {
                    errorAlert('결제를 취소했습니다.');
                } else {
                    errorAlert(error.message);
                }
            });
    });
}