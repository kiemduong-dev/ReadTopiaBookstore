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

                <!-- Logo -->
                <div class="logo-section text-center mb-3">
                    <div class="logo-bear"></div>
                    <div class="logo-text">READTOPIA</div>
                </div>

                <!-- Title -->
                <h2 class="text-center mb-3">🔐 OTP Verification</h2>

                <!-- Description -->
                <p class="text-center mb-4">
                    An OTP code has been sent to your email:
                    <strong>${sessionScope.resetEmail}</strong><br />
                    Please enter the 6-digit code below to reset your password.
                </p>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- OTP Input Form -->
                <form action="${pageContext.request.contextPath}/verify-otp-reset" method="post">
                    <div class="form-group mb-3">
                        <label for="otp" class="form-label">* OTP Code:</label>
                        <input type="text"
                               id="otp"
                               name="otp"
                               class="form-control"
                               required
                               maxlength="6"
                               pattern="[0-9]{6}"
                               placeholder="Enter 6-digit code" />
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        ✅ Verify OTP
                    </button>
                </form>

                <!-- Resend OTP -->
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/forgot-password" class="link">
                        🔁 Resend OTP
                    </a>
                </div>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
