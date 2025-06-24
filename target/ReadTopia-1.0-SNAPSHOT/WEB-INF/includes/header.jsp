<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="header">
    <div class="header-content">
        <!-- Logo section -->
        <div class="logo">
            <div class="logo-icon">?</div>
            ReadTopia
        </div>

        <!-- Search bar -->
        <div class="search-container">
            <input type="text" class="search-input" placeholder="Search book..." aria-label="Search books">
            <button class="header-icon" id="searchBtn" title="Search">
                <i class="fas fa-search"></i>
            </button>
        </div>

        <!-- Action icons -->
        <div class="header-actions">
            <div class="header-icons">
                <button class="header-icon" title="Promotions" onclick="showPromotions()">
                    <i class="fas fa-bell"></i>
                </button>
                <button class="header-icon" title="Statistics">
                    <i class="fas fa-chart-line"></i>
                </button>
                <button class="header-icon" title="Cart" onclick="showCart()">
                    <i class="fas fa-shopping-cart"></i>
                </button>
            </div>

            <!-- User dropdown menu -->
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
                            <a href="${pageContext.request.contextPath}/orders" class="dropdown-item" role="menuitem">
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
