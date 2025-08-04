<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Add New Book</h1>
        </div>

        <!-- Thông báo thành công -->
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session" />
        </c:if>

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
                    <input type="text" name="title" class="form-input" required value="${param.title}" />
                </div>

                <div class="form-group">
                    <label class="form-label required">Category</label>
                    <select name="categoryID" class="form-select" required>
                        <option value="">-- Select category --</option>
                        <c:forEach var="cat" items="${categoryList}">
                            <option value="${cat.categoryID}" <c:if test="${param.categoryID == cat.categoryID}">selected</c:if>>${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label required">Author</label>
                    <select name="author" class="form-select" required>
                        <option value="">-- Select Author --</option>
                        <c:forEach var="author" items="${authors}">
                            <option value="${author}" <c:if test="${param.author == author}">selected</c:if>>${author}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Translator</label>
                    <select name="translator" class="form-select">
                        <option value="">-- Select Translator --</option>
                        <c:forEach var="translator" items="${translators}">
                            <option value="${translator}" <c:if test="${param.translator == translator}">selected</c:if>>${translator}</option>
                        </c:forEach>
</select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Publisher</label>
                    <select name="publisher" class="form-select">
                        <option value="">-- Select Publisher --</option>
                        <c:forEach var="publisher" items="${publishers}">
                            <option value="${publisher}" <c:if test="${param.publisher == publisher}">selected</c:if>>${publisher}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label required">Publication Year</label>
                    <input type="number" name="publicationYear" class="form-input" required value="${param.publicationYear}" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">ISBN</label>
                    <input type="text" name="isbn" class="form-input" value="${param.isbn}" />
                </div>

                <div class="form-group">
                    <label class="form-label">Dimension</label>
                    <input type="text" name="dimension" class="form-input" value="${param.dimension}" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Weight (gram)</label>
                    <input type="number" name="weight" class="form-input" value="${param.weight}" />
                </div>

                <div class="form-group">
                    <label class="form-label required">Price (VND)</label>
                    <input type="number" name="price" class="form-input" required value="${param.price}" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label required">Quantity</label>
                    <input type="number" name="quantity" class="form-input" required value="${param.quantity}" />
                </div>

                <div class="form-group">
                    <label class="form-label">Hardcover</label>
                    <select name="hardcover" class="form-select">
                        <option value="1" ${param.hardcover == '1' ? 'selected' : ''}>Yes</option>
                        <option value="0" ${param.hardcover != '1' ? 'selected' : ''}>No</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Image URL</label>
                <input type="text" name="image" class="form-input" value="${param.image}" />
            </div>

            <div class="form-group">
<label class="form-label">Description</label>
                <textarea name="description" class="form-textarea" rows="3">${param.description}</textarea>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">Add Book</button>
                <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-secondary">⬅ Cancel</a>
            </div>
        </form>
    </div>
</div>