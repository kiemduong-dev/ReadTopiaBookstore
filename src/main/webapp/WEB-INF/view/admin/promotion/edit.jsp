<%-- 
    Document   : edit
    Created on : Jun 17, 2025, 3:01:33 PM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.PromotionDTO" %>
<jsp:useBean id="promotion" type="dto.PromotionDTO" scope="request" />
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<html>
    <head>
        <title>Edit Promotion</title>
    </head>
    <body>
        <div class="main-content">
            <div class="content-area">
                <div class="page-header">
                    <h2 class="page-title">Edit Promotion</h2>
                    <p class="page-subtitle">Modify the information of an existing promotion.</p>
                </div>

                <form method="post" action="edit">
                    <input type="hidden" name="proID" value="<%= promotion.getProID()%>" />

                    <div class="form-row">
                        <div class="form-group">
                            <label for="proName" class="form-label">Name</label>
                            <input type="text" class="form-input" name="proName" id="proName" value="<%= promotion.getProName()%>" required>
                        </div>

                        <div class="form-group">
                            <label for="proCode" class="form-label">Code</label>
                            <input type="text" class="form-input" name="proCode" id="proCode" value="<%= promotion.getProCode()%>" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="discount" class="form-label">Discount (%)</label>
                            <input type="number" step="0.01" class="form-input" name="discount" id="discount" value="<%= promotion.getDiscount()%>" required>
                        </div>

                        <div class="form-group">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-input" name="startDate" id="startDate" value="<%= promotion.getStartDate()%>" required>
                        </div>

                        <div class="form-group">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-input" name="endDate" id="endDate" value="<%= promotion.getEndDate()%>" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="quantity" class="form-label">Quantity</label>
                            <input type="number" class="form-input" name="quantity" id="quantity" value="<%= promotion.getQuantity()%>" required>
                        </div>

                        <div class="form-group">
                            <label for="proStatus" class="form-label">Status</label>
                            <input type="number" class="form-input" name="proStatus" id="proStatus" value="<%= promotion.getProStatus()%>">
                        </div>

                        <div class="form-group">
                            <label for="createdBy" class="form-label">Created By</label>
                            <input type="number" class="form-input" name="createdBy" id="createdBy" value="<%= promotion.getCreatedBy()%>">
                        </div>

                        <div class="form-group">
                            <label for="approvedBy" class="form-label">Approved By</label>
                            <input type="number" class="form-input" name="approvedBy" id="approvedBy" value="<%= promotion.getApprovedBy()%>">
                        </div>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Update Promotion
                        </button>
                        <a href="list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
