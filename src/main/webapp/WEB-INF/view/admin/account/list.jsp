<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">

        <!-- Thông báo nếu có -->
        <c:if test="${not empty sessionScope.message}">
            <div class="success-message">
                <i class="fas fa-check-circle"></i>
                <span>${sessionScope.message}</span>
            </div>
            <c:remove var="message" scope="session" />
        </c:if>

        <!-- Tiêu đề trang -->
        <div class="page-header">
            <h1 class="page-title">📘 Account Management</h1>
        </div>

        <!-- Card chứa toolbar và bảng -->
        <div class="card">
            <!-- Thanh công cụ -->
            <div class="toolbar" style="display:flex; gap:10px; align-items:center;">
                <a href="${pageContext.request.contextPath}/admin/account/add" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Add Account
                </a>

                <!-- Form tìm kiếm gửi GET -->
                <form method="get" action="${pageContext.request.contextPath}/admin/account/list" style="display:flex; gap:5px; align-items:center; margin:0;">
                    <input 
                        type="text" 
                        name="keyword" 
                        value="${keyword}" 
                        placeholder="Search by username or full name" 
                        class="search-box" 
                        autocomplete="off"
                    />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <!-- Bảng danh sách tài khoản -->
            <div class="table-container" style="margin-top:15px;">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Sex</th>
                            <th>Role</th>
                            <th>Email</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="accountTableBody">
                        <c:forEach var="acc" items="${accounts}">
                            <tr>
                                <td>${acc.username}</td>
                                <td>${acc.firstName} ${acc.lastName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${acc.sex == 1}">Male</c:when>
                                        <c:otherwise>Female</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${acc.role == 0}">Admin</c:when>
                                        <c:when test="${acc.role == 1}">Customer</c:when>
                                        <c:when test="${acc.role == 2}">Staff</c:when>
                                        <c:otherwise>Unknown</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${acc.email}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/account/detail?username=${acc.username}" class="btn btn-icon btn-info" title="View">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/account/edit?username=${acc.username}" class="btn btn-icon btn-warning" title="Edit">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a href="javascript:void(0);" onclick="confirmDelete('${acc.username}')" class="btn btn-icon btn-danger" title="Delete">
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
</div>

<!-- Modal xác nhận xóa -->
<div id="deleteConfirmModal" class="modal">
    <div class="modal-content confirmation-dialog">
        <div class="modal-header">
            <h2>Confirm Delete</h2>
            <button class="close" onclick="closeModal('deleteConfirmModal')">&times;</button>
        </div>
        <div class="modal-body">
            <p id="deleteMessage">Do you want to delete this account?</p>
            <div class="btn-group">
                <button class="btn btn-secondary" onclick="closeModal('deleteConfirmModal')">Cancel</button>
                <button class="btn btn-danger" onclick="performDelete()">Delete</button>
            </div>
        </div>
    </div>
</div>
