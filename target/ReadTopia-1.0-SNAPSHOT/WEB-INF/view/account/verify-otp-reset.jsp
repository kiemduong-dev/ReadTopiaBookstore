<%-- 
    Document   : verify-otp-reset
    Created on : Jun 23, 2025, 9:27:34 PM
    Author     : ADMIN
--%>

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
                <h2 class="text-center mb-3">📨 Xác thực OTP</h2>

                <!-- Mô tả -->
                <p class="text-center mb-4">
                    Mã OTP đã được gửi đến email:
                    <strong>${sessionScope.resetEmail}</strong><br/>
                    Vui lòng nhập mã gồm 6 chữ số bên dưới để đặt lại mật khẩu.
                </p>

                <!-- Thông báo lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- Form nhập OTP -->
                <form action="${pageContext.request.contextPath}/verify-otp-reset" method="post">
                    <div class="form-group mb-3">
                        <label for="otp" class="form-label">* Mã OTP:</label>
                        <input type="text"
                               id="otp"
                               name="otp"
                               class="form-control"
                               required
                               maxlength="6"
                               pattern="[0-9]{6}"
                               placeholder="Nhập mã gồm 6 số" />
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary w-100">✅ Xác nhận</button>
                    </div>
                </form>

                <!-- Gửi lại OTP -->
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/forgot-password" class="link">
                        🔁 Gửi lại mã OTP
                    </a>
                </div>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
