<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<%
    String contextPath = request.getContextPath();
%>

<script>
    const contextPath = '<%= contextPath%>';
</script>

<div class="container-fluid py-4">
    <div class="row">
        <div class="col-12">
            <div class="card shadow">
                <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <h2 class="mb-0"><i class="bi bi-cart3 me-2"></i>Giỏ hàng của bạn</h2>
                    <button class="btn btn-danger btn-sm" id="deleteSelectedBtn">
                        <i class="bi bi-trash-fill me-1"></i>Xóa mục đã chọn
                    </button>
                </div>

                <div class="card-body">
                    <!-- Alert messages -->
                    <c:if test="${not empty error}">
                        <!-- error message logic... -->
                    </c:if>

                    <form id="updateForm" method="post" action="${pageContext.request.contextPath}/cart/edit">
                        <input type="hidden" name="cartID" id="form-cartID" />
                        <input type="hidden" name="quantity" id="form-quantity" />
                    </form>

                    <c:choose>
                        <c:when test="${empty cartItemsWithBooks}">
                            <div class="text-center py-5">
                                <i class="bi bi-cart-x display-1 text-muted"></i>
                                <h3 class="mt-3">Giỏ hàng của bạn đang trống</h3>
                                <p class="text-muted">Hiện bạn chưa có sản phẩm nào trong giỏ hàng.</p>
                                <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-primary mt-3">
                                    <i class="bi bi-book me-1"></i>Xem thêm sách
                                </a>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="text-center">
                                                <input type="checkbox" id="selectAll" />
                                            </th>
                                            <th>Sản phẩm</th>
                                            <th class="text-center">Số lượng</th>
                                            <th class="text-end">Đơn giá</th>
                                            <th class="text-end">Thành tiền</th>
                                            <th class="text-center">       </th>
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
                                                        <img src="${item.book.image}" 
                                                             alt="Book cover"
                                                             class="rounded me-3" width="60" height="80" style="object-fit: cover;"
                                                             onerror="this.src='https://via.placeholder.com/60x80?text=No+Image'">
                                                        <div>
                                                            <h6 class="mb-1">${item.book.bookTitle}</h6>
                                                            <small class="text-muted">Tác giả: ${item.book.author}</small><br>
                                                            <small class="text-muted">ID: ${item.book.bookID}</small>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="text-center">
                                                    <input type="number" 
                                                           class="form-control text-center quantity-input" 
                                                           style="width: 80px; display: inline-block;"
                                                           value="${item.cartItem.quantity}" 
                                                           min="1"
                                                           data-cart-id="${item.cartItem.cartID != null ? item.cartItem.cartID : 0}" />
                                                </td>
                                                <td class="text-end">
                                                    <fmt:formatNumber value="${item.book.bookPrice}" pattern="#,#00" /> VND
                                                </td>
                                                <td class="text-end fw-bold">
                                                    <fmt:formatNumber value="${item.itemTotal}" pattern="#,#00" /> VND
                                                </td>



                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-active">
                                            <td colspan="4" class="text-end fw-bold fs-5">Tổng cộng:</td>
                                            <td class="text-end fw-bold fs-5 text-primary">
                                                <fmt:formatNumber value="${totalAmount}" pattern="#,#00" /> VND
                                            </td>
                                            <td></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>

                            <div class="d-flex justify-content-between align-items-center mt-4">
                                <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-outline-secondary">
                                    <i class="bi bi-arrow-left me-1"></i>Tiếp tục mua sắm
                                </a>
                                <form id="checkoutForm" method="post" action="${pageContext.request.contextPath}/checkout">
                                    <input type="hidden" name="ids" id="selectedCheckoutIds" />
                                    <button type="submit" class="btn btn-success btn-lg" id="checkoutBtn">
                                        <i class="bi bi-credit-card me-1"></i>Thanh toán
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

<!-- Delete Modal -->
<div class="modal fade" id="deleteConfirmModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-warning text-dark">
                <h5 class="modal-title">
                    <i class="bi bi-exclamation-triangle me-2"></i>Xác nhận xóa
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>Bạn có chắc muốn xóa <strong id="deleteProductName"></strong> khỏi giỏ hàng?</p>
                <p class="text-muted">Hành động này không thể hoàn tác.</p>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Không</button>
                <button class="btn btn-danger" id="confirmDeleteBtn">Có, xóa ngay</button>
            </div>
        </div>
    </div>
</div>

<script>
    let deleteCartID = null;

    function confirmDelete(cartID, productName) {
        deleteCartID = cartID;
        document.getElementById('deleteProductName').textContent = productName;
        new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
    }

    document.getElementById('confirmDeleteBtn').addEventListener('click', function () {
        if (deleteCartID) {
            window.location.href = contextPath + '/cart/delete?cartID=' + deleteCartID;
        }
    });

    // Cập nhật số lượng
    document.querySelectorAll('.quantity-input').forEach(input => {
        input.addEventListener('change', function () {
            const cartID = this.dataset.cartId;
            const newQuantity = parseInt(this.value);

            if (!cartID || isNaN(newQuantity) || newQuantity < 1) {
                alert("Số lượng không hợp lệ");
                this.value = 1;
                return;
            }

            document.getElementById('form-cartID').value = cartID;
            document.getElementById('form-quantity').value = newQuantity;
            document.getElementById('updateForm').submit();
        });
    });

    // Xử lý chọn tất cả
    document.getElementById('selectAll').addEventListener('change', function () {
        const checked = this.checked;
        document.querySelectorAll('.select-item').forEach(cb => cb.checked = checked);
    });

    // Xoá nhiều mục
    document.getElementById('deleteSelectedBtn').addEventListener('click', function () {
        const selected = Array.from(document.querySelectorAll('.select-item:checked'))
                .map(cb => cb.value);
        if (selected.length === 0) {
            alert("Vui lòng chọn ít nhất một mục để xóa.");
            return;
        }

        if (!confirm("Bạn có chắc chắn muốn xóa các mục đã chọn khỏi giỏ hàng?"))
            return;

        window.location.href = contextPath + '/cart/delete?ids=' + selected.join(',');
    });
   
    
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />