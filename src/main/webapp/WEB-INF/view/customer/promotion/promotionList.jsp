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
    <h2 class="mb-4 text-center">🎉 Exclusive Promotions Just for You!</h2>

    <div class="row row-cols-1 row-cols-md-3 g-4">
        <c:forEach var="promotion" items="${promotionList}">
            <div class="col">
                <div class="card border-primary shadow-sm h-100">
                    <div class="card-body d-flex flex-column justify-content-between">
                        <div>
                            <h5 class="card-title text-primary">${promotion.proName}</h5>
                            <p>
                                <button class="btn btn-light border px-3" onclick="copyToClipboard(this, '${promotion.proCode}')">
                                    <i class="bi bi-clipboard"></i> ${promotion.proCode}
                                </button>
                            </p>
                            <p class="card-text text-success"><strong>Discount:</strong> <fmt:formatNumber value="${promotion.discount}" maxFractionDigits="1" groupingUsed="false" />%</p>
                            <p class="card-text">
                                <strong>Valid from:</strong>
                                <fmt:formatDate value="${promotion.startDate}" pattern="dd/MM/yyyy" /> to 
                                <fmt:formatDate value="${promotion.endDate}" pattern="dd/MM/yyyy" />
                            </p>
                            <p class="card-text"><strong>Remaining quantity:</strong> ${promotion.quantity}</p>
                        </div>

                        <div class="mt-3">
                            <p class="text-muted mb-1">Expires: <fmt:formatDate value="${promotion.endDate}" pattern="dd/MM/yyyy" /></p>
                            <button class="btn btn-primary w-100" onclick="copyToClipboard(this, '${promotion.proCode}')">Copy Code</button>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty promotionList}">
        <div class="alert alert-warning mt-4 text-center">No available promotions!</div>
    </c:if>
</div>

<style>
    .copy-toast {
        position: fixed;
        top: 80px;
        left: 50%;
        transform: translateX(-50%);
        background-color: rgba(0, 0, 0, 0.8);
        color: #fff;
        padding: 8px 16px;
        border-radius: 8px;
        font-size: 14px;
        z-index: 9999;
        display: none;
        animation: fadeInOut 2s ease-in-out;
    }

    @keyframes fadeInOut {
        0% { opacity: 0; }
        10% { opacity: 1; }
        90% { opacity: 1; }
        100% { opacity: 0; }
    }
</style>

<div id="copy-toast" class="copy-toast">Copied</div>

<script>
    function copyToClipboard(button, code) {
        navigator.clipboard.writeText(code).then(() => {
            const toast = document.getElementById("copy-toast");
            toast.style.display = "block";
            setTimeout(() => {
                toast.style.display = "none";
            }, 2000);
        }).catch(err => {
            console.error("Failed to copy: ", err);
        });
    }
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
