<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Supplier Management</h1>

        <!-- Toolbar: Add + Search -->
        <div class="toolbar">
            <a href="${pageContext.request.contextPath}/admin/supplier/add" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Supplier
            </a>
        </div>

        <!-- Hiển thị số lượng kết quả -->
        <c:if test="${not empty suppliers}">
            <p style="margin-top: 10px; color: #333;">Found <strong>${fn:length(suppliers)}</strong> supplier(s).</p>
        </c:if>

        <!-- Danh sách nhà cung cấp -->
        <c:choose>
            <c:when test="${empty suppliers}">
                <p class="text-muted" style="text-align:center;">No supplier found.</p>
            </c:when>
            <c:otherwise>
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
                        <tbody>
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
                                        <a href="${pageContext.request.contextPath}/admin/supplier/delete?id=${s.supID}" class="btn-icon btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this supplier?')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Phân trang -->
        <div class="pagination-container">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <!-- Previous button -->
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/supplier/list?page=${currentPage - 1}">&lt;</a>
                    </li>

                    <!-- Page numbers -->
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/admin/supplier/list?page=${i}">${i}</a>
                        </li>
                    </c:forEach>

                    <!-- Next button -->
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/supplier/list?page=${currentPage + 1}">&gt;</a>
                    </li>
                </ul>
            </nav>
        </div>

    </div>
</div>
