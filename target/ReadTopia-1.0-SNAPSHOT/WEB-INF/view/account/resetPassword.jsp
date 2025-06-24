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

                <!-- Tiêu đề -->
                <h2 class="text-center mb-3">🔐 Đặt Lại Mật Khẩu</h2>

                <!-- Thông báo lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- Thông báo thành công -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success text-center" role="alert">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <!-- Form đặt lại mật khẩu -->
                <form action="${pageContext.request.contextPath}/reset-password" method="post">
                    <div class="form-group mb-3">
                        <label class="form-label">Mật khẩu mới:</label>
                        <input type="password"
                               name="newPassword"
                               class="form-control"
                               required
                               minlength="6"
                               placeholder="Tối thiểu 6 ký tự" />
                    </div>

                    <div class="form-group mb-4">
                        <label class="form-label">Xác nhận mật khẩu:</label>
                        <input type="password"
                               name="confirmPassword"
                               class="form-control"
                               required
                               minlength="6"
                               placeholder="Nhập lại mật khẩu mới" />
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">✅ Đổi mật khẩu</button>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">🔙 Quay lại đăng nhập</a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
