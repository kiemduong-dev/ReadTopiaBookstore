<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/includes/head.jsp" />

<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="main-content">
    <div class="form-container"
         style="max-width: 500px; margin: 50px auto; background: #fff;
         border-radius: 15px; padding: 30px;
         box-shadow: 0 4px 15px rgba(0,0,0,0.1);">

        <!-- Brand Logo -->
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 alt="ReadTopia Logo"
                 style="height: 50px;" />
        </div>

        <!-- Title -->
        <h2 class="text-center mb-3">Create New Password</h2>
        <p class="text-center text-muted mb-4">
            Please enter your new password. Make sure it's secure and easy to remember.
        </p>

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

        <!-- Reset Password Form -->
        <form method="post" action="${pageContext.request.contextPath}/reset-password" autocomplete="off">
            <!-- New Password -->
            <div class="form-group mb-3">
                <label for="newPassword" class="form-label">New Password <span class="text-danger">*</span></label>
                <input type="password"
                       id="newPassword"
                       name="newPassword"
                       class="form-control"
                       required
                       minlength="8"
                       placeholder="Min 8 chars, must include Aa#1" />
                <div id="passwordStrength" class="form-text mt-1"></div>
            </div>

            <!-- Confirm Password -->
            <div class="form-group mb-4">
                <label for="confirmPassword" class="form-label">Confirm Password <span class="text-danger">*</span></label>
                <input type="password"
                       id="confirmPassword"
                       name="confirmPassword"
                       class="form-control"
                       required
                       placeholder="Retype your new password" />
                <div id="matchInfo" class="form-text text-danger d-none">Passwords do not match.</div>
            </div>

            <!-- Actions -->
            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary">Reset Password</button>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">← Back to Login</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />

<!-- JS: Check password match and strength -->
<script>
    const newPassword = document.getElementById("newPassword");
    const confirmPassword = document.getElementById("confirmPassword");
    const form = document.querySelector("form");
    const matchInfo = document.getElementById("matchInfo");
    const strengthInfo = document.getElementById("passwordStrength");

    form.addEventListener("submit", function (e) {
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            matchInfo.classList.remove("d-none");
        }
    });

    confirmPassword.addEventListener("input", function () {
        matchInfo.classList.toggle("d-none", newPassword.value === confirmPassword.value);
    });

    newPassword.addEventListener("input", function () {
        const val = newPassword.value;
        let strength = "", color = "";
        if (val.match(/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}/)) {
            strength = "Strong password";
            color = "green";
        } else if (val.length >= 6) {
            strength = "Medium password";
            color = "orange";
        } else {
            strength = "Weak password";
            color = "red";
        }
        strengthInfo.textContent = strength;
        strengthInfo.style.color = color;
    });

    newPassword?.focus();
</script>
</body>
</html>
