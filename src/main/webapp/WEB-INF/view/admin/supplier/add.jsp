<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Add New Supplier</h1>

        <form action="${pageContext.request.contextPath}/admin/supplier/add" method="post" style="max-width: 700px; margin: auto;">
            <div class="form-group">
                <label class="form-label">Name</label>
                <input type="text" name="name" class="form-input" required />
            </div>

            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-input" required />
            </div>

            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-input" required />
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" class="form-input" required />
                </div>

                <div class="form-group">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select">
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Address</label>
                <input type="text" name="address" class="form-input" required />
            </div>

            <div class="form-group">
                <label class="form-label">Image URL</label>
                <input type="text" name="image" class="form-input" required />
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary"><i class="fas fa-plus"></i> Add Supplier</button>
                <a href="${pageContext.request.contextPath}/admin/supplier/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
