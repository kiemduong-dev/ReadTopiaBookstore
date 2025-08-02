<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Staff Management</h1>

        <!-- ✅ Success Message -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fas fa-check-circle"></i>
                <span>${sessionScope.message}</span>
            </div>
            <c:remove var="message" scope="session" />
        </c:if>

        <!-- 📌 Paging logic -->
        <c:set var="pageSize" value="10" />
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
        <c:set var="totalStaffs" value="${fn:length(staffs)}" />
        <c:set var="totalPages" value="${(totalStaffs + pageSize - 1) div pageSize}" scope="page" />
        <fmt:formatNumber value="${totalPages}" maxFractionDigits="0" var="totalPagesInt" />
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}" />
        <c:set var="endIndex" value="${currentPage * pageSize > totalStaffs ? totalStaffs : currentPage * pageSize}" />

        <div class="card">
            <div class="toolbar" style="display:flex; gap:10px; align-items:center;">
                <!-- ✅ Only Staff Manager (role = 1) can Add -->
                <c:if test="${sessionScope.account.role == 1}">
                    <a href="${pageContext.request.contextPath}/admin/staff/add" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Add Staff
                    </a>
                </c:if>

                <!-- 🔍 Search -->
                <form method="get" action="${pageContext.request.contextPath}/admin/staff/search"
                      style="display:flex; gap:5px; align-items:center; margin:0;">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Search by username or full name"
                           class="search-box" autocomplete="off" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <!-- 🧾 Staff Table -->
            <div class="table-container" style="margin-top:15px;">
                <table class="table table-bordered align-middle text-center">
                    <thead>
                        <tr>
                            <th>Staff ID</th>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Sex</th>
                            <th>Role</th>
                            <th>Email</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="staff" begin="${startIndex}" end="${endIndex - 1}" items="${staffs}">
                            <tr>
                                <td>${staff.staffID}</td>
                                <td>${staff.username}</td>
                                <td>${staff.firstName} ${staff.lastName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${staff.sex == 1}">Male</c:when>
                                        <c:otherwise>Female</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${staff.role == 1}">Staff</c:when>
                                        <c:when test="${staff.role == 2}">Seller Staff</c:when>
                                        <c:when test="${staff.role == 3}">Warehouse Staff</c:when>
                                        <c:otherwise>Unknown</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${staff.email}</td>
                                <td>
                                    <span class="status-badge ${staff.accStatus == 1 ? 'active' : 'inactive'}">
                                        ${staff.accStatus == 1 ? 'Active' : 'Disabled'}
                                    </span>
                                </td>
                                <td>
                                    <!-- 👁 View -->
                                    <a href="${pageContext.request.contextPath}/admin/staff/detail?staffID=${staff.staffID}"
                                       class="btn btn-icon btn-info" title="View">
                                        <i class="fas fa-eye"></i>
                                    </a>

                                    <!-- ✏️🗑️ Edit/Delete: only for Staff Manager -->
                                    <c:if test="${sessionScope.account.role == 1}">
                                        <c:if test="${staff.username == sessionScope.account.username || staff.role == 2 || staff.role == 3}">
                                            <a href="${pageContext.request.contextPath}/admin/staff/edit?staffID=${staff.staffID}"
                                               class="btn btn-icon btn-warning" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                        </c:if>
                                        <c:if test="${(staff.role == 2 || staff.role == 3) && staff.username != sessionScope.account.username}">
                                            <a href="${pageContext.request.contextPath}/admin/staff/delete?id=${staff.staffID}"
                                               onclick="return confirm('Do you really want to delete ${staff.firstName} ${staff.lastName}?');"
                                               class="btn btn-icon btn-danger" title="Delete">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty staffs}">
                            <tr>
                                <td colspan="8">No staff found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- 📄 Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="d-flex justify-content-between align-items-center mt-4 flex-wrap gap-3">
                    <div><span class="text-muted">${startIndex + 1}–${endIndex} of ${totalStaffs} items</span></div>

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
                    <form method="get" action="${pageContext.request.contextPath}/admin/staff/search" class="d-flex align-items-center gap-2">
                        <input type="number" name="page" min="1" max="${totalPagesInt}" class="form-control form-control-sm"
                               style="width: 80px;" placeholder="Page" value="${param.page}" />
                        <c:if test="${not empty keyword}">
                            <input type="hidden" name="keyword" value="${keyword}" />
                        </c:if>
                        <button class="btn btn-sm btn-outline-secondary" type="submit">Go</button>
                    </form>
                </div>
            </c:if>
        </div>
    </div>
</div>

<style>
    .status-badge.active { color: green; font-weight: bold; }
    .status-badge.inactive { color: red; font-weight: bold; }
    .pagination .page-item.disabled .page-link {
        color: #ccc;
        pointer-events: none;
        background-color: #f8f9fa;
    }
</style>

<script>
document.addEventListener("DOMContentLoaded", () => {
    const maxPage = parseInt("${totalPages}", 10);
    const minPage = 1;

    // ✅ Kiểm tra form Go to page
    const goToPageForm = document.querySelector('form[action$="/admin/staff/search"]');
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
