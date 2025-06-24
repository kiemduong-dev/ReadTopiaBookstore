<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <!-- Filter Section -->
        <div class="filter-section">
            <div class="filter-content">
                <div class="filter-group">
                    <label>Sort by:</label>
                    <select class="filter-select" id="sortSelect">
                        <option value="default" selected>Default</option>
                        <option value="price-asc">Giá tăng dần</option>
                        <option value="price-desc">Giá giảm dần</option>
                        <option value="name-asc">Tên A-Z</option>
                        <option value="name-desc">Tên Z-A</option>
                    </select>
                </div>

                <div class="filter-group">
                    <label>Filter:</label>
                    <select class="filter-select" id="categoryFilter">
                        <option value="all" selected>All</option>
                        <option value="light-novel">Light Novel</option>
                        <option value="history">Lịch sử</option>
                        <option value="science">Khoa học</option>
                        <option value="kids">Trẻ em</option>
                    </select>
                </div>

                <div class="filter-group">
                    <input type="text" class="search-input" placeholder="Tìm kiếm sách hoặc tác giả..."/>
                    <button class="header-icon" id="searchBtn">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
            </div>
        </div>

        <!-- Welcome Section -->
        <div class="welcome-section">
            <div class="welcome-content">
                Chào mừng bạn đến với ReadTopia! Dưới đây là các sách có sẵn trong hệ thống.
            </div>
        </div>

        <!-- Main Content -->
        <main class="main-content">
            <div class="category-section">
                <h2 class="category-title">Danh sách sách</h2>
                <div id="booksGrid" class="books-grid">
                    <c:if test="${empty books}">
                        <div style="text-align: center; color: #666; font-size: 18px; padding: 30px;">
                            <p>Chưa có sách nào để hiển thị.</p>
                            <p>Vui lòng quay lại sau hoặc liên hệ quản trị viên để cập nhật sách.</p>
                        </div>
                    </c:if>

                    <c:forEach var="book" items="${books}">
                        <div class="book-card" onclick="showBookDetail(${book.id})" data-category="${book.category}">
                            <img src="${book.image}" alt="${book.title}" class="book-image" />
                            <div class="book-title">${book.title}</div>
                            <div class="book-author">Author: ${book.author}</div>
                            <div class="book-price">${book.price} đ</div>
                            <div class="book-quantity">Quantity: ${book.quantity}</div>
                            <button class="add-to-cart-btn" onclick="event.stopPropagation(); addToCart(${book.id})">
                                <i class="fas fa-shopping-cart"></i> Add to Cart
                            </button>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </main>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />

        <!-- Link to your external JS or embed your JS here -->
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

    </body>
</html>
