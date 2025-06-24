<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h2 class="page-title">Category Details</h2>

        <form style="max-width: 700px; margin: 0 auto;">
            <div class="form-group">
                <label class="form-label">Category Name:</label>
                <input type="text" class="form-input" value="${category.categoryName}" readonly />
            </div>

            <div class="form-group">
                <label class="form-label">Category Description:</label>
                <textarea class="form-input" rows="4" readonly>${category.categoryDescription}</textarea>
            </div>

            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Back
                </a>
            </div>
        </form>
    </div>
</div>
