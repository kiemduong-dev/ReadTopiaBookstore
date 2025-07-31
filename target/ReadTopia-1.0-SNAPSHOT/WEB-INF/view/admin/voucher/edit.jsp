<%-- 
    Document   : edit
    Created on : Jul 21, 2025, 4:13:14 PM
    Author     : default
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Voucher</title>
    </head>
    <body>
        <div class="main-content">
            <div class="content-area">
                <div class="page-header">
                    <h2 class="page-title">Edit Voucher</h2>
                    <p class="page-subtitle">Update voucher information</p>
                </div>

                <c:if test="${not empty errors}">
                    <div class="alert alert-danger" style="color: red; margin-bottom: 20px;">
                        <ul>
                            <c:forEach var="err" items="${errors}">
                                <li>${err}</li>
                                </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <form method="post" action="edit">
                    <input type="hidden" name="vouID" value="${voucher.vouID}" />
                    <input type="hidden" name="createdBy" value="${voucher.createdBy}" />

                    <!-- Start Date & End Date -->
                    <div class="form-row">
                        <div class="form-group" style="width: 48%; margin-right: 4%;">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-input" name="startDate" id="startDate"
                                   value="${voucher.startDate}" required>
                        </div>

                        <div class="form-group" style="width: 48%;">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-input" name="endDate" id="endDate"
                                   value="${voucher.endDate}" required>
                        </div>
                    </div>

                    <!-- Quantity -->
                    <div class="form-row">
                        <div class="form-group" style="width: 48%;">
                            <label for="quantity" class="form-label">Quantity</label>
                            <input type="number" class="form-input" name="quantity" id="quantity"
                                   value="${voucher.quantity}" required>
                        </div>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Save Changes
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
