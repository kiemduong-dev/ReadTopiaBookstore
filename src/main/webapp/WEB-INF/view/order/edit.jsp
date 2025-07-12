<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="form-container bg-white shadow p-5 rounded mx-auto" style="max-width: 600px; margin-top: 60px;">
            <h2 class="fw-bold text-center mb-4">✏️ Edit Order Status</h2>

            <!-- Alert Messages -->
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

            <!-- Form -->
            <form method="post" action="${pageContext.request.contextPath}/order/edit">
                <input type="hidden" name="orderID" value="${order.orderID}" />

                <div class="mb-3">
                    <label class="form-label">Customer</label>
                    <input type="text" class="form-control" value="${order.username}" readonly />
                </div>

                <div class="mb-3">
                    <label class="form-label">Order Date</label>
                    <input type="text" class="form-control"
                           value="<fmt:formatDate value='${order.orderDate}' pattern='dd/MM/yyyy HH:mm'/>" readonly />
                </div>

                <div class="mb-3">
                    <label class="form-label">Current Status</label><br />
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
                        <c:when test="${order.orderStatus == 5}">
                            <span class="badge bg-primary">Payment</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-light text-dark">Unknown</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="mb-3">
                    <label class="form-label">New Status</label>
                    <select name="newStatus" class="form-select" required>
                        <option value="">-- Select Status --</option>
                        <option value="0">Processing</option>
                        <option value="1">Delivering</option>
                        <option value="2">Delivered</option>
                        <option value="3">Cancelled</option>
                        <option value="4">Returned</option>
                        <option value="5">Payment</option>
                    </select>
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-success px-4 me-2">
                        <i class="fas fa-save"></i> Update
                    </button>
                </div>
            </form>

            <!-- ✅ Back button under the form -->
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/order/management" class="btn btn-secondary">
                    ⬅ Back to Order Management
                </a>
            </div>
        </div>
    </div>
</div>
