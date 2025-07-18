<%-- 
    Document   : notification-widget
    Created on : Jun 29, 2025, 7:05:46 PM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Notification Icon -->
<button class="header-icon" title="notification" onclick="toggleNotificationDropdown()">
    <i class="fas fa-bell"></i>
</button>

<!-- Notification Dropdown -->
<div class="notification-dropdown" id="notificationDropdown">
    <div class="notification-title">Notifications</div>

    <c:choose>
        <c:when test="${empty sessionScope.account}">
            <div class="notification-content locked">
                <i class="fas fa-lock fa-2x"></i>
                <p>Please log in to view notifications</p>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-danger">Log In</a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-danger">Register</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="notification-content">
                <c:forEach var="noti" items="${notifications}">
                    <div class="notification-item">
                        <strong>${noti.notTitle}</strong>
                        <p>${noti.notDescription}</p>
                    </div>
                </c:forEach>
                <c:if test="${empty notifications}">
                    <p>No notifications available.</p>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<style>
    .notification-dropdown {
        position: absolute;
        top: 70px;
        right: 300px;
        background: #fff;
        width: 400px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.15);
        padding: 12px;
        display: none;
        z-index: 1000;
        color: #000;
        
    }
    .notification-dropdown.show {
        display: block;
    }
    .notification-title {
        font-weight: bold;
        font-size: 16px;
        margin-bottom: 10px;
    }
    .notification-content.locked {
        text-align: center;
        padding: 20px;
    }
    .notification-content .btn {
        margin-top: 8px;
        display: block;
        width: 100%;
    }
    .notification-item {
        padding: 8px 0;
        border-bottom: 1px solid #eee;
    }
    .notification-item:last-child {
        border-bottom: none;
    }

</style>

<script>
    function toggleNotificationDropdown() {
        const dropdown = document.getElementById('notificationDropdown');
        dropdown.classList.toggle('show');
    }

    window.addEventListener('click', function (e) {
        const dropdown = document.getElementById('notificationDropdown');
        const bell = e.target.closest('.header-icon');
        if (!bell && !e.target.closest('#notificationDropdown')) {
            dropdown?.classList.remove('show');
        }
    });

</script>