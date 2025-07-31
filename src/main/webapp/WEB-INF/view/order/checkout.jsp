<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container-fluid py-4">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow">
                <div class="card-header text-white text-center" style="background-color: #0d84e9;">
                    <h2 class="mb-0">Checkout</h2>
                </div>
                <div class="card-body">

                    <!-- Recipient Info -->
                    <div class="mb-4">
                        <h5>Recipient Information</h5>
                        <p><strong>Full Name:</strong> ${account.firstName} ${account.lastName}</p>
                        <p><strong>Phone:</strong> ${account.phone}</p>
                        <p><strong>Email:</strong> ${account.email}</p>
                    </div>

                    <!-- Order Details -->
                    <div class="alert alert-info">
                        <h5>Order Details</h5>

                        <c:choose>
                            <c:when test="${type == 'buynow' || type == 'single-cart'}">
                                <p><strong>Product:</strong> ${bookTitle}</p>
                                <p><strong>Quantity:</strong> ${quantity}</p>
                                <p><strong>Unit Price:</strong>
                                    <fmt:formatNumber value="${unitPriceRaw}" pattern="#,##0" /> VND
                                </p>
                                <p><strong>Total:</strong>
                                    <span class="text-primary fw-bold" id="originalAmount">
                                        <fmt:formatNumber value="${amountRaw}" pattern="#,##0" />
                                    </span> VND
                                </p>
                            </c:when>
                            <c:otherwise>
                                <table class="table table-bordered table-striped align-middle mb-3">
                                    <thead class="text-center" style="background-color: #0d84e9; color: white;">
                                        <tr>
                                            <th>Product</th>
                                            <th>Qty</th>
                                            <th class="text-end">Unit Price</th>
                                            <th class="text-end">Subtotal</th>
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
                                            <td colspan="3" class="text-end fw-bold">Total:</td>
                                            <td class="text-end text-danger fw-bold fs-5">
                                                <span id="originalAmount">
                                                    <fmt:formatNumber value="${amountRaw}" pattern="#,##0" />
                                                </span> VND
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </c:otherwise>
                        </c:choose>

                        <!-- Voucher select -->
                        <div class="mt-3">
                            <label for="voucherSelect" class="form-label">Voucher:</label>
                            <select class="form-select" id="voucherSelect">
                                <option value="0" data-discount="0" <c:if test="${voucherID == 0}">selected</c:if>>
                                    -- No Voucher --
                                </option>
                                <c:forEach var="voucher" items="${vouchers}">
                                    <option value="${voucher.vouID}" data-discount="${voucher.discount}"
                                            <c:if test="${voucher.vouID == voucherID}">selected</c:if>>
                                        ${voucher.vouCode} - ${voucher.vouName} (-${voucher.discount}%)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Applied Voucher Info -->
                        <c:if test="${true}">
                            <div id="voucherInfoDisplay" class="mt-3 p-3 border rounded bg-light">
                                <p><strong>Discount Amount:</strong> 
                                    <span class="text-danger fw-bold" id="discountAmountDisplay">
                                        - <fmt:formatNumber value="${discountAmount}" pattern="#,##0" /> VND
                                    </span>
                                </p>

                                <p><strong>Final Amount:</strong> 
                                    <span class="text-success fw-bold fs-5" id="finalAmountDisplay">
                                        <fmt:formatNumber value="${finalAmount}" pattern="#,##0" /> VND
                                    </span>
                                </p>
                            </div>
                        </c:if>

                        <!-- Payment Form -->
                        <form id="checkoutForm" method="post" action="${pageContext.request.contextPath}/payment/process">
                            <input type="hidden" name="amount" value="${amountRaw}" />
                            <input type="hidden" name="type" value="${type}" />
                            <input type="hidden" name="bookId" value="${bookId}" />
                            <input type="hidden" name="quantity" value="${quantity}" />
                            <input type="hidden" name="voucherID" id="selectedVoucherHidden" value="${voucherID}" />
                            <input type="hidden" name="discountAmount" value="${discountAmount}" />
                            <input type="hidden" name="finalAmount" value="${finalAmount}" />

                            <c:forEach var="id" items="${selectedItems}">
                                <input type="hidden" name="selectedCartIDs" value="${id}" />
                            </c:forEach>

                            <div class="mb-3">
                                <label for="orderAddress" class="form-label">Shipping Address *</label>
                                <textarea class="form-control" id="orderAddress" name="orderAddress" rows="3" required>${account.address}</textarea>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Payment Method *</label>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="bank" value="CREDIT_CARD" checked />
                                    <label class="form-check-label" for="bank">Bank Transfer (QR code)</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="cash" value="CASH" />
                                    <label class="form-check-label" for="cash">Cash on Delivery</label>
                                </div>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-between">
                                <a href="${pageContext.request.contextPath}/cart/view" class="btn btn-outline-secondary">
                                    Back to Cart
                                </a>
                                <button type="submit" class="btn btn-primary btn-lg">
                                    Confirm Order
                                </button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const voucherSelect = document.getElementById("voucherSelect");

        const originalAmount = parseFloat("${amountRaw}");
        const discountAmountInput = document.querySelector("input[name='discountAmount']");
        const finalAmountInput = document.querySelector("input[name='finalAmount']");
        const voucherHiddenInput = document.getElementById("selectedVoucherHidden");

        const discountAmountDisplay = document.getElementById("discountAmountDisplay");
        const finalAmountDisplay = document.getElementById("finalAmountDisplay");

        function updateDiscountValues() {
            const selectedOption = voucherSelect.options[voucherSelect.selectedIndex];
            const discountPercent = parseFloat(selectedOption.getAttribute("data-discount")) || 0;
            const selectedVoucherID = selectedOption.value;

            const discountAmount = Math.round(originalAmount * discountPercent / 100);
            const finalAmount = originalAmount - discountAmount;

            voucherHiddenInput.value = selectedVoucherID;
            discountAmountInput.value = discountAmount;
            finalAmountInput.value = finalAmount;

            discountAmountDisplay.textContent = "- " + discountAmount.toLocaleString("vi-VN") + " VND";
            finalAmountDisplay.textContent = finalAmount.toLocaleString("vi-VN") + " VND";
        }

        updateDiscountValues();

        voucherSelect.addEventListener("change", updateDiscountValues);

        const form = document.getElementById("checkoutForm");
        form.addEventListener("submit", function (e) {
            const method = form.querySelector("input[name='paymentMethod']:checked").value;
            if (method === "CREDIT_CARD") {
                e.preventDefault();
                updateDiscountValues();
                form.action = form.action.replace("/process", "/online");
                form.submit();
            }
        });
    });
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
