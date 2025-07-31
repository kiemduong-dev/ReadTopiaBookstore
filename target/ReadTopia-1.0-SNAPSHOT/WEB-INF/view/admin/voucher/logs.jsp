<%-- 
    Document   : log
    Created on : Jul 21, 2025, 4:42:51 PM
    Author     : default
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, dto.VoucherLogDTO" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />
<%
    List<VoucherLogDTO> logs = (List<VoucherLogDTO>) request.getAttribute("logs");
    Integer actionFilter = (Integer) request.getAttribute("actionFilter");
%>

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <div class="page-title">Voucher Logs</div>
            <div class="page-subtitle">History of add, edit, and delete actions on vouchers</div>
        </div>

        <!-- Filter Form -->
        <form method="get" action="${pageContext.request.contextPath}/admin/voucher/logs" id="filterForm">
            <div class="form-group" style="max-width: 300px;">
                <label for="action" class="form-label">Filter by Action</label>
                <select name="action" id="action" class="form-select" onchange="document.getElementById('filterForm').submit();">
                    <option value="" <%= (actionFilter == null) ? "selected" : ""%>>-- All --</option>
                    <option value="1" <%= (actionFilter != null && actionFilter == 1) ? "selected" : ""%>>Add</option>
                    <option value="2" <%= (actionFilter != null && actionFilter == 2) ? "selected" : ""%>>Edit</option>
                    <option value="3" <%= (actionFilter != null && actionFilter == 3) ? "selected" : ""%>>Delete</option>
                </select>
            </div>
        </form>

        <!-- Table Log -->
        <div class="card table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>Log Description</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (logs != null && !logs.isEmpty()) {
                            for (VoucherLogDTO log : logs) {
                                String actionText = "";
                                switch (log.getVouAction()) {
                                    case 1:
                                        actionText = "added";
                                        break;
                                    case 2:
                                        actionText = "edited";
                                        break;
                                    case 3:
                                        actionText = "deleted";
                                        break;
                                    default:
                                        actionText = "performed an unknown action on";
                                }

                                String roleText = (log.getRole() == 0) ? "Admin" : "Seller Staff";

                                String voucherIDText = log.getVouID() > 0 ? String.valueOf(log.getVouID()) : "Unknown ID";
                    %>
                    <tr>
                        <td><%= roleText%> <%= actionText%> voucher with ID "<%= voucherIDText%>"</td>
                        <td><%= log.getVouLogDate()%></td>
                    </tr>
                    <% }
            } else { %>
                    <tr>
                        <td colspan="2">No logs found.</td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
