<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Account Management</h1>

        <!-- ✅ Thông báo thành công -->
        <c:if test="${not empty flashMessage}">
    <div class="alert alert-success alert-dismissible fade show" id="autoDismissAlert" role="alert">
        <i class="fas fa-check-circle me-1"></i>
        <span>${flashMessage}</span>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>


        <!-- 🔢 Paging logic -->
        <c:set var="pageSize" value="10" />
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
        <c:set var="totalAccounts" value="${fn:length(accountList)}" />
        <c:set var="totalPages" value="${(totalAccounts + pageSize - 1) div pageSize}" />
        <fmt:formatNumber value="${totalPages}" maxFractionDigits="0" var="totalPagesInt" />
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}" />
        <c:set var="endIndex" value="${currentPage * pageSize > totalAccounts ? totalAccounts : currentPage * pageSize}" />

        <div class="card">
            <div class="toolbar d-flex gap-2 align-items-center">
                <a href="${pageContext.request.contextPath}/admin/account/add" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Add Account
                </a>
                <form method="get" action="${pageContext.request.contextPath}/admin/account/search"
                      class="d-flex gap-2 align-items-center mb-0">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Search by username or full name"
                           class="form-control" autocomplete="off" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <!-- 📋 Account Table -->
            <div class="table-container mt-3">
                <table class="table table-bordered align-middle text-center">
                    <thead>
                        <tr>
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
                        <c:if test="${not empty accountList}">
                            <c:forEach var="acc" begin="${startIndex}" end="${endIndex - 1}" items="${accountList}">
                                <tr>
                                    <td>${acc.username}</td>
                                    <td>${acc.firstName} ${acc.lastName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${acc.sex == 1}">Male</c:when>
                                            <c:otherwise>Female</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${acc.role == 0}">Admin</c:when>
                                            <c:when test="${acc.role == 1}">Staff</c:when>
                                            <c:when test="${acc.role == 2}">Seller Staff</c:when>
                                            <c:when test="${acc.role == 3}">Warehouse Staff</c:when>
                                            <c:when test="${acc.role == 4}">Customer</c:when>
                                            <c:otherwise>Unknown</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${acc.email}</td>
                                    <td>
                                        <span class="status-badge ${acc.accStatus == 1 ? 'active' : 'inactive'}">
                                            ${acc.accStatus == 1 ? 'Active' : 'Disabled'}
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/account/detail?username=${acc.username}"
                                           class="btn btn-icon btn-info" title="View">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <c:if test="${(acc.role == 1 || acc.role == 4) && acc.username != sessionScope.account.username}">
                                            <a href="${pageContext.request.contextPath}/admin/account/edit?username=${acc.username}"
                                               class="btn btn-icon btn-warning" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/account/delete?username=${acc.username}"
                                               onclick="return confirm('Do you really want to delete ${acc.username}?');"
                                               class="btn btn-icon btn-danger" title="Delete">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>

                        <c:if test="${empty accountList}">
                            <tr><td colspan="7" class="text-muted">No accounts found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- 📄 Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="d-flex justify-content-between align-items-center mt-4 flex-wrap gap-3">
                    <div><span class="text-muted">${startIndex + 1}–${endIndex} of ${totalAccounts} items</span></div>

                    <nav>
                        <ul class="pagination mb-0">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${currentPage == 1 ? '#' : '?page='}${currentPage - 1}${not empty keyword ? '&keyword=' : ''}${keyword}">&lt;</a>
                            </li>

                            <c:set var="dotBefore" value="false" />
                            <c:set var="dotAfter" value="false" />
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <c:choose>
                                    <c:when test="${i <= 3 || i > totalPages - 3 || (i >= currentPage - 1 && i <= currentPage + 1)}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}${not empty keyword ? '&keyword=' : ''}${keyword}">${i}</a>
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
                                <a class="page-link" href="${currentPage == totalPages ? '#' : '?page='}${currentPage + 1}${not empty keyword ? '&keyword=' : ''}${keyword}">&gt;</a>
                            </li>
                        </ul>
                    </nav>

                    <!-- Go to page -->
                    <form method="get" action="" class="d-flex align-items-center gap-2">
                        <input type="number" name="page" min="1" max="${totalPagesInt}"
                               class="form-control form-control-sm" style="width: 80px;" placeholder="Page"
                               value="${param.page}" />
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
    .status-badge.active {
        color: green;
        font-weight: bold;
    }
    .status-badge.inactive {
        color: red;
        font-weight: bold;
    }
</style>

<script>
document.addEventListener("DOMContentLoaded", () => {
    const maxPage = parseInt("${totalPages}", 10);
    const minPage = 1;

    const goToPageForm = document.querySelector('form[action=""]'); // form Go to page
    if (goToPageForm) {
        goToPageForm.addEventListener("submit", function (e) {
            const pageInput = this.querySelector('input[name="page"]');
            let pageValue = parseInt(pageInput.value, 10);

            if (isNaN(pageValue) || pageValue < minPage || pageValue > maxPage) {
                e.preventDefault();
                alert(`Please enter right number`);
                pageInput.focus();
            }
        });
    }

    const alertBox = document.getElementById("autoDismissAlert");
    if (alertBox) {
        setTimeout(() => {
            alertBox.classList.remove("show");
            alertBox.classList.add("hide");
        }, 3000);
    }
});
</script>
