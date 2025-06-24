<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="mb-4">📖 Book Detail</h1>

        <div class="row">
            <!-- Ảnh sách -->
            <div class="col-md-4 mb-4">
                <img src="${book.image}" class="img-fluid rounded shadow-sm" alt="${book.bookTitle}">
            </div>

            <!-- Thông tin chi tiết -->
            <div class="col-md-8">
                <table class="table table-bordered">
                    <tr><th>ID</th><td>${book.bookID}</td></tr>
                    <tr><th>Title</th><td>${book.bookTitle}</td></tr>
                    <tr><th>Author</th><td>${book.author}</td></tr>
                    <tr><th>Translator</th><td>${book.translator}</td></tr>
                    <tr><th>Publisher</th><td>${book.publisher}</td></tr>
                    <tr><th>Publication Year</th><td>${book.publicationYear}</td></tr>
                    <tr><th>ISBN</th><td>${book.isbn}</td></tr>
                    <tr>
                        <th>Hardcover</th>
                        <td>
                            <c:choose>
                                <c:when test="${book.hardcover == 1}">✔ Yes</c:when>
                                <c:otherwise>❌ No</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr><th>Dimension</th><td>${book.dimension}</td></tr>
                    <tr><th>Weight</th><td>${book.weight} g</td></tr>
                    <tr><th>Price</th><td class="text-danger fw-bold">${book.bookPrice} VNĐ</td></tr>
                    <tr><th>Quantity</th><td>${book.bookQuantity}</td></tr>
                    <tr><th>Description</th><td>${book.bookDescription}</td></tr>
                    <tr>
                        <th>Status</th>
                        <td>
                            <c:choose>
                                <c:when test="${book.bookStatus == 1}">🟢 Visible</c:when>
                                <c:otherwise>🔴 Hidden</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>

                <div class="text-end mt-3">
                    <!-- Nút quay lại dùng biến returnUrl từ Servlet, nếu không có thì quay về list mặc định -->
                    <a href="${returnUrl != null ? returnUrl : (pageContext.request.contextPath + '/admin/book/list')}" class="btn btn-secondary">
                        ⬅ Back to Book List
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
