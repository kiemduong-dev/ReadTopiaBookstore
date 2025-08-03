<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Inventory Management</h1>
            <p class="page-subtitle">Manage imported stock</p>
        </div>

        <div id="importTab" class="card">
            <div class="card-title">Import Stock List</div>

            <c:if test="${not empty sessionScope.successMessage}">
                <div id="successMessage" class="alert-success-custom">
                    <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                </div>
            </c:if>

            <div class="btn-group" style="justify-content: flex-end; margin-bottom: 15px;">
                <a href="${pageContext.request.contextPath}/admin/stock/list?action=add" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Add Stock
                </a>
            </div>

            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Supplier</th>
                            <th>Import Date</th>
                            <th>Staff</th>
                            <th>Total Price</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody class="stock-body">
                        <c:forEach var="s" items="${importList}">
                            <tr>
                                <td>${s.id}</td>
                                <td>${s.supplierName}</td>
                                <td><fmt:formatDate value="${s.importDate}" pattern="yyyy-MM-dd" /></td>
                                <td>${s.staffName}</td>
                                <td><fmt:formatNumber value="${s.totalPrice}" type="number" groupingUsed="true" /> VND</td>
                                <td>
                                    <span class="status-badge ${s.status == 1 ? 'active' : 'inactive'}">
                                        ${s.status == 1 ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/stock/ImportStockDetailServlet?isid=${s.id}" class="btn btn-info">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="pagination-wrapper">
                <ul class="pagination">
                    <li>
                        <button class="page-btn"
                                onclick="pagingStock(${currentPage - 1})"
                                data-page="${currentPage - 1}"
                                ${currentPage == 1 ? 'disabled' : ''}>
                            &lt;
                        </button>
                    </li>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li>
                            <button class="page-btn ${i == currentPage ? 'active' : ''}"
                                    onclick="pagingStock(${i})"
                                    data-page="${i}">
                                ${i}
                            </button>
                        </li>
                    </c:forEach>

                    <li>
                        <button class="page-btn"
                                onclick="pagingStock(${currentPage + 1})"
                                data-page="${currentPage + 1}"
                                ${currentPage == totalPages ? 'disabled' : ''}>
                            &gt;
                        </button>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/paging-stock.js"></script>
<script>
    window.addEventListener("DOMContentLoaded", function () {
        const success = document.getElementById("successMessage");
        if (success) {
            setTimeout(() => {
                success.style.display = "none";
            }, 3000);
        }
    });
</script>
