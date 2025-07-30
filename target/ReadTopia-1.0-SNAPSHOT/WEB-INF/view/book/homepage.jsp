<%@page import="java.text.DecimalFormatSymbols"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.text.DecimalFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<%
    // Setup the number format for displaying prices
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    DecimalFormat formatter = new DecimalFormat("###,###,###", symbols);
    request.setAttribute("formatter", formatter);
%>

<div class="container py-4">
    <h2 class="mb-4">All Books</h2>

    <!-- Toolbar -->
    <form class="row g-3 mb-4" method="get" action="${pageContext.request.contextPath}/customer/book/list">
        <div class="col-md-3">
            <input type="text" class="form-control" name="keyword" placeholder="Search by title or author" value="${param.keyword}"/>
        </div>
        <div class="col-md-3">
            <select name="categoryID" class="form-select">
                <option value="">-- Category --</option>
                <c:forEach var="cat" items="${categoryList}">
                    <option value="${cat.categoryID}" ${param.categoryID == cat.categoryID ? 'selected' : ''}>
                        ${cat.categoryName}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-3">
            <select name="sort" class="form-select">
                <option value="">-- Sort --</option>
                <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                <option value="title_asc" ${param.sort == 'title_asc' ? 'selected' : ''}>Title A - Z</option>
                <option value="title_desc" ${param.sort == 'title_desc' ? 'selected' : ''}>Title Z - A</option>
            </select>
        </div>
        <div class="col-md-3">
            <button class="btn btn-primary w-100" type="submit">Search</button>
        </div>
    </form>

    <!-- Book List -->
    <div class="row row-cols-1 row-cols-md-4 g-4">
        <c:forEach var="book" items="${bookList}">
            <div class="col">
                <a href="${pageContext.request.contextPath}/customer/book/detail?id=${book.bookID}" class="text-decoration-none text-dark">
                    <div class="card h-100 shadow-sm book-card" style="transition: all 0.3s;">
                        <div class="position-relative">
                            <img src="${book.image}" class="card-img-top" alt="${book.bookTitle}"
                                 onerror="this.src='https://via.placeholder.com/200x300?text=No+Image'">
                            <div class="hover-overlay position-absolute top-0 start-0 w-100 h-100" style="background: rgba(0, 0, 0, 0.15); opacity: 0; transition: opacity 0.3s;"></div>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title">${book.bookTitle}</h5>
                            <p class="card-text text-muted">Author: ${book.author}</p>
                            <p class="card-text text-danger fw-bold">
                                <c:out value="${formatter.format(book.bookPrice)}"/> VND
                            </p>
                        </div>
                    </div>
                </a>
            </div>
        </c:forEach>

        <!-- If no books are found -->
        <c:if test="${empty bookList}">
            <div class="col-12">
                <div class="alert alert-warning text-center">
                    No books found based on your search criteria.
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />

<style>
    .book-card:hover {
        transform: scale(1.03);
    }
    .book-card:hover .hover-overlay {
        opacity: 1;
    }
</style>
