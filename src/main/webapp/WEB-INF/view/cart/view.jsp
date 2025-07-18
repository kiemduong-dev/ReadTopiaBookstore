<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<%
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    DecimalFormat formatter = new DecimalFormat("###,###", symbols);
    request.setAttribute("formatter", formatter);
    String contextPath = request.getContextPath();
%>

<script>
    const contextPath = '<%= contextPath%>';
</script>

<div class="container-fluid py-4">

    <!-- ✅ Success Alert for Deleted Items -->
    <c:if test="${param.msg == 'deleted'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert" id="deleteSuccessAlert">
            <i class="bi bi-check-circle-fill me-2"></i>
            Selected item(s) have been successfully removed from your cart.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row">
        <div class="col-12">
            <div class="card shadow">
                <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <h2 class="mb-0"><i class="bi bi-cart3 me-2"></i>Your Shopping Cart</h2>
                    <button class="btn btn-danger btn-sm" id="deleteSelectedBtn">
                        <i class="bi bi-trash-fill me-1"></i>Remove Selected
                    </button>
                </div>

                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty cartItemsWithBooks}">
                            <div class="text-center py-5">
                                <i class="bi bi-cart-x display-1 text-muted"></i>
                                <h3 class="mt-3">Your cart is empty</h3>
                                <p class="text-muted">You haven't added any products yet.</p>
                                <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-primary mt-3">
                                    <i class="bi bi-book me-1"></i>Browse Books
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form id="updateForm" method="post" action="${pageContext.request.contextPath}/cart/edit">
                                <input type="hidden" name="cartID" id="form-cartID" />
                                <input type="hidden" name="quantity" id="form-quantity" />
                            </form>

                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="text-center"><input type="checkbox" id="selectAll" /></th>
                                            <th>Product</th>
                                            <th class="text-center">Quantity</th>
                                            <th class="text-end">Unit Price</th>
                                            <th class="text-end">Subtotal</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${cartItemsWithBooks}">
                                            <tr>
                                                <td class="text-center">
                                                    <input type="checkbox" class="select-item" value="${item.cartItem.cartID}" />
                                                </td>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <img src="${item.book.image}" width="60" height="80" class="rounded me-3" style="object-fit: cover;" 
                                                             onerror="this.src='https://via.placeholder.com/60x80?text=No+Image'">
                                                        <div>
                                                            <h6 class="mb-1">${item.book.bookTitle}</h6>
                                                            <small class="text-muted">Author: ${item.book.author}</small><br>
                                                            <small class="text-muted">ID: ${item.book.bookID}</small>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="text-center">
                                                    <input type="number" class="form-control text-center quantity-input"
                                                           style="width: 80px;" min="1"
                                                           value="${item.cartItem.quantity}"
                                                           data-cart-id="${item.cartItem.cartID}" />
                                                </td>
                                                <td class="text-end">
                                                    ${formatter.format(item.book.bookPrice)} VND
                                                </td>
                                                <td class="text-end fw-bold">
                                                    ${formatter.format(item.itemTotal)} VND
                                                </td>
                                                <td></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-active">
                                            <td colspan="4" class="text-end fw-bold fs-5">Total:</td>
                                            <td class="text-end fw-bold fs-5 text-primary">
                                                ${formatter.format(totalAmount)} VND
                                            </td>
                                            <td></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>

                            <div class="d-flex justify-content-between align-items-center mt-4">
                                <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-outline-secondary">
                                    <i class="bi bi-arrow-left me-1"></i>Continue Shopping
                                </a>
                                <form method="get" action="${pageContext.request.contextPath}/order/checkout" id="checkoutForm">
                                    <input type="hidden" name="ids" id="selectedCheckoutIds" />
                                    <button type="submit" class="btn btn-success btn-lg" id="checkoutBtn">
                                        <i class="bi bi-credit-card me-1"></i>Checkout Selected
                                    </button>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ✅ Script: Checkout, Delete, Quantity Change, Alert Timeout -->
<script>
    // Handle checkout
    document.getElementById("checkoutBtn").addEventListener("click", function (e) {
        const selected = [...document.querySelectorAll('.select-item:checked')].map(cb => cb.value);
        if (selected.length === 0) {
            e.preventDefault();
            alert("Please select at least one product to checkout.");
            return;
        }
        document.getElementById("selectedCheckoutIds").value = selected.join(",");
    });

    // Select all toggle
    document.getElementById('selectAll').addEventListener('change', function () {
        const checked = this.checked;
        document.querySelectorAll('.select-item').forEach(cb => cb.checked = checked);
    });

    // Delete selected
    document.getElementById('deleteSelectedBtn').addEventListener('click', function () {
        const selected = [...document.querySelectorAll('.select-item:checked')].map(cb => cb.value);
        if (selected.length === 0) {
            alert("Please select at least one item to remove.");
            return;
        }
        if (confirm("Are you sure you want to remove selected items?")) {
            window.location.href = contextPath + "/cart/delete?ids=" + selected.join(",") + "&msg=deleted";
        }
    });

    // Update quantity
    document.querySelectorAll('.quantity-input').forEach(input => {
        input.addEventListener('change', function () {
            const cartID = this.dataset.cartId;
            const newQuantity = parseInt(this.value);
            if (!cartID || isNaN(newQuantity) || newQuantity < 1) {
                alert("Invalid quantity.");
                this.value = 1;
                return;
            }
            document.getElementById('form-cartID').value = cartID;
            document.getElementById('form-quantity').value = newQuantity;
            document.getElementById('updateForm').submit();
        });
    });

    // ✅ Auto dismiss alert after 10 seconds
    window.addEventListener('DOMContentLoaded', function () {
        const alertBox = document.getElementById('deleteSuccessAlert');
        if (alertBox) {
            setTimeout(() => {
                const alertInstance = bootstrap.Alert.getOrCreateInstance(alertBox);
                alertInstance.close();
            }, 10000); // 10 seconds
        }
    });
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
