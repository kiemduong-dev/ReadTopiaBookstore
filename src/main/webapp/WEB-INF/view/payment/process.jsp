<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container-fluid py-4">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow">
                <div class="card-header bg-warning text-dark text-center">
                    <h2 class="mb-0"><i class="bi bi-clock me-2"></i>Đang xử lý thanh toán</h2>
                </div>

                <div class="card-body">
                    <!-- Order Info -->
                    <div class="alert alert-info mb-4">
                        <h5><i class="bi bi-info-circle me-2"></i>Thông tin đơn hàng</h5>
                        <p class="mb-1"><strong>Mã đơn hàng:</strong> #${orderId}</p>
                        <p class="mb-1"><strong>Số tiền:</strong> 
                            <span class="text-primary fw-bold">
                                <fmt:formatNumber value="${amount}" type="currency" 
                                                  currencySymbol="" pattern="#,##0" /> VND
                            </span>
                        </p>
                        <p class="mb-0"><strong>Địa chỉ giao hàng:</strong> ${orderAddress}</p>
                    </div>

                    <!-- QR Code Payment -->
                    <c:if test="${paymentMethod == 'QR_CODE'}">
                        <div class="text-center">
                            <h4 class="mb-3"><i class="bi bi-qr-code me-2"></i>Quét mã QR để thanh toán</h4>

                            <!-- QR Code Display -->
                            <div class="qr-container mb-4">
                                <div id="qrcode" class="d-flex justify-content-center"></div>
                            </div>

                            <!-- Bank Info -->
                            <div class="card bg-light">
                                <div class="card-body">
                                    <h6 class="card-title">Thông tin chuyển khoản</h6>
                                    <p class="mb-1"><strong>Ngân hàng:</strong> BIDV</p>
                                    <p class="mb-1"><strong>Số tài khoản:</strong> 7850673111</p>
                                    <p class="mb-1"><strong>Tên tài khoản:</strong> NGUYEN THAI ANH</p>
                                    <p class="mb-1"><strong>Số tiền:</strong> 
                                        <fmt:formatNumber value="${amount}" type="currency" 
                                                          currencySymbol="" pattern="#,##0" /> VND
                                    </p>
                                    <p class="mb-0"><strong>Nội dung:</strong> DH${orderId} ${payment.paymentId.substring(0, 8)}</p>
                                </div>
                            </div>

                            <div class="mt-4">
                                <p class="text-muted">Sau khi chuyển khoản thành công, vui lòng nhấn "Xác nhận thanh toán"</p>
                                <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                                    <a href="${pageContext.request.contextPath}/payment/confirm?paymentId=${payment.paymentId}&action=confirm" 
                                       class="btn btn-success">
                                        <i class="bi bi-check-circle me-1"></i>Xác nhận thanh toán
                                    </a>
                                    <a href="${pageContext.request.contextPath}/payment/confirm?paymentId=${payment.paymentId}&action=cancel" 
                                       class="btn btn-outline-danger">
                                        <i class="bi bi-x-circle me-1"></i>Hủy thanh toán
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <!-- Bank Transfer Payment -->
                    <c:if test="${paymentMethod == 'BANK_TRANSFER'}">
                        <div class="text-center">
                            <h4 class="mb-3"><i class="bi bi-bank me-2"></i>Chuyển khoản ngân hàng</h4>

                            <div class="card bg-light mb-4">
                                <div class="card-body">
                                    <h6 class="card-title">Thông tin chuyển khoản</h6>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="mb-1"><strong>Ngân hàng:</strong> BIDV</p>
                                            <p class="mb-1"><strong>Số tài khoản:</strong> 7850673111</p>
                                            <p class="mb-1"><strong>Tên tài khoản:</strong> NGUYEN THAI ANH</p>
                                        </div>
                                        <div class="col-md-6">
                                            <p class="mb-1"><strong>Số tiền:</strong> 
                                                <span class="text-danger fw-bold">
                                                    <fmt:formatNumber value="${amount}" type="currency" 
                                                                      currencySymbol="" pattern="#,##0" /> VND
                                                </span>
                                            </p>
                                            <p class="mb-1"><strong>Mã giao dịch:</strong> ${transferCode}</p>
                                            <p class="mb-0"><strong>Nội dung:</strong> DH${orderId} ${payment.paymentId.substring(0, 8)}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="alert alert-warning">
                                <i class="bi bi-exclamation-triangle me-2"></i>
                                <strong>Lưu ý:</strong> Vui lòng chuyển khoản đúng số tiền và ghi đúng nội dung để đơn hàng được xử lý nhanh chóng.
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                                <a href="${pageContext.request.contextPath}/payment/confirm?paymentId=${payment.paymentId}&action=confirm" 
                                   class="btn btn-success">
                                    <i class="bi bi-check-circle me-1"></i>Đã chuyển khoản
                                </a>
                                <a href="${pageContext.request.contextPath}/payment/confirm?paymentId=${payment.paymentId}&action=cancel" 
                                   class="btn btn-outline-danger">
                                    <i class="bi bi-x-circle me-1"></i>Hủy thanh toán
                                </a>
                            </div>
                        </div>
                    </c:if>

                    <!-- Credit Card Payment -->
                    <c:if test="${paymentMethod == 'CREDIT_CARD'}">
                        <div class="text-center">
                            <div class="alert alert-success">
                                <i class="bi bi-check-circle me-2"></i>
                                <h4 class="mb-2">Thanh toán thành công!</h4>
                                <p class="mb-1">Mã giao dịch: ${transactionId}</p>
                                <p class="mb-0">Trạng thái: ${payment.status}</p>
                            </div>

                            <a href="${pageContext.request.contextPath}/order/history" class="btn btn-primary">
                                <i class="bi bi-list-ul me-1"></i>Xem lịch sử đơn hàng
                            </a>
                        </div>
                    </c:if>

                    <!-- Cash Payment -->
                    <c:if test="${paymentMethod == 'CASH'}">
                        <div class="text-center">
                            <div class="alert alert-info">
                                <i class="bi bi-cash me-2"></i>
                                <h4 class="mb-2">Thanh toán khi nhận hàng</h4>
                                <p class="mb-0">Đơn hàng của bạn đã được xác nhận. Bạn sẽ thanh toán khi nhận hàng.</p>
                            </div>

                            <a href="${pageContext.request.contextPath}/order/history" class="btn btn-primary">
                                <i class="bi bi-list-ul me-1"></i>Xem lịch sử đơn hàng
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- QR Code Library -->
<script src="https://cdn.jsdelivr.net/npm/qrcode@1.5.3/build/qrcode.min.js"></script>
<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

<script>
    // Generate QR Code for payment
    <c:if test="${paymentMethod == 'QR_CODE'}">
    const qrData = "${qrData}";
    if (qrData) {
        QRCode.toCanvas(document.getElementById('qrcode'), qrData, {
            width: 256,
            height: 256,
            colorDark: '#000000',
            colorLight: '#ffffff',
            margin: 2
        }, function (error) {
            if (error)
                console.error(error);
            console.log('QR code generated successfully!');
        });
    }
    </c:if>
</script>

<style>
    .card {
        border-radius: 10px;
    }
    .qr-container {
        padding: 20px;
        background: white;
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        display: inline-block;
    }
    .alert {
        border-radius: 10px;
    }
</style>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
