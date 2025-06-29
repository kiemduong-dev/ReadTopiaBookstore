<%-- 
    Document   : list
    Created on : May 29, 2025, 7:26:58 AM
    Author     : ngtua
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<!DOCTYPE html>
<html>
    <head>
        <title>Notification List</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/your-custom-style.css">
    </head>
    <body>
        <div class="main-content">
            <div class="content-area">
                <div class="page-header">
                    <div class="page-title">Notification List</div>
                    <div class="page-subtitle">All system notifications</div>
                </div>

                <!-- Search and Add Form -->
                <form action="${pageContext.request.contextPath}/admin/notification/list" method="get" class="toolbar">
                    <input type="text" name="search" class="search-box" placeholder="Search by title..." value="${param.search}" />
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">Search</button>
                        <a href="${pageContext.request.contextPath}/admin/notification/add" class="btn btn-primary">+ Add New</a>
                    </div>
                </form>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="success-message" style="background: #fdecea; color: #d32f2f;">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        ${error}
                    </div>
                </c:if>

                <!-- Notification Table -->
                <div class="card">
                    <div class="card-title">Notification Table</div>
                    <div class="table-container">
                        <table class="table">
                            <thead>
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
                                        <td>
                                            <c:choose>
                                                <c:when test="${noti.receiver == 0}">Admin</c:when>
                                                <c:when test="${noti.receiver == 1}">Customer</c:when>
                                                <c:when test="${noti.receiver == 2}">Seller Staff</c:when>
                                                <c:when test="${noti.receiver == 3}">Warehouse Staff</c:when>
                                                <c:when test="${noti.receiver == 4}">All</c:when>
                                                <c:otherwise>Unknown</c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <div class="btn-group">
                                                <a href="${pageContext.request.contextPath}/admin/notification/detail?id=${noti.notID}" 
                                                   class="btn btn-icon btn-info" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/notification/delete?id=${noti.notID}" 
                                                   class="btn btn-icon btn-danger" title="Delete"
                                                   onclick="return confirm('Are you sure you want to delete this notification?')">
                                                    <i class="fas fa-trash-alt"></i>
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
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
