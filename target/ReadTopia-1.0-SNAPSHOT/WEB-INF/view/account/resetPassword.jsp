<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <div class="main-content">
            <div class="form-container"
                 style="max-width: 500px; margin: 50px auto; background: #fff;
                 border-radius: 15px; padding: 30px;
                 box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

                <!-- Logo Branding -->
                <div class="logo-section text-center mb-3">
                    <div class="logo-bear"></div>
                    <div class="logo-text">READTOPIA</div>
                </div>

                <!-- Title -->
                <h2 class="text-center mb-4">🔐 Reset Your Password</h2>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- Success Message -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success text-center" role="alert">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <!-- Password Reset Form -->
                <form method="post" action="${pageContext.request.contextPath}/reset-password">
                    <!-- New Password -->
                    <div class="form-group mb-3">
                        <label class="form-label" for="newPassword">* New Password:</label>
                        <input type="password"
                               id="newPassword"
                               name="newPassword"
                               class="form-control"
                               placeholder="At least 6 characters"
                               minlength="6"
                               required />
                    </div>

                    <!-- Confirm Password -->
                    <div class="form-group mb-4">
                        <label class="form-label" for="confirmPassword">* Confirm Password:</label>
                        <input type="password"
                               id="confirmPassword"
                               name="confirmPassword"
                               class="form-control"
                               placeholder="Re-enter your new password"
                               minlength="6"
                               required />
                    </div>

                    <!-- Actions -->
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">💾 Reset Password</button>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">↩ Back to Login</a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
