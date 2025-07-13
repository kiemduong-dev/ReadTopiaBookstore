<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add Import Stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />
<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">Add Import Stock</h1>
            <p class="page-subtitle">Fill in the details for a new stock import</p>
        </div>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
            </div>
        </c:if>
        <div class="card">
            <div class="card-title">Import Information</div>
            <form method="post" action="${pageContext.request.contextPath}/admin/stock/list">
                <input type="hidden" name="action" value="add" />
                <div class="form-group">
                    <label class="form-label">Supplier</label>
                    <select name="sup" class="form-select" required>
                        <option value="">-- Select Supplier --</option>
                        <c:forEach var="s" items="${supplierList}">
                            <option value="${s.supID}">${s.supName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Import Date</label>
                    <input type="date" name="importDate" class="form-input" required />
                </div>
                <div class="form-group">
                    <label class="form-label">Staff</label>
                    <select name="staffID" class="form-select" required>
                        <option value="">-- Select Staff --</option>
                        <c:forEach var="s" items="${staffList}">
                            <option value="${s.staffID}">${s.firstName} ${s.lastName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <div class="card-title">Book Items</div>
                    <div id="stockItems">
                        <div class="form-row base-row">
                            <select name="bookIDList[]" class="form-select" required>
                                <option value="">-- Select Book --</option>
                                <c:forEach var="b" items="${bookList}">
                                    <option value="${b.bookID}">${b.bookTitle}</option>
                                </c:forEach>
                            </select>
                            <input type="number" name="quantityList[]" placeholder="Quantity" class="form-input" min="1" required />
                            <input type="number" name="priceList[]" placeholder="Import Price" class="form-input" min="0" step="0.01" required />
                        </div>
                    </div>
                    <button type="button" class="btn btn-secondary" onclick="addRow()">
                        <i class="fas fa-plus"></i> Add another book
                    </button>
                </div>
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Submit
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/stock/list?action=list" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Cancel
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/stock/list?action=list" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left"></i> Back to List
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    function addRow() {
        const container = document.getElementById("stockItems");
        const baseRow = container.querySelector(".base-row");
        const newRow = baseRow.cloneNode(true);
        newRow.classList.remove("base-row");
        newRow.querySelectorAll("input").forEach(input => input.value = "");
        newRow.querySelector("select").selectedIndex = 0;
        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.className = "btn btn-danger";
        removeBtn.textContent = "Remove";
        removeBtn.style.marginLeft = "10px";
        removeBtn.onclick = () => newRow.remove();
        newRow.appendChild(removeBtn);
        container.appendChild(newRow);
    }
</script>
</body>
</html>
