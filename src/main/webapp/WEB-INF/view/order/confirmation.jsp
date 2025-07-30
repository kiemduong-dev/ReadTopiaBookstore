<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container-fluid py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow text-center">
                <div class="card-header text-white" style="background-color: #0d84e9;">
                    <h2 class="mb-0">Order Confirmation</h2>
                </div>

                <div class="card-body py-5">
                    <div class="mb-4">
                        <div class="success-icon mb-3">
                            <i class="bi bi-check-circle" style="font-size: 5rem; color: #0d84e9;"></i>
                        </div>
                        <h3 class="mb-3">Thank you for your order!</h3>
                        <p class="lead mb-4">
                            Your order has been placed successfully.
                        </p>
                    </div>

                    <div class="row g-3 justify-content-center">
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/order/history"
                               class="btn btn-outline-primary w-100">
                                View Order History
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/customer/book/list"
                               class="btn w-100"
                               style="background-color: #0d84e9; border-color: #0d84e9; color: white;">
                                Continue Shopping
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

<style>
    .card {
        border-radius: 15px;
        border: none;
        overflow: hidden;
    }
    .card-header {
        border-radius: 15px 15px 0 0 !important;
        padding: 1.5rem;
    }
    .card-body {
        padding: 2rem;
    }
    .success-icon {
        animation: bounce 1s ease-in-out;
    }
    .btn {
        padding: 12px 24px;
        border-radius: 8px;
        font-weight: 500;
    }
    .alert {
        border-radius: 10px;
        padding: 1rem;
    }

    @keyframes bounce {
        0%, 20%, 50%, 80%, 100% {
            transform: translateY(0);
        }
        40% {
            transform: translateY(-10px);
        }
        60% {
            transform: translateY(-5px);
        }
    }
</style>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
