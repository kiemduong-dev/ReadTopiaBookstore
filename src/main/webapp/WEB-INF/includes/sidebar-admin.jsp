<%-- 
    Document   : sidebar-admin
    Created on : Jun 22, 2025, 2:22:25 PM
    Author     : ADMIN
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />

<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <div class="logo">
                <div class="logo-icon">🐻</div>
                READTOPIA
            </div>
            <div class="user-info">
                <div><strong>admin</strong></div>
                <div style="font-size: 12px; opacity: 0.8;">Administrator</div>
            </div>
        </div>

        <nav class="sidebar-menu">
            <a href="#" class="menu-item active" onclick="showPage('dashboard')">
                <i class="fas fa-tachometer-alt"></i>
                Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/account/list" class="menu-item">
                <i class="fas fa-users"></i>
                Account Management
            </a>
            <a href="${pageContext.request.contextPath}/admin/staff/list" class="menu-item">
                <i class="fas fa-user-tie"></i>
                Staff Management
            </a>
            <a href="#" class="menu-item" onclick="showPage('book-management')">
                <i class="fas fa-book"></i>
                Book Management
            </a>
            <a href="#" class="menu-item" onclick="showPage('order-management')">
                <i class="fas fa-shopping-cart"></i>
                Order Management
            </a>
            <a href="#" class="menu-item" onclick="showPage('promotion-management')">
                <i class="fas fa-tags"></i>
                Promotion Management
            </a>
            <a href="#" class="menu-item" onclick="showPage('supplier-management')">
                <i class="fas fa-truck"></i>
                Supplier Management
            </a>
            <a href="#" class="menu-item" onclick="showPage('category-management')">
                <i class="fas fa-list"></i>
                Category Management
            </a>
            <a href="#" class="menu-item" onclick="showPage('inventory-management')">
                <i class="fas fa-warehouse"></i>
                Inventory Management
            </a>
            <a href="#" class="menu-item" onclick="showPage('notification-management')">
                <i class="fas fa-bell"></i>
                Notification Management
            </a>
        </nav>
    </div>
</body>
