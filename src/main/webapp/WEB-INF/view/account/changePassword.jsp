<%-- 
    Document   : changePassword
    Created on : May 27, 2025
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
            <div class="form-container" style="max-width: 600px; margin: 40px auto;">
                <h2 class="text-center mb-4">🔑 Change Password</h2>

                <!-- Thông báo lỗi/thành công -->
                <c:if test="${not empty error}">
                    <div class="success-message" style="background: #fce4ec; color: #c62828;">
                        <i class="fas fa-times-circle"></i>
                        <span>${error}</span>
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i>
                        <span>${success}</span>
                    </div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/change-password">
                    <div class="form-group">
                        <label class="form-label">Current Password *</label>
                        <input type="password" class="form-input" name="oldPassword" placeholder="Enter current password" required />
                    </div>

                    <div class="form-group">
                        <label class="form-label">New Password *</label>
                        <input type="password" class="form-input" name="newPassword" placeholder="Enter new password" required />
                    </div>

                    <div class="form-group">
                        <label class="form-label">Confirm Password *</label>
                        <input type="password" class="form-input" name="confirmPassword" placeholder="Re-enter new password" required />
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">💾 Save</button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
