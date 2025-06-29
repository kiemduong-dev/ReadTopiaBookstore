<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container py-5">
    <div class="card shadow-lg">
        <div class="card-header bg-info text-white text-center">
            <h4><i class="bi bi-credit-card me-2"></i>Thông tin thanh toán</h4>
        </div>
        <div class="card-body">
            <div class="row">
                <!-- Cột trái: QR -->
                <div class="col-md-5 d-flex justify-content-center align-items-start">
                    <div class="qr-container text-center">
                        <h5><i class="bi bi-qr-code me-2"></i>Quét mã QR để thanh toán</h5>
                        <img src="${pageContext.request.contextPath}/img/z6755080902176_6e54f4f14f5bf5ee3caaed1d5ed253a4.jpg"
                             alt="QR Code thanh toán"
                             class="img-fluid rounded shadow"
                             style="max-width: 280px;" />
                    </div>
                </div>

                <!-- Cột phải: Thông tin -->
                <div class="col-md-7">
                    <!-- Đơn hàng -->
                    <div class="mb-3">
                        <p><strong>Mã đơn hàng:</strong> DH${orderId}</p>
                        <p><strong>Số tiền cần thanh toán:</strong>
                            <span class="text-danger fw-bold">
                                <fmt:formatNumber value="${amount}" pattern="#,##0" /> VND
                            </span>
                        </p>
                        <p><strong>Nội dung chuyển khoản:</strong>
                            <span class="text-primary fw-bold">${transferCode}</span>
                        </p>
                    </div>

                    <!-- Chuyển khoản -->
                    <div class="border rounded p-3 mb-4 bg-light">
                        <h6 class="mb-2"><i class="bi bi-bank me-2"></i>Chuyển khoản ngân hàng</h6>
                        <p><strong>Ngân hàng:</strong> BIDV</p>
                        <p><strong>Số tài khoản:</strong> 7850673111</p>
                        <p><strong>Tên tài khoản:</strong> NGUYEN THAI ANH</p>
                        <p><strong>Nội dung:</strong>
                            <span class="text-primary fw-bold">${transferCode}</span>
                        </p>

                        <div class="mt-3 text-center">
                            <a href="${pageContext.request.contextPath}/payment/confirm?paymentId=${payment.paymentId}"
                               class="btn btn-success btn-lg">
                                <i class="bi bi-check-circle me-1"></i>✅ Tôi đã chuyển khoản
                            </a>
                            <a href="${pageContext.request.contextPath}/customer/book/list"
                               class="btn btn-outline-danger">❌ Hủy thanh toán</a>
                        </div>
                    </div>
                </div> 
            </div> 
        </div> 
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />

<style>
    .qr-container {
        padding: 20px;
        background-color: #fff;
        border-radius: 12px;
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
        display: inline-block;
    }
</style>
