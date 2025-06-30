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

                <!-- Title -->
                <h2 class="text-center mb-2">🔐 Reset Your Password</h2>
                <p class="text-center text-muted mb-4">
                    Enter your new password below. Make sure it's strong and secure.
                </p>

                <!-- Alerts -->
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
                <form method="post" action="${pageContext.request.contextPath}/reset-password" autocomplete="off">
                    <!-- New Password -->
                    <div class="form-group mb-3">
                        <label class="form-label" for="newPassword">* New Password</label>
                        <input type="password"
                               id="newPassword"
                               name="newPassword"
                               class="form-control"
                               required
                               minlength="8"
                               placeholder="Min 8 chars, include Aa, number, symbol" />
                        <div id="passwordStrength" class="text-muted mt-1" style="font-size: 0.9rem;"></div>
                    </div>

                    <!-- Confirm -->
                    <div class="form-group mb-4">
                        <label class="form-label" for="confirmPassword">* Confirm Password</label>
                        <input type="password"
                               id="confirmPassword"
                               name="confirmPassword"
                               class="form-control"
                               required
                               placeholder="Re-enter your password" />
                        <small id="matchInfo" class="form-text text-danger d-none">❌ Passwords do not match.</small>
                    </div>

                    <!-- Action Buttons -->
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">💾 Reset Password</button>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">↩ Back to Login</a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />

        <!-- JS: Password match + strength -->
        <script>
            const newPassword = document.getElementById("newPassword");
            const confirmPassword = document.getElementById("confirmPassword");
            const form = document.querySelector("form");
            const matchInfo = document.getElementById("matchInfo");
            const strengthInfo = document.getElementById("passwordStrength");

            // Password match validation
            form.addEventListener("submit", function (e) {
                if (newPassword.value !== confirmPassword.value) {
                    e.preventDefault();
                    matchInfo.classList.remove("d-none");
                }
            });

            confirmPassword.addEventListener("input", function () {
                matchInfo.classList.toggle("d-none", newPassword.value === confirmPassword.value);
            });

            // Password strength
            newPassword.addEventListener("input", function () {
                const val = newPassword.value;
                let strength = "Weak";
                let color = "red";
                if (val.match(/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}/)) {
                    strength = "Strong";
                    color = "green";
                } else if (val.length >= 6) {
                    strength = "Medium";
                    color = "orange";
                }
                strengthInfo.textContent = "Password strength: " + strength;
                strengthInfo.style.color = color;
            });

            newPassword?.focus();
        </script>
    </body>
</html>
