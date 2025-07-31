<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="header">
    <div class="header-content">

        <!-- Logo dạng hình ảnh -->
        <div class="logo">
            <a href="${pageContext.request.contextPath}/customer/book/list" class="logo-link">
                <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="ReadTopia Logo" style="height: 40px;">
            </a>
            <span style="font-size: 22px; font-weight: bold; margin-left: 10px;">ReadTopia</span>
        </div>

       

        <!-- Các hành động -->
        <div class="header-actions">
            <div class="header-icons">
                <jsp:include page="/WEB-INF/view/customer/notification/notification-widget.jsp" />

                <a href="${pageContext.request.contextPath}/customer/voucher/list" class="header-icon" title="Promotion">
                    <i class="fas fa-tags"></i>
                </a>

                <a href="${pageContext.request.contextPath}/cart/view" class="header-icon" title="Giỏ hàng">
                    <i class="fas fa-shopping-cart"></i>
                </a>

                <c:if test="${sessionScope.account.role == 0}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="header-icon btn-admin-dashboard" title="Admin Dashboard">
                        <i class="fas fa-tools"></i>
                    </a>
                </c:if>
            </div>

            <!-- Menu người dùng -->
            <div class="user-dropdown">
                <div class="user-menu" onclick="toggleDropdown()" tabindex="0" role="button" aria-haspopup="true" aria-expanded="false">
                    <i class="fas fa-user"></i>
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">
                            <span>${sessionScope.account.username}</span>
                        </c:when>
                        <c:otherwise>
                            <span>Login</span>
                        </c:otherwise>
                    </c:choose>
                    <i class="fas fa-chevron-down"></i>
                </div>

                <div class="dropdown-content" id="userDropdown" role="menu" aria-label="User menu">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">
                            <a href="${pageContext.request.contextPath}/profile" class="dropdown-item" role="menuitem">
                                <i class="fas fa-user"></i> Profile
                            </a>
                            <a href="${pageContext.request.contextPath}/order/history" class="dropdown-item" role="menuitem">
                                <i class="fas fa-shopping-bag"></i> Order History
                            </a>
                            <a href="${pageContext.request.contextPath}/logout" class="dropdown-item" role="menuitem">
                                <i class="fas fa-sign-out-alt"></i> Logout
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login" class="dropdown-item" role="menuitem">
                                <i class="fas fa-sign-in-alt"></i> Login
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</header>
