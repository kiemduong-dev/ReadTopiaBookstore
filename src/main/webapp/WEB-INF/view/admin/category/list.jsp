<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <!-- Tiêu đề -->
        <div class="page-header">
            <h1 class="page-title">Category Management</h1>
        </div>

        <!-- 📌 Paging logic -->
        <c:set var="pageSize" value="10" />
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
        <c:set var="totalCategories" value="${fn:length(categoryList)}" />
        <c:set var="totalPages" value="${(totalCategories + pageSize - 1) div pageSize}" scope="page" />
        <fmt:formatNumber value="${totalPages}" maxFractionDigits="0" var="totalPagesInt" />
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}" />
        <c:set var="endIndex" value="${currentPage * pageSize > totalCategories ? totalCategories : currentPage * pageSize}" />

        <!-- Toolbar -->
        <c:if test="${fn:length(categoryList) > 10}">
            <div class="toolbar d-flex justify-content-between align-items-center mb-3">
                <form action="${pageContext.request.contextPath}/admin/category/list" method="get" class="d-flex gap-2">
                    <input type="text" name="keyword" class="form-control search-box"
                           placeholder="Search category name..."
                           value="${fn:escapeXml(keyword)}" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
                <button class="btn btn-success" onclick="location.href = '${pageContext.request.contextPath}/admin/category/add'">
                    <i class="fas fa-plus"></i> Add Category
                </button>
            </div>
        </c:if>
        <c:if test="${fn:length(categoryList) <= 10}">
            <div class="d-flex justify-content-end mb-3">
                <button class="btn btn-success" onclick="location.href = '${pageContext.request.contextPath}/admin/category/add'">
                    <i class="fas fa-plus"></i> Add Category
                </button>
            </div>
        </c:if>

        <!-- Thông báo thành công -->
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show mt-3">
                <i class="fas fa-check-circle"></i> ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>

        <!-- Số kết quả -->
        <c:if test="${not empty categoryList}">
            <div class="text-muted text-end mb-2">
                Found <strong>${fn:length(categoryList)}</strong> result(s).
            </div>
        </c:if>

        <!-- Bảng danh sách -->
        <c:if test="${not empty categoryList}">
            <div class="table-container mt-3">
                <table class="table table-bordered align-middle text-center">
                    <thead>
                        <tr>
                            <th style="width: 10%;">ID</th>
                            <th>Name</th>
                            <th style="width: 20%;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cat" begin="${startIndex}" end="${endIndex - 1}" items="${categoryList}">
                            <tr>
                                <td>${cat.categoryID}</td>
                                <td>${cat.categoryName}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/category/detail?id=${cat.categoryID}" class="btn btn-icon btn-info" title="View">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cat.categoryID}" class="btn btn-icon btn-warning" title="Edit">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/category/delete?id=${cat.categoryID}"
                                       onclick="return confirm('Are you sure you want to delete this category?');"
                                       class="btn btn-icon btn-danger" title="Delete">
                                        <i class="fas fa-trash-alt"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <!-- Không có dữ liệu -->
        <c:if test="${empty categoryList}">
            <div class="alert alert-info mt-4 text-center">
                No categories found in the system.
            </div>
        </c:if>

        <!-- 📄 Pagination -->
        <c:if test="${totalPages > 1}">
            <div class="d-flex justify-content-between align-items-center mt-4 flex-wrap gap-3">
                <div><span class="text-muted">${startIndex + 1}–${endIndex} of ${totalCategories} items</span></div>
                <nav>
                    <ul class="pagination mb-0">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${currentPage == 1 ? '#' : '?page='}${currentPage - 1}">&lt;</a>
                        </li>
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
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${currentPage == totalPages ? '#' : '?page='}${currentPage + 1}">&gt;</a>
                        </li>
                    </ul>
                </nav>

                <!-- Go to page -->
                <form method="get" action="${pageContext.request.contextPath}/admin/category/list" class="d-flex align-items-center gap-2">
                    <input type="number" name="page" min="1" max="${totalPagesInt}" 
                           class="form-control form-control-sm" style="width: 80px;" placeholder="Page" value="${param.page}" />
                    <c:if test="${not empty keyword}">
                        <input type="hidden" name="keyword" value="${keyword}" />
                    </c:if>
                    <button class="btn btn-sm btn-outline-secondary" type="submit">Go</button>
                </form>
            </div>
        </c:if>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const maxPage = parseInt("${totalPagesInt}", 10);
        const minPage = 1;

        // Go to page form validation
        const goToPageForm = document.querySelector('form[action$="/admin/category/list"]');
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
        // Chặn click Previous/Next vượt số trang
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