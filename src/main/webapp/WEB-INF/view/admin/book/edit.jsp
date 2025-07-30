<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Edit Book</h1>
        </div>

        <!-- Thông báo lỗi -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i> ${error}
            </div>
        </c:if>

        <!-- Form chỉnh sửa sách -->
        <form action="${pageContext.request.contextPath}/admin/book/edit" method="post">
            <input type="hidden" name="id" value="${book.bookID}"/>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label required">Title</label>
                    <input type="text" name="title" class="form-input" value="${book.bookTitle}" required />
                </div>

                <div class="form-group">
                    <label class="form-label required">Category</label>
                    <select name="categoryID" class="form-select" required>
                        <option value="">-- Select category --</option>
                        <c:forEach var="cat" items="${categoryList}">
                            <option value="${cat.categoryID}" <c:if test="${cat.categoryID == book.categoryID}">selected</c:if>>${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <!-- Dropdown cho tác giả -->
                <div class="form-group">
                    <label class="form-label required">Author</label>
                    <select name="author" class="form-select" required>
                        <option value="">-- Select Author --</option>
                        <c:forEach var="author" items="${authors}">
                            <option value="${author}" <c:if test="${author == book.author}">selected</c:if>>${author}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Dropdown cho người phiên dịch -->
                <div class="form-group">
                    <label class="form-label">Translator</label>
                    <select name="translator" class="form-select">
                        <option value="">-- Select Translator --</option>
                        <c:forEach var="translator" items="${translators}">
                            <option value="${translator}" <c:if test="${translator == book.translator}">selected</c:if>>${translator}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <!-- Dropdown cho nhà xuất bản -->
                <div class="form-group">
                    <label class="form-label">Publisher</label>
                    <select name="publisher" class="form-select">
                        <option value="">-- Select Publisher --</option>
                        <c:forEach var="publisher" items="${publishers}">
                            <option value="${publisher}" <c:if test="${publisher == book.publisher}">selected</c:if>>${publisher}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label required">Publication Year</label>
                    <input type="number" name="year" class="form-input" value="${book.publicationYear}" required />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">ISBN</label>
                    <input type="text" name="isbn" class="form-input" value="${book.isbn}" />
                </div>

                <div class="form-group">
                    <label class="form-label">Dimension</label>
                    <input type="text" name="dimension" class="form-input" value="${book.dimension}" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Weight (gram)</label>
                    <input type="number" name="weight" class="form-input" value="${book.weight}" />
                </div>

                <div class="form-group">
                    <label class="form-label required">Price (VND)</label>
                    <input type="number" name="price" class="form-input" value="${book.bookPrice}" required />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label required">Quantity</label>
                    <input type="number" name="quantity" class="form-input" value="${book.bookQuantity}" required />
                </div>

                <div class="form-group">
                    <label class="form-label">Hardcover</label>
                    <select name="hardcover" class="form-select">
                        <option value="1" <c:if test="${book.hardcover == 1}">selected</c:if>>Yes</option>
                        <option value="0" <c:if test="${book.hardcover == 0}">selected</c:if>>No</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Image URL</label>
                    <input type="text" name="image" class="form-input" value="${book.image}" />
            </div>

            <div class="form-group">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-textarea" rows="3">${book.bookDescription}</textarea>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-success">
                    Update Book
                </button>
                <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>
