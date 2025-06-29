<%-- 
    Document   : list
    Created on : Jun 17, 2025, 8:37:34 AM
    Author     : ngtua
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %> 
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h2 class="page-title text-center">Promotion List</h2>
        </div>

        <!-- Search and Action Bar -->
        <form action="${pageContext.request.contextPath}/admin/promotion/list" method="get" class="toolbar">
            <input type="text" name="search" value="${search}" class="search-box" placeholder="Search by name or code" />

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="${pageContext.request.contextPath}/admin/promotion/add" class="btn btn-primary">Add New</a>
                <a href="${pageContext.request.contextPath}/admin/promotion/logs" class="btn btn-secondary">Log</a>
            </div>
        </form>

        <!-- Table -->
        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Code</th>
                        <th>Discount (%)</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Quantity</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${promotionList}">
                        <tr>
                            <td>${p.proID}</td>
                            <td>${p.proName}</td>
                            <td>${p.proCode}</td>
                            <td>${p.discount}%</td>
                            <td>${p.startDate}</td>
                            <td>${p.endDate}</td>
                            <td>${p.quantity}</td>
                            <td>
                                <span class="status-badge ${p.proStatus == 1 ? 'active' : 'inactive'}">
                                    ${p.proStatus == 1 ? 'Active' : 'Inactive'}
                                </span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/promotion/detail?proID=${p.proID}" 
                                   class="btn btn-icon btn-info" title="View">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/promotion/edit?proID=${p.proID}" 
                                   class="btn btn-icon btn-warning" title="Edit">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" 
                                        class="btn btn-icon btn-danger" 
                                        onclick="showDeleteModal(${p.proID})" 
                                        title="Delete">
                                    <i class="fas fa-trash-alt"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Custom Modal -->
        <div class="modal" id="confirmDeleteModal">
            <div class="modal-content confirmation-dialog">
                <div class="modal-header">
                    <h3>Confirm Deletion</h3>
                    <button class="close" onclick="closeModal()">&times;</button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete promotion "<span id="promotionIdText"></span>"?</p>
                    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/promotion/delete">
                        <input type="hidden" name="proID" id="deleteProID" />
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
            function showDeleteModal(proID) {
                document.getElementById("promotionIdText").innerText = proID;
                document.getElementById("deleteProID").value = proID;
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
    </div>
</div>
