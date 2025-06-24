<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.BookDAO, dto.BookDTO, dto.OrderDetailDTO, dto.OrderDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<%
    BookDAO bookDAO = new BookDAO();
    OrderDTO order = (OrderDTO) request.getAttribute("order");
%>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <div>
                <h5 class="mb-0">Chi tiết đơn hàng</h5>
                <small>Đơn hàng #<%= order.getOrderID() %> - 
                    <fmt:formatDate value="<%= order.getOrderDate() %>" pattern="dd/MM/yyyy HH:mm" />
                </small>
            </div>
            <a href="${pageContext.request.contextPath}/order/history" class="btn btn-light btn-sm">Quay lại</a>
        </div>
        <div class="card-body">
            <h6><i class="bi bi-info-circle"></i> Trạng thái:
                <%
                    String statusText = "";
                    String statusClass = "";
                    switch (order.getOrderStatus()) {
                        case 0: statusText = "Đang xử lý"; statusClass = "warning"; break;
                        case 1: statusText = "Đang giao hàng"; statusClass = "info"; break;
                        case 2: statusText = "Đã giao hàng"; statusClass = "success"; break;
                        case 3: statusText = "Đã hủy"; statusClass = "danger"; break;
                        case 4: statusText = "Đã trả hàng"; statusClass = "secondary"; break;
                        default: statusText = "Không xác định"; statusClass = "dark"; break;
                    }
                %>
                <span class="badge bg-<%= statusClass %>"><%= statusText %></span>
            </h6>
            <p><strong>Địa chỉ giao hàng:</strong> <%= order.getOrderAddress() != null ? order.getOrderAddress() : "Chưa cập nhật" %></p>

            <div class="table-responsive mt-4">
                <table class="table table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>#</th>
                            <th>Sản phẩm</th>
                            <th>Số lượng</th>
                            <th>Đơn giá</th>
                            <th>Thành tiền</th>
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
                                        <small class="text-muted">Tác giả: <%= book.getAuthor() %></small>
                                    <% } else { %>
                                        <span class="text-danger">Sách không tồn tại</span>
                                    <% } %>
                                </td>
                                <td>${detail.quantity}</td>
                                <td>
                                    <% if (book != null) { %>
                                        <fmt:formatNumber value="<%= book.getBookPrice() %>" pattern="#,##0" /> ₫
                                    <% } else { %>
                                        N/A
                                    <% } %>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> ₫
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="text-end mt-3">
                <h5>Tổng cộng: 
                    <fmt:formatNumber value="<%= order.getTotalAmount() %>" pattern="#,##0" /> ₫
                </h5>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
