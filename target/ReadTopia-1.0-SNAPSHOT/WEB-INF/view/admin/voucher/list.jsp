<%-- 
    Document   : list
    Created on : Jul 21, 2025, 3:48:10 PM
    Author     : default
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pagging.css" />
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h2 class="page-title text-center">Voucher List</h2>
        </div>

        <form action="${pageContext.request.contextPath}/admin/voucher/list" method="get" class="toolbar" style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">
            <div style="display: flex; align-items: center; gap: 5px;">
                <input type="text" name="search" value="${search}" class="search-box" placeholder="Search by name or code" />
                <button type="submit" class="btn btn-primary">Search</button>
            </div>

            <select name="status" class="form-select" onchange="this.form.submit()" style="width: 150px;">
                <c:set var="statusVal" value="${status != null ? status : -1}" />
                <option value="-1" <c:if test="${statusVal == -1}">selected</c:if>>All</option>
                <option value="1" <c:if test="${statusVal == 1}">selected</c:if>>Active</option>
                <option value="0" <c:if test="${statusVal == 0}">selected</c:if>>Inactive</option>
                </select>

                <a href="${pageContext.request.contextPath}/admin/voucher/add" class="btn btn-primary">Add New</a>
            <a href="${pageContext.request.contextPath}/admin/voucher/logs" class="btn btn-secondary">Log</a>
        </form>

        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Code</th>
                        <th>Discount</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Quantity</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${voucherList}">
                        <tr>
                            <td>${v.vouID}</td>
                            <td>${v.vouName}</td>
                            <td>${v.vouCode}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.discount % 1 == 0}">
                                        ${v.discount.intValue()}%
                                    </c:when>
                                    <c:otherwise>
                                        ${v.discount}%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${v.startDate}</td>
                            <td>${v.endDate}</td>
                            <td>${v.quantity}</td>
                            <td>
                                <span class="status-badge ${v.vouStatus == 1 ? 'active' : 'inactive'}">
                                    ${v.vouStatus == 1 ? 'Active' : 'Inactive'}
                                </span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/voucher/detail?vouID=${v.vouID}" class="btn btn-icon btn-info" title="View">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/voucher/edit?vouID=${v.vouID}" class="btn btn-icon btn-warning" title="Edit">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-icon btn-danger" onclick="showDeleteModal(${v.vouID})" title="Delete">
                                    <i class="fas fa-trash-alt"></i>
                                </button>

                                <c:if test="${sessionScope.role == 0 && v.vouStatus == 0}">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/voucher/approve?vouID=${v.vouID}" style="display:inline">
                                        <button type="submit" class="btn btn-primary btn-sm" title="Approve">Approve</button>
                                    </form>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/voucher/decline?vouID=${v.vouID}" style="display:inline">
                                        <button type="submit" class="btn btn-danger btn-sm" title="Decline">Decline</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="pagination-wrapper">
            <ul class="pagination">
                <li>
                    <a class="page-btn ${currentPage == 1 ? 'disabled' : ''}"
                       href="?page=${currentPage - 1}&search=${search}&status=${status}">
                        &lt;
                    </a>
                </li>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li>
                        <a class="page-btn ${i == currentPage ? 'active' : ''}"
                           href="?page=${i}&search=${search}&status=${status}">
                            ${i}
                        </a>
                    </li>
                </c:forEach>

                <li>
                    <a class="page-btn ${currentPage == totalPages ? 'disabled' : ''}"
                       href="?page=${currentPage + 1}&search=${search}&status=${status}">
                        &gt;
                    </a>
                </li>
            </ul>
        </div>

        <!-- Delete Modal -->
        <div class="modal" id="confirmDeleteModal">
            <div class="modal-content confirmation-dialog">
                <div class="modal-header">
                    <h3>Confirm Deletion</h3>
                    <button class="close" onclick="closeModal()">&times;</button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete voucher "<span id="voucherIdText"></span>"?</p>
                    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/voucher/delete">
                        <input type="hidden" name="vouID" id="deleteVouID" />
                        <div class="btn-group">
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">No</button>
                            <button type="submit" class="btn btn-danger">Yes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script>
            function showDeleteModal(vouID) {
                document.getElementById("voucherIdText").innerText = vouID;
                document.getElementById("deleteVouID").value = vouID;
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

