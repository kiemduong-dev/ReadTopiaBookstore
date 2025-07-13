<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <jsp:include page="/WEB-INF/includes/head.jsp" />

    <body>
        <jsp:include page="/WEB-INF/includes/header.jsp" />

        <div class="main-content">
            <div class="form-container"
                 style="max-width: 600px; margin: 40px auto; background: #fff; border-radius: 12px; padding: 30px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">

                <h2 class="text-center mb-4">✏️ Edit Profile</h2>

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

                <form action="${pageContext.request.contextPath}/edit-profile" method="post">
                    <div class="form-group mb-3">
                        <label class="form-label">Username</label>
                        <input type="text" class="form-control" value="${user.username}" readonly />
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label class="form-label">* First Name</label>
                            <input type="text" name="firstName" class="form-control" value="${user.firstName}" required />
                        </div>
                        <div class="form-group col-md-6">
                            <label class="form-label">* Last Name</label>
                            <input type="text" name="lastName" class="form-control" value="${user.lastName}" required />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label class="form-label">* Email</label>
                            <input type="email" name="email" class="form-control" value="${user.email}" required />
                        </div>
                        <div class="form-group col-md-6">
                            <label class="form-label">* Phone</label>
                            <input type="text" name="phone" class="form-control" value="${user.phone}" required />
                        </div>
                    </div>

                    <div class="form-group mb-3">
                        <label class="form-label">* Address</label>
                        <textarea name="address" class="form-control" rows="3" required>${user.address}</textarea>
                    </div>

                    <div class="form-group mb-3">
                        <label class="form-label">* Date of Birth</label>
                        <input type="date" name="dob" class="form-control"
                               value="<c:out value='${user.dob}'/>" required />
                    </div>

                    <div class="form-group mb-4">
                        <label class="form-label">* Gender</label>
                        <div class="radio-group d-flex">
                            <div class="form-check me-4">
                                <input type="radio" id="male" name="sex" value="1" class="form-check-input"
                                       <c:if test="${user.sex == 1}">checked</c:if> />
                                       <label for="male" class="form-check-label">Male</label>
                                </div>
                                <div class="form-check">
                                    <input type="radio" id="female" name="sex" value="0" class="form-check-input"
                                    <c:if test="${user.sex == 0}">checked</c:if> />
                                    <label for="female" class="form-check-label">Female</label>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between">
                            <button type="submit" class="btn btn-primary">💾 Save Changes</button>
                            <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">↩ Cancel</a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/includes/footer.jsp" />
    </body>
</html>
