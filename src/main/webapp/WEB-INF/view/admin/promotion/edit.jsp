<%-- 
    Document   : edit
    Created on : Jun 17, 2025, 3:01:33 PM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.PromotionDTO" %>
<jsp:useBean id="promotion" type="dto.PromotionDTO" scope="request" />
<jsp:include page="/WEB-INF/includes/head.jsp" />

<html>
    <head>
        <title>Edit Promotion</title>
    </head>
    <body class="bg-light">

        <div class="container mt-5">
            <h2 class="mb-4">Edit Promotion</h2>

            <form method="post" action="edit" class="row g-3">
                <input type="hidden" name="proID" value="<%= promotion.getProID()%>" />

                <div class="col-md-6">
                    <label for="proName" class="form-label">Name</label>
                    <input type="text" class="form-control" name="proName" id="proName" value="<%= promotion.getProName()%>" required>
                </div>

                <div class="col-md-6">
                    <label for="proCode" class="form-label">Code</label>
                    <input type="text" class="form-control" name="proCode" id="proCode" value="<%= promotion.getProCode()%>" required>
                </div>

                <div class="col-md-4">
                    <label for="discount" class="form-label">Discount (%)</label>
                    <input type="number" class="form-control" name="discount" id="discount" step="0.01" value="<%= promotion.getDiscount()%>" required>
                </div>

                <div class="col-md-4">
                    <label for="startDate" class="form-label">Start Date</label>
                    <input type="date" class="form-control" name="startDate" id="startDate" value="<%= promotion.getStartDate()%>" required>
                </div>

                <div class="col-md-4">
                    <label for="endDate" class="form-label">End Date</label>
                    <input type="date" class="form-control" name="endDate" id="endDate" value="<%= promotion.getEndDate()%>" required>
                </div>

                <div class="col-md-3">
                    <label for="quantity" class="form-label">Quantity</label>
                    <input type="number" class="form-control" name="quantity" id="quantity" value="<%= promotion.getQuantity()%>" required>
                </div>

                <div class="col-md-3">
                    <label for="proStatus" class="form-label">Status</label>
                    <input type="number" class="form-control" name="proStatus" id="proStatus" value="<%= promotion.getProStatus()%>">
                </div>

                <div class="col-md-3">
                    <label for="createdBy" class="form-label">Created By</label>
                    <input type="number" class="form-control" name="createdBy" id="createdBy" value="<%= promotion.getCreatedBy()%>">
                </div>

                <div class="col-md-3">
                    <label for="approvedBy" class="form-label">Approved By)</label>
                    <input type="number" class="form-control" name="approvedBy" id="approvedBy" value="<%= promotion.getApprovedBy()%>">
                </div>

                <div class="col-12 mt-4">
                    <button type="submit" class="btn btn-primary">Update Promotion</button>
                    <a href="list" class="btn btn-secondary ms-2">Back to List</a>
                </div>
            </form>
        </div>

    </body>
</html>
