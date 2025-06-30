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
                <h2 class="text-center mb-2">🔐 Reset Password – OTP Verification</h2>

                <!-- Description -->
                <p class="text-center mb-3 text-muted">
                    We've sent a 6-digit OTP to your email:<br>
                    <strong><c:out value="${sessionScope.resetEmail}" /></strong><br>
                    Please enter the code below.
                </p>

                <!-- Countdown timer -->
                <p class="text-center text-muted" id="countdownText">OTP is valid for: <span id="timer">02:00</span></p>

                <!-- Error Alert -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <!-- OTP Verification Form -->
                <form action="${pageContext.request.contextPath}/verify-otp-reset" method="post" autocomplete="off">
                    <div class="form-group mb-3">
                        <label for="otp" class="form-label">* OTP Code</label>
                        <input type="text"
                               id="otp"
                               name="otp"
                               class="form-control text-center"
                               placeholder="Enter 6-digit OTP"
                               maxlength="6"
                               pattern="[0-9]{6}"
                               required />
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary w-100">
                            ✅ Verify OTP
                        </button>
                    </div>
                </form>

                <!-- Resend link with countdown -->
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/forgot-password" class="link disabled" id="resendLink" tabindex="-1" aria-disabled="true">
                        🔁 Resend OTP (after 2:00)
                    </a>
                </div>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />

        <!-- JavaScript: Autofocus & Countdown -->
        <script>
            document.getElementById("otp")?.focus();

            let timeLeft = 120; // seconds
            const timerDisplay = document.getElementById("timer");
            const resendLink = document.getElementById("resendLink");

            const countdown = setInterval(() => {
                if (timeLeft <= 0) {
                    clearInterval(countdown);
                    timerDisplay.textContent = "00:00";
                    resendLink.classList.remove("disabled");
                    resendLink.setAttribute("tabindex", "0");
                    resendLink.setAttribute("aria-disabled", "false");
                    resendLink.innerHTML = "🔁 Resend OTP Now";
                } else {
                    const minutes = String(Math.floor(timeLeft / 60)).padStart(2, "0");
                    const seconds = String(timeLeft % 60).padStart(2, "0");
                    timerDisplay.textContent = `${minutes}:${seconds}`;
                                timeLeft--;
                            }
                        }, 1000);
        </script>
    </body>
</html>
