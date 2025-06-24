<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Add New Staff</h1>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/staff/add" class="card">
            <div class="form-row">
                <div class="form-group">
                    <label for="username" class="form-label">* Username</label>
                    <input type="text" id="username" name="username" required class="form-input" value="${staff.username}" />
                </div>
                <div class="form-group">
                    <label for="dob" class="form-label">Date of Birth</label>
                    <input type="date" id="dob" name="dob" class="form-input" value="${staff.dob}" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="firstName" class="form-label">* First Name</label>
                    <input type="text" id="firstName" name="firstName" required class="form-input" value="${staff.firstName}" />
                </div>
                <div class="form-group">
                    <label for="lastName" class="form-label">* Last Name</label>
                    <input type="text" id="lastName" name="lastName" required class="form-input" value="${staff.lastName}" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="email" class="form-label">* Email</label>
                    <input type="email" id="email" name="email" required class="form-input" value="${staff.email}" />
                </div>
                <div class="form-group">
                    <label for="role" class="form-label">Role</label>
                    <select id="role" name="role" class="form-select">
                        <option value="">Please select role</option>
                        <option value="0" ${staff.role == 0 ? 'selected' : ''}>Admin</option>
                        <option value="1" ${staff.role == 1 ? 'selected' : ''}>Staff</option>
                        <option value="2" ${staff.role == 2 ? 'selected' : ''}>Seller Staff</option>
                        <option value="3" ${staff.role == 3 ? 'selected' : ''}>Warehouse Staff</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="tel" id="phone" name="phone" class="form-input" value="${staff.phone}" />
                </div>
                <div class="form-group">
                    <label class="form-label">* Sex</label>
                    <div class="radio-group">
                        <div class="radio-item">
                            <input type="radio" id="sexFemale" name="sex" value="0" ${staff.sex == 0 ? 'checked' : ''} />
                            <label for="sexFemale">Female</label>
                        </div>
                        <div class="radio-item">
                            <input type="radio" id="sexMale" name="sex" value="1" ${staff.sex == 1 ? 'checked' : ''} />
                            <label for="sexMale">Male</label>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label for="address" class="form-label">Address</label>
                <textarea id="address" name="address" rows="3" class="form-textarea">${staff.address}</textarea>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">Add Staff</button>
                <a href="${pageContext.request.contextPath}/admin/staff/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
