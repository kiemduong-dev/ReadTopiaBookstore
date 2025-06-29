<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
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
                    <!-- Thông tin người nhận -->
                    <div class="mb-4">
                        <h5><i class="bi bi-person me-2"></i>Thông tin người nhận</h5>
                        <p><strong>Họ tên:</strong> ${account.firstName} ${account.lastName}</p>
                        <p><strong>SĐT:</strong> ${account.phone}</p>
                        <p><strong>Email:</strong> ${account.email}</p>
                    </div>

                    <!-- Thông tin đơn hàng -->
                    <div class="alert alert-info">
                        <h5><i class="bi bi-info-circle me-2"></i>Thông tin đơn hàng</h5>

                        <c:choose>
                            <c:when test="${type == 'buynow' || type == 'single-cart'}">
                                <p><strong>Sản phẩm:</strong> ${bookTitle}</p>
                                <p><strong>Số lượng:</strong> ${quantity}</p>
                                <p><strong>Đơn giá:</strong>
                                    <fmt:formatNumber value="${amount / quantity}" pattern="#,##0" /> VND
                                </p>
                                <p><strong>Tổng tiền:</strong>
                                    <span class="text-primary fw-bold">
                                        <fmt:formatNumber value="${amount}" pattern="#,##0" /> VND
                                    </span>
                                </p>
                            </c:when>
                            <c:otherwise>
                                <table class="table table-bordered table-striped align-middle mb-3">
                                    <thead class="table-success text-center">
                                        <tr>
                                            <th style="width: 50%">Sản phẩm</th>
                                            <th style="width: 15%">Số lượng</th>
                                            <th style="width: 15%" class="text-end">Đơn giá</th>
                                            <th style="width: 20%" class="text-end">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${cartItemsWithBooks}">
                                            <tr>
                                                <td>${item.book.bookTitle}</td>
                                                <td class="text-center">${item.cartItem.quantity}</td>
                                                <td class="text-end">
                                                    <fmt:formatNumber value="${item.book.bookPrice}" pattern="#,##0" /> VND
                                                </td>
                                                <td class="text-end text-primary fw-bold">
                                                    <fmt:formatNumber value="${item.itemTotal}" pattern="#,##0" /> VND
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-light">
                                            <td colspan="3" class="text-end fw-bold">Tổng tiền:</td>
                                            <td class="text-end fw-bold text-danger fs-5">
                                                <fmt:formatNumber value="${amount}" pattern="#,##0" /> VND
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Form thanh toán -->
                    <form id="checkoutForm" method="post" action="${pageContext.request.contextPath}/payment/process">
                        <input type="hidden" name="amount" value="${amount}" />
                        <input type="hidden" name="type" value="${type}" />
                        <input type="hidden" name="bookId" value="${bookId}" />
                        <input type="hidden" name="quantity" value="${quantity}" />
                        <input type="hidden" name="selectedItems" value="${selectedItems}" />

                        <!-- Địa chỉ giao hàng -->
                        <div class="mb-3">
                            <label for="orderAddress" class="form-label">
                                <i class="bi bi-geo-alt me-1"></i>Địa chỉ giao hàng *
                            </label>
                            <textarea class="form-control" id="orderAddress" name="orderAddress"
                                      rows="3" required placeholder="Nhập địa chỉ giao hàng chi tiết...">${account.address}</textarea>
                        </div>

                        <!-- Phương thức thanh toán -->
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="bi bi-credit-card me-1"></i>Phương thức thanh toán *
                            </label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod"
                                       id="creditCard" value="CREDIT_CARD" checked>
                                <label class="form-check-label" for="creditCard">
                                    <i class="bi bi-credit-card me-1"></i>Ngân hàng
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

<!-- Bootstrap icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

<!-- JavaScript điều hướng theo phương thức thanh toán -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById("checkoutForm");
        const creditOption = document.getElementById("creditCard");

        form.addEventListener("submit", function (e) {
            if (creditOption.checked) {
                e.preventDefault();

                // Lấy tất cả dữ liệu trong form và chuyển sang URL
                const params = new URLSearchParams(new FormData(form)).toString();
                window.location.href = form.action.replace("/payment/process", "/payment/online") + "?" + params;
            }
            // Nếu là "CASH", để form submit bình thường
        });
    });
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
