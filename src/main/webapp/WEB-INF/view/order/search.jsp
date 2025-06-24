<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container-fluid py-4">
    <div class="card shadow">
        <div class="card-header bg-info text-white">
            <div class="d-flex justify-content-between align-items-center">
                <h2 class="mb-0"><i class="bi bi-search me-2"></i>Advanced Order Search</h2>
                <a href="${pageContext.request.contextPath}/admin/order/management" class="btn btn-light btn-sm">
                    <i class="bi bi-arrow-left me-1"></i>Back to Management
                </a>
            </div>
        </div>
        
        <div class="card-body">
            <!-- Search Form -->
            <div class="row mb-4">
                <div class="col-12">
                    <form method="get" action="${pageContext.request.contextPath}/admin/order/search">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="username" class="form-label">
                                    <i class="bi bi-person me-1"></i>Search by Username
                                </label>
                                <input type="text" class="form-control" id="username" name="username" 
                                       placeholder="Enter username" value="${searchUsername}">
                            </div>
                            <div class="col-md-6">
                                <label for="bookID" class="form-label">
                                    <i class="bi bi-book me-1"></i>Search by Book ID
                                </label>
                                <input type="number" class="form-control" id="bookID" name="bookID" 
                                       placeholder="Enter Book ID" value="${bookID}">
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-12 d-flex justify-content-center gap-2">
                                <button type="submit" class="btn btn-info">
                                    <i class="bi bi-search me-1"></i>Search Orders
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/order/search" 
                                   class="btn btn-outline-secondary">
                                    <i class="bi bi-arrow-clockwise me-1"></i>Clear Search
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="bi bi-exclamation-triangle me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Search Results -->
            <c:if test="${not empty orders}">
                <div class="alert alert-info border-0">
                    <i class="bi bi-info-circle me-2"></i>
                    Found <strong>${orders.size()}</strong> order(s) matching your search criteria.
                </div>
                
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th><i class="bi bi-hash me-1"></i>Order ID</th>
                                <th><i class="bi bi-person me-1"></i>Customer</th>
                                <th><i class="bi bi-calendar me-1"></i>Order Date</th>
                                <th><i class="bi bi-flag me-1"></i>Status</th>
                                <th><i class="bi bi-gear me-1"></i>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td class="fw-bold">#${order.orderID}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <i class="bi bi-person-circle me-2 text-muted"></i>
                                            ${order.username}
                                        </div>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.orderStatus == 0}">
                                                <span class="badge bg-warning text-dark">
                                                    <i class="bi bi-clock me-1"></i>Processing
                                                </span>
                                            </c:when>
                                            <c:when test="${order.orderStatus == 1}">
                                                <span class="badge bg-info">
                                                    <i class="bi bi-truck me-1"></i>Delivering
                                                </span>
                                            </c:when>
                                            <c:when test="${order.orderStatus == 2}">
                                                <span class="badge bg-success">
                                                    <i class="bi bi-check-circle me-1"></i>Delivered
                                                </span>
                                            </c:when>
                                            <c:when test="${order.orderStatus == 3}">
                                                <span class="badge bg-danger">
                                                    <i class="bi bi-x-circle me-1"></i>Cancelled
                                                </span>
                                            </c:when>
                                            <c:when test="${order.orderStatus == 4}">
                                                <span class="badge bg-secondary">
                                                    <i class="bi bi-arrow-return-left me-1"></i>Returned
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-light text-dark">Unknown</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <a href="${pageContext.request.contextPath}/admin/order/details?orderID=${order.orderID}" 
                                               class="btn btn-sm btn-outline-info" title="View Details">
                                                <i class="bi bi-eye-fill"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/order/edit?orderID=${order.orderID}" 
                                               class="btn btn-sm btn-outline-primary" title="Edit Status">
                                                <i class="bi bi-pencil-fill"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <!-- No Results Found -->
            <c:if test="${empty orders && (not empty searchUsername || not empty bookID)}">
                <div class="text-center py-5">
                    <i class="bi bi-search display-5 text-muted"></i>
                    <h4 class="mt-3 text-muted">No orders found</h4>
                    <p class="text-muted">Try different search criteria or check your spelling.</p>
                </div>
            </c:if>

            <!-- Initial State -->
            <c:if test="${empty searchUsername && empty bookID}">
                <div class="text-center py-5">
                    <i class="bi bi-search display-5 text-info"></i>
                    <h4 class="mt-3 text-info">Advanced Order Search</h4>
                    <p class="text-muted">Enter username or book ID above to search for specific orders.</p>
                    <div class="mt-4">
                        <div class="row justify-content-center">
                            <div class="col-md-8">
                                <div class="card bg-light border-0">
                                    <div class="card-body">
                                        <h6 class="card-title">Search Tips:</h6>
                                        <ul class="list-unstyled mb-0 text-start">
                                            <li><i class="bi bi-check text-success me-2"></i>Use exact username for user-specific orders</li>
                                            <li><i class="bi bi-check text-success me-2"></i>Enter Book ID to find orders containing specific books</li>
                                            <li><i class="bi bi-check text-success me-2"></i>Leave fields empty and search to see all orders</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

<style>
    .card {
        border-radius: 10px;
    }
    .form-control:focus {
        border-color: #17a2b8;
        box-shadow: 0 0 0 0.2rem rgba(23, 162, 184, 0.25);
    }
    .btn {
        padding: 10px 20px;
        border-radius: 8px;
    }
    .badge {
        font-size: 0.75em;
        padding: 0.5em 0.75em;
    }
    .alert {
        border-radius: 10px;
    }
</style>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
