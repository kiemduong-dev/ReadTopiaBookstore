<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container-fluid py-4">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow">
                <div class="card-header bg-success text-white text-center">
                    <h2 class="mb-0"><i class="bi bi-credit-card me-2"></i>Thanh toán</h2>
                </div>
                
                <div class="card-body">
                    <!-- Order Summary -->
                    <div class="alert alert-info">
                        <h5><i class="bi bi-info-circle me-2"></i>Thông tin đơn hàng</h5>
                        <p class="mb-1"><strong>Mã đơn hàng:</strong> #${orderId}</p>
                        <p class="mb-1"><strong>Số sản phẩm:</strong> ${orderItems}</p>
                        <p class="mb-0"><strong>Tổng tiền:</strong> 
                            <span class="text-primary fw-bold">
                                <fmt:formatNumber value="${amount}" type="currency" 
                                                currencySymbol="" pattern="#,##0" /> VND
                            </span>
                        </p>
                    </div>

                    <!-- Payment Form -->
                    <form method="post" action="${pageContext.request.contextPath}/payment/process">
                        <input type="hidden" name="orderId" value="${orderId}">
                        <input type="hidden" name="amount" value="${amount}">
                        
                        <div class="mb-3">
                            <label for="orderAddress" class="form-label">
                                <i class="bi bi-geo-alt me-1"></i>Địa chỉ giao hàng *
                            </label>
                            <textarea class="form-control" id="orderAddress" name="orderAddress" 
                                    rows="3" required placeholder="Nhập địa chỉ giao hàng chi tiết..."></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <i class="bi bi-credit-card me-1"></i>Phương thức thanh toán *
                            </label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod" 
                                       id="creditCard" value="CREDIT_CARD" checked>
                                <label class="form-check-label" for="creditCard">
                                    <i class="bi bi-credit-card me-1"></i>Thẻ tín dụng
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod" 
                                       id="cash" value="CASH">
                                <label class="form-check-label" for="cash">
                                    <i class="bi bi-cash me-1"></i>Thanh toán khi nhận hàng
                                </label>
                            </div>
                        </div>

                        <!-- Credit Card Details -->
                        <div id="cardDetails" style="display: none;">
                            <div class="row">
                                <div class="col-12 mb-3">
                                    <label for="cardNumber" class="form-label">Số thẻ *</label>
                                    <input type="text" class="form-control" id="cardNumber" name="cardNumber" 
                                           placeholder="1234 5678 9012 3456" maxlength="19">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="expiryDate" class="form-label">Ngày hết hạn *</label>
                                    <input type="text" class="form-control" id="expiryDate" name="expiryDate" 
                                           placeholder="MM/YY" maxlength="5">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="cvv" class="form-label">CVV *</label>
                                    <input type="text" class="form-control" id="cvv" name="cvv" 
                                           placeholder="123" maxlength="4">
                                </div>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="notes" class="form-label">
                                <i class="bi bi-chat-text me-1"></i>Ghi chú (tùy chọn)
                            </label>
                            <textarea class="form-control" id="notes" name="notes" 
                                    rows="2" placeholder="Ghi chú thêm cho đơn hàng..."></textarea>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-between">
                            <a href="${pageContext.request.contextPath}/cart/view" 
                               class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-1"></i>Quay lại giỏ hàng
                            </a>
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="bi bi-check-circle me-1"></i>Xác nhận thanh toán
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

<script>
    // Toggle card details based on payment method
    document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
        radio.addEventListener('change', function () {
            const cardDetails = document.getElementById('cardDetails');
            const cardInputs = cardDetails.querySelectorAll('input');

            if (this.value === 'CREDIT_CARD') {
                cardDetails.style.display = 'block';
                cardInputs.forEach(input => input.required = true);
            } else {
                cardDetails.style.display = 'none';
                cardInputs.forEach(input => input.required = false);
            }
        });
    });

    // On page load: show/hide based on default selected
    window.addEventListener("DOMContentLoaded", () => {
        document.querySelector('input[name="paymentMethod"]:checked').dispatchEvent(new Event('change'));
    });

    // Format card number input
    document.getElementById('cardNumber').addEventListener('input', function (e) {
        let value = e.target.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
        let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
        e.target.value = formattedValue;
    });

    // Format expiry date input
    document.getElementById('expiryDate').addEventListener('input', function (e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length >= 2) {
            value = value.substring(0, 2) + '/' + value.substring(2, 4);
        }
        e.target.value = value;
    });

    // CVV input validation
    document.getElementById('cvv').addEventListener('input', function (e) {
        e.target.value = e.target.value.replace(/[^0-9]/g, '');
    });
</script>

<style>
    .card {
        border-radius: 10px;
    }

    .form-control:focus {
        border-color: #28a745;
        box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.25);
    }

    .alert-info {
        border-left: 4px solid #17a2b8;
    }
</style>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
