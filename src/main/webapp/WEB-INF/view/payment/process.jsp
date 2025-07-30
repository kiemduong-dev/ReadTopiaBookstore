<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="container py-5">
    <div class="card shadow-lg">
        <div class="card-header text-white text-center" style="background-color: #0d84e9;">
            <h4>Bank Transfer Payment</h4>
        </div>
        <div class="card-body">
            <div class="row">
                <!-- Left: QR Code -->
                <div class="col-md-5 d-flex justify-content-center align-items-start">
                    <div class="qr-container text-center">
                        <h5>Scan QR to Pay</h5>
                        <img src="${pageContext.request.contextPath}/img/z6755080902176_6e54f4f14f5bf5ee3caaed1d5ed253a4.jpg"
                             alt="QR Code"
                             class="img-fluid rounded shadow"
                             style="max-width: 280px;" />
                    </div>
                </div>

                <!-- Right: Payment Info -->
                <div class="col-md-7">
                    <!-- Order Summary -->
                    <div class="mb-3">                   
                        <p><strong>Amount to Pay:</strong>
                            <span class="text-danger fw-bold">
                                <fmt:formatNumber value="${finalAmount}" pattern="#,##0" /> VND
                            </span>
                        </p>
                        <p><strong>Transfer Note:</strong>
                            <span class="text-primary fw-bold">${transferCode}</span>
                        </p>
                    </div>

                    <!-- Bank Details -->
                    <div class="border rounded p-3 mb-4 bg-light">
                        <h6 class="mb-2">Bank Account Info</h6>
                        <p><strong>Bank:</strong> BIDV</p>
                        <p><strong>Account Number:</strong> 7850673111</p>
                        <p><strong>Account Holder:</strong> NGUYEN THAI ANH</p>
                        <p><strong>Transfer Note:</strong>
                            <span class="text-primary fw-bold">${transferCode}</span>
                        </p>
                        <!-- Confirm Payment Form -->
                        <form method="post" action="${pageContext.request.contextPath}/payment/confirm">
                            <input type="hidden" name="amount" value="${amount}" />
                            <input type="hidden" name="finalAmount" value="${finalAmount}" />
                            <input type="hidden" name="type" value="${type}" />
                            <input type="hidden" name="bookId" value="${bookId}" />
                            <input type="hidden" name="quantity" value="${quantity}" />
                            <input type="hidden" name="promotionID" value="${proID}" />
                            <input type="hidden" name="orderAddress" value="${orderAddress}" />
                            <input type="hidden" name="paymentMethod" value="${paymentMethod}" />
                            <input type="hidden" name="transferCode" value="${transferCode}" />

                            <c:if test="${not empty selectedCartIDsString}">
                                <c:forEach var="id" items="${fn:split(selectedCartIDsString, ',')}">
                                    <input type="hidden" name="selectedCartIDs" value="${id}" />
                                </c:forEach>
                            </c:if>

                            <div class="mt-3 text-center">
                                <button type="submit" class="btn btn-lg" style="background-color: #0d84e9; color: white;">
                                    I Have Transferred
                                </button>
                                <a href="${pageContext.request.contextPath}/customer/book/list"
                                   class="btn btn-outline-danger">Cancel Payment</a>
                            </div>
                        </form>
                    </div>
                </div> 
            </div> 
        </div> 
    </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />

<style>
    .qr-container {
        padding: 20px;
        background-color: #fff;
        border-radius: 12px;
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
        display: inline-block;
    }
</style>
