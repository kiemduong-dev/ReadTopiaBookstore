<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Edit Staff</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger fw-bold text-center">${error}</div>
        </c:if>

        <div class="card">
            <form method="post" action="${pageContext.request.contextPath}/admin/staff/edit" class="form-container">
                <input type="hidden" name="staffID" value="${staff.staffID}" />

                <!-- Username (readonly) -->
                <div class="form-group">
                    <label>Username:</label>
                    <input type="text" name="username" value="${staff.username}" readonly class="form-input" />
                </div>

                <!-- First Name -->
                <div class="form-group">
                    <label>First Name:</label>
                    <input type="text" name="firstName" value="${staff.firstName}" required class="form-input" />
                    <c:if test="${not empty fieldErrors['firstName']}">
                        <div class="text-danger">${fieldErrors['firstName']}</div>
                    </c:if>
                </div>

                <!-- Last Name -->
                <div class="form-group">
                    <label>Last Name:</label>
                    <input type="text" name="lastName" value="${staff.lastName}" required class="form-input" />
                    <c:if test="${not empty fieldErrors['lastName']}">
                        <div class="text-danger">${fieldErrors['lastName']}</div>
                    </c:if>
                </div>

                <!-- Gender -->
                <div class="form-group">
                    <label>Sex:</label>
                    <select name="sex" class="form-select" required>
                        <option value="1" <c:if test="${staff.sex == 1}">selected</c:if>>Male</option>
                        <option value="0" <c:if test="${staff.sex == 0}">selected</c:if>>Female</option>
                    </select>
                    <c:if test="${not empty fieldErrors['sex']}">
                        <div class="text-danger">${fieldErrors['sex']}</div>
                    </c:if>
                </div>

                <!-- Role -->
                <div class="form-group">
                    <label>Role:</label>
                    <c:choose>
                        <c:when test="${staff.role == 2 || staff.role == 3}">
                            <select name="role" class="form-select" required>
                                <option value="2" <c:if test="${staff.role == 2}">selected</c:if>>Seller Staff</option>
                                <option value="3" <c:if test="${staff.role == 3}">selected</c:if>>Warehouse Staff</option>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="role" value="1" />
                            <input type="text" value="Staff" readonly class="form-input" />
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty fieldErrors['role']}">
                        <div class="text-danger">${fieldErrors['role']}</div>
                    </c:if>
                </div>

                <!-- Date of Birth -->
                <div class="form-group">
                    <label>Date of Birth:</label>
                    <input type="text" name="dob" value="${dobRaw}" class="form-input" placeholder="dd/MM/yyyy" />
                    <c:if test="${not empty fieldErrors['dob']}">
                        <div class="text-danger">${fieldErrors['dob']}</div>
                    </c:if>
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value="${staff.email}" required class="form-input" />
                    <c:if test="${not empty fieldErrors['email']}">
                        <div class="text-danger">${fieldErrors['email']}</div>
                    </c:if>
                </div>

                <!-- Phone -->
                <div class="form-group">
                    <label>Phone:</label>
                    <input type="text" name="phone" value="${staff.phone}" required class="form-input" />
                    <c:if test="${not empty fieldErrors['phone']}">
                        <div class="text-danger">${fieldErrors['phone']}</div>
                    </c:if>
                </div>

                <!-- Address -->
                <div class="form-group">
                    <label>Address:</label>
                    <input type="text" name="address" value="${staff.address}" required class="form-input" />
                    <c:if test="${not empty fieldErrors['address']}">
                        <div class="text-danger">${fieldErrors['address']}</div>
                    </c:if>
                </div>

                <!-- Button Group -->
                <div class="btn-group mt-4">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/admin/staff/list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>
