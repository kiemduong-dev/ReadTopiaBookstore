<%-- 
    Document   : list
    Created on : May 29, 2025, 7:26:58 AM
    Author     : ngtua
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<!DOCTYPE html>
<style>
    .btn {
        padding: 8px 16px;
        border: none;
        border-radius: 6px;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }

    .btn-primary {
        background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
        color: white;
    }

    .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(76,175,80,0.3);
    }

    .btn-secondary {
        background: #f5f5f5;
        color: #666;
    }

    .btn-danger {
        background: #f44336;
        color: white;
    }

    .btn-icon {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
    }

    .btn-icon.btn-info {
        background: #2196f3;
        color: white;
    }

    .btn-icon.btn-warning {
        background: #ff9800;
        color: white;
    }

    .btn-icon.btn-danger {
        background: #f44336;
        color: white;
    }
</style>

<html>
    <head>
        <title>Notification List</title>
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="text-center mb-4">Notification List</h2>

            <!-- Search Form -->
            <form action="${pageContext.request.contextPath}/admin/notification/list" method="get" class="row g-2 mb-3">
                <div class="col-md-8">
                    <input type="text" name="search" class="form-control" placeholder="Search by title..." value="${param.search}" />
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">Search</button>
                </div>
                <div class="col-md-2">
                    <a href="${pageContext.request.contextPath}/admin/notification/add" class="btn btn-success w-100">+ Add New</a>
                </div>
            </form>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="table-pagination-wrapper" style="min-height: 400px; position: relative;">
                <!-- Notification Table -->
                <div class="table-responsive">
                    <table class="table table-bordered table-hover align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Receiver (ID)</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="noti" items="${notifications}">
                                <tr>
                                    <td>${noti.notID}</td>
                                    <td>${noti.notTitle}</td>
                                    <td>${noti.receiver}</td>
                                    <td>
                                        <div class="d-flex gap-2">
                                            <a href="${pageContext.request.contextPath}/admin/notification/detail?id=${noti.notID}" 
                                               class="btn-icon btn-info" 
                                               title="View">
                                                <i class="bi bi-eye"></i>
                                            </a>                                           
                                            <a href="${pageContext.request.contextPath}/admin/notification/delete?id=${noti.notID}" 
                                               class="btn-icon btn-danger" 
                                               title="Delete"
                                               onclick="return confirm('Are you sure you want to delete this notification?')">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty notifications}">
                                <tr>
                                    <td colspan="4" class="text-center text-muted">No notifications found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <div class="mt-3">
                        <nav>
                            <ul class="pagination">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin/notification/list?page=${currentPage - 1}&search=${param.search}">&lt;</a>
                                </li>
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/admin/notification/list?page=${i}&search=${param.search}">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin/notification/list?page=${currentPage + 1}&search=${param.search}">&gt;</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>

