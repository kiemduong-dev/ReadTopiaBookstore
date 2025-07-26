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
        <h2 class="text-center mb-3">Verify Your OTP</h2>

        <!-- Description -->
        <p class="text-center text-muted mb-3">
            We sent a 6-digit code to your email:<br />
            <strong><c:out value="${sessionScope.resetEmail}" /></strong><br />
            Please enter it below to continue.
        </p>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center" role="alert">
                ${error}
            </div>
        </c:if>

        <!-- OTP Form -->
        <form action="${pageContext.request.contextPath}/verify-otp-reset" method="post" autocomplete="off">
            <div class="form-group mb-4">
                <label for="otp" class="form-label">Verification Code <span class="text-danger">*</span></label>
                <input type="text"
                       id="otp"
                       name="otp"
                       class="form-control text-center"
                       placeholder="Enter 6-digit code"
                       maxlength="6"
                       pattern="[0-9]{6}"
                       required
                       autofocus />
            </div>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary w-100">Verify</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
