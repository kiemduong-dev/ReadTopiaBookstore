<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h2 class="page-title">✏️ Edit Category: <c:out value="${category.categoryName}" /></h2>

        <!-- Hiển thị lỗi nếu có -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/category/edit" style="max-width: 700px; margin: 0 auto;">
            <input type="hidden" name="categoryID" value="${category.categoryID}" />

            <div class="form-group">
                <label class="form-label">Category Name:</label>
                <input type="text" name="categoryName" value="${category.categoryName}" required class="form-input" />
            </div>

            <div class="form-group">
                <label class="form-label">Category Description:</label>
                <textarea name="description" rows="4" required class="form-input">${category.categoryDescription}</textarea>
            </div>

            <div class="form-group">
                <label class="form-label">Parent Category:</label>
                <select name="parentID" class="form-select">
                    <option value="">-- None (Top-level) --</option>
                    <c:forEach var="cat" items="${parentList}">
                        <option value="${cat.categoryID}" <c:if test="${cat.categoryID == category.parentID}">selected</c:if>>
                            ${cat.categoryName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Update Category
                </button>
                <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Cancel
                </a>
            </div>
        </form>
    </div>
</div>
