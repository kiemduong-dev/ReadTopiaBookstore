<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Staff Management</h1>

        <!-- ✅ Success Message -->
        <c:if test="${not empty sessionScope.message}">
            <div class="success-message">
                <i class="fas fa-check-circle"></i>
                <span>${sessionScope.message}</span>
            </div>
            <c:remove var="message" scope="session" />
        </c:if>

        <div class="card">
            <div class="toolbar" style="display:flex; gap:10px; align-items:center;">
                <!-- ✅ Only Staff Manager (role = 1) can Add -->
                <c:if test="${sessionScope.account.role == 1}">
                    <a href="${pageContext.request.contextPath}/admin/staff/add" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Add Staff
                    </a>
                </c:if>

                <!-- 🔍 Search -->
                <form method="get" action="${pageContext.request.contextPath}/admin/staff/search"
                      style="display:flex; gap:5px; align-items:center; margin:0;">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Search by username or full name"
                           class="search-box" autocomplete="off" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <!-- 🧾 Staff Table -->
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
                                        <c:otherwise>Unknown</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${staff.email}</td>
                                <td>
                                    <span class="status-badge ${staff.accStatus == 1 ? 'active' : 'inactive'}">
                                        ${staff.accStatus == 1 ? 'Active' : 'Disabled'}
                                    </span>
                                </td>
                                <td>
                                    <!-- 👁 View: all roles -->
                                    <a href="${pageContext.request.contextPath}/admin/staff/detail?staffID=${staff.staffID}"
                                       class="btn btn-icon btn-info" title="View">
                                        <i class="fas fa-eye"></i>
                                    </a>

                                    <!-- ✏️🗑️ Edit/Delete: only for Staff Manager -->
                                    <c:if test="${sessionScope.account.role == 1}">
                                        <!-- ✏️ Edit nếu là chính mình hoặc role 2/3 -->
                                        <c:if test="${staff.username == sessionScope.account.username || staff.role == 2 || staff.role == 3}">
                                            <a href="${pageContext.request.contextPath}/admin/staff/edit?staffID=${staff.staffID}"
                                               class="btn btn-icon btn-warning" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                        </c:if>

                                        <!-- 🗑️ Delete nếu không phải chính mình và role 2/3 -->
                                        <c:if test="${(staff.role == 2 || staff.role == 3) && staff.username != sessionScope.account.username}">
                                            <a href="${pageContext.request.contextPath}/admin/staff/delete?id=${staff.staffID}"
                                               onclick="return confirm('Do you really want to delete ${staff.firstName} ${staff.lastName}?');"
                                               class="btn btn-icon btn-danger" title="Delete">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>

                        <!-- 💤 Nếu không có staff -->
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
