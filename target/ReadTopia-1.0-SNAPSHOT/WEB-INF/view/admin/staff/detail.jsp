<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Staff Details</h1>
        </div>

        <div class="card">
            <dl class="staff-detail-list">
                <dt>Staff ID:</dt>
                <dd>${staff.staffID}</dd>

                <dt>Username:</dt>
                <dd>${staff.username}</dd>

                <dt>First Name:</dt>
                <dd>${staff.firstName}</dd>

                <dt>Last Name:</dt>
                <dd>${staff.lastName}</dd>

                <dt>Date of Birth:</dt>
                <dd>
                    <c:choose>
                        <c:when test="${not empty staff.dob}">
                            <fmt:formatDate value="${staff.dob}" pattern="dd/MM/yyyy" />
                        </c:when>
                        <c:otherwise>Not specified</c:otherwise>
                    </c:choose>
                </dd>

                <dt>Email:</dt>
                <dd>${staff.email}</dd>

                <dt>Phone:</dt>
                <dd>${staff.phone}</dd>

                <dt>Sex:</dt>
                <dd>
                    <c:choose>
                        <c:when test="${staff.sex == 1}">Male</c:when>
                        <c:otherwise>Female</c:otherwise>
                    </c:choose>
                </dd>

                <dt>Role:</dt>
                <dd>
                    <c:choose>
                        <c:when test="${staff.role == 0}">Admin</c:when>
                        <c:when test="${staff.role == 1}">Staff</c:when>
                        <c:when test="${staff.role == 2}">Seller Staff</c:when>
                        <c:when test="${staff.role == 3}">Warehouse Staff</c:when>
                        <c:otherwise>Unknown</c:otherwise>
                    </c:choose>
                </dd>

                <dt>Address:</dt>
                <dd>
                    <c:choose>
                        <c:when test="${not empty staff.address}">
                            ${staff.address}
                        </c:when>
                        <c:otherwise>Not specified</c:otherwise>
                    </c:choose>
                </dd>
            </dl>
        </div>

        <a href="${pageContext.request.contextPath}/admin/staff/list" class="btn btn-secondary">Back to list</a>
    </div>
</div>
