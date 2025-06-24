<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <div class="main-content">
            <div class="form-container"
                 style="max-width: 500px; margin: 40px auto; background: #fff;
                 border-radius: 15px; padding: 30px;
                 box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

                <!-- Logo -->
                <div class="logo-section text-center mb-3">
                    <div class="logo-bear"></div>
                    <div class="logo-text">READTOPIA</div>
                </div>

                <!-- Tiêu đề -->
                <h2 class="text-center mb-4">🔐 Quên Mật Khẩu</h2>

                <!-- Thông báo -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success text-center" role="alert">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <!-- Form -->
                <form action="${pageContext.request.contextPath}/forgot-password" method="post" autocomplete="off">
                    <div class="form-group mb-3">
                        <label class="form-label">* Tên đăng nhập:</label>
                        <input type="text" name="username" class="form-control" required />
                    </div>

                    <div class="form-group mb-4">
                        <label class="form-label">* Email:</label>
                        <input type="email" name="email" class="form-control" required />
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-paper-plane"></i> Gửi mã OTP
                    </button>

                    <div class="text-center mt-4">
                        <a href="${pageContext.request.contextPath}/login" class="link">
                            🔙 Quay lại đăng nhập
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
