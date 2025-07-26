<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/includes/head.jsp" />

<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="main-content">
    <div class="form-container"
         style="max-width: 500px; margin: 50px auto; background: #fff;
         border-radius: 15px; padding: 30px;
         box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

        <!-- Logo -->
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 alt="ReadTopia Logo"
                 style="height: 50px;" />
        </div>

        <!-- Title -->
        <h2 class="text-center mb-3">Reset Your Password</h2>
        <p class="text-center text-muted mb-4">
            Enter your <strong>username</strong> and <strong>email</strong>.<br/>
            We will send a verification code to your email to help you reset your password.
        </p>

        <!-- Alerts -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center" role="alert">
                ${error}
            </div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success text-center" role="alert">
                ${success}
            </div>
        </c:if>

        <!-- Forgot Password Form -->
        <form action="${pageContext.request.contextPath}/forgot-password" method="post" autocomplete="off">
            <div class="mb-3">
                <label class="form-label">Username <span class="text-danger">*</span></label>
                <input type="text"
                       name="username"
                       class="form-control"
                       required
                       placeholder="Enter your username"
                       value="${param.username}" />
            </div>

            <div class="mb-4">
                <label class="form-label">Email <span class="text-danger">*</span></label>
                <input type="email"
                       name="email"
                       class="form-control"
                       required
                       placeholder="Enter your email"
                       value="${param.email}" />
            </div>

            <button type="submit" class="btn btn-primary w-100">
                Send Verification Code
            </button>

            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                    ← Back to Login
                </a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
