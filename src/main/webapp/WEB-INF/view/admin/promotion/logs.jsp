<%-- 
    Document   : logs
    Created on : Jun 17, 2025, 3:30:32 PM
    Author     : ngtua
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, dto.PromotionLogDTO" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />
<%
    List<PromotionLogDTO> logs = (List<PromotionLogDTO>) request.getAttribute("logs");
    Integer actionFilter = (Integer) request.getAttribute("actionFilter");
%>

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <div class="page-title">Promotion Logs</div>
            <div class="page-subtitle">History of add, edit, and delete actions on promotions</div>
        </div>

        <!-- Filter Form -->
        <form method="get" action="${pageContext.request.contextPath}/admin/promotion/logs" id="filterForm">
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
                        <th>Log ID</th>
                        <th>Promotion ID</th>
                        <th>Staff</th>
                        <th>Action</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (logs != null && !logs.isEmpty()) {
                            for (PromotionLogDTO log : logs) {
                                String actionText = "";
                                switch (log.getProAction()) {
                                    case 1:
                                        actionText = "Add";
                                        break;
                                    case 2:
                                        actionText = "Edit";
                                        break;
                                    case 3:
                                        actionText = "Delete";
                                        break;
                                    default:
                                        actionText = "Unknown";
                                        break;
                                }
                    %>
                    <tr>
                        <td><%= log.getProLogID()%></td>
                        <td><%= log.getProID()%></td>
                        <td><%= (log.getStaffID() == 1) ? "Admin" : "Seller Staff"%></td>                       
                        <td><%= actionText%></td>
                        <td><%= log.getProLogDate()%></td>
                    </tr>
                    <%  }
                    } else { %>
                    <tr>
                        <td colspan="5">No logs found.</td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </div>
</div>