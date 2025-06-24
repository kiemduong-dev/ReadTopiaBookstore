<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">📚 Add New Book</h1>
        </div>

        <!-- Thông báo lỗi -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle"></i> ${error}
            </div>
        </c:if>

        <!-- Form thêm sách -->
        <form action="${pageContext.request.contextPath}/admin/book/add" method="post">
            <div class="form-row">
                <div class="form-group">
                    <label class="form-label required">Title</label>
                    <input type="text" name="title" class="form-input" required />
                </div>

                <div class="form-group">
                    <label class="form-label required">Category</label>
                    <select name="categoryID" class="form-select" required>
                        <option value="">-- Select category --</option>
                        <c:forEach var="cat" items="${categoryList}">
                            <option value="${cat.categoryID}">${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label required">Author</label>
                    <input type="text" name="author" class="form-input" required />
                </div>

                <div class="form-group">
                    <label class="form-label">Translator</label>
                    <input type="text" name="translator" class="form-input" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Publisher</label>
                    <input type="text" name="publisher" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label required">Publication Year</label>
                    <input type="number" name="publicationYear" class="form-input" required />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">ISBN</label>
                    <input type="text" name="isbn" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Dimension</label>
                    <input type="text" name="dimension" class="form-input" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Weight (gram)</label>
                    <input type="number" name="weight" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label required">Price (VND)</label>
                    <input type="number" name="price" class="form-input" required />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label required">Quantity</label>
                    <input type="number" name="quantity" class="form-input" required />
                </div>

                <div class="form-group">
                    <label class="form-label">Hardcover</label>
                    <select name="hardcover" class="form-select">
                        <option value="1">Yes</option>
                        <option value="0" selected>No</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Image URL</label>
                <input type="text" name="image" class="form-input" />
            </div>

            <div class="form-group">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-textarea" rows="3"></textarea>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">
                    ✅ Add Book
                </button>
                <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-secondary">
                    ⬅ Cancel
                </a>
            </div>
        </form>
    </div>
</div>
