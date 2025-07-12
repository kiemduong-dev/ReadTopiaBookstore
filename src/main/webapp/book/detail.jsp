<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<%
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    DecimalFormat formatter = new DecimalFormat("###,###,###", symbols);
    request.setAttribute("formatter", formatter);
%>

<div class="container py-5">
    <div class="row">
        <div class="col-md-5">
            <img src="${book.image}" alt="${book.bookTitle}" class="img-fluid rounded shadow-sm"
                 onerror="this.src='https://via.placeholder.com/300x400?text=No+Image'">
        </div>
        <div class="col-md-7">
            <h2 class="mb-3">${book.bookTitle}</h2>
            <p><strong>Author:</strong> ${book.author}</p>
            <p><strong>Translator:</strong> ${book.translator}</p>
            <p><strong>Publisher:</strong> ${book.publisher}</p>
            <p><strong>Year:</strong> ${book.publicationYear}</p>
            <p><strong>ISBN:</strong> ${book.isbn}</p>
            <p><strong>Size:</strong> ${book.dimension}</p>
            <p><strong>Weight:</strong> ${book.weight} g</p>
            <p><strong>Description:</strong><br>${book.bookDescription}</p>
            <p><strong>In Stock:</strong> ${book.bookQuantity}</p>
            <p class="text-danger fs-4 fw-bold">Price: <c:out value="${formatter.format(book.bookPrice)}"/> VND</p>

            <form action="${pageContext.request.contextPath}/cart/add" method="post" class="mt-3 d-flex">
                <input type="hidden" name="bookID" value="${book.bookID}" />
                <input type="number" name="quantity" value="1" min="1" max="${book.bookQuantity}" class="form-control w-25 me-2" />
                <button type="submit" name="action" value="addToCart" class="btn btn-success me-2">🛒 Add to Cart</button>
                <button type="submit" name="action" value="buyNow" class="btn btn-warning">⚡ Buy Now</button>
            </form>

            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-outline-secondary">⬅ Back to List</a>
            </div>
        </div>
    </div>
</div>

<%-- ✅ MODAL for Add to Cart Confirmation --%>
<c:if test="${param.added == 'true'}">
    <div class="modal fade show" id="addedModal" tabindex="-1" style="display: block; background-color: rgba(0,0,0,0.5);">
        <div class="modal-dialog">
            <div class="modal-content p-3">
                <div class="modal-header">
                    <h5 class="modal-title">🎉 Added to Cart</h5>
                </div>
                <div class="modal-body d-flex">
                    <img src="${book.image}" alt="${book.bookTitle}" class="me-3 rounded" style="width: 80px; height: 100px; object-fit: cover;"
                         onerror="this.src='https://via.placeholder.com/80x100?text=No+Image'">
                    <div>
                        <p><strong>${book.bookTitle}</strong></p>
                        <p class="text-muted">Author: ${book.author}</p>
                        <p>Price: <span class="text-danger fw-bold"><c:out value="${formatter.format(book.bookPrice)}"/> VND</span></p>
                        <p>Do you want to proceed to checkout now?</p>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}/cart/view" class="btn btn-success">🛒 Go to Cart</a>
                    <a href="${pageContext.request.contextPath}/customer/book/list" class="btn btn-secondary">No, Continue Shopping</a>
                </div>
            </div>
        </div>
    </div>
    <script>
        setTimeout(() => {
            document.getElementById("addedModal").style.display = 'none';
        }, 10000);
    </script>
</c:if>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
