<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">View Staff Detail</h1>

        <form>
            <div class="form-group">
                <label>Staff ID:</label>
                <input type="text" value="${staff.staffID}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Username:</label>
                <input type="text" value="${staff.username}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>First Name:</label>
                <input type="text" value="${staff.firstName}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" value="${staff.lastName}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Sex:</label>
                <select class="form-select" disabled>
                    <option value="1" <c:if test="${staff.sex == 1}">selected</c:if>>Male</option>
                    <option value="0" <c:if test="${staff.sex == 0}">selected</c:if>>Female</option>
                </select>
            </div>

            <div class="form-group">
                <label>Role:</label>
                <select class="form-select" disabled>
                    <option value="1" <c:if test="${staff.role == 1}">selected</c:if>>Staff</option>
                    <option value="2" <c:if test="${staff.role == 2}">selected</c:if>>Seller Staff</option>
                    <option value="3" <c:if test="${staff.role == 3}">selected</c:if>>Warehouse Staff</option>
                </select>
            </div>

            <div class="form-group">
                <label>Date of Birth:</label>
                <input type="text" class="form-input" readonly
                       value="<fmt:formatDate value='${staff.dob}' pattern='dd/MM/yyyy'/>" />
            </div>

            <div class="form-group">
                <label>Email:</label>
                <input type="email" value="${staff.email}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Phone:</label>
                <input type="text" value="${staff.phone}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Address:</label>
                <input type="text" value="${staff.address}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Status:</label>
                <input type="text" class="form-input" readonly
                       value="${staff.accStatus == 1 ? 'Active' : 'Disabled'}" />
            </div>

            <div class="btn-group mt-4">
                <a href="${pageContext.request.contextPath}/admin/staff/list" class="btn btn-secondary">Back to List</a>
            </div>
        </form>
    </div>
</div>
