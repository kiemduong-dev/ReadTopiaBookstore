<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <title>ReadTopia - Trang chủ</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
    </head>
    <body>
        <div class="container mt-4">

            <!-- Row chứa Sort, Filter, Search -->
            <div class="row align-items-center mb-4">
                <!-- Sort By -->
                <div class="col-md-3 col-12 mb-2 mb-md-0">
                    <form action="${pageContext.request.contextPath}/book/home" method="get" id="formSort">
                        <input type="hidden" name="keyword" value="${keyword != null ? keyword : ''}" />
                        <input type="hidden" name="catID" value="${selectedCatID != null ? selectedCatID : 0}" />
                        <label for="sortBy" class="form-label fw-bold">Sort by:</label>
                        <select name="sortBy" id="sortBy" class="form-select" onchange="document.getElementById('formSort').submit()">
                            <option value="bookID" <c:if test="${sortBy == 'bookID'}">selected</c:if>>Default</option>
                            <option value="price_asc" <c:if test="${sortBy == 'price_asc'}">selected</c:if>>Price: Low to High</option>
                            <option value="price_desc" <c:if test="${sortBy == 'price_desc'}">selected</c:if>>Price: High to Low</option>
                            <option value="title_asc" <c:if test="${sortBy == 'title_asc'}">selected</c:if>>Title: A-Z</option>
                            <option value="title_desc" <c:if test="${sortBy == 'title_desc'}">selected</c:if>>Title: Z-A</option>
                            </select>
                        </form>
                    </div>

                    <!-- Filter (Category) -->
                    <div class="col-md-3 col-12 mb-2 mb-md-0">
                        <form action="${pageContext.request.contextPath}/book/home" method="get" id="formFilter">
                        <input type="hidden" name="keyword" value="${keyword != null ? keyword : ''}" />
                        <input type="hidden" name="sortBy" value="${sortBy != null ? sortBy : 'bookID'}" />
                        <label for="catID" class="form-label fw-bold">Filter:</label>
                        <select name="catID" id="catID" class="form-select" onchange="document.getElementById('formFilter').submit()">
                            <option value="0" <c:if test="${selectedCatID == 0}">selected</c:if>>All</option>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.categoryID}" <c:if test="${selectedCatID == cat.categoryID}">selected</c:if>>
                                    ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </div>

                <!-- Search -->
                <div class="col-md-6 col-12">
                    <form action="${pageContext.request.contextPath}/book/home" method="get" id="formSearch" class="d-flex">
                        <input type="hidden" name="catID" value="${selectedCatID != null ? selectedCatID : 0}" />
                        <input type="hidden" name="sortBy" value="${sortBy != null ? sortBy : 'bookID'}" />
                        <input type="text" name="keyword" class="form-control me-2" placeholder="Search books with title or author..." value="${keyword != null ? keyword : ''}" />
                        <button class="btn btn-primary" type="submit">🔍 Search</button>
                    </form>
                </div>
            </div>

            <!-- Danh sách sách -->
            <div class="row row-cols-1 row-cols-md-4 g-4">
                <c:forEach var="book" items="${bookList}">
                    <div class="col">
                        <div class="card h-100 shadow" style="cursor:pointer;" onclick="location.href = '${pageContext.request.contextPath}/book/detail?id=${book.bookID}'">
                            <img src="${book.image}" class="card-img-top" alt="${book.bookTitle}" style="height: 250px; object-fit: cover;">
                            <div class="card-body">
                                <h5 class="card-title">${book.bookTitle}</h5>
                                <p class="card-text">👤 ${book.author}</p>
                                <p class="card-text text-danger fw-bold">💰 ${book.bookPrice} VNĐ</p>
                                <!-- nút xem đã bị bỏ -->
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty bookList}">
                    <div class="col-12">
                        <div class="alert alert-warning">❌ No books available.</div>
                    </div>
                </c:if>
            </div>


        </div>
    </body>
</html>
