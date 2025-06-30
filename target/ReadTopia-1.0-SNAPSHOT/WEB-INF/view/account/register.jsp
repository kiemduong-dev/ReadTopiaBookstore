<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <div class="form-container">
            <div class="logo-section">
                <div class="logo-bear"></div>
                <div class="logo-text">Register Account</div>
            </div>

            <!-- Toast -->
            <jsp:include page="/WEB-INF/includes/toast.jsp" />

            <!-- Alerts -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-times-circle"></i> ${error}
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>

            <!-- Registration Form -->
            <form action="${pageContext.request.contextPath}/register" method="post" autocomplete="off">
                <!-- Username + Email -->
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">* Username</label>
                        <input type="text" name="username" class="form-input" required
                               placeholder="Only letters & numbers, max 30"
                               value="${param.username}" />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Email</label>
                        <input type="email" name="email" class="form-input" required
                               placeholder="example@gmail.com"
                               value="${param.email}" />
                    </div>
                </div>

                <!-- Password + Confirm -->
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">* Password</label>
                        <input type="password" name="password" class="form-input" required
                               placeholder="8+ chars, Aa, number, symbol" />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Confirm Password</label>
                        <input type="password" name="confirmPassword" class="form-input" required
                               placeholder="Re-enter password" />
                    </div>
                </div>

                <!-- First + Last name -->
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">* First Name</label>
                        <input type="text" name="firstName" class="form-input" required
                               placeholder="Ex: An"
                               value="${param.firstName}" />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Last Name</label>
                        <input type="text" name="lastName" class="form-input" required
                               placeholder="Ex: Kiếm"
                               value="${param.lastName}" />
                    </div>
                </div>

                <!-- Phone + DOB -->
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">* Phone</label>
                        <input type="tel" name="phone" class="form-input" required
                               placeholder="Start with 03,05,07,08,09"
                               value="${param.phone}" />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Date of Birth</label>
                        <input type="text" name="dob" class="form-input" required
                               placeholder="dd/MM/yyyy"
                               value="${param.dob}" />
                    </div>
                </div>

                <!-- Gender -->
                <div class="form-group">
                    <label class="form-label">* Gender</label>
                    <div class="radio-group">
                        <div class="radio-item">
                            <input type="radio" id="female" name="sex" value="female"
                                   <c:if test="${param.sex == 'female'}">checked</c:if> />
                                   <label for="female">Female</label>
                            </div>
                            <div class="radio-item">
                                <input type="radio" id="male" name="sex" value="male"
                                <c:if test="${param.sex == 'male'}">checked</c:if> />
                                <label for="male">Male</label>
                            </div>
                        </div>
                    </div>

                    <!-- Address -->
                    <div class="form-group">
                        <label class="form-label">* Address</label>
                        <textarea name="address" class="form-input" rows="3" required
                                  placeholder="Enter your address...">${param.address}</textarea>
                </div>

                <!-- Actions -->
                <div class="btn-group mt-4">
                    <button type="submit" class="btn btn-primary">✅ Register</button>
                    <button type="reset" class="btn btn-secondary">Reset</button>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">← Back to Login</a>
                </div>
            </form>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
