<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Add New Account</h1>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center fw-bold">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/account/add" class="form-container">

            <!-- Username -->
            <div class="form-group">
                <label>Username:</label>
                <input type="text" name="username" required class="form-input" value="" />
            </div>

            <!-- First Name -->
            <div class="form-group">
                <label>First Name:</label>
                <input type="text" name="firstName" required class="form-input" value="" />
            </div>

            <!-- Last Name -->
            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" name="lastName" required class="form-input" value="" />
            </div>

            <!-- Sex -->
            <div class="form-group">
                <label>Sex:</label>
                <select name="sex" class="form-select" required>
                    <option disabled selected value="">-- Select Gender --</option>
                    <option value="1">Male</option>
                    <option value="0">Female</option>
                </select>
            </div>

            <!-- Role -->
            <div class="form-group">
                <label>Role:</label>
                <select name="role" class="form-select" required>
                    <option disabled selected value="">-- Select Role --</option>
                    <option value="0">Admin</option>
                    <option value="1">Staff Manager</option>
                </select>
            </div>

            <!-- DOB -->
            <div class="form-group">
                <label>Date of Birth (dd/MM/yyyy):</label>
                <input type="text" name="dob" class="form-input" placeholder="dd/MM/yyyy" value="" />
            </div>

            <!-- Email -->
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" required class="form-input" value="" />
            </div>

            <!-- Phone -->
            <div class="form-group">
                <label>Phone:</label>
                <input type="text" name="phone" required class="form-input" value="" />
            </div>

            <!-- Address -->
            <div class="form-group">
                <label>Address:</label>
                <input type="text" name="address" required class="form-input" value="" />
            </div>

            <!-- Buttons -->
            <div class="btn-group mt-4">
                <button type="submit" class="btn btn-primary">Add Account</button>
                <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>