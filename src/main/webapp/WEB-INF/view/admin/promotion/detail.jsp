<%-- 
    Document   : detail
    Created on : Jun 22, 2025, 6:49:33 PM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />
<jsp:include page="/WEB-INF/includes/head.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h2 class="page-title">Promotion Details</h2>
            <p class="page-subtitle">View detailed information of the selected promotion.</p>
        </div>

        <div class="card">
            <h3 class="card-title">Promotion Information</h3>
            <div class="form-group">
                <label class="form-label">Promotion ID:</label>
                <p>${promotion.proID}</p>
            </div>
            <div class="form-group">
                <label class="form-label">Name:</label>
                <p>${promotion.proName}</p>
            </div>
            <div class="form-group">
                <label class="form-label">Code:</label>
                <p>${promotion.proCode}</p>
            </div>
            <div class="form-group">
                <label class="form-label">Discount (%):</label>
                <p>${promotion.discount}</p>
            </div>
            <div class="form-group">
                <label class="form-label">Start Date:</label>
                <p>${promotion.startDate}</p>
            </div>
            <div class="form-group">
                <label class="form-label">End Date:</label>
                <p>${promotion.endDate}</p>
            </div>
            <div class="form-group">
                <label class="form-label">Quantity:</label>
                <p>${promotion.quantity}</p>
            </div>
            <div class="form-group">
                <label class="form-label">Status:</label>
                <p>
                    <c:choose>
                        <c:when test="${promotion.proStatus == 1}">
                            <span class="status-badge active">Active</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge inactive">Inactive</span>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <c:choose>
                <c:when test="${promotion.creatorRole == 0}">Created by: Admin</c:when>
                <c:otherwise>Created by: Seller Staff</c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${promotion.approverRole == 0}">Created by: Admin</c:when>
                <c:otherwise>Created by: Seller Staff</c:otherwise>
            </c:choose>

            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/admin/promotion/list" class="btn btn-secondary">
                    <i class="fa fa-arrow-left"></i> Back to List
                </a>
                <a href="${pageContext.request.contextPath}/admin/promotion/edit?proID=${promotion.proID}" class="btn btn-primary">
                    <i class="fa fa-edit"></i> Edit
                </a>
            </div>
        </div>
    </div>
</div>
