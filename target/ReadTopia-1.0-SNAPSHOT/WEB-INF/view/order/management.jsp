<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1>Order Management</h1>

        <!-- Message -->
        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success alert-dismissible fade show">
                ✅ Order status updated successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger alert-dismissible fade show">
                ❌ Failed to update order status!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Search Form -->
        <form method="get" action="${pageContext.request.contextPath}/order/management" class="row g-3 mb-4">
            <input type="hidden" name="action" value="search">
            <div class="col-md-3">
                <label class="form-label">Order ID</label>
                <input type="number" name="orderID" class="form-control" value="${param.orderID}" placeholder="Enter Order ID" />
            </div>
            <div class="col-md-3">
                <label class="form-label">Start Date</label>
                <input type="date" name="startDate" class="form-control" value="${param.startDate}" />
            </div>
            <div class="col-md-3">
                <label class="form-label">End Date</label>
                <input type="date" name="endDate" class="form-control" value="${param.endDate}" />
            </div>
            <div class="col-md-3 d-flex align-items-end">
                <button class="btn btn-primary me-2" type="submit">🔍 Search</button>
                <a href="${pageContext.request.contextPath}/order/management" class="btn btn-outline-secondary">🔄 Reset</a>
            </div>
        </form>

        <!-- Orders Table -->
        <c:if test="${not empty orders}">
            <table class="table table-bordered table-hover align-middle">
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
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td class="fw-bold">#${order.orderID}</td>
                            <td>${order.username}</td>
                            <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.orderStatus == 0}">
                                        <span class="badge bg-warning text-dark">Processing</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == 1}">
                                        <span class="badge bg-info">Delivering</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == 2}">
                                        <span class="badge bg-success">Delivered</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == 3}">
                                        <span class="badge bg-danger">Cancelled</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == 4}">
                                        <span class="badge bg-secondary">Returned</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-dark">Unknown</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/order/details?orderID=${order.orderID}" class="btn btn-sm btn-outline-info" title="View">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/order/edit?orderID=${order.orderID}" class="btn btn-sm btn-outline-primary" title="Edit">
                                    <i class="fas fa-edit"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- No orders -->
        <c:if test="${empty orders}">
            <div class="alert alert-info text-center py-4">
                <i class="bi bi-inbox fs-1 text-muted"></i>
                <p class="mt-2 mb-0">No orders found. Try another search or come back later.</p>
            </div>
        </c:if>
    </div>
</div>
