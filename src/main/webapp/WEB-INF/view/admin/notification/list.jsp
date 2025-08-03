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
        <c:if test="${not empty sessionScope.successMessage}">
            <div id="successAlert" style="
                 background-color: #d4edda;
                 color: #155724;
                 padding: 10px 20px;
                 border: 1px solid #c3e6cb;
                 border-radius: 5px;
                 margin-bottom: 15px;
                 transition: opacity 0.5s ease-in-out;
                 ">
                <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
            </div>
            <script>             
                setTimeout(function () {
                    const alertBox = document.getElementById('successAlert');
                    if (alertBox) {
                        alertBox.style.opacity = '0';
                        setTimeout(() => alertBox.remove(), 500); 
                    }
                }, 2000);
            </script>
            <c:remove var="successMessage" scope="session" />
        </c:if>

        <!-- Search and Add Form -->
        <form action="${pageContext.request.contextPath}/admin/notification/list" method="get" class="toolbar">
            
            <div style="display: flex; gap: 10px;">
                     <input type="text" name="search" class="search-box" placeholder="Search by title..." value="${param.search}" />
            <button type="submit" class="btn btn-primary">Search</button>
            </div
            
            <div class="btn-group">             
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
                            <th>Receiver</th>
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
                                        <c:when test="${noti.receiver == 2}">Seller Staff</c:when>
                                        <c:when test="${noti.receiver == 3}">Warehouse Staff</c:when>
                                        <c:when test="${noti.receiver == 4}">Customer</c:when>
                                        <c:when test="${noti.receiver == 5}">All</c:when>
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

        <div class="pagination-wrapper">
            <ul class="pagination">
                <li>
                    <a class="page-btn ${currentPage == 1 ? 'disabled' : ''}"
                       href="?page=${currentPage - 1}&search=${search}">
                        &laquo;
                    </a>
                </li>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li>
                        <a class="page-btn ${i == currentPage ? 'active' : ''}"
                           href="?page=${i}&search=${search}">
                            ${i}
                        </a>
                    </li>
                </c:forEach>

                <li>
                    <a class="page-btn ${currentPage == totalPages ? 'disabled' : ''}"
                       href="?page=${currentPage + 1}&search=${search}">
                        &raquo;
                    </a>
                </li>
            </ul>
        </div>

        <!-- Custom CSS Modal -->
        <div id="confirmDeleteModal" class="custom-modal">
            <div class="custom-modal-content">
                <div class="custom-modal-header">
                    <h5>Confirm Deletion</h5>
                    <span class="close-button" onclick="hideDeleteModal()">&times;</span>
                </div>
                <div class="custom-modal-body">
                    Are you sure you want to delete notification "<span id="notificationTitleText"></span>"?
                    <input type="hidden" name="notID" id="deleteNotID" />
                </div>
                <div class="custom-modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="hideDeleteModal()">No</button>
                    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/notification/delete" style="display:inline;">
                        <input type="hidden" name="notID" id="hiddenDeleteID" />
                        <button type="submit" class="btn btn-danger">Yes</button>
                    </form>
                </div>
            </div>
        </div>



        <script>
            function showDeleteModal(notID, notTitle) {
                document.getElementById("notificationTitleText").innerText = notTitle;
                document.getElementById("hiddenDeleteID").value = notID;
                document.getElementById("confirmDeleteModal").style.display = "block";
            }

            function hideDeleteModal() {
                document.getElementById("confirmDeleteModal").style.display = "none";
            }

            // Optional: Close modal when clicking outside
            window.onclick = function (event) {
                const modal = document.getElementById("confirmDeleteModal");
                if (event.target == modal) {
                    hideDeleteModal();
                }
            }
        </script>

    </div>
</div>

<!-- Font Awesome (for icons) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
