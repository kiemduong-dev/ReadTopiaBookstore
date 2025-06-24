<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.BookDAO, dto.BookDTO, dto.OrderDetailDTO, dto.OrderDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <%
            BookDAO bookDAO = new BookDAO();
            double totalAmount = 0;
            OrderDTO currentOrder = (OrderDTO) request.getAttribute("order");
        %>

        <div class="container-fluid py-4">
            <div class="row justify-content-center">
                <div class="col-12 col-xl-10">
                    <div class="card shadow-lg border-0">
                        <div class="card-header bg-gradient-primary text-white">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h2 class="mb-1"><i class="bi bi-receipt me-2"></i>Chi tiết đơn hàng</h2>
                                    <% if (currentOrder != null) {%>
                                    <small class="opacity-75">Đơn hàng #<%= currentOrder.getOrderID()%> - 
                                        <fmt:formatDate value="<%= currentOrder.getOrderDate()%>" pattern="dd/MM/yyyy HH:mm" />
                                    </small>
                                    <% } %>
                                </div>
                                <a href="${pageContext.request.contextPath}/order/management" 
                                   class="btn btn-light btn-sm shadow-sm">
                                    <i class="bi bi-arrow-left me-1"></i> Quay lại
                                </a>
                            </div>
                        </div>

                        <% if (currentOrder != null) {%>
                        <div class="card-body border-bottom bg-light">
                            <div class="row">
                                <div class="col-md-6">
                                    <h3 class="text-dark fw-bold mb-2"><i class="bi bi-person me-1"></i>Thông tin khách hàng</h3>
                                    <p class="mb-1 small"><strong class="small">Tên khách hàng:</strong> <span class="small"><%= currentOrder.getUsername()%></span></p>
                                    <p class="mb-0 small"><strong class="small">Địa chỉ giao hàng:</strong> <span class="small"><%= currentOrder.getOrderAddress() != null ? currentOrder.getOrderAddress() : "Chưa cập nhật"%></span></p>
                                </div>
                                <div class="col-md-6">
                                    <h3 class="text-dark fw-bold mb-2"><i class="bi bi-info-circle me-1"></i>Trạng thái đơn hàng</h3>
                                    <p class="mb-1">
                                        <strong class="small">Trạng thái:</strong>
                                        <%
                                            int status = currentOrder.getOrderStatus();
                                            String statusClass = "";
                                            String statusText = "";
                                            switch (status) {
                                                case 0:
                                                    statusClass = "warning";
                                                    statusText = "Đang xử lý";
                                                    break;
                                                case 1:
                                                    statusClass = "info";
                                                    statusText = "Đang giao hàng";
                                                    break;
                                                case 2:
                                                    statusClass = "success";
                                                    statusText = "Đã giao hàng";
                                                    break;
                                                case 3:
                                                    statusClass = "danger";
                                                    statusText = "Đã hủy";
                                                    break;
                                                case 4:
                                                    statusClass = "secondary";
                                                    statusText = "Đã trả hàng";
                                                    break;
                                                default:
                                                    statusClass = "light text-dark";
                                                    statusText = "Không xác định";
                                            }
                                        %>
                                        <span class="badge bg-<%= statusClass%> fs-6"><%= statusText%></span>
                                    </p>
                                    <p class="mb-0 small"><strong class="small">Ngày đặt:</strong> <span class="small"><fmt:formatDate value="<%= currentOrder.getOrderDate()%>" pattern="dd/MM/yyyy HH:mm" /></span></p>
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
                                            <th>Sản phẩm</th>
                                            <th class="text-center">Số lượng</th>
                                            <th class="text-end">Đơn giá</th>
                                            <th class="text-end pe-4">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="detail" items="${orderDetails}" varStatus="loop">
                                            <%
                                                OrderDetailDTO detail = (OrderDetailDTO) pageContext.getAttribute("detail");
                                                BookDTO book = bookDAO.getBookByID(detail.getBookID());
                                                totalAmount += detail.getTotalPrice();
                                            %>
                                            <tr class="border-bottom">
                                                <td class="ps-4 align-middle">
                                                    <span class="badge bg-light text-dark rounded-pill">${loop.index + 1}</span>
                                                </td>
                                                <td class="py-3">
                                                    <div class="d-flex align-items-center">
                                                        <% if (book != null) {%>
                                                        <div class="position-relative me-3">
                                                            <img src="<%= book.getImage()%>" alt="Book cover" 
                                                                 class="rounded shadow-sm" width="60" height="80" 
                                                                 onerror="this.src='https://via.placeholder.com/60x80?text=No+Image'">
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold text-primary mb-1"><%= book.getBookTitle()%></div>
                                                            <small class="text-muted d-block">
                                                                <i class="bi bi-person me-1"></i>Tác giả: <%= book.getAuthor()%>
                                                            </small>
                                                            <small class="text-muted">
                                                                <i class="bi bi-hash me-1"></i>ID: ${detail.bookID}
                                                            </small>
                                                        </div>
                                                        <% } else { %>
                                                        <div class="d-flex align-items-center">
                                                            <div class="bg-light rounded me-3 d-flex align-items-center justify-content-center" 
                                                                 style="width: 60px; height: 80px;">
                                                                <i class="bi bi-image text-muted"></i>
                                                            </div>
                                                            <div>
                                                                <div class="fw-bold text-danger">Sản phẩm không tồn tại</div>
                                                                <small class="text-muted">ID: ${detail.bookID}</small>
                                                            </div>
                                                        </div>
                                                        <% } %>
                                                    </div>
                                                </td>
                                                <td class="text-center align-middle">
                                                    <span class="badge bg-primary rounded-pill fs-6">${detail.quantity}</span>
                                                </td>
                                                <td class="text-end align-middle">
                                                    <% if (book != null) {%>
                                                    <span class="text-muted">
                                                        <fmt:formatNumber value="<%= book.getBookPrice()%>" type="number" pattern="#,##0" /> ₫
                                                    </span>
                                                    <% } else { %>
                                                    <span class="text-muted">N/A</span>
                                                    <% }%>
                                                </td>
                                                <td class="text-end align-middle pe-4">
                                                    <span class="fw-bold text-success fs-6">
                                                        <fmt:formatNumber value="${detail.totalPrice}" type="number" pattern="#,##0" /> ₫
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>