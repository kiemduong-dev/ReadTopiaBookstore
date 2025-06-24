<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Add New Account</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/account/add">
            <div class="form-group">
                <label>Username:</label>
                <input type="text" name="username" required class="form-input" />
            </div>
            <div class="form-group">
                <label>First Name:</label>
                <input type="text" name="firstName" required class="form-input" />
            </div>
            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" name="lastName" required class="form-input" />
            </div>
            <div class="form-group">
                <label>Sex:</label>
                <select name="sex" class="form-select" required>
                    <option value="1">Male</option>
                    <option value="0">Female</option>
                </select>
            </div>
            <div class="form-group">
                <label>Role:</label>
                <select name="role" class="form-select" required>
                    <option value="1">Staff</option>
                    <option value="2">Admin</option>
                </select>
            </div>
            <div class="form-group">
                <label>Date of Birth:</label>
                <input type="date" name="dob" class="form-input" />
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" required class="form-input" />
            </div>
            <div class="form-group">
                <label>Phone:</label>
                <input type="text" name="phone" required class="form-input" />
            </div>
            <div class="form-group">
                <label>Address:</label>
                <input type="text" name="address" required class="form-input" />
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">Add Account</button>
                <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
