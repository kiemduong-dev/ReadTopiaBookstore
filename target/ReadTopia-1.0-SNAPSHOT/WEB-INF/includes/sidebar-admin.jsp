<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Sidebar -->
<div class="sidebar">
    <!-- Header -->
    <div class="sidebar-header">
        <div class="logo">
            <div class="logo-icon">🐻</div>
            READTOPIA
        </div>
        <div class="user-info">
            <div><strong><c:out value="${sessionScope.account.username}" /></strong></div>
            <div style="font-size: 12px; opacity: 0.8;">
                <c:choose>
                    <c:when test="${sessionScope.account.role == 0}">Administrator</c:when>
                    <c:when test="${sessionScope.account.role == 1}">Staff</c:when>
                    <c:when test="${sessionScope.account.role == 2}">Seller Staff</c:when>
                    <c:when test="${sessionScope.account.role == 3}">Warehouse Staff</c:when>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Menu -->
    <nav class="sidebar-menu">

        <c:if test="${sessionScope.account.role == 0}">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/account/list" class="menu-item">
                <i class="fas fa-users"></i> Account Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/staff/list" class="menu-item">
                <i class="fas fa-user-tie"></i> Staff Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/book/list" class="menu-item">
                <i class="fas fa-book"></i> Book Management
            </a>
            <a href="${pageContext.request.contextPath}/order/management" class="menu-item">
                <i class="fas fa-shopping-cart"></i> Order Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/promotion/list" class="menu-item">
                <i class="fas fa-tags"></i> Promotion Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/supplier/list" class="menu-item">
                <i class="fas fa-truck"></i> Supplier Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/category/list" class="menu-item">
                <i class="fas fa-list"></i> Category Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/stock/list" class="menu-item">
                <i class="fas fa-warehouse"></i> Inventory Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/notification/list" class="menu-item">
                <i class="fas fa-bell"></i> Notification Management
            </a>
            <a href="${pageContext.request.contextPath}/customer/book/list" class="menu-item">
                <i class="fas fa-home"></i> Back to Homepage
            </a>
        </c:if>

        <c:if test="${sessionScope.account.role == 1}">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/account/list" class="menu-item">
                <i class="fas fa-users"></i> Account Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/staff/list" class="menu-item">
                <i class="fas fa-user-tie"></i> Staff Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/notification/list" class="menu-item">
                <i class="fas fa-bell"></i> Notification Management
            </a>
        </c:if>

        <c:if test="${sessionScope.account.role == 2}">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/promotion/list" class="menu-item">
                <i class="fas fa-tags"></i> Promotion Management
            </a>
            <a href="${pageContext.request.contextPath}/order/management" class="menu-item">
                <i class="fas fa-shopping-cart"></i> Order Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/notification/list" class="menu-item">
                <i class="fas fa-bell"></i> Notification Management
            </a>
        </c:if>

        <c:if test="${sessionScope.account.role == 3}">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/stock/list" class="menu-item">
                <i class="fas fa-warehouse"></i> Inventory Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/supplier/list" class="menu-item">
                <i class="fas fa-truck"></i> Supplier Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/notification/list" class="menu-item">
                <i class="fas fa-bell"></i> Notification Management
            </a>
        </c:if>

        <a href="${pageContext.request.contextPath}/logout" class="menu-item">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>

    </nav>
</div>
