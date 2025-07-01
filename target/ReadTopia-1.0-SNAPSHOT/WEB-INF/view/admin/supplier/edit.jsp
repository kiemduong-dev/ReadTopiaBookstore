<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Edit Supplier</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}" escapeXml="false" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/supplier/edit" method="post" enctype="multipart/form-data" style="max-width: 700px; margin: auto;">
            <input type="hidden" name="id" value="${supplier.supID}" />

            <div class="form-group">
                <label class="form-label">Name</label>
                <input type="text" name="name" value="${supplier.supName}" class="form-input" required />
                <c:if test="${not empty nameError}">
                    <small class="text-danger">${nameError}</small>
                </c:if>
            </div>

            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" value="${supplier.supPassword}" class="form-input" required />
                <c:if test="${not empty passwordError}">
                    <small class="text-danger">${passwordError}</small>
                </c:if>
            </div>

            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" name="email" value="${supplier.supEmail}" class="form-input" required />
                <c:if test="${not empty emailError}">
                    <small class="text-danger">${emailError}</small>
                </c:if>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" value="${supplier.supPhone}" class="form-input" required />
                    <c:if test="${not empty phoneError}">
                        <small class="text-danger">${phoneError}</small>
                    </c:if>
                </div>

                <div class="form-group">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select" required>
                        <option value="1" ${supplier.supStatus == 1 ? 'selected' : ''}>Active</option>
                        <option value="0" ${supplier.supStatus == 0 ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Address</label>
                <input type="text" name="address" value="${supplier.supAddress}" class="form-input" required />
                <c:if test="${not empty addressError}">
                    <small class="text-danger">${addressError}</small>
                </c:if>
            </div>

            <div class="form-group">
                <label class="form-label">Current Image</label><br>
                <img src="${pageContext.request.contextPath}/${supplier.supImage}" alt="Avatar" style="max-width: 150px;">
            </div>

            <div class="form-group">
                <label class="form-label">Choose New Image</label>
                <input type="file" name="imageFile" class="form-input" />
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Update Supplier
                </button>
                <a href="${pageContext.request.contextPath}/admin/supplier/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
