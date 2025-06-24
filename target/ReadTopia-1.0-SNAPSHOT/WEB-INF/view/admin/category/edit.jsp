<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h2 class="page-title">Edit Category: <c:out value="${category.categoryName}" /></h2>

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

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">Update Category</button>
                <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
