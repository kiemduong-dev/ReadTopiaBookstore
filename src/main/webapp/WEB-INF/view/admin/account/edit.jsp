<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Edit Account</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="card">
            <form method="post" action="${pageContext.request.contextPath}/admin/account/edit">
                <div class="form-group">
                    <label>Username:</label>
                    <input type="text" name="username" value="${account.username}" readonly class="form-input" />
                </div>
                <div class="form-group">
                    <label>First Name:</label>
                    <input type="text" name="firstName" value="${account.firstName}" required class="form-input" />
                </div>
                <div class="form-group">
                    <label>Last Name:</label>
                    <input type="text" name="lastName" value="${account.lastName}" required class="form-input" />
                </div>
                <div class="form-group">
                    <label>Sex:</label>
                    <select name="sex" class="form-select" required>
                        <option value="1" ${account.sex == 1 ? 'selected' : ''}>Male</option>
                        <option value="0" ${account.sex == 0 ? 'selected' : ''}>Female</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Role:</label>
                    <select name="role" class="form-select" required>
                        <option value="0" ${account.role == 0 ? 'selected' : ''}>Admin</option>
                        <option value="1" ${account.role == 1 ? 'selected' : ''}>Staff</option>
                        <option value="2" ${account.role == 2 ? 'selected' : ''}>Seller Staff</option>
                        <option value="3" ${account.role == 3 ? 'selected' : ''}>Warehouse Staff</option>
                        <option value="4" ${account.role == 4 ? 'selected' : ''}>Customer</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Date of Birth:</label>
                    <input type="date" name="dob" value="${account.dob}" class="form-input" />
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value="${account.email}" required class="form-input" />
                </div>
                <div class="form-group">
                    <label>Phone:</label>
                    <input type="text" name="phone" value="${account.phone}" required class="form-input" />
                </div>
                <div class="form-group">
                    <label>Address:</label>
                    <input type="text" name="address" value="${account.address}" required class="form-input" />
                </div>
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>
