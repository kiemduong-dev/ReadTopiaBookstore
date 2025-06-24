<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h2 class="page-title">➕ Add Category</h2>

        <form action="${pageContext.request.contextPath}/admin/category/add" method="post" style="max-width: 700px; margin: 0 auto;">
            <div class="form-group">
                <label class="form-label">Category Name:</label>
                <input type="text" name="categoryName" class="form-input" placeholder="Enter category name" required />
            </div>

            <div class="form-group">
                <label class="form-label">Category Description:</label>
                <textarea name="categoryDescription" class="form-input" rows="4" placeholder="Enter category description" required></textarea>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Save
                </button>
                <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Cancel
                </a>
            </div>
        </form>
    </div>
</div>
