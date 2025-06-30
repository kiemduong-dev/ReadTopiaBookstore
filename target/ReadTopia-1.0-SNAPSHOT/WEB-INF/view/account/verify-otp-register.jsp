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

                <!-- Logo thương hiệu -->
                <div class="logo-section text-center mb-3">
                    <div class="logo-bear"></div>
                    <div class="logo-text">READTOPIA</div>
                </div>

                <!-- Tiêu đề -->
                <h2 class="text-center mb-3">🔐 Verify Registration OTP</h2>

                <!-- Mô tả -->
                <p class="text-center mb-3">
                    A verification code has been sent to:
                    <strong><c:out value="${sessionScope.pendingAccount.email}" /></strong><br />
                    Please enter the 6-digit code below to complete your registration.
                </p>

                <!-- Thông báo lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- Form nhập OTP -->
                <form action="${pageContext.request.contextPath}/verify-otp-register" method="post" autocomplete="off">
                    <div class="form-group mb-4">
                        <label for="otp" class="form-label">OTP Code *</label>
                        <input type="text"
                               id="otp"
                               name="otp"
                               class="form-control text-center"
                               required
                               maxlength="6"
                               pattern="[0-9]{6}"
                               inputmode="numeric"
                               placeholder="Enter 6-digit code"
                               autofocus />
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-primary w-100">
                            ✅ Confirm & Register
                        </button>
                    </div>
                </form>

                <!-- Quay lại hoặc resend -->
                <div class="text-center">
                    <a href="${pageContext.request.contextPath}/register" class="link d-block mb-2">
                        🔁 Re-enter registration information
                    </a>
                    <span class="text-muted" style="font-size: 0.9rem;">
                        Didn’t receive the code? Check spam or re-register.
                    </span>
                </div>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
