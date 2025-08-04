<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/includes/head.jsp" />

<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="main-content">
    <div class="form-container" style="max-width: 600px; margin: 50px auto; background: #fff; border-radius: 15px; padding: 30px; box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

        <!-- Logo -->
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="ReadTopia Logo" style="height: 50px;" />
        </div>

        <!-- Title -->
        <h2 class="text-center mb-4">Edit Profile</h2>

        <!-- Alerts -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success text-center">
                <i class="fas fa-check-circle"></i> ${success}
            </div>
        </c:if>

        <!-- Format Date -->
        <fmt:formatDate value="${user.dob}" pattern="dd/MM/yyyy" var="dobFormatted" />

        <!-- Role Mapping -->
        <c:choose>
            <c:when test="${user.role == 0}">
                <c:set var="roleLabel" value="Admin" />
            </c:when>
            <c:when test="${user.role == 1}">
                <c:set var="roleLabel" value="Staff" />
            </c:when>
            <c:when test="${user.role == 2}">
                <c:set var="roleLabel" value="Seller Staff" />
            </c:when>
            <c:when test="${user.role == 3}">
                <c:set var="roleLabel" value="Warehouse Staff" />
            </c:when>
            <c:otherwise>
                <c:set var="roleLabel" value="Customer" />
            </c:otherwise>
        </c:choose>

        <!-- Form -->
        <form action="${pageContext.request.contextPath}/edit-profile" method="post">
            <!-- Username (readonly) -->
            <div class="form-group mb-3">
                <label class="form-label">Username</label>
                <input type="text" class="form-control" value="${user.username}" readonly />
            </div>

            <!-- Date of Birth -->
            <div class="form-group mb-3">
                <label class="form-label">Date of Birth</label>
                <input type="text" name="dob" class="form-control" value="${dobFormatted}" placeholder="dd/MM/yyyy" required />
            </div>

            <!-- First + Last Name -->
            <div class="form-group mb-3">
                <label class="form-label">First Name</label>
                <input type="text" name="firstName" class="form-control" value="${user.firstName}" required />
            </div>
            <div class="form-group mb-3">
                <label class="form-label">Last Name</label>
                <input type="text" name="lastName" class="form-control" value="${user.lastName}" required />
            </div>

            <!-- Email + Phone (readonly but hidden inputs to pass data) -->
            <div class="form-group mb-3">
                <label class="form-label">Email</label>
                <input type="text" class="form-control" value="${user.email}" readonly />
                <input type="hidden" name="email" value="${user.email}" />
            </div>
            <div class="form-group mb-3">
                <label class="form-label">Phone</label>
                <input type="text" class="form-control" value="${user.phone}" readonly />
                <input type="hidden" name="phone" value="${user.phone}" />
            </div>

            <!-- Role -->
            <div class="form-group mb-3">
                <label class="form-label">Role</label>
                <input type="text" class="form-control" value="${roleLabel}" readonly />
            </div>

            <!-- Gender -->
            <div class="form-group mb-3">
                <label class="form-label">Gender</label>
                <select name="sex" class="form-control" required>
                    <option value="1" <c:if test="${user.sex == 1}">selected</c:if>>Male</option>
                    <option value="0" <c:if test="${user.sex == 0}">selected</c:if>>Female</option>
                </select>
            </div>

            <!-- Address -->
            <div class="form-group mb-4">
                <label class="form-label">Address</label>
                <textarea name="address" class="form-control" rows="3" required>${user.address}</textarea>
            </div>

            <!-- Actions -->
            <div class="d-flex justify-content-between">
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">↩ Cancel</a>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
