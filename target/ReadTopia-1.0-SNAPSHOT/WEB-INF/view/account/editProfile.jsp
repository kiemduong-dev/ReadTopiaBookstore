<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/includes/head.jsp" />

<body>
    <!-- Header -->
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <!-- Main Form Container -->
    <div class="form-container" style="max-width: 600px; margin: 50px auto;">
        <h2 class="text-center">✏️ Edit Profile</h2>

        <!-- Logo Branding -->
        <div class="logo-section">
            <div class="logo-bear"></div>
            <div class="logo-text">READTOPIA</div>
        </div>

        <!-- Toast Notification -->
        <jsp:include page="/WEB-INF/includes/toast.jsp" />

        <!-- Feedback Message -->
        <c:if test="${not empty error}">
            <div class="success-message" style="background: #fdecea; color: #d32f2f;">
                <i class="fas fa-exclamation-circle"></i>
                <span>${error}</span>
            </div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="success-message">
                <i class="fas fa-check-circle"></i>
                <span>${success}</span>
            </div>
        </c:if>

        <!-- Edit Profile Form -->
        <form method="post" action="${pageContext.request.contextPath}/edit-profile">
            <!-- Username -->
            <div class="form-group">
                <label class="form-label">Username</label>
                <input type="text" class="form-input" name="username" value="${user.username}" readonly>
            </div>

            <!-- Date of Birth -->
            <div class="form-group">
                <label class="form-label">Date of Birth</label>
                <input type="date" class="form-input" name="dob" value="${user.dob}" required>
            </div>

            <!-- First + Last Name in Row -->
            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">First Name</label>
                    <input type="text" class="form-input" name="firstName" value="${user.firstName}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Last Name</label>
                    <input type="text" class="form-input" name="lastName" value="${user.lastName}" required>
                </div>
            </div>

            <!-- Email -->
            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" class="form-input" name="email" value="${user.email}" required>
            </div>

            <!-- Phone -->
            <div class="form-group">
                <label class="form-label">Phone</label>
                <input type="text" class="form-input" name="phone" value="${user.phone}" required>
            </div>

            <!-- Gender -->
            <div class="form-group">
                <label class="form-label">Gender</label>
                <div class="radio-group">
                    <div class="radio-item">
                        <input type="radio" id="male" name="sex" value="1" ${user.sex == 1 ? 'checked' : ''}>
                        <label for="male">Male</label>
                    </div>
                    <div class="radio-item">
                        <input type="radio" id="female" name="sex" value="0" ${user.sex == 0 ? 'checked' : ''}>
                        <label for="female">Female</label>
                    </div>
                </div>
            </div>

            <!-- Address -->
            <div class="form-group">
                <label class="form-label">Address</label>
                <textarea class="form-input" name="address" rows="2" required>${user.address}</textarea>
            </div>

            <!-- Action Buttons -->
            <div class="btn-group">
                <button type="submit" class="btn btn-primary">Save</button>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>

    <!-- Footer -->
    <jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
