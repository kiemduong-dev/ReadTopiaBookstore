<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Staff Management</h1>
        </div>

        <!-- Nút thêm nhân viên mới -->
        <button class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/admin/staff/add'">
            <i class="fas fa-plus"></i> Add Staff
        </button>

        <!-- Thanh tìm kiếm -->
        <div class="toolbar" style="margin: 15px 0;">
            <input type="text" id="staffSearch" class="search-box" placeholder="Search by staff ID or name" onkeyup="filterStaffTable()" />
        </div>

        <div class="table-container">
            <table class="table" id="staffTable">
                <thead>
                    <tr>
                        <th>Staff ID</th>
                        <th>Username</th>
                        <th>Full Name</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="staff" items="${staffs}">
                        <tr>
                            <td><c:out value="${staff.staffID}" /></td>
                            <td><c:out value="${staff.username}" /></td>
                            <td><c:out value="${staff.firstName}" /> <c:out value="${staff.lastName}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${staff.role == 0}">Admin</c:when>
                                    <c:when test="${staff.role == 1}">Staff</c:when>
                                    <c:when test="${staff.role == 2}">Seller Staff</c:when>
                                    <c:when test="${staff.role == 3}">Warehouse Staff</c:when>
                                    <c:otherwise>Unknown</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/staff/detail?staffID=${staff.staffID}" class="btn btn-icon btn-info" title="View">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/staff/edit?staffID=${staff.staffID}" class="btn btn-icon btn-warning" title="Edit">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/staff/delete?id=${staff.staffID}" 
                                   class="btn btn-icon btn-danger" 
                                   title="Delete" 
                                   onclick="return confirm('Do you really want to delete staff: ${staff.firstName} ${staff.lastName}?');">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

