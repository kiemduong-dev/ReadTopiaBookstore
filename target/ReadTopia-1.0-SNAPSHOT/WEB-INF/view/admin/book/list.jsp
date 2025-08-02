<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

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
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fas fa-check-circle"></i> ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session" />
        </c:if>

        <!-- Toolbar -->
        <div class="toolbar d-flex gap-2 mb-3">
            <form action="${pageContext.request.contextPath}/admin/book/list" method="get" class="d-flex gap-2">
                <input type="text" name="keyword" class="form-control search-box" placeholder="Search by title or author" value="${keyword}" />
                <button type="submit" class="btn btn-primary"> Search</button>
            </form>
            <a href="${pageContext.request.contextPath}/admin/book/add" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Book
            </a>
        </div>

        <!-- Paging logic -->
        <c:set var="pageSize" value="10" />
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
        <c:set var="totalBooks" value="${fn:length(bookList)}" />
        <c:set var="totalPages" value="${(totalBooks + pageSize - 1) div pageSize}" scope="page" />
        <fmt:formatNumber value="${totalPages}" maxFractionDigits="0" var="totalPagesInt" />
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}" />
        <c:set var="endIndex" value="${currentPage * pageSize > totalBooks ? totalBooks : currentPage * pageSize}" />

        <!-- Bảng sách -->
        <c:if test="${not empty bookList}">
            <div class="table-container">
                <table class="table table-bordered align-middle text-center">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Translator</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" begin="${startIndex}" end="${endIndex - 1}" items="${bookList}">
                            <tr>
                                <td>${b.bookID}</td>
                                <td>${b.bookTitle}</td>
                                <td>${b.author}</td>
                                <td>${b.translator}</td>
                                <td>${b.bookQuantity}</td>
                                <td class="text-danger fw-bold">${b.bookPrice} VNĐ</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${b.bookStatus == 1}">
                                            <span class="status-badge active">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge inactive">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
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

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="d-flex justify-content-between align-items-center mt-3 flex-wrap gap-3">
                    <div><span class="text-muted">${startIndex + 1}–${endIndex} of ${totalBooks} items</span></div>
                    <nav>
                        <ul class="pagination mb-0">
                            <!-- Previous -->
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${currentPage == 1 ? '#' : '?page='}${currentPage - 1}">&lt;</a>
                            </li>

                            <!-- Smart pagination -->
                            <c:set var="dotBefore" value="false" />
                            <c:set var="dotAfter" value="false" />
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <c:choose>
                                    <c:when test="${i <= 3 || i > totalPages - 3 || (i >= currentPage - 1 && i <= currentPage + 1)}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}">${i}</a>
                                        </li>
                                    </c:when>
                                    <c:when test="${i == 4 && !dotBefore && currentPage > 5}">
                                        <c:set var="dotBefore" value="true" />
                                        <li class="page-item disabled"><span class="page-link">...</span></li>
                                        </c:when>
                                        <c:when test="${i == totalPages - 3 && !dotAfter && currentPage < totalPages - 4}">
                                            <c:set var="dotAfter" value="true" />
                                        <li class="page-item disabled"><span class="page-link">...</span></li>
                                        </c:when>
                                    </c:choose>
                                </c:forEach>

                            <!-- Next -->
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="${currentPage == totalPages ? '#' : '?page='}${currentPage + 1}">&gt;</a>
                            </li>
                        </ul>
                    </nav>

                    <!-- Go to page -->
                    <form method="get" action="${pageContext.request.contextPath}/admin/book/list" class="d-flex align-items-center gap-2">
                        <input type="number" name="page" min="1" max="${totalPagesInt}" class="form-control form-control-sm"
                               style="width: 80px;" placeholder="Page" value="${param.page}" />
                        <c:if test="${not empty keyword}">
                            <input type="hidden" name="keyword" value="${keyword}" />
                        </c:if>
                        <button class="btn btn-sm btn-outline-secondary" type="submit">Go</button>
                    </form>
                </div>
            </c:if>
        </c:if>

        <!-- Không có sách -->
        <c:if test="${empty bookList}">
            <div class="alert alert-info mt-4 text-center">
                No books found in the system.
            </div>
        </c:if>
    </div>
</div>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const maxPage = parseInt("${totalPagesInt}", 10);
        const minPage = 1;

        // ✅ Kiểm tra form Go to page
        const goToPageForm = document.querySelector('form[action$="/admin/book/list"]');
        if (goToPageForm) {
            goToPageForm.addEventListener("submit", function (e) {
                const pageInput = this.querySelector('input[name="page"]');
                let pageValue = parseInt(pageInput.value, 10);

                if (isNaN(pageValue) || pageValue < minPage || pageValue > maxPage) {
                    e.preventDefault();
                    alert(`Please enter from ${minPage} to ${maxPage}.`);
                    pageInput.focus();
                }
            });
        }

        // ✅ Giới hạn nút Previous/Next
        document.querySelectorAll(".pagination .page-link").forEach(link => {
            link.addEventListener("click", function (e) {
                const url = new URL(this.href, window.location.origin);
                const pageParam = url.searchParams.get("page");

                if (pageParam) {
                    const pageNum = parseInt(pageParam, 10);
                    if (isNaN(pageNum) || pageNum < minPage || pageNum > maxPage) {
                        e.preventDefault();
                    }
                }
            });
        });
    });
</script>
