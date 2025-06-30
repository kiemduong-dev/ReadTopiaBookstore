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
                <form method="post" action="${pageContext.request.contextPath}/change-password" autocomplete="off">
                    <!-- Mật khẩu cũ -->
                    <div class="form-group mb-3">
                        <label for="oldPassword" class="form-label">* Current Password</label>
                        <input type="password" id="oldPassword" name="oldPassword" class="form-control"
                               placeholder="Enter your current password" required />
                    </div>

                    <!-- Mật khẩu mới -->
                    <div class="form-group mb-3">
                        <label for="newPassword" class="form-label">* New Password</label>
                        <input type="password" id="newPassword" name="newPassword" class="form-control"
                               placeholder="At least 8 characters with upper/lowercase, number & symbol"
                               required minlength="8" />
                    </div>

                    <!-- Nhập lại mật khẩu -->
                    <div class="form-group mb-4">
                        <label for="confirmPassword" class="form-label">* Confirm Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
                               placeholder="Re-enter new password" required minlength="8" />
                        <small id="matchMessage" class="text-danger d-none">❌ Passwords do not match.</small>
                    </div>

                    <!-- Buttons -->
                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary">💾 Save</button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">↩ Cancel</a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />

        <!-- JS: Confirm password match -->
        <script>
            const form = document.querySelector("form");
            const newPassword = document.getElementById("newPassword");
            const confirmPassword = document.getElementById("confirmPassword");
            const matchMessage = document.getElementById("matchMessage");

            form.addEventListener("submit", function (e) {
                if (newPassword.value !== confirmPassword.value) {
                    e.preventDefault();
                    matchMessage.classList.remove("d-none");
                    confirmPassword.focus();
                }
            });

            confirmPassword.addEventListener("input", () => {
                matchMessage.classList.add("d-none");
            });

            newPassword?.focus();
        </script>

    </body>
</html>
