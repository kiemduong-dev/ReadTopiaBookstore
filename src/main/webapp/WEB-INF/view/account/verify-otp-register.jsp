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

        <!-- Logo -->
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 alt="ReadTopia Logo"
                 style="height: 50px;" />
        </div>

        <!-- Title -->
        <h2 class="text-center mb-3 fw-bold">Verify Your Email</h2>

        <!-- Description -->
        <p class="text-center text-muted mb-4">
            We have sent a 6-digit OTP code to your email address:<br/>
            <strong><c:out value="${sessionScope.pendingAccount.email}" /></strong><br/>
            Please enter the code below to complete your registration.
        </p>

        <!-- Alert -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center" role="alert">
                ${error}
            </div>
        </c:if>

        <!-- OTP Form -->
        <form action="${pageContext.request.contextPath}/verify-otp-register" method="post" autocomplete="off">
            <div class="form-group mb-4">
                <label for="otp" class="form-label">OTP Code <span class="text-danger">*</span></label>
                <input type="text"
                       id="otp"
                       name="otp"
                       class="form-control text-center"
                       required
                       maxlength="6"
                       pattern="[0-9]{6}"
                       inputmode="numeric"
                       placeholder="Enter 6-digit code"
                       autofocus />
            </div>

            <div class="d-grid mb-3">
                <button type="submit" class="btn btn-primary w-100">
                    Confirm & Complete Registration
                </button>
            </div>
        </form>

        <!-- Navigation -->
        <div class="text-center">
            <a href="${pageContext.request.contextPath}/register" class="text-decoration-none d-block mb-2">
                Return to Registration Form
            </a>
            <p class="text-muted mb-0" style="font-size: 0.9rem;">
                Didn’t receive the code? Please check your spam folder or try again later.
            </p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
