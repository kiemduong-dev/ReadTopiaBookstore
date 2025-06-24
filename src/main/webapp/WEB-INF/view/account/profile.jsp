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

            <form method="post" action="${pageContext.request.contextPath}/edit-profile" id="profileForm">
                <div class="form-row">
                    <div class="form-group">
                        <label for="profileUsername" class="form-label">* Username</label>
                        <input type="text" id="profileUsername" class="form-input" name="username" value="${user.username}" readonly />
                    </div>
                    <div class="form-group">
                        <label for="profileDob" class="form-label">* Date of Birth</label>
                        <input type="date" id="profileDob" class="form-input" name="dob" value="${user.dob}" readonly />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="profileFirstName" class="form-label">* First Name</label>
                        <input type="text" id="profileFirstName" class="form-input" name="firstName" value="${user.firstName}" readonly />
                    </div>
                    <div class="form-group">
                        <label for="profileLastName" class="form-label">* Last Name</label>
                        <input type="text" id="profileLastName" class="form-input" name="lastName" value="${user.lastName}" readonly />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="profileEmail" class="form-label">* Email</label>
                        <input type="email" id="profileEmail" class="form-input" name="email" value="${user.email}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Role</label>
                        <select id="profileRole" class="form-input" disabled>
                            <option <c:if test="${user.role == 1}">selected</c:if>>Customer</option>
                            <option <c:if test="${user.role == 2}">selected</c:if>>Seller Staff</option>
                            <option <c:if test="${user.role == 3}">selected</c:if>>Warehouse Staff</option>
                            <option <c:if test="${user.role == 0}">selected</c:if>>Admin</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="profilePhone" class="form-label">* Phone</label>
                            <input type="text" id="profilePhone" class="form-input" name="phone" value="${user.phone}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">* Sex</label>
                        <div class="radio-group">
                            <label class="radio-item">
                                <input type="radio" name="sex" id="profileMale" value="0" <c:if test="${user.sex == 0}">checked</c:if> disabled />
                                    Male
                                </label>
                                <label class="radio-item">
                                    <input type="radio" name="sex" id="profileFemale" value="1" <c:if test="${user.sex == 1}">checked</c:if> disabled />
                                    Female
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="profileAddress" class="form-label">* Address</label>
                        <textarea name="address" id="profileAddress" class="form-input" rows="3" readonly>${user.address}</textarea>
                </div>

                <div class="btn-group">
                    <button type="button" class="btn btn-secondary" id="editProfileBtn" onclick="toggleProfileEdit()">Edit</button>
                    <a href="${pageContext.request.contextPath}/change-password" class="btn btn-primary">Change Password</a>
                </div>
            </form>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />


    </body>
</html>
