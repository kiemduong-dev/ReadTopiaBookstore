<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="form-container bg-white shadow p-5 rounded mx-auto" style="max-width: 800px; margin-top: 50px;">
            <h2 class="fw-bold text-center mb-4"> Edit Book</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-danger"><c:out value="${error}" /></div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/admin/book/edit">
                <input type="hidden" name="id" value="${book.bookID}"/>

                <div class="form-group">
                    <label class="form-label">Title</label>
                    <input type="text" name="title" value="${book.bookTitle}" class="form-input" required />
                </div>

                <div class="form-group">
                    <label class="form-label">Author</label>
                    <input type="text" name="author" value="${book.author}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Translator</label>
                    <input type="text" name="translator" value="${book.translator}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Publisher</label>
                    <input type="text" name="publisher" value="${book.publisher}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Publication Year</label>
                    <input type="number" name="year" value="${book.publicationYear}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">ISBN</label>
                    <input type="text" name="isbn" value="${book.isbn}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Image URL</label>
                    <input type="text" name="image" value="${book.image}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Description</label>
                    <textarea name="description" class="form-textarea" rows="4">${book.bookDescription}</textarea>
                </div>

                <div class="form-group">
                    <label class="form-label">Hardcover</label>
                    <select name="hardcover" class="form-select">
                        <option value="1" <c:if test="${book.hardcover == 1}">selected</c:if>>Yes</option>
                        <option value="0" <c:if test="${book.hardcover == 0}">selected</c:if>>No</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Dimension</label>
                        <input type="text" name="dimension" value="${book.dimension}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Weight (grams)</label>
                    <input type="number" step="0.01" name="weight" value="${book.weight}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Price (VND)</label>
                    <input type="number" step="0.01" name="price" value="${book.bookPrice}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Quantity</label>
                    <input type="number" name="quantity" value="${book.bookQuantity}" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select">
                        <option value="1" <c:if test="${book.bookStatus == 1}">selected</c:if>>Active</option>
                        <option value="0" <c:if test="${book.bookStatus == 0}">selected</c:if>>Inactive</option>
                        </select>
                    </div>

                    <div class="btn-group mt-4">
                        <button type="submit" class="btn btn-success"> Update Book</button>
                        <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-secondary"> Back to List</a>
                </div>
            </form>
        </div>
    </div>
</div>
