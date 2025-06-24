<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />
<div class="container py-5">
    <div class="row">
        <div class="col-md-5">
            <img src="${book.image}" alt="${book.bookTitle}" class="img-fluid rounded shadow-sm"
                 onerror="this.src='https://via.placeholder.com/300x400?text=No+Image'">
        </div>
        <div class="col-md-7">
            <h2 class="mb-3">${book.bookTitle}</h2>
            <p><strong>Tác giả:</strong> ${book.author}</p>
            <p><strong>Dịch giả:</strong> ${book.translator}</p>
            <p><strong>Nhà xuất bản:</strong> ${book.publisher}</p>
            <p><strong>Năm XB:</strong> ${book.publicationYear}</p>
            <p><strong>ISBN:</strong> ${book.isbn}</p>
            <p><strong>Kích thước:</strong> ${book.dimension}</p>
            <p><strong>Trọng lượng:</strong> ${book.weight} g</p>
            <p><strong>Mô tả:</strong><br>${book.bookDescription}</p>
            <p><strong>Số lượng còn lại:</strong> ${book.bookQuantity}</p>
            <p class="text-danger fs-4 fw-bold">Giá: ${book.bookPrice} VNĐ</p>

            <form action="${pageContext.request.contextPath}/cart/add" method="post" class="mt-3 d-flex">
                <input type="hidden" name="bookID" value="${book.bookID}" />
                <input type="number" name="quantity" value="1" min="1" max="${book.bookQuantity}" class="form-control w-25 me-2" />
                <button type="submit" class="btn btn-success me-2">🛒 Thêm vào giỏ</button>
                <a href="${pageContext.request.contextPath}/checkout?bookID=${book.bookID}&quantity=1"
                   class="btn btn-warning">⚡ Mua ngay</a>
            </form>

            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-outline-secondary">⬅ Quay lại danh sách</a>
            </div>
        </div>
    </div>
</div>

<%-- ✅ MODAL xác nhận khi thêm vào giỏ hàng thành công --%>
<c:if test="${param.added == 'true'}">
    <div class="modal fade show" id="addedModal" tabindex="-1" style="display: block; background-color: rgba(0,0,0,0.5);">
        <div class="modal-dialog">
            <div class="modal-content p-3">
                <div class="modal-header">
                    <h5 class="modal-title">🎉 Đã thêm vào giỏ hàng</h5>
                </div>
                <div class="modal-body d-flex">
                    <img src="${book.image}" alt="${book.bookTitle}" class="me-3 rounded" style="width: 80px; height: 100px; object-fit: cover;"
                         onerror="this.src='https://via.placeholder.com/80x100?text=No+Image'">
                    <div>
                        <p><strong>${book.bookTitle}</strong></p>
                        <p class="text-muted">Tác giả: ${book.author}</p>
                        <p>Giá: <span class="text-danger fw-bold">${book.bookPrice} VNĐ</span></p>
                        <p>Bạn có muốn đặt hàng ngay bây giờ?</p>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}/cart/view" class="btn btn-success">🛒 Đi tới giỏ hàng</a>
                    <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-secondary">Không, tiếp tục mua</a>
                </div>
            </div>
        </div>
    </div>
    <script>
        setTimeout(() => {
            document.getElementById("addedModal").style.display = 'none';
        }, 10000);
    </script>
</c:if>


