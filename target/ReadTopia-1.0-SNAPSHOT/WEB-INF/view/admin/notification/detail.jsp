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
    <style>
        .notification-card {
            max-width: 700px;
            margin: 30px auto;
            padding: 25px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        .notification-card .form-group {
            margin-bottom: 20px;
        }
        .notification-card .form-label {
            font-weight: bold;
            color: #333;
            font-size: 16px;
        }
        .notification-card p {
            margin: 5px 0 0;
            font-size: 15px;
            color: #555;
        }
        .back-btn {
            display: block;
            width: fit-content;
            margin: 30px auto 0;
        }
        .page-title {
            font-size: 28px;
            font-weight: bold;
            margin-top: 20px;
            color: #2c3e50;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="main-content">
        <div class="content-area">
            <h2 class="page-title">Notification Details</h2>

            <c:if test="${not empty error}">
                <div class="success-message" style="background: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; text-align: center;">
                    ${error}
                </div>
            </c:if>

            <c:if test="${not empty notification}">
                <div class="notification-card">
                    <div class="form-group">
                        <label class="form-label">Title:</label>
                        <p>${notification.notTitle}</p>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Receiver:</label>
                        <p>
                            <c:choose>
                                <c:when test="${notification.receiver == 0}">Admin</c:when>                               
                                <c:when test="${notification.receiver == 2}">Seller Staff</c:when>
                                <c:when test="${notification.receiver == 3}">Warehouse Staff</c:when>
                                <c:when test="${notification.receiver == 4}">Customer</c:when>
                                <c:when test="${notification.receiver == 5}">All</c:when>
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

            <div class="back-btn text-center">
                <a href="${pageContext.request.contextPath}/admin/notification/list" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Back to List
                </a>
            </div>
        </div>
    </div>

    <!-- Font Awesome (for icons) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</body>
</html>