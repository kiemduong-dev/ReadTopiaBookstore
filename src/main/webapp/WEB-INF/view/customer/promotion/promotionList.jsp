<%-- 
    Document   : promotionList
    Created on : Jun 27, 2025, 7:57:57 PM
    Author     : ngtua
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container py-4">
    <h2 class="mb-4 text-center">🎉 Ưu đãi hấp dẫn dành cho bạn!</h2>

    <div class="row row-cols-1 row-cols-md-3 g-4">
        <c:forEach var="promotion" items="${promotionList}">
            <div class="col">
                <div class="card border-primary shadow-sm h-100">
                    <div class="card-body">
                        <h5 class="card-title text-primary">${promotion.proName}</h5>
                        <p class="card-text"><strong>Mã khuyến mãi:</strong> <code>${promotion.proCode}</code></p>
                        <p class="card-text text-success"><strong>Giảm giá:</strong> ${promotion.discount}%</p>
                        <p class="card-text">
                            <strong>Hiệu lực:</strong>
                            <fmt:formatDate value="${promotion.startDate}" pattern="dd/MM/yyyy" /> - 
                            <fmt:formatDate value="${promotion.endDate}" pattern="dd/MM/yyyy" />
                        </p>
                        <p class="card-text"><strong>Số lượng còn lại:</strong> ${promotion.quantity}</p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty promotionList}">
        <div class="alert alert-warning mt-4 text-center">Không có khuyến mãi nào khả dụng!</div>
    </c:if>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
