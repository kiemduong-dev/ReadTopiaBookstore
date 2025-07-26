<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Add New Staff</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center fw-bold">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/staff/add" class="form-container">

            <div class="form-group">
                <label>Username:</label>
                <input type="text" name="username" required class="form-input"
                       value="${staff.username != null ? staff.username : ''}" />
            </div>

            <div class="form-group">
                <label>First Name:</label>
                <input type="text" name="firstName" required class="form-input"
                       value="${staff.firstName != null ? staff.firstName : ''}" />
            </div>

            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" name="lastName" required class="form-input"
                       value="${staff.lastName != null ? staff.lastName : ''}" />
            </div>

            <div class="form-group">
                <label>Sex:</label>
                <select name="sex" class="form-select" required>
                    <option value="1" <c:if test="${staff.sex == 1}">selected</c:if>>Male</option>
                    <option value="0" <c:if test="${staff.sex == 0}">selected</c:if>>Female</option>
                </select>
            </div>

            <div class="form-group">
                <label>Role:</label>
                <select name="role" class="form-select" required>
                    <option value="2" <c:if test="${staff.role == 2}">selected</c:if>>Seller Staff</option>
                    <option value="3" <c:if test="${staff.role == 3}">selected</c:if>>Warehouse Staff</option>
                </select>
            </div>

            <div class="form-group">
                <label>Date of Birth (dd/MM/yyyy):</label>
                <input type="text" name="dob" class="form-input"
                       value="${dobRaw != null ? dobRaw : ''}" placeholder="dd/MM/yyyy" />
            </div>

            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" required class="form-input"
                       value="${staff.email != null ? staff.email : ''}" />
            </div>

            <div class="form-group">
                <label>Phone:</label>
                <input type="text" name="phone" required class="form-input"
                       value="${staff.phone != null ? staff.phone : ''}" />
            </div>

            <div class="form-group">
                <label>Address:</label>
                <input type="text" name="address" required class="form-input"
                       value="${staff.address != null ? staff.address : ''}" />
            </div>

            <div class="btn-group mt-4">
                <button type="submit" class="btn btn-primary">Add Staff</button>
                <a href="${pageContext.request.contextPath}/admin/staff/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
