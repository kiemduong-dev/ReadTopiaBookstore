<%@ page import="dto.ImportStockDetailDTO, java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Import Stock Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
</head>
<body>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<%
    List<ImportStockDetailDTO> details = (List<ImportStockDetailDTO>) request.getAttribute("details");
    Integer isid = (Integer) request.getAttribute("isid");
%>

<div class="main-content">
    <div class="content-area">
        <!-- Page Header -->
        <div class="page-header">
            <h1 class="page-title">Import Stock Detail</h1>
            <p class="page-subtitle">Details for Import ID: <strong><%= (isid != null) ? isid : "N/A" %></strong></p>
        </div>

        <!-- Detail Card -->
        <div class="card">
            <div class="card-title">Imported Book Details</div>
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Book ID</th>
                            <th>Book Title</th>
                            <th>Quantity</th>
                            <th>Import Price (VND)</th>
                            <th>Total Price (VND)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty details}">
                                <tr>
                                    <td colspan="5" style="text-align:center; color: #999;">No data available.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <% 
                                    double grandTotal = 0;
                                    for (ImportStockDetailDTO d : details) {
                                        double rowTotal = d.getISDQuantity() * d.getImportPrice();
                                        grandTotal += rowTotal;
                                %>
                                    <tr>
                                        <td><%= d.getBookID() %></td>
                                        <td><%= d.getBookTitle() %></td>
                                        <td><%= d.getISDQuantity() %></td>
                                        <td><%= String.format("%,.0f", d.getImportPrice()) %> VND</td>
                                        <td><%= String.format("%,.0f", rowTotal) %> VND</td>
                                    </tr>
                                <% } %>
                                <tr style="font-weight: bold;">
                                    <td colspan="4" style="text-align:right;">Total:</td>
                                    <td><%= String.format("%,.0f", grandTotal) %> VND</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <!-- Back Button -->
            <div class="btn-group" style="margin-top: 30px;">
                <a href="${pageContext.request.contextPath}/admin/stock/list?action=list" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Back to List
                </a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
