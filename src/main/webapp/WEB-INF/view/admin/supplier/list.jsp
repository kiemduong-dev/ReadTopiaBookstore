<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Supplier Management</h1>
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" id="autoDismissAlert" role="alert">
                <i class="fas fa-check-circle me-1"></i>
                <span>${sessionScope.successMessage}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="successMessage" scope="session" />
        </c:if>

        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" id="autoDismissAlert" role="alert">
                <i class="fas fa-exclamation-circle me-1"></i>
                <span>${sessionScope.errorMessage}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="errorMessage" scope="session" />
        </c:if>



        <div class="toolbar">
            <a href="${pageContext.request.contextPath}/admin/supplier/add" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Supplier
            </a>
            <input type="text" id="searchSupplierInput" class="search-box" placeholder="Search supplier by name..." />
        </div>

        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody class="admin-main-content-body-tbody">
                    <c:forEach var="s" items="${suppliers}">
                        <tr>
                            <td>${s.supID}</td>
                            <td>${s.supName}</td>
                            <td>${s.supPhone}</td>
                            <td>${s.supAddress}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${s.supStatus == 1}">
                                        <span class="status-badge active">Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge inactive">Inactive</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/supplier/detail?id=${s.supID}" class="btn-icon btn-info">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/supplier/edit?id=${s.supID}" class="btn-icon btn-warning">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/supplier/delete?id=${s.supID}" class="btn-icon btn-danger" onclick="return confirm('Are you sure you want to delete this supplier?')">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="admin-main-content-pagination">
            <ul class="pagination">
                <li>
                    <button class="page-btn" onclick="pagingSupplierPage(${currentPage - 1})" data-page="${currentPage - 1}" <c:if test="${currentPage == 1}">disabled</c:if>> &lt; </button>
                    </li>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li>
                        <button class="page-btn ${i == currentPage ? 'active' : ''}" onclick="pagingSupplierPage(${i})" data-page="${i}">${i}</button>
                    </li>
                </c:forEach>
                <li>
                    <button class="page-btn" onclick="pagingSupplierPage(${currentPage + 1})" data-page="${currentPage + 1}" <c:if test="${currentPage == totalPages}">disabled</c:if>> &gt; </button>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/paging-supplier.js"></script>
<script>
                        window.addEventListener("DOMContentLoaded", function () {
                            const msg = document.getElementById("successMessage");
                            if (msg) {
                                setTimeout(() => {
                                    msg.style.display = "none";
                                }, 3000);
                            }
                        });
</script>
