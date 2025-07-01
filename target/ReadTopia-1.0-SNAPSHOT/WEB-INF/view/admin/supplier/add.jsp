<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Add New Supplier</h1>

        <c:if test="${not empty error}">
            <div class="text-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/supplier/add" method="post"
              enctype="multipart/form-data" style="max-width: 700px; margin: auto;">
              
            <div class="form-group">
                <label class="form-label">Name</label>
                <input type="text" name="name" class="form-input" value="${name}" required />
                <c:if test="${not empty nameError}">
                    <div class="text-danger">${nameError}</div>
                </c:if>
            </div>

            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-input" value="${password}" required />
                <c:if test="${not empty passwordError}">
                    <div class="text-danger">${passwordError}</div>
                </c:if>
            </div>

            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-input" value="${email}" required />
                <c:if test="${not empty emailError}">
                    <div class="text-danger">${emailError}</div>
                </c:if>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" class="form-input" value="${phone}" required />
                    <c:if test="${not empty phoneError}">
                        <div class="text-danger">${phoneError}</div>
                    </c:if>
                </div>

                <div class="form-group">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select">
                        <option value="true" <c:if test="${status == 'true'}">selected</c:if>>Active</option>
                        <option value="false" <c:if test="${status == 'false'}">selected</c:if>>Inactive</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Address</label>
                <input type="text" name="address" class="form-input" value="${address}" required />
                <c:if test="${not empty addressError}">
                    <div class="text-danger">${addressError}</div>
                </c:if>
            </div>

            <div class="form-group">
                <label class="form-label">Avatar Image</label>
                <input type="file" name="imageFile" class="form-input" accept="image/*" required />
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary"><i class="fas fa-plus"></i> Add Supplier</button>
                <a href="${pageContext.request.contextPath}/admin/supplier/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
