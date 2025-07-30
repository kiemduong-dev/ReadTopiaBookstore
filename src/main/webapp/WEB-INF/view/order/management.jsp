<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Order Management</h1>
        </div>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success alert-dismissible fade show">
                Order status updated successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger alert-dismissible fade show">
                Failed to update order status!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Toolbar -->
        <div class="toolbar mb-4">
            <form action="${pageContext.request.contextPath}/order/management" method="get" class="row g-2 align-items-end flex-wrap">
                <input type="hidden" name="action" value="search" />
                <div class="col-auto">
                    <label for="orderID" class="form-label">Order ID</label>
                    <input type="number" id="orderID" name="orderID" class="form-control" value="${param.orderID}" placeholder="Enter Order ID" />
                </div>
                <div class="col-auto">
                    <label for="startDate" class="form-label">Start Date</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fas fa-calendar-alt"></i></span>
                        <input type="date" id="startDate" name="startDate" class="form-control" value="${param.startDate}" />
                    </div>
                </div>
                <div class="col-auto">
                    <label for="endDate" class="form-label">End Date</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fas fa-calendar-alt"></i></span>
                        <input type="date" id="endDate" name="endDate" class="form-control" value="${param.endDate}" />
                    </div>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <a href="${pageContext.request.contextPath}/order/management" class="btn btn-outline-secondary">Reset</a>
                </div>
            </form>
        </div>

        <!-- Paging logic -->
        <c:set var="pageSize" value="10" />
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
        <c:set var="totalOrders" value="${fn:length(orders)}" />
        <c:set var="totalPages" value="${(totalOrders + pageSize - 1) / pageSize}" />
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}" />
        <c:set var="endIndex" value="${currentPage * pageSize > totalOrders ? totalOrders : currentPage * pageSize}" />

        <!-- Table -->
        <c:if test="${not empty orders}">
            <div class="table-container">
                <table class="table table-bordered align-middle text-center">
                    <thead class="table-light">
                        <tr>
                            <th>Order ID</th>
                            <th>Customer</th>
                            <th>Order Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" begin="${startIndex}" end="${endIndex - 1}" items="${orders}">
                            <tr>
                                <td class="fw-bold">#${order.orderID}</td>
                                <td>${order.username}</td>
                                <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.orderStatus == 0}"><span class="status-badge processing">Processing</span></c:when>
                                        <c:when test="${order.orderStatus == 1}"><span class="status-badge delivering">Delivering</span></c:when>
                                        <c:when test="${order.orderStatus == 2}"><span class="status-badge delivered">Delivered</span></c:when>
                                        <c:when test="${order.orderStatus == 3}"><span class="status-badge cancelled">Cancelled</span></c:when>
                                        <c:when test="${order.orderStatus == 4}"><span class="status-badge returned">Returned</span></c:when>
                                        <c:when test="${order.orderStatus == 5}"><span class="status-badge payment">Payment</span></c:when>
                                        <c:otherwise><span class="status-badge unknown">Unknown</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/order/details?orderID=${order.orderID}" class="action-btn btn-view"><i class="fas fa-eye"></i></a>
                                    <a href="${pageContext.request.contextPath}/order/edit?orderID=${order.orderID}" class="action-btn btn-edit"><i class="fas fa-edit"></i></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="d-flex justify-content-between align-items-center mt-4 flex-wrap gap-3">
                    <div><span class="text-muted">${startIndex + 1}–${endIndex} of ${totalOrders} items</span></div>

                    <nav>
                        <ul class="pagination mb-0">
                            <!-- Previous -->
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage - 1}">&lt;</a>
                            </li>

                            <!-- Smart pagination -->
                            <c:set var="dotBefore" value="false" />
                            <c:set var="dotAfter" value="false" />
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <c:choose>
                                   
                                    <c:when test="${i <= 3 || i > totalPages - 3 || (i >= currentPage - 1 && i <= currentPage + 1)}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}">${i}</a>
                                        </li>
                                    </c:when>

                                   
                                    <c:when test="${i == 4 && !dotBefore && currentPage > 5}">
                                        <c:set var="dotBefore" value="true" />
                                        <li class="page-item disabled"><span class="page-link">...</span></li>
                                        </c:when>

                                   
                                    <c:when test="${i == totalPages - 3 && !dotAfter && currentPage < totalPages - 4}">
                                        <c:set var="dotAfter" value="true" />
                                        <li class="page-item disabled"><span class="page-link">...</span></li>
                                        </c:when>
                                    </c:choose>
                                </c:forEach>

                            <!-- Next -->
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage + 1}">&gt;</a>
                            </li>
                        </ul>
                    </nav>

                    <!-- Go to page form -->
                    <form method="get" action="${pageContext.request.contextPath}/order/management" class="d-flex align-items-center gap-2">
                        <input type="number" name="page" min="1" max="${totalPages}" class="form-control form-control-sm"
                               style="width: 80px;" placeholder="Page" value="${param.page}" />
                        <c:if test="${not empty param.orderID}">
                            <input type="hidden" name="orderID" value="${param.orderID}" />
                        </c:if>
                        <c:if test="${not empty param.startDate}">
                            <input type="hidden" name="startDate" value="${param.startDate}" />
                        </c:if>
                        <c:if test="${not empty param.endDate}">
                            <input type="hidden" name="endDate" value="${param.endDate}" />
                        </c:if>
                        <c:if test="${not empty param.action}">
                            <input type="hidden" name="action" value="${param.action}" />
                        </c:if>
                        <button class="btn btn-sm btn-outline-secondary" type="submit">Go</button>
                    </form>
                </div>
            </c:if>
        </c:if>

        <c:if test="${empty orders}">
            <div class="alert alert-info text-center py-4">
                <i class="bi bi-inbox fs-1 text-muted"></i>
                <p class="mt-2 mb-0">No orders found. Try another search or come back later.</p>
            </div>
        </c:if>
    </div>
</div>

<style>
    .status-badge {
        display: inline-block;
        padding: 4px 8px;
        border-radius: 5px;
        font-weight: bold;
        font-size: 0.85rem;
    }

    .processing {
        background-color: #fff3cd;
        color: #856404;
    }
    .delivering {
        background-color: #d1ecf1;
        color: #0c5460;
    }
    .delivered  {
        background-color: #d4edda;
        color: #155724;
    }
    .cancelled  {
        background-color: #f8d7da;
        color: #721c24;
    }
    .returned   {
        background-color: #e2e3e5;
        color: #6c757d;
    }
    .payment    {
        background-color: #d1e7dd;
        color: #0f5132;
    }
    .unknown    {
        background-color: #fefefe;
        color: #6c757d;
    }

    .action-btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 36px;
        height: 36px;
        border-radius: 50%;
        color: #fff;
        font-size: 16px;
        text-decoration: none;
        margin: 2px;
    }

    .btn-view {
        background-color: #2196f3;
    }
    .btn-edit {
        background-color: #ff9800;
    }
    .action-btn:hover {
        opacity: 0.85;
    }

    .pagination .page-item .page-link {
        border-radius: 6px;
        margin: 0 2px;
    }

    .pagination .page-item.active .page-link {
        background-color: #0d6efd;
        border-color: #0d6efd;
        color: #fff;
    }

    .pagination .page-item.disabled .page-link {
        color: #ccc;
        pointer-events: none;
        background-color: #f8f9fa;
    }
</style>
