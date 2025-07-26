<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <div class="main-content">
            <div class="form-container"
                 style="max-width: 500px; margin: 80px auto; background: #fff;
                 border-radius: 15px; padding: 30px;
                 box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

                <!-- Logo -->
                <div class="text-center mb-3">
                    <a href="${pageContext.request.contextPath}/customer/book/list">
                        <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                             alt="ReadTopia Logo" style="height: 40px;" />
                    </a>
                    <div style="font-size: 22px; font-weight: bold; margin-top: 10px;">
                        ReadTopia
                    </div>
                </div>

                <!-- Title -->
                <h2 class="text-center mb-4">Login to Your Account</h2>

                <!-- Alerts -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center">
                        ${error}
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success text-center">
                        ${success}
                    </div>
                </c:if>

                <!-- Login Form -->
                <form action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username <span class="text-danger">*</span></label>
                        <input type="text" id="username" name="username"
                               class="form-control" required placeholder="Enter your username" />
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Password <span class="text-danger">*</span></label>
                        <input type="password" id="password" name="password"
                               class="form-control" required placeholder="Enter your password" />
                    </div>

                    <!-- Forgot Password -->
                    <div class="text-end mb-3">
                        <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none">
                            Forgot password?
                        </a>
                    </div>

                    <!-- Submit -->
                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-primary w-100">Log In</button>
                    </div>

                    <!-- Register -->
                    <div class="text-center">
                        <p class="mb-1">Don't have an account?</p>
                        <a href="${pageContext.request.contextPath}/register" class="text-decoration-none">Register here</a>
                    </div>

                    <!-- Back to Home -->
                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/customer/book/list" class="text-decoration-none">
                            ← Back to Homepage
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
