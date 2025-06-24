<%-- 
    Document   : list
    Created on : Jun 17, 2025, 8:37:34 AM
    Author     : ngtua
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<style>
    .btn {
        padding: 8px 16px;
        border: none;
        border-radius: 6px;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }

    .btn-primary {
        background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
        color: white;
    }

    .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(76,175,80,0.3);
    }

    .btn-secondary {
        background: #f5f5f5;
        color: #666;
    }

    .btn-danger {
        background: #f44336;
        color: white;
    }

    .btn-icon {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
    }

    .btn-icon.btn-info {
        background: #2196f3;
        color: white;
    }

    .btn-icon.btn-warning {
        background: #ff9800;
        color: white;
    }

    .btn-icon.btn-danger {
        background: #f44336;
        color: white;
    }
</style>

<div class="container mt-5">
    <h2 class="text-center mb-4">Promotion List</h2>

    <!-- Search and Add Form -->
    <form action="${pageContext.request.contextPath}/admin/promotion/list" method="get" class="row mb-4 g-2">
        <div class="col-md-8">
            <input type="text" name="search" value="${search}" class="form-control" placeholder="Search by name or code">
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-primary w-100">Search</button>
        </div>
        <div class="col-auto ms-auto d-flex gap-2">
            <a href="${pageContext.request.contextPath}/admin/promotion/add" class="btn btn-success w-100">Add New</a>
            <a href="${pageContext.request.contextPath}/admin/promotion/logs" class="btn btn-secondary w-100">Log</a>

        </div>
    </form>

    <!-- Wrapper để cố định chiều cao -->
    <div class="table-pagination-wrapper" style="min-height: 400px; position: relative;">

        <!-- Table -->
        <div class="table-responsive">
            <table class="table table-bordered table-striped align-middle mb-0">
                <thead class="table-dark">
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
                                <span class="badge ${p.proStatus == 1 ? 'bg-success' : 'bg-secondary'}">
                                    ${p.proStatus == 1 ? 'Active' : 'Inactive'}
                                </span>
                            </td>
                            <td>
                                <div class="d-flex gap-2">
                                    <a href="${pageContext.request.contextPath}/admin/promotion/detail?proID=${p.proID}" 
                                       class="btn-icon btn-info" 
                                       title="Detail">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/promotion/edit?proID=${p.proID}" 
                                       class="btn-icon btn-warning" 
                                       title="Edit">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <button type="button"
                                            class="btn-icon btn-danger"
                                            title="Delete"
                                            onclick="showDeleteModal(${p.proID})">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Pagination fixed to bottom-left inside wrapper -->
        <div style="position: absolute; bottom: 0; left: 0;" class="mt-4">
            <nav aria-label="Page navigation">
                <ul class="pagination mb-0">
                    <!-- Previous button -->
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/promotion/list?page=${currentPage - 1}&keyword=${keyword}">&lt;</a>
                    </li>

                    <!-- Page numbers -->
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/admin/promotion/list?page=${i}&keyword=${keyword}">${i}</a>
                        </li>
                    </c:forEach>

                    <!-- Next button -->
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/promotion/list?page=${currentPage + 1}&keyword=${keyword}">&gt;</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>

    <!-- ✅ Dialog xác nhận xóa -->
    <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content p-3">
                <div class="modal-body text-center">
                    <div class="text-warning fs-4 mb-2">
                        <i class="bi bi-exclamation-circle"></i>
                        <strong>Confirm Deletion</strong>
                    </div>
                    <p>Are you sure you want to delete promotion "<span id="promotionIdText"></span>"?</p>
                    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/promotion/delete">
                        <input type="hidden" name="proID" id="deleteProID" />
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>
                        <button type="submit" class="btn btn-danger">Yes</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- ✅ JavaScript để gọi modal -->
    <script>
        function showDeleteModal(proID) {
            document.getElementById("promotionIdText").innerText = proID;
            document.getElementById("deleteProID").value = proID;
            var deleteModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
            deleteModal.show();
        }
    </script>


