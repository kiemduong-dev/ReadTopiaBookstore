<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>View Account Detail</h1>

        <form>
            <div class="form-group">
                <label>Username:</label>
                <input type="text" value="${account.username}" readonly class="form-input" />
            </div>
            <div class="form-group">
                <label>First Name:</label>
                <input type="text" value="${account.firstName}" readonly class="form-input" />
            </div>
            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" value="${account.lastName}" readonly class="form-input" />
            </div>
            <div class="form-group">
                <label>Sex:</label>
                <select class="form-select" disabled>
                    <option value="1" ${account.sex == 1 ? 'selected' : ''}>Male</option>
                    <option value="0" ${account.sex == 0 ? 'selected' : ''}>Female</option>
                </select>
            </div>
            <div class="form-group">
                <label>Role:</label>
                <select class="form-select" disabled>
                    <option value="0" ${account.role == 0 ? 'selected' : ''}>Admin</option>
                    <option value="1" ${account.role == 1 ? 'selected' : ''}>Staff</option>
                    <option value="2" ${account.role == 2 ? 'selected' : ''}>Seller Staff</option>
                    <option value="3" ${account.role == 3 ? 'selected' : ''}>Warehouse Staff</option>
                    <option value="4" ${account.role == 4 ? 'selected' : ''}>Customer</option>
                </select>
            </div>
            <div class="form-group">
                <label>Date of Birth:</label>
                <input type="date" value="${account.dob}" readonly class="form-input" />
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="email" value="${account.email}" readonly class="form-input" />
            </div>
            <div class="form-group">
                <label>Phone:</label>
                <input type="text" value="${account.phone}" readonly class="form-input" />
            </div>
            <div class="form-group">
                <label>Address:</label>
                <input type="text" value="${account.address}" readonly class="form-input" />
            </div>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Back to List</a>
            </div>
        </form>
    </div>
</div>
