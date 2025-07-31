<%-- 
    Document   : add
    Created on : Jul 21, 2025, 3:56:44 PM
    Author     : default
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add Voucher</title>
    </head>
    <body>
        <div class="main-content">
            <div class="content-area">
                <div class="page-header">
                    <h2 class="page-title">Add New Voucher</h2>
                    <p class="page-subtitle">Create a new voucher for customers</p>
                </div>

                <!-- Hiển thị lỗi -->
                <c:if test="${not empty errors}">
                    <div class="alert alert-danger" style="color: red; margin-bottom: 20px;">
                        <ul>
                            <c:forEach var="err" items="${errors}">
                                <li>${err}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <form method="post" action="add">
                    <!-- Name & Code -->
                    <div class="form-row">
                        <div class="form-group" style="width: 48%; margin-right: 4%;">
                            <label for="vouName" class="form-label">Name</label>
                            <input type="text" class="form-input" name="vouName" id="vouName"
                                   value="${vouName != null ? vouName : ''}" required>
                        </div>

                        <div class="form-group" style="width: 48%;">
                            <label for="vouCode" class="form-label">Code</label>
                            <input type="text" class="form-input" name="vouCode" id="vouCode"
                                   value="${vouCode != null ? vouCode : ''}" required>
                        </div>
                    </div>

                    <!-- Start Date & End Date -->
                    <div class="form-row">
                        <div class="form-group" style="width: 48%; margin-right: 4%;">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-input" name="startDate" id="startDate"
                                   value="${startDate != null ? startDate : ''}" required>
                        </div>

                        <div class="form-group" style="width: 48%;">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-input" name="endDate" id="endDate"
                                   value="${endDate != null ? endDate : ''}" required>
                        </div>
                    </div>

                    <!-- Discount & Quantity -->
                    <div class="form-row">
                        <div class="form-group" style="width: 48%; margin-right: 4%;">
                            <label for="discount" class="form-label">Discount (%)</label>
                            <input type="number" step="0.01" class="form-input" name="discount" id="discount"
                                   value="${discount != null ? discount : ''}" required>
                        </div>

                        <div class="form-group" style="width: 48%;">
                            <label for="quantity" class="form-label">Quantity</label>
                            <input type="number" class="form-input" name="quantity" id="quantity"
                                   value="${quantity != null ? quantity : ''}" required>
                        </div>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add Voucher
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
