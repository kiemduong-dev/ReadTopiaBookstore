<%-- 
    Document   : detail
    Created on : Jul 21, 2025, 4:19:08 PM
    Author     : default
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h2 class="page-title">Voucher Details</h2>
            <p class="page-subtitle">View detailed information of the selected voucher.</p>
        </div>

        <div class="card" style="margin-left: 100px; margin-right: 100px">
            <h3 class="card-title">Voucher Information</h3>

            <div class="row" style="padding-left: 50px">
                <div class="col" style="display: flex; width: 40%; float: left; margin-right: 4%;">
                    <label class="form-label">Voucher ID: </label>
                    <p>${voucher.vouID}</p>
                </div>
                <div class="col" style="display: flex; width: 40%; float: left;">
                    <label class="form-label">Name:</label>
                    <p>${voucher.vouName}</p>
                </div>
            </div>

            <div class="row">
                <div class="col" style="display: flex; width: 40%; float: left; margin-right: 4%;">
                    <label class="form-label">Code:</label>
                    <p>${voucher.vouCode}</p>
                </div>
                <div class="col" style="display: flex; width: 40%; float: left;">
                    <label class="form-label">Discount: </label>
                    <p>${voucher.discount}</p>
                </div>
            </div>

            <div class="row">
                <div class="col" style="display: flex; width: 40%; float: left; margin-right: 4%;">
                    <label class="form-label">Start Date: </label>
                    <p>${voucher.startDate}</p>
                </div>
                <div class="col" style="display: flex; width: 40%; float: left;">
                    <label class="form-label">End Date: </label>
                    <p>${voucher.endDate}</p>
                </div>
            </div>

            <div class="row">
                <div class="col" style="display: flex; width: 40%; float: left; margin-right: 4%;">
                    <label class="form-label">Quantity: </label>
                    <p>${voucher.quantity}</p>
                </div>
                <div class="col" style="display: flex; width: 40%; float: left;">
                    <label class="form-label">Status: </label>
                    <p>
                        <c:choose>
                            <c:when test="${voucher.vouStatus == 1}">
                                <span class="status-badge active">ACTIVE</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge inactive">INACTIVE</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <div class="row">
                <div class="col" style="display: flex; width: 40%; float: left; margin-right: 4%;">
                    <label class="form-label">Created by: </label>
                    <p>
                        <c:choose>
                            <c:when test="${voucher.creatorRole == 0}">Admin</c:when>
                            <c:otherwise>Seller Staff</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <div class="col" style="display: flex; width: 40%; float: left;">
                    <label class="form-label">Approved by: </label>
                    <p>
                        <c:choose>
                            <c:when test="${voucher.approverRole == 0}">Admin</c:when>
                            <c:otherwise>Seller Staff</c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <div style="clear: both;"></div>

            <div class="btn-group" style="margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/admin/voucher/list" class="btn btn-secondary">
                    <i class="fa fa-arrow-left"></i> Back to List
                </a>
                <a href="${pageContext.request.contextPath}/admin/voucher/edit?vouID=${voucher.vouID}" class="btn btn-primary">
                    <i class="fa fa-edit"></i> Edit
                </a>
            </div>
        </div>

    </div>
</div>
