<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <div class="form-container">
            <h2>👤 Profile</h2>

            <jsp:include page="/WEB-INF/includes/toast.jsp" />

            <form>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">* Username</label>
                        <input type="text" class="form-input" value="${user.username}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Date of Birth</label>
                        <input type="date" class="form-input" value="${user.dob}" readonly />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">* First Name</label>
                        <input type="text" class="form-input" value="${user.firstName}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Last Name</label>
                        <input type="text" class="form-input" value="${user.lastName}" readonly />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">* Email</label>
                        <input type="email" class="form-input" value="${user.email}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Role</label>
                        <select class="form-input" disabled>
                            <option <c:if test="${user.role == 1}">selected</c:if>>Customer</option>
                            <option <c:if test="${user.role == 2}">selected</c:if>>Seller Staff</option>
                            <option <c:if test="${user.role == 3}">selected</c:if>>Warehouse Staff</option>
                            <option <c:if test="${user.role == 0}">selected</c:if>>Admin</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">* Phone</label>
                            <input type="text" class="form-input" value="${user.phone}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Gender</label>
                        <div class="radio-group">
                            <label class="radio-item">
                                <input type="radio" value="male" <c:if test="${user.sex == 1}">checked</c:if> disabled />
                                    Male
                                </label>
                                <label class="radio-item">
                                    <input type="radio" value="female" <c:if test="${user.sex == 0}">checked</c:if> disabled />
                                    Female
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">* Address</label>
                        <textarea class="form-input" rows="3" readonly>${user.address}</textarea>
                </div>

                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/edit-profile" class="btn btn-secondary">✏️ Edit Profile</a>
                    <a href="${pageContext.request.contextPath}/change-password" class="btn btn-primary">🔒 Change Password</a>
                </div>
            </form>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
