<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <!-- Tiêu đề -->
        <div class="page-header">
            <h1 class="page-title"> Book Management</h1>
        </div>

        <!-- Thông báo thành công -->
        <c:if test="${not empty sessionScope.success}">
            <div class="success-message">
                <i class="fas fa-check-circle"></i> ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session" />
        </c:if>

        <!-- Toolbar -->
        <div class="toolbar">
            <form action="${pageContext.request.contextPath}/admin/book/list" method="get" class="d-flex gap-2">
                <input type="text" name="keyword" class="form-control search-box" placeholder="Search by title or author" value="${keyword}" />
                <button type="submit" class="btn btn-primary"> Search</button>
            </form>
            <a href="${pageContext.request.contextPath}/admin/book/add" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Book
            </a>
        </div>

        <!-- Bảng sách -->
        <c:if test="${not empty bookList}">
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th class="text-center">ID</th>
                            <th class="text-center">Title</th>
                            <th class="text-center">Author</th>
                            <th class="text-center">Translator</th>
                            <th class="text-center">Quantity</th>
                            <th class="text-center">Price</th>
                            <th class="text-center">Status</th>
                            <th class="text-center">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${bookList}">
                            <tr>
                                <td class="text-center">${b.bookID}</td>
                                <td class="text-center">${b.bookTitle}</td>
                                <td class="text-center">${b.author}</td>
                                <td class="text-center">${b.translator}</td>
                                <td class="text-center">${b.bookQuantity}</td>
                                <td class="text-center text-danger fw-bold">${b.bookPrice} VNĐ</td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${b.bookStatus == 1}">
                                            <span class="status-badge active">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge inactive">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <!-- ✅ Đã sửa đường dẫn View -->
                                    <a href="${pageContext.request.contextPath}/admin/book/detail?id=${b.bookID}" class="btn-icon btn-info" title="View">
                                        <i class="fas fa-eye"></i>
                                    </a>

                                    <a href="${pageContext.request.contextPath}/admin/book/edit?id=${b.bookID}" class="btn-icon btn-warning" title="Edit">
                                        <i class="fas fa-edit"></i>
                                    </a>

                                    <a href="${pageContext.request.contextPath}/admin/book/delete?id=${b.bookID}"
                                       onclick="return confirm('Are you sure you want to delete this book?');"
                                       class="btn-icon btn-danger" title="Delete">
                                        <i class="fas fa-trash-alt"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <!-- Không có sách -->
        <c:if test="${empty bookList}">
            <div class="alert alert-info mt-4 text-center">
                No books found in the system.
            </div>
        </c:if>
    </div>
</div>
