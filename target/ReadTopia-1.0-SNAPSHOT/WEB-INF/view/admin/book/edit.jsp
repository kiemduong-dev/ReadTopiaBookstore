<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Book - Admin Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">✏️ Edit Book</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/book/edit">
        <input type="hidden" name="id" value="${book.bookID}">

        <div class="row">
            <div class="col-md-6">
                <label>Title:</label>
                <input type="text" name="title" class="form-control" value="${book.bookTitle}" required>

                <label class="mt-2">Author:</label>
                <input type="text" name="author" class="form-control" value="${book.author}">

                <label class="mt-2">Translator:</label>
                <input type="text" name="translator" class="form-control" value="${book.translator}">

                <label class="mt-2">Publisher:</label>
                <input type="text" name="publisher" class="form-control" value="${book.publisher}">

                <label class="mt-2">Publication Year:</label>
                <input type="number" name="year" class="form-control" value="${book.publicationYear}">

                <label class="mt-2">ISBN:</label>
                <input type="text" name="isbn" class="form-control" value="${book.isbn}">

                <label class="mt-2">Image URL:</label>
                <input type="text" name="image" class="form-control" value="${book.image}">
            </div>

            <div class="col-md-6">
                <label>Description:</label>
                <textarea name="description" class="form-control" rows="5">${book.bookDescription}</textarea>

                <label class="mt-2">Hardcover (1 = Yes, 0 = No):</label>
                <input type="number" name="hardcover" class="form-control" value="${book.hardcover}">

                <label class="mt-2">Dimension:</label>
                <input type="text" name="dimension" class="form-control" value="${book.dimension}">

                <label class="mt-2">Weight (grams):</label>
                <input type="number" step="0.01" name="weight" class="form-control" value="${book.weight}">

                <label class="mt-2">Price (VND):</label>
                <input type="number" step="0.01" name="price" class="form-control" value="${book.bookPrice}">

                <label class="mt-2">Quantity:</label>
                <input type="number" name="quantity" class="form-control" value="${book.bookQuantity}">

                <label class="mt-2">Status (1 = Active, 0 = Inactive):</label>
                <input type="number" name="status" class="form-control" value="${book.bookStatus}">
            </div>
        </div>

        <div class="mt-4">
            <button class="btn btn-success" type="submit">💾 Update Book</button>
            <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-secondary">⬅ Back to List</a>
        </div>
    </form>
</div>
</body>
</html>
