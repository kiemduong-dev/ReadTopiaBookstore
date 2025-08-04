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
        <div class="page-header" style="padding-bottom: 30px">
            <h2 class="page-title text-center">Voucher List</h2>
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
                // Ẩn sau 2 giây (2000ms)
                setTimeout(function () {
                    const alertBox = document.getElementById('successAlert');
                    if (alertBox) {
                        alertBox.style.opacity = '0';
                        setTimeout(() => alertBox.remove(), 500); // Xóa hẳn sau khi hiệu ứng ẩn hoàn tất
                    }
                }, 2000);
            </script>
            <c:remove var="successMessage" scope="session" />
        </c:if>
        <form action="${pageContext.request.contextPath}/admin/voucher/list" method="get" class="toolbar" 
              style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">

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

                <div style="display: flex; gap: 10px; padding-right: 30px">
                    <a href="${pageContext.request.contextPath}/admin/voucher/add" class="btn btn-primary">Add New</a>
                <a href="${pageContext.request.contextPath}/admin/voucher/logs" class="btn btn-primary">Log</a>
</div>
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
                                <a href="${pageContext.request.contextPath}/admin/voucher/detail?vouID=${v.vouID}" 
                                   class="btn btn-icon btn-info" title="View">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/voucher/edit?vouID=${v.vouID}" 
                                   class="btn btn-icon btn-warning" title="Edit">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-icon btn-danger" title="Delete"
                                        onclick="showDeleteModal(${v.vouID}, '${v.vouName}')">
                                    <i class="fas fa-trash-alt"></i>
                                </button>

                                <c:if test="${sessionScope.role == 0 && v.vouStatus == 0}">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/voucher/approve?vouID=${v.vouID}" 
                                          style="display:inline">
<button type="submit" class="btn btn-primary btn-sm" title="Approve">Approve</button>
                                    </form>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/voucher/decline?vouID=${v.vouID}" 
                                          style="display:inline">
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

        <!-- Custom CSS Modal -->
        <div id="confirmDeleteModal" class="custom-modal">
            <div class="custom-modal-content">
                <div class="custom-modal-header">
                    <h5>Confirm Deletion</h5>
                    <span class="close-button" onclick="hideDeleteModal()">&times;</span>
                </div>
                <div class="custom-modal-body">
                    Are you sure you want to delete voucher "<span id="voucherNameText"></span>"?
                    <input type="hidden" name="vouID" id="deleteVouID" />
                </div>
                <div class="custom-modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="hideDeleteModal()">No</button>
                    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/voucher/delete" 
                          style="display:inline;">
                        <input type="hidden" name="vouID" id="hiddenDeleteID" />
                        <button type="submit" class="btn btn-danger">Yes</button>
                    </form>
                </div>
            </div>
        </div>

        <script>
            function showDeleteModal(vouID, vouName) {
document.getElementById("voucherNameText").innerText = vouName;
                document.getElementById("hiddenDeleteID").value = vouID;
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