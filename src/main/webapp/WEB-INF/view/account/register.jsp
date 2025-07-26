<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

        <!-- Logo + Title -->
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 alt="ReadTopia Logo" style="height: 50px;" />
            <h2 class="mt-3">Create Your Account</h2>
            <p class="text-muted">Sign up to buy your favorite books from ReadTopia.</p>
        </div>

        <!-- Toast Messages -->
        <jsp:include page="/WEB-INF/includes/toast.jsp" />

        <!-- Alerts -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success text-center">${success}</div>
        </c:if>

        <!-- Register Form -->
        <form action="${pageContext.request.contextPath}/register" method="post" autocomplete="off">

            <!-- Username + Email -->
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Username <span class="text-danger">*</span></label>
                    <input type="text" name="username" class="form-control" required
                           placeholder="Choose a unique username"
                           value="${param.username}" />
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Email <span class="text-danger">*</span></label>
                    <input type="email" name="email" class="form-control" required
                           placeholder="e.g., user@example.com"
                           value="${param.email}" />
                </div>
            </div>

            <!-- Password + Confirm -->
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Password <span class="text-danger">*</span></label>
                    <input type="password" name="password" class="form-control" required
                           placeholder="At least 8 chars, include Aa#1" />
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Confirm Password <span class="text-danger">*</span></label>
                    <input type="password" name="confirmPassword" class="form-control" required
                           placeholder="Retype your password" />
                </div>
            </div>

            <!-- Name -->
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">First Name <span class="text-danger">*</span></label>
                    <input type="text" name="firstName" class="form-control" required
                           placeholder="e.g., John"
                           value="${param.firstName}" />
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Last Name <span class="text-danger">*</span></label>
                    <input type="text" name="lastName" class="form-control" required
                           placeholder="e.g., Smith"
                           value="${param.lastName}" />
                </div>
            </div>

            <!-- Phone + DOB -->
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Phone Number <span class="text-danger">*</span></label>
                    <input type="tel" name="phone" class="form-control" required
                           placeholder="e.g., 0912345678"
                           value="${param.phone}" />
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Date of Birth <span class="text-danger">*</span></label>
                    <input type="text" name="dob" class="form-control" required
                           placeholder="dd/MM/yyyy (e.g., 25/07/2002)"
                           value="${param.dob}" />
                </div>
            </div>

            <!-- Gender -->
            <div class="mb-3">
                <label class="form-label">Gender <span class="text-danger">*</span></label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="sex" value="1"
                           <c:if test="${param.sex == '1'}">checked</c:if> />
                    <label class="form-check-label">Male</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="sex" value="0"
                           <c:if test="${param.sex == '0'}">checked</c:if> />
                    <label class="form-check-label">Female</label>
                </div>
            </div>

            <!-- Address -->
            <div class="mb-3">
                <label class="form-label">Address <span class="text-danger">*</span></label>
                <textarea name="address" class="form-control" rows="3" required
                          placeholder="Your full home address">${param.address}</textarea>
            </div>

            <!-- Buttons -->
            <div class="d-grid gap-2 mt-4">
                <button type="submit" class="btn btn-primary">Create Account</button>
                <button type="reset" class="btn btn-secondary">Clear Form</button>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-link">← Back to Login</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
