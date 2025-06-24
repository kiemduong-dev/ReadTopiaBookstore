<%-- 
    File        : changePassword.jsp
    Description : Allows authenticated users to update their account password.
    Created on  : May 27, 2025
    Author      : ADMIN 
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
                 style="max-width: 600px; margin: 40px auto; background: #fff;
                 border-radius: 12px; padding: 30px;
                 box-shadow: 0 4px 12px rgba(0,0,0,0.1);">

                <!-- Tiêu đề -->
                <h2 class="text-center mb-4">🔑 Change Password</h2>

                <!-- Thông báo lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-times-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- Thông báo thành công -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success text-center" role="alert">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <!-- Form đổi mật khẩu -->
                <form method="post" action="${pageContext.request.contextPath}/change-password">
                    <div class="form-group mb-3">
                        <label for="oldPassword" class="form-label">Current Password *</label>
                        <input type="password" id="oldPassword" name="oldPassword" class="form-control"
                               placeholder="Enter current password" required />
                    </div>

                    <div class="form-group mb-3">
                        <label for="newPassword" class="form-label">New Password *</label>
                        <input type="password" id="newPassword" name="newPassword" class="form-control"
                               placeholder="Enter new password" required minlength="6" />
                    </div>

                    <div class="form-group mb-4">
                        <label for="confirmPassword" class="form-label">Confirm Password *</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
                               placeholder="Re-enter new password" required minlength="6" />
                    </div>

                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary">
                            💾 Save
                        </button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">
                            Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
