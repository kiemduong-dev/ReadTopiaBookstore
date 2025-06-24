<%-- 
    Document   : detail
    Created on : May 29, 2025, 7:27:16 AM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />

<!DOCTYPE html>
<html>
    <head>
        <title>Notification Details</title>
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="mb-4 text-center">Notification Details</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <c:if test="${not empty notification}">
                <div class="card shadow-sm p-4">
                    <p><strong>ID:</strong> ${notification.notID}</p>
                    <p><strong>Title:</strong> ${notification.notTitle}</p>
                    <p><strong>Receiver:</strong>
                        <c:choose>
                            <c:when test="${notification.receiver == 0}">All</c:when>
                            <c:when test="${notification.receiver == 1}">Admin</c:when>
                            <c:when test="${notification.receiver == 2}">Staff</c:when>
                            <c:when test="${notification.receiver == 3}">Customer</c:when>
                            <c:otherwise>Unknown</c:otherwise>
                        </c:choose>
                    </p>

                    <p><strong>Content:</strong> ${notification.notDescription}</p>
                    </p>
                </div>
            </c:if>

            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/admin/notification/list" class="btn btn-secondary">Back to List</a>
            </div>
        </div>
    </body>
</html>

