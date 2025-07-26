<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/includes/head.jsp" />

<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="main-content">
    <div class="form-container"
         style="max-width: 600px; margin: 40px auto; background: #fff;
         border-radius: 15px; padding: 30px;
         box-shadow: 0 4px 12px rgba(0,0,0,0.1);">

        <!-- Brand Logo -->
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 alt="ReadTopia Logo"
                 style="height: 50px;" />
        </div>

        <!-- Title -->
        <h2 class="text-center fw-bold mb-4">Change Your Password</h2>

        <!-- Alerts -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center" role="alert">
                ${error}
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success text-center" role="alert">
                ${success}
            </div>
        </c:if>

        <!-- Change Password Form -->
        <form method="post" action="${pageContext.request.contextPath}/change-password" autocomplete="off">
            <!-- Old Password -->
            <div class="form-group mb-3">
                <label for="oldPassword" class="form-label">Current Password *</label>
                <input type="password" id="oldPassword" name="oldPassword"
                       class="form-control" placeholder="Enter your current password" required />
            </div>

            <!-- New Password -->
            <div class="form-group mb-3">
                <label for="newPassword" class="form-label">New Password *</label>
                <input type="password" id="newPassword" name="newPassword"
                       class="form-control"
                       placeholder="Min 8 characters with Aa#1"
                       required minlength="8" />
                <small id="strengthMessage" class="form-text"></small>
            </div>

            <!-- Confirm Password -->
            <div class="form-group mb-4">
                <label for="confirmPassword" class="form-label">Confirm New Password *</label>
                <input type="password" id="confirmPassword" name="confirmPassword"
                       class="form-control"
                       placeholder="Re-enter new password" required minlength="8" />
                <small id="matchMessage" class="form-text text-danger d-none">Passwords do not match.</small>
            </div>

            <!-- Actions -->
            <div class="d-flex justify-content-between">
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-secondary">
                    Cancel
                </a>
                <button type="submit" class="btn btn-success">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />

<!-- JS: Password Validation -->
<script>
    const form = document.querySelector("form");
    const newPassword = document.getElementById("newPassword");
    const confirmPassword = document.getElementById("confirmPassword");
    const matchMessage = document.getElementById("matchMessage");
    const strengthMessage = document.getElementById("strengthMessage");

    // Check password match
    form.addEventListener("submit", function (e) {
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            matchMessage.classList.remove("d-none");
            confirmPassword.focus();
        }
    });

    confirmPassword.addEventListener("input", function () {
        matchMessage.classList.add("d-none");
    });

    // Check password strength
    newPassword.addEventListener("input", function () {
        const val = newPassword.value;
        let message = "";
        let color = "gray";

        if (val.match(/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}/)) {
            message = "Strong password";
            color = "green";
        } else if (val.length >= 6) {
            message = "Medium strength";
            color = "orange";
        } else {
            message = "Weak password";
            color = "red";
        }

        strengthMessage.textContent = message;
        strengthMessage.style.color = color;
    });

    newPassword?.focus();
</script>
</body>
</html>
