<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Edit Account</h1>

        <!-- Thông báo lỗi -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/account/edit">
            <div class="form-group">
                <label>Username:</label>
                <input type="text" name="username" value="${account.username}" class="form-input" readonly />
            </div>

            <div class="form-group">
                <label>First Name:</label>
                <input type="text" name="firstName" value="${account.firstName}" class="form-input" required />
            </div>

            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" name="lastName" value="${account.lastName}" class="form-input" required />
            </div>

            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" value="${account.email}" class="form-input" required />
            </div>

            <div class="form-group">
                <label>Phone:</label>
                <input type="text" name="phone" value="${account.phone}" class="form-input" />
            </div>

            <div class="form-group">
                <label>Address:</label>
                <textarea name="address" class="form-input">${account.address}</textarea>
            </div>

            <div class="form-group">
                <label>Date of Birth:</label>
                <input type="date" name="dob" value="${account.dob}" class="form-input" />
            </div>

            <div class="form-group">
                <label>Sex:</label>
                <label><input type="radio" name="sex" value="1" <c:if test="${account.sex == 1}">checked</c:if> /> Male</label>
                <label><input type="radio" name="sex" value="0" <c:if test="${account.sex == 0}">checked</c:if> /> Female</label>
            </div>

            <div class="form-group">
                <label>Role:</label>
                <select name="role" class="form-input">
                    <option value="0" <c:if test="${account.role == 0}">selected</c:if>>Admin</option>
                    <option value="1" <c:if test="${account.role == 1}">selected</c:if>>Customer</option>
                    <!-- Có thể thêm role khác nếu có -->
                </select>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-success">Submit</button>
                <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
