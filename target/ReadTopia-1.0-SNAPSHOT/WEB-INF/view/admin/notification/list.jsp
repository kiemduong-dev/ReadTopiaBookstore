<%-- 
    Document   : list
    Created on : May 29, 2025, 7:26:58 AM
    Author     : ngtua
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <div class="page-title">Notification List</div>
            <div class="page-subtitle">All system notifications</div>
        </div>

        <!-- Search and Add Form -->
        <form action="${pageContext.request.contextPath}/admin/notification/list" method="get" class="toolbar">
            <input type="text" name="search" class="search-box" placeholder="Search by title..." value="${param.search}" />
            <div class="btn-group">
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="${pageContext.request.contextPath}/admin/notification/add" class="btn btn-primary">+ Add New</a>
            </div>
        </form>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="success-message" style="background: #fdecea; color: #d32f2f;">
                <i class="bi bi-exclamation-triangle-fill"></i>
                ${error}
            </div>
        </c:if>

        <!-- Notification Table -->
        <div class="card">
            <div class="card-title">Notification Table</div>
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Receiver (ID)</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="noti" items="${notifications}">
                            <tr>
                                <td>${noti.notID}</td>
                                <td>${noti.notTitle}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${noti.receiver == 0}">Admin</c:when>
                                        <c:when test="${noti.receiver == 1}">Customer</c:when>
                                        <c:when test="${noti.receiver == 2}">Seller Staff</c:when>
                                        <c:when test="${noti.receiver == 3}">Warehouse Staff</c:when>
                                        <c:when test="${noti.receiver == 4}">All</c:when>
                                        <c:otherwise>Unknown</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <a href="${pageContext.request.contextPath}/admin/notification/detail?id=${noti.notID}" 
                                           class="btn btn-icon btn-info" title="View">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <button type="button" class="btn btn-icon btn-danger" title="Delete"
                                                onclick="showDeleteModal(${noti.notID}, '${noti.notTitle}')">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty notifications}">
                            <tr>
                                <td colspan="4" class="text-center text-muted">No notifications found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Delete Notification Modal -->
        <div class="modal" id="confirmDeleteModal">
            <div class="modal-content confirmation-dialog">
                <div class="modal-header">
                    <h3>Confirm Deletion</h3>
                    <button class="close" onclick="closeModal()">&times;</button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete notification "<span id="notificationTitleText"></span>"?</p>
                    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/notification/delete">
                        <input type="hidden" name="notID" id="deleteNotID" />
                        <div class="btn-group">
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">No</button>
                            <button type="submit" class="btn btn-danger">Yes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal Script -->
        <script>
            function showDeleteModal(notID, notTitle) {
                document.getElementById("notificationTitleText").innerText = notTitle;
                document.getElementById("deleteNotID").value = notID;
                document.getElementById("confirmDeleteModal").style.display = "block";
            }

            function closeModal() {
                document.getElementById("confirmDeleteModal").style.display = "none";
            }

            window.onclick = function (event) {
                const modal = document.getElementById("confirmDeleteModal");
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            };
        </script>

        <!-- Modal Style -->
        <style>
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.4);
            }
            .modal-content {
                background-color: #fff;
                margin: 15% auto;
                width: 400px;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.2);
                overflow: hidden;
            }
            .modal-header {
                background-color: #f44336;
                color: white;
                padding: 12px 16px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .modal-body {
                padding: 16px;
            }
            .modal-footer, .btn-group {
                text-align: right;
                padding: 12px 16px;
            }
            .close {
                cursor: pointer;
                font-size: 20px;
                border: none;
                background: none;
            }
        </style>
    </div>
</div>

<!-- Font Awesome (for icons) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
