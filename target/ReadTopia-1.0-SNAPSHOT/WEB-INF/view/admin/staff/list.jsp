<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Staff Management</h1>
        </div>

        <div class="card">
            <div class="toolbar" style="display:flex; gap:10px; align-items:center;">
                <a href="${pageContext.request.contextPath}/admin/staff/add" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Add Staff
                </a>
                <form method="get" action="${pageContext.request.contextPath}/admin/staff/search" style="display:flex; gap:5px; align-items:center; margin:0;">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Search by username or full name" class="search-box" autocomplete="off" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <div class="table-container" style="margin-top:15px;">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Staff ID</th>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Sex</th>
                            <th>Role</th>
                            <th>Email</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="staff" items="${staffs}">
                            <tr>
                                <td>${staff.staffID}</td>
                                <td>${staff.username}</td>
                                <td>${staff.firstName} ${staff.lastName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${staff.sex == 1}">Male</c:when>
                                        <c:otherwise>Female</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${staff.role == 1}">Staff</c:when>
                                        <c:when test="${staff.role == 2}">Seller Staff</c:when>
                                        <c:when test="${staff.role == 3}">Warehouse Staff</c:when>
                                    </c:choose>
                                </td>
                                <td>${staff.email}</td>
                                <td>${staff.accStatus == 1 ? 'Active' : 'Disabled'}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/staff/detail?staffID=${staff.staffID}" class="btn btn-icon btn-info" title="View">
                                        <i class="fas fa-eye"></i>
                                    </a>

                                    <c:if test="${staff.role == 2 || staff.role == 3}">
                                        <a href="${pageContext.request.contextPath}/admin/staff/edit?staffID=${staff.staffID}" class="btn btn-icon btn-warning" title="Edit">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/staff/delete?id=${staff.staffID}" 
                                           onclick="return confirm('Do you really want to delete ${staff.firstName} ${staff.lastName}?');" 
                                           class="btn btn-icon btn-danger" title="Delete">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty staffs}">
                            <tr>
                                <td colspan="8" style="text-align:center;">No staff found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
