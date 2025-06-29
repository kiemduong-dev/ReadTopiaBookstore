<%-- 
    Document   : detail
    Created on : May 29, 2025, 7:27:16 AM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<!DOCTYPE html>
<html>
    <head>
        <title>Notification Details</title>
    </head>
    <body>
        <div class="main-content">
            <div class="content-area">
                <div class="page-header text-center">
                    <h2 class="page-title">Notification Details</h2>
                </div>

                <c:if test="${not empty error}">
                    <div class="success-message" style="background: #f8d7da; color: #721c24;">
                        ${error}
                    </div>
                </c:if>

                <c:if test="${not empty notification}">
                    <div class="card">
                        <div class="form-group">
                            <label class="form-label">ID:</label>
                            <p>${notification.notID}</p>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Title:</label>
                            <p>${notification.notTitle}</p>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Receiver:</label>
                            <p>
                                <c:choose>
                                    <c:when test="${notification.receiver == 0}">All</c:when>
                                    <c:when test="${notification.receiver == 1}">Admin</c:when>
                                    <c:when test="${notification.receiver == 2}">Staff</c:when>
                                    <c:when test="${notification.receiver == 3}">Customer</c:when>
                                    <c:otherwise>Unknown</c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Content:</label>
                            <p>${notification.notDescription}</p>
                        </div>
                    </div>
                </c:if>

                <div class="btn-group" style="margin-top: 20px;">
                    <a href="${pageContext.request.contextPath}/admin/notification/list" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Back to List
                    </a>
                </div>
            </div>
        </div>

        <!-- Font Awesome (for optional icon) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </body>
</html>
