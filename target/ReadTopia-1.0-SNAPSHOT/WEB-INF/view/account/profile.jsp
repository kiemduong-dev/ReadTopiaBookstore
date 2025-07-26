<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/includes/head.jsp" />

<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="main-content">
    <div class="form-container"
         style="max-width: 600px; margin: 50px auto; background: #fff;
         border-radius: 15px; padding: 30px;
         box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

        <!-- Brand Logo -->
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 alt="ReadTopia Logo"
                 style="height: 50px;" />
        </div>

        <!-- Title -->
        <h2 class="text-center mb-4">Your Profile</h2>

        <!-- Toast -->
        <jsp:include page="/WEB-INF/includes/toast.jsp" />

        <!-- Format Role -->
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

        <!-- Format DOB -->
        <fmt:formatDate value="${user.dob}" pattern="dd/MM/yyyy" var="formattedDob" />

        <!-- Profile Info -->
        <form>
            <div class="form-row">
                <div class="form-group mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" class="form-control" value="${user.username}" readonly />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label">Date of Birth</label>
                    <input type="text" class="form-control" value="${formattedDob}" readonly />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group mb-3">
                    <label class="form-label">First Name</label>
                    <input type="text" class="form-control" value="${user.firstName}" readonly />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label">Last Name</label>
                    <input type="text" class="form-control" value="${user.lastName}" readonly />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" value="${user.email}" readonly />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label">Role</label>
                    <input type="text" class="form-control" value="${roleLabel}" readonly />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group mb-3">
                    <label class="form-label">Phone</label>
                    <input type="text" class="form-control" value="${user.phone}" readonly />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label">Gender</label>
                    <input type="text" class="form-control"
                           value="${user.sex == 1 ? 'Male' : 'Female'}" readonly />
                </div>
            </div>

            <div class="form-group mb-4">
                <label class="form-label">Address</label>
                <textarea class="form-control" rows="3" readonly>${user.address}</textarea>
            </div>

            <div class="d-flex justify-content-between">
                <a href="${pageContext.request.contextPath}/edit-profile" class="btn btn-outline-primary">
                    Edit Profile
                </a>
                <a href="${pageContext.request.contextPath}/change-password" class="btn btn-primary">
                    Change Password
                </a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
