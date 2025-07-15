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
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form method="post" action="edit">
                    <input type="hidden" name="proID" value="<%= promotion.getProID()%>" />

                    <div class="form-group">
                        <label for="startDate" class="form-label">Start Date</label>
                        <input type="date" class="form-input" name="startDate" id="startDate"
                               value="<%= promotion.getStartDate()%>" required>
                    </div>

                    <div class="form-group">
                        <label for="endDate" class="form-label">End Date</label>
                        <input type="date" class="form-input" name="endDate" id="endDate"
                               value="<%= promotion.getEndDate()%>" required>
                    </div>

                    <div class="form-group">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" class="form-input" name="quantity" id="quantity"
                               value="<%= promotion.getQuantity()%>" required>
                    </div>

                    <input type="hidden" name="createdBy" value="<%= promotion.getCreatedBy()%>" />

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
