<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Chi tiết sách - ReadTopia</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">📖 Chi tiết sách</h2>

    <div class="row">
        <div class="col-md-4 text-center">
            <img src="${book.image}" class="img-fluid rounded shadow-sm mb-3" alt="${book.bookTitle}">
            
            <!-- Add book to cart section -->
            <div class="border rounded p-3">
                <h5>Add book to cart</h5>
                <form action="${pageContext.request.contextPath}/cart/add" method="post" class="d-flex align-items-center gap-2">
                    <input type="hidden" name="bookID" value="${book.bookID}" />
                    <label for="quantity" class="mb-0">Quantity:</label>
                    <input type="number" id="quantity" name="quantity" value="1" min="1" max="${book.bookQuantity}" class="form-control w-25" />
                    <button type="submit" name="action" value="buyNow" class="btn btn-danger">Buy now</button>
                    <button type="submit" name="action" value="addToCart" class="btn btn-outline-danger">Add to cart</button>
                </form>
            </div>
        </div>
        
        <div class="col-md-8">
            <h3>${book.bookTitle}</h3>
            <p><strong>Tác giả:</strong> ${book.author}</p>
            <p><strong>Người dịch:</strong> ${book.translator}</p>
            <p><strong>Nhà xuất bản:</strong> ${book.publisher}</p>
            <p><strong>Năm xuất bản:</strong> ${book.publicationYear}</p>
            <p><strong>ISBN:</strong> ${book.isbn}</p>
            <p><strong>Bìa cứng:</strong> 
                <c:choose>
                    <c:when test="${book.hardcover == 1}">Có</c:when>
                    <c:otherwise>Không</c:otherwise>
                </c:choose>
            </p>
            <p><strong>Kích thước:</strong> ${book.dimension}</p>
            <p><strong>Trọng lượng:</strong> ${book.weight} gram</p>
            <p><strong>Giá:</strong> <span class="text-danger fw-bold">${book.bookPrice} VNĐ</span></p>
            <p><strong>Số lượng còn:</strong> ${book.bookQuantity}</p>
            <p><strong>Mô tả:</strong> ${book.bookDescription}</p>

            <a href="${pageContext.request.contextPath}/book/home" class="btn btn-secondary mt-3">⬅ Quay lại</a>
        </div>
    </div>
</div>
</body>
</html>
