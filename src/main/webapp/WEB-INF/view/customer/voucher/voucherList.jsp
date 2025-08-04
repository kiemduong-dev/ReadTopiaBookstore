<%-- 
    Document   : voucherList
    Created on : Jul 21, 2025, 7:55:56 PM
    Author     : default
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container py-4">
    <h2 class="mb-4 text-center">🎁 Available Vouchers Just for You!</h2>

    <div class="row row-cols-1 row-cols-md-3 g-4">
        <c:forEach var="voucher" items="${voucherList}">
            <div class="col">
                <div class="card border-light shadow-sm h-100" style="background-color: #fff6f6; border-radius: 15px; border: 1px solid #fce4e4;">
                    <div class="card-body position-relative">
                        <!-- New label -->
                        <span class="badge bg-success position-absolute top-0 end-0 m-2">New</span>

                        <!-- Title -->
                        <div class="d-flex align-items-center mb-2">
                            <span class="fs-4 me-2" style="color: #ff5722;">🔥</span>
                            <h5 class="card-title mb-0 fw-bold">${voucher.vouName}</h5>
                        </div>

                        <!-- Discount -->
                        <p class="fw-bold" style="color: red; font-size: 1.5rem;">
                            🔥 <fmt:formatNumber value="${voucher.discount}" maxFractionDigits="0" />% OFF
                        </p>

                        <!-- Code -->
                        <div class="bg-light px-3 py-2 rounded d-flex justify-content-between align-items-center mb-3" style="border: 1px solid #ccc;">
                            <span class="fw-bold text-uppercase">${voucher.vouCode}</span>
                            <button class="btn btn-light border px-3" onclick="copyToClipboard(this, '${voucher.vouCode}')">
                                <i class="bi bi-clipboard"></i>
                            </button>

                        </div>

                        <!-- Valid date -->
                        <p class="text-muted mb-2">
                            <i class="bi bi-calendar-event"></i>
                            Valid: <fmt:formatDate value="${voucher.startDate}" pattern="yyyy-MM-dd" /> - 
                            <fmt:formatDate value="${voucher.endDate}" pattern="yyyy-MM-dd" />
                        </p>

                        <!-- Uses Remaining -->                     
                        <p class="text-muted mb-1">
                            <i class="bi bi-clock-history"></i>
                            Total vouchers: <strong>${voucher.maxQuantity}</strong>
                        </p>
                        <div class="progress mb-2" style="height: 8px;">
                            <div class="progress-bar bg-success" role="progressbar" 
                                 style="width: 100%;" 
                                 aria-valuenow="${voucher.maxQuantity}" 
                                 aria-valuemin="0" aria-valuemax="${voucher.maxQuantity}"></div>
                        </div>

                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty voucherList}">
        <div class="alert alert-warning mt-4 text-center">No available vouchers!</div>
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
    .progress {
        background-color: #e0e0e0;
    }

    .progress-bar {
        transition: width 0.6s ease;
    }

    .badge.bg-success {
        font-size: 0.8rem;
        padding: 0.4em 0.6em;
        border-radius: 10px;
    }

    @keyframes fadeInOut {
        0% {
            opacity: 0;
        }
        10% {
            opacity: 1;
        }
        90% {
            opacity: 1;
        }
        100% {
            opacity: 0;
        }
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
