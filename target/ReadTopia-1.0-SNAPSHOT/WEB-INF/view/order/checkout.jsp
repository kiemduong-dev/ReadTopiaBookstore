<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container-fluid py-4">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow">
                <div class="card-header text-white text-center">
                    <h2 class="mb-0"><i class="bi bi-credit-card me-2"></i>Checkout</h2>
                </div>

                <div class="card-body">
                    <!-- Customer Info -->
                    <div class="mb-4">
                        <h5><i class="bi bi-person me-2"></i>Recipient Information</h5>
                        <p><strong>Full Name:</strong> ${account.firstName} ${account.lastName}</p>
                        <p><strong>Phone:</strong> ${account.phone}</p>
                        <p><strong>Email:</strong> ${account.email}</p>
                    </div>

                    <!-- Order Summary -->
                    <div class="alert alert-info">
                        <h5><i class="bi bi-info-circle me-2"></i>Order Details</h5>

                        <c:choose>
                            <c:when test="${type == 'buynow' || type == 'single-cart'}">
                                <p><strong>Product:</strong> ${bookTitle}</p>
                                <p><strong>Quantity:</strong> ${quantity}</p>
                                <p><strong>Unit Price:</strong> ${unitPrice} VND</p>
                                <p><strong>Total Amount:</strong>
                                    <span class="text-primary fw-bold">${amount} VND</span>
                                </p>
                            </c:when>
                            <c:otherwise>
                                <table class="table table-bordered table-striped align-middle mb-3">
                                    <thead class="table-primary text-center">
                                        <tr>
                                            <th style="width: 50%">Product</th>
                                            <th style="width: 15%">Quantity</th>
                                            <th style="width: 15%" class="text-end">Unit Price</th>
                                            <th style="width: 20%" class="text-end">Subtotal</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${cartItemsWithBooks}">
                                            <tr>
                                                <td>${item.book.bookTitle}</td>
                                                <td class="text-center">${item.cartItem.quantity}</td>
                                                <td class="text-end">${item.formattedItemTotal} VND</td>
                                                <td class="text-end text-primary fw-bold">${item.formattedItemTotal} VND</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-light">
                                            <td colspan="3" class="text-end fw-bold">Total:</td>
                                            <td class="text-end fw-bold text-danger fs-5">${amount} VND</td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Payment Form -->
                    <form id="checkoutForm" method="post" action="${pageContext.request.contextPath}/payment/process">
                        <input type="hidden" name="amount" value="${amount}" />
                        <input type="hidden" name="type" value="${type}" />
                        <input type="hidden" name="bookId" value="${bookId}" />
                        <input type="hidden" name="quantity" value="${quantity}" />

                        <c:forEach var="id" items="${selectedItems}">
                            <input type="hidden" name="selectedCartIDs" value="${id}" />
                        </c:forEach>

                        <!-- Shipping Address -->
                        <div class="mb-3">
                            <label for="orderAddress" class="form-label">
                                <i class="bi bi-geo-alt me-1"></i>Shipping Address *
                            </label>
                            <textarea class="form-control" id="orderAddress" name="orderAddress"
                                      rows="3" required placeholder="Enter your full shipping address...">${account.address}</textarea>
                        </div>

                        <!-- Payment Method -->
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="bi bi-credit-card me-1"></i>Payment Method *
                            </label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod"
                                       id="creditCard" value="CREDIT_CARD" checked>
                                <label class="form-check-label" for="creditCard">
                                    <i class="bi bi-credit-card me-1"></i>Bank Transfer
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod"
                                       id="cash" value="CASH">
                                <label class="form-check-label" for="cash">
                                    <i class="bi bi-cash me-1"></i>Cash on Delivery
                                </label>
                            </div>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-between">
                            <a href="${pageContext.request.contextPath}/cart/view"
                               class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-1"></i>Back to Cart
                            </a>
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-check-circle me-1"></i>Confirm Order
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

<style>
    .card-header {
        background-color: #007bff !important;
        border-radius: 15px 15px 0 0;
        padding: 1.5rem;
    }

    .btn-primary {
        background-color: #007bff !important;
        border-color: #007bff !important;
    }

    .text-primary {
        color: #007bff !important;
    }

    .table-primary {
        background-color: #e6f0ff;
    }
</style>

<!-- ✅ JavaScript to handle different payment flows -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById("checkoutForm");

        form.addEventListener("submit", function (e) {
            const formData = new FormData(form);
            const paymentMethod = formData.get("paymentMethod");

            if (paymentMethod === "CREDIT_CARD") {
                e.preventDefault();
                const queryParams = new URLSearchParams(formData).toString();
                const targetUrl = form.action.replace("/payment/process", "/payment/online") + "?" + queryParams;
                window.location.href = targetUrl;
            } else if (paymentMethod === "CASH") {
            }
        });
    });

</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
