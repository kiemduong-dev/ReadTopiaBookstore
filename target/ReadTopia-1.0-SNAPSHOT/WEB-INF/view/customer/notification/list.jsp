<%-- 
    Document   : list
    Created on : Jun 28, 2025, 2:34:41 PM
    Author     : ngtua
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />

<!DOCTYPE html>
<html>
<head>
    <title>User Notifications</title>
</head>
<body>
    <h2>Your Notifications</h2>

    <c:if test="${not empty error}">
        <div style="color:red">${error}</div>
    </c:if>

    <c:forEach var="noti" items="${notifications}">
        <div style="border:1px solid #ccc; padding:10px; margin:10px 0">
            <h4>${noti.notTitle}</h4>
            <div><strong>Description:</strong></div>
            <div><c:out value="${noti.notDescription}" escapeXml="false"/></div>
        </div>
    </c:forEach>

    <c:if test="${empty notifications}">
        <p>No notifications available for your role.</p>
    </c:if>
</body>
</html>
