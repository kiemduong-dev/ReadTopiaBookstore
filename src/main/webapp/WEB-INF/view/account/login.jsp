<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <div class="main-content">
            <div class="modal-content" style="max-width: 500px; margin: 80px auto; padding: 30px;
                 background: #fff; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

                <!-- Title -->
                <div class="modal-header text-center mb-4">
                    <h2>Login to ReadTopia</h2>
                </div>

                <!-- Logo -->
                <div class="logo-section text-center mb-3">
                    <div class="logo-bear"></div>
                    <div class="logo-text">READTOPIA</div>
                </div>

                <!-- Error -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- Success -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success text-center">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <!-- Login Form -->
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="form-group mb-3">
                        <label for="username" class="form-label">* Username:</label>
                        <input type="text" class="form-control" id="username" name="username" required />
                    </div>

                    <div class="form-group mb-3">
                        <label for="password" class="form-label">* Password:</label>
                        <input type="password" class="form-control" id="password" name="password" required />
                    </div>

                    <!-- Forgot Password -->
                    <div class="text-center mb-3">
                        <a href="${pageContext.request.contextPath}/forgot-password" class="link">
                            Forgot your password?
                        </a>
                    </div>

                    <!-- Submit -->
                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-primary w-100">Log In</button>
                    </div>

                    <!-- Register -->
                    <div class="text-center">
                        <span>Don't have an account?</span>
                        <a href="${pageContext.request.contextPath}/register" class="link">Register</a>
                    </div>

                    <!-- Back Home -->
                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/customer/book/list" class="link">Back to Home</a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
