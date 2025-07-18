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
        <form action="${pageContext.request.contextPath}/admin/promotion/list" method="get" class="toolbar" style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">

            <!-- Ô tìm kiếm + nút search nằm cạnh nhau -->
            <div style="display: flex; align-items: center; gap: 5px;">
                <input type="text" name="search" value="${search}" class="search-box" placeholder="Search by name or code" />
                <button type="submit" class="btn btn-primary">Search</button>
            </div>

            <!-- Dropdown status -->
            <select name="status" class="form-select" onchange="this.form.submit()" style="width: 150px;">
                <c:set var="statusVal" value="${status != null ? status : -1}" />
                <option value="-1" <c:if test="${statusVal == -1}">selected</c:if>>All</option>
                <option value="1" <c:if test="${statusVal == 1}">selected</c:if>>Active</option>
                <option value="0" <c:if test="${statusVal == 0}">selected</c:if>>Inactive</option>
                </select>

                <!-- Các nút khác -->
                <a href="${pageContext.request.contextPath}/admin/promotion/add" class="btn btn-primary">Add New</a>
            <a href="${pageContext.request.contextPath}/admin/promotion/logs" class="btn btn-secondary">Log</a>
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
                            <td>
                                <c:choose>
                                    <c:when test="${p.discount % 1 == 0}">
                                        ${p.discount.intValue()}%
                                    </c:when>
                                    <c:otherwise>
                                        ${p.discount}%
                                    </c:otherwise>
                                </c:choose>
                            </td>

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

                                <!-- Chỉ admin mới thấy nút này, và chỉ khi promotion chưa duyệt -->
                                <c:if test="${sessionScope.role == 0 && p.proStatus == 0}">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/promotion/approve?proID=${p.proID}" style="display:inline">
                                        <button type="submit" class="btn btn-primary btn-sm" title="Approve">
                                            Approve
                                        </button>
                                    </form>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/promotion/decline?proID=${p.proID}" style="display:inline">
                                        <button type="submit" class="btn btn-danger btn-sm" title="Decline">
                                            Decline
                                        </button>
                                    </form>
                                </c:if>
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
