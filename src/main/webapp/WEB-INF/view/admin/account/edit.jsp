<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Edit Account</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger fw-bold text-center">${error}</div>
        </c:if>

        <div class="card">
            <form method="post" action="${pageContext.request.contextPath}/admin/account/edit" class="form-container">

                <!-- Username -->
                <div class="form-group">
                    <label>Username:</label>
                    <input type="text" name="username" value="${account.username}" readonly class="form-input" />
                </div>

                <!-- First Name -->
                <div class="form-group">
                    <label>First Name:</label>
                    <input type="text" name="firstName" value="${account.firstName}" required class="form-input" />
                </div>

                <!-- Last Name -->
                <div class="form-group">
                    <label>Last Name:</label>
                    <input type="text" name="lastName" value="${account.lastName}" required class="form-input" />
                </div>

                <!-- Sex -->
                <div class="form-group">
                    <label>Sex:</label>
                    <select name="sex" class="form-select" required>
                        <option value="1" <c:if test="${account.sex == 1}">selected</c:if>>Male</option>
                        <option value="0" <c:if test="${account.sex == 0}">selected</c:if>>Female</option>
                    </select>
                </div>

                <!-- Role -->
                <div class="form-group">
                    <label>Role:</label>
                    <select name="role" class="form-select" required>
                        <option value="1" <c:if test="${account.role == 1}">selected</c:if>>Staff</option>
                        <option value="4" <c:if test="${account.role == 4}">selected</c:if>>Customer</option>
                    </select>
                </div>

                <!-- Date of Birth -->
                <div class="form-group">
                    <label>Date of Birth:</label>
                    <input type="text" name="dob" value="${dobRaw}" placeholder="dd/MM/yyyy" class="form-input" required />
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value="${account.email}" required class="form-input" />
                </div>

                <!-- Phone -->
                <div class="form-group">
                    <label>Phone:</label>
                    <input type="text" name="phone" value="${account.phone}" required class="form-input" />
                </div>

                <!-- Address -->
                <div class="form-group">
                    <label>Address:</label>
                    <input type="text" name="address" value="${account.address}" required class="form-input" />
                </div>

                <!-- Button Group -->
                <div class="btn-group mt-4">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>