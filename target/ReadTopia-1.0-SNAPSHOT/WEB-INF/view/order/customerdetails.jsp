<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.BookDAO, dto.BookDTO, dto.OrderDetailDTO, dto.OrderDTO" %>
<%@ page import="java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<%
    BookDAO bookDAO = new BookDAO();
    OrderDTO order = (OrderDTO) request.getAttribute("order");

    // Format tiền: dấu cách ngăn cách hàng nghìn
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    DecimalFormat formatter = new DecimalFormat("###,###", symbols);
    request.setAttribute("formatter", formatter);
%>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <div>
                <h5 class="mb-0">Order Details</h5>
                <small>Order #<%= order.getOrderID() %> -
                    <fmt:formatDate value="<%= order.getOrderDate() %>" pattern="dd/MM/yyyy HH:mm" />
                </small>
            </div>
            <a href="${pageContext.request.contextPath}/order/history" class="btn btn-light btn-sm">Back to History</a>
        </div>
        <div class="card-body">
            <!-- Status -->
            <h6><i class="bi bi-info-circle"></i> Status:
                <%
                    int status = order.getOrderStatus();
                    String statusText = "", statusClass = "";
                    switch (status) {
                        case 0: statusText = "Processing"; statusClass = "warning"; break;
                        case 1: statusText = "Shipping"; statusClass = "info"; break;
                        case 2: statusText = "Delivered"; statusClass = "success"; break;
                        case 3: statusText = "Cancelled"; statusClass = "danger"; break;
                        case 4: statusText = "Returned"; statusClass = "secondary"; break;
                        case 5: statusText = "Payment"; statusClass = "primary"; break;
                        default: statusText = "Unknown"; statusClass = "dark"; break;
                    }
                %>
                <span class="badge bg-<%= statusClass %>"><%= statusText %></span>
            </h6>
            <p><strong>Shipping Address:</strong> <%= order.getOrderAddress() != null ? order.getOrderAddress() : "Not provided" %></p>

            <!-- ✅ Action Buttons -->
            <div class="mb-3">
                <form method="post" action="${pageContext.request.contextPath}/order/update-status" onsubmit="return confirmAction(this);">
                    <input type="hidden" name="orderID" value="${order.orderID}" />
                    <c:choose>
                        <c:when test="${order.orderStatus == 0 || order.orderStatus == 5}">
                            <input type="hidden" name="newStatus" value="3" />
                            <button type="submit" class="btn btn-danger">
                                <i class="bi bi-x-circle me-1"></i> Cancel Order
                            </button>
                        </c:when>
                        <c:when test="${order.orderStatus == 2}">
                            <input type="hidden" name="newStatus" value="4" />
                            <button type="submit" class="btn btn-warning">
                                <i class="bi bi-arrow-return-left me-1"></i> Return Order
                            </button>
                        </c:when>
                    </c:choose>
                </form>
            </div>

            <!-- Product Table -->
            <div class="table-responsive mt-3">
                <table class="table table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>#</th>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Unit Price</th>
                            <th>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${orderDetails}" varStatus="loop">
                            <%
                                OrderDetailDTO detail = (OrderDetailDTO) pageContext.getAttribute("detail");
                                BookDTO book = bookDAO.getBookByID(detail.getBookID());
                            %>
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>
                                    <% if (book != null) { %>
                                        <div><strong><%= book.getBookTitle() %></strong></div>
                                        <small class="text-muted">Author: <%= book.getAuthor() %></small>
                                    <% } else { %>
                                        <span class="text-danger">Book not found</span>
                                    <% } %>
                                </td>
                                <td>${detail.quantity}</td>
                                <td>
                                    <% if (book != null) { %>
                                        <%= formatter.format(book.getBookPrice()) %> VND
                                    <% } else { %>
                                        N/A
                                    <% } %>
                                </td>
                                <td>
                                    <%= formatter.format(detail.getTotalPrice()) %> VND
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Total Amount -->
            <div class="text-end mt-3">
                <h5>Total:
                    <%= formatter.format(order.getTotalAmount()) %> VND
                </h5>
            </div>
        </div>
    </div>
</div>

<!-- ✅ Xác nhận khi hủy/hoàn trả đơn -->
<script>
    function confirmAction(form) {
        const newStatus = form.querySelector('input[name="newStatus"]').value;
        let message = "";

        if (newStatus === "3") {
            message = "Are you sure you want to cancel this order?";
        } else if (newStatus === "4") {
            message = "Are you sure you want to return this order?";
        } else {
            return true;
        }

        return confirm(message);
    }
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
