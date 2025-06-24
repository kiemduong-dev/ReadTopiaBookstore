<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Edit Supplier</h1>

        <form action="${pageContext.request.contextPath}/admin/supplier/edit" method="post" style="max-width: 700px; margin: auto;">
            <input type="hidden" name="id" value="${supplier.id}">

            <div class="form-group">
                <label class="form-label">Name</label>
                <input type="text" name="name" value="${supplier.supName}" class="form-input" required />
            </div>

            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" value="${supplier.password}" class="form-input" required />
            </div>

            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" name="email" value="${supplier.email}" class="form-input" required />
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" value="${supplier.phone}" class="form-input" required />
                </div>
                <div class="form-group">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select" required>
                        <option value="true" ${supplier.status ? 'selected' : ''}>Active</option>
                        <option value="false" ${!supplier.status ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Address</label>
                <input type="text" name="address" value="${supplier.address}" class="form-input" required />
            </div>

            <div class="form-group">
                <label class="form-label">Image URL</label>
                <input type="text" name="image" value="${supplier.image}" class="form-input" required />
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Update Supplier
                </button>
                <a href="${pageContext.request.contextPath}/admin/supplier/list" class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>
