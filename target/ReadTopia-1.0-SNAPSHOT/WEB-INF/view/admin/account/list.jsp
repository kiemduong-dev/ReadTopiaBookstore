<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Account Management</h1>

        <c:if test="${not empty sessionScope.message}">
            <div class="success-message">
                <i class="fas fa-check-circle"></i>
                <span>${sessionScope.message}</span>
            </div>
            <c:remove var="message" scope="session" />
        </c:if>

        <div class="card">
            <div class="toolbar" style="display:flex; gap:10px; align-items:center;">
                <a href="${pageContext.request.contextPath}/admin/account/add" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Add Account
                </a>
                <form method="get" action="${pageContext.request.contextPath}/admin/account/search" style="display:flex; gap:5px; align-items:center; margin:0;">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Search by username or full name" class="search-box" autocomplete="off" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <div class="table-container" style="margin-top:15px;">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Sex</th>
                            <th>Role</th>
                            <th>Email</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="accountTableBody">
                        <c:forEach var="acc" items="${accountList}">
                            <tr>
                                <td>${acc.username}</td>
                                <td>${acc.firstName} ${acc.lastName}</td>
                                <td><c:choose><c:when test="${acc.sex == 1}">Male</c:when><c:otherwise>Female</c:otherwise></c:choose></td>
                                <td><c:choose><c:when test="${acc.role == 0}">Admin</c:when><c:when test="${acc.role == 1}">Staff</c:when><c:when test="${acc.role == 2}">Seller Staff</c:when><c:when test="${acc.role == 3}">Warehouse Staff</c:when><c:when test="${acc.role == 4}">Customer</c:when></c:choose></td>
                                <td>${acc.email}</td>
                                <td>${acc.accStatus == 1 ? 'Active' : 'Disabled'}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/account/detail?username=${acc.username}" class="btn btn-icon btn-info" title="View"><i class="fas fa-eye"></i></a>

                                    <c:if test="${acc.role == 1 || acc.role == 4}">
                                        <a href="${pageContext.request.contextPath}/admin/account/edit?username=${acc.username}" class="btn btn-icon btn-warning" title="Edit"><i class="fas fa-edit"></i></a>
                                        <a href="${pageContext.request.contextPath}/admin/account/delete?username=${acc.username}" onclick="return confirm('Are you sure?')" class="btn btn-icon btn-danger" title="Delete"><i class="fas fa-trash"></i></a>
                                        </c:if>
                                </td>

                            </tr>
                        </c:forEach>
                        <c:if test="${empty accountList}">
                            <tr>
                                <td colspan="7" style="text-align:center;">No accounts found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>