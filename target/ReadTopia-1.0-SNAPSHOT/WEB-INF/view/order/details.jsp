<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%@ page import="dao.BookDAO, dto.BookDTO, dto.OrderDetailDTO, dto.OrderDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<%
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    DecimalFormat formatter = new DecimalFormat("###,###", symbols);
    request.setAttribute("formatter", formatter);

    BookDAO bookDAO = new BookDAO();
    double totalAmount = 0;
    OrderDTO currentOrder = (OrderDTO) request.getAttribute("order");
%>

<div class="main-content">
    <div class="content-area">
        <div class="container-fluid py-4">
            <div class="row justify-content-center">
                <div class="col-12 col-xl-10">
                    <div class="card shadow-lg border-0">
                        <div class="card-header bg-gradient-primary text-white">
                            <h2 class="mb-0"><i class="bi bi-receipt me-2"></i>Order Details</h2>
                        </div>

                        <% if (currentOrder != null) { %>
                        <div class="card-body border-bottom bg-light">
                            <div class="row">
                                <div class="col-md-6">
                                    <h4 class="fw-bold">Customer Info</h4>
                                    <p><strong>Username:</strong> <%= currentOrder.getUsername() %></p>
                                    <p><strong>Shipping Address:</strong> <%= currentOrder.getOrderAddress() != null ? currentOrder.getOrderAddress() : "Not provided" %></p>
                                </div>
                                <div class="col-md-6">
                                    <h4 class="fw-bold">Order Info</h4>
                                    <p><strong>Order Date:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(currentOrder.getOrderDate()) %></p>
                                    <%
                                        int status = currentOrder.getOrderStatus();
                                        String statusClass = "";
                                        String statusText = "";
                                        switch (status) {
                                            case 0: statusClass = "warning"; statusText = "Processing"; break;
                                            case 1: statusClass = "info"; statusText = "Delivering"; break;
                                            case 2: statusClass = "success"; statusText = "Delivered"; break;
                                            case 3: statusClass = "danger"; statusText = "Cancelled"; break;
                                            case 4: statusClass = "secondary"; statusText = "Returned"; break;
                                            case 5: statusClass = "primary"; statusText = "Payment"; break;
                                            default: statusClass = "light text-dark"; statusText = "Unknown"; break;
                                        }
                                    %>
                                    <p><strong>Status:</strong> <span class="badge bg-<%= statusClass %>"><%= statusText %></span></p>
                                </div>
                            </div>
                        </div>
                        <% } %>

                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead class="table-dark">
                                        <tr>
                                            <th class="ps-4">#</th>
                                            <th>Product</th>
                                            <th class="text-center">Quantity</th>
                                            <th class="text-end">Unit Price</th>
                                            <th class="text-end pe-4">Subtotal</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="od" items="${orderDetails}" varStatus="loop">
                                            <%
                                                OrderDetailDTO od = (OrderDetailDTO) pageContext.getAttribute("od");
                                                BookDTO book = bookDAO.getBookByID(od.getBookID());
                                                totalAmount += od.getTotalPrice();
                                            %>
                                            <tr>
                                                <td class="ps-4 align-middle">${loop.index + 1}</td>
                                                <td class="py-3">
                                                    <div class="d-flex align-items-center">
                                                        <% if (book != null) { %>
                                                        <img src="<%= book.getImage() %>" class="rounded shadow-sm me-3" width="60" height="80"
                                                             onerror="this.src='https://via.placeholder.com/60x80?text=No+Image'">
                                                        <div>
                                                            <div class="fw-bold text-primary"><%= book.getBookTitle() %></div>
                                                            <small class="text-muted">Author: <%= book.getAuthor() %></small><br>
                                                            <small class="text-muted">ID: ${od.bookID}</small>
                                                        </div>
                                                        <% } else { %>
                                                        <div class="text-danger">Product not found (ID: ${od.bookID})</div>
                                                        <% } %>
                                                    </div>
                                                </td>
                                                <td class="text-center align-middle">
                                                    <span class="badge bg-primary fs-6">${od.quantity}</span>
                                                </td>
                                                <td class="text-end align-middle">
                                                    <% if (book != null) { %>
                                                        <%= formatter.format(book.getBookPrice()) %> VND
                                                    <% } else { %>
                                                        N/A
                                                    <% } %>
                                                </td>
                                                <td class="text-end align-middle pe-4">
                                                    <strong class="text-success fs-6">
                                                        <%= formatter.format(od.getTotalPrice()) %> VND
                                                    </strong>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <!-- Total row -->
                                        <tr class="bg-light fw-bold">
                                            <td colspan="4" class="text-end pe-4">Total Amount:</td>
                                            <td class="text-end pe-4 text-success"><%= formatter.format(totalAmount) %> VND</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- ✅ Footer with Back Button placed correctly -->
                        <div class="card-footer text-end">
                            <a href="/ReadTopia/order/management" class="btn btn-secondary">
                        ⬅ Back to Order Management
                    </a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
