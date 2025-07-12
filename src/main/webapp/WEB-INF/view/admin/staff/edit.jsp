<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">✏️ Edit Staff</h1>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/staff/edit">
            <input type="hidden" name="staffID" value="${staff.staffID}" />

            <div class="form-group">
                <label>Username:</label>
                <input type="text" name="username" value="${staff.username}" class="form-input" readonly />
            </div>

            <div class="form-group">
                <label>First Name:</label>
                <input type="text" name="firstName" value="${staff.firstName}" class="form-input" required />
            </div>

            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" name="lastName" value="${staff.lastName}" class="form-input" required />
            </div>

            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" value="${staff.email}" class="form-input" required />
            </div>

            <div class="form-group">
                <label>Phone:</label>
                <input type="text" name="phone" value="${staff.phone}" class="form-input" required />
            </div>

            <div class="form-group">
                <label>Address:</label>
                <textarea name="address" class="form-input">${staff.address}</textarea>
            </div>

            <div class="form-group">
                <label>Date of Birth:</label>
                <input type="date" name="dob" value="${staff.dob}" class="form-input" />
            </div>

            <div class="form-group">
                <label>Sex:</label>
                <label><input type="radio" name="sex" value="1" <c:if test="${staff.sex == 1}">checked</c:if> /> Male</label>
                <label><input type="radio" name="sex" value="0" <c:if test="${staff.sex == 0}">checked</c:if> /> Female</label>
                </div>

                <div class="form-group">
                    <label>Role:</label>
                    <select name="role" class="form-select" required>
                        <option value="2" <c:if test="${staff.role == 2}">selected</c:if>>Seller Staff</option>
                    <option value="3" <c:if test="${staff.role == 3}">selected</c:if>>Warehouse Staff</option>
                    </select>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/admin/staff/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
