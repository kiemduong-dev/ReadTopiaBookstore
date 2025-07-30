<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<%
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    DecimalFormat moneyFormatter = new DecimalFormat("###,###", symbols);
    request.setAttribute("moneyFormatter", moneyFormatter);
%>

<div class="container-fluid py-4">
    <div class="card shadow">
        <div class="card-header text-white" style="background-color: #0d84e9;">
            <div class="d-flex justify-content-between align-items-center">
                <h2 class="mb-0">Your Order History</h2>
                <a href="${pageContext.request.contextPath}/cart/view" class="btn btn-light btn-sm">
                    Back to Cart
                </a>
            </div>
        </div>

        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>Order #</th>
                            <th>Date</th>
                            <th>Items</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td class="fw-bold">${order.orderID}</td>
                                <td>
                                    <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.itemCount != null && order.itemCount > 1}">
                                            ${order.itemCount} items
                                        </c:when>
                                        <c:otherwise>
                                            ${order.itemCount} item
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="fw-bold">
                                    ${moneyFormatter.format(order.totalAmount)} VND
                                </td>
                                <td>
                                    <span class="badge
                                        ${order.orderStatus == 0 ? 'bg-warning text-dark' :
                                          order.orderStatus == 1 ? 'bg-info text-dark' :
                                          order.orderStatus == 2 ? 'bg-success' :
                                          order.orderStatus == 3 ? 'bg-danger' :
                                          order.orderStatus == 4 ? 'bg-secondary' :
                                          order.orderStatus == 5 ? 'bg-primary' :
                                          'bg-light text-dark'}">
                                        ${order.orderStatus == 0 ? 'Processing' :
                                          order.orderStatus == 1 ? 'Delivering' :
                                          order.orderStatus == 2 ? 'Delivered' :
                                          order.orderStatus == 3 ? 'Cancelled' :
                                          order.orderStatus == 4 ? 'Returned' :
                                          order.orderStatus == 5 ? 'Payment' :
                                          'Unknown'}
                                    </span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/order/customerdetails?orderID=${order.orderID}" 
                                       class="btn btn-sm btn-outline-primary">
                                        Details
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <c:if test="${empty orders}">
                <div class="text-center py-5">
                    <h4 class="mt-3">No orders found</h4>
                    <p class="text-muted">You haven't placed any orders yet.</p>
                    <a href="${pageContext.request.contextPath}/customer/book/list" class="btn mt-2" style="background-color: #0d84e9; color: white;">
                        Start Shopping
                    </a>
                </div>
            </c:if>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav class="mt-4">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage - 1}">Previous</a>
                        </li>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}">${i}</a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage + 1}">Next</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<style>
    .card {
        border-radius: 10px;
    }
    .table th {
        white-space: nowrap;
    }
    .badge {
        font-size: 0.85em;
        padding: 0.5em 0.75em;
    }
    .pagination .page-item.active .page-link {
        background-color: #0d84e9;
        border-color: #0d6efd;
    }
</style>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
