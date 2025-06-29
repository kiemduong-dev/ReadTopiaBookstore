<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />

<jsp:include page="/WEB-INF/includes/header.jsp" />
<div class="container py-4">
    <div class="container py-4">
        <!-- Banner Carousel -->
        <div id="promoCarousel" class="carousel slide mb-4" data-bs-ride="carousel">
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <a href="${pageContext.request.contextPath}/customer/promotion/list">
                        <img src="${pageContext.request.contextPath}/images/slide1.jpg" class="d-block w-100" alt="Khuyến mãi hè vui vẻ">
                    </a>
                </div>
                <div class="carousel-item">
                    <a href="${pageContext.request.contextPath}/customer/promotion/list">
                        <img src="${pageContext.request.contextPath}/images/slide2.jpg" class="d-block w-100" alt="Ưu đãi đặc biệt">
                    </a>
                </div>

            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#promoCarousel" data-bs-slide="prev">
                <span class="carousel-control-prev-icon"></span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#promoCarousel" data-bs-slide="next">
                <span class="carousel-control-next-icon"></span>
            </button>
        </div>


        <h2 class="mb-4">📚 Tất cả sách</h2>

        <!-- Toolbar -->
        <form class="row g-3 mb-4" method="get" action="${pageContext.request.contextPath}/customer/book/list">
            <div class="col-md-3">
                <input type="text" class="form-control" name="keyword" placeholder="Tìm theo tên hoặc tác giả" value="${param.keyword}"/>
            </div>
            <div class="col-md-3">
                <select name="categoryID" class="form-select">
                    <option value="">-- Danh mục --</option>
                    <c:forEach var="cat" items="${categoryList}">
                        <option value="${cat.categoryID}" ${param.categoryID == cat.categoryID ? 'selected' : ''}>
                            ${cat.categoryName}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <select name="sort" class="form-select">
                    <option value="">-- Sắp xếp --</option>
                    <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                    <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                    <option value="title_asc" ${param.sort == 'title_asc' ? 'selected' : ''}>Tên A - Z</option>
                    <option value="title_desc" ${param.sort == 'title_desc' ? 'selected' : ''}>Tên Z - A</option>
                </select>
            </div>
            <div class="col-md-3">
                <button class="btn btn-primary w-100" type="submit">🔍 Tìm kiếm</button>
            </div>
        </form>

        <!-- Danh sách sách -->
        <div class="row row-cols-1 row-cols-md-4 g-4">
            <c:forEach var="book" items="${bookList}">
                <div class="col">
                    <div class="card h-100 shadow-sm">
                        <img src="${book.image}" class="card-img-top" alt="${book.bookTitle}"
                             onerror="this.src='https://via.placeholder.com/200x300?text=No+Image'">
                        <div class="card-body">
                            <h5 class="card-title">${book.bookTitle}</h5>
                            <p class="card-text text-muted">Tác giả: ${book.author}</p>
                            <p class="card-text text-danger fw-bold">${book.bookPrice} VNĐ</p>
                            <a href="${pageContext.request.contextPath}/customer/book/detail?id=${book.bookID}" class="btn btn-primary btn-sm">Chi tiết</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
