<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <c:choose>
            <c:when test="${not empty supplier}">
                <div class="card" style="max-width: 950px; margin: auto; display: flex; gap: 30px; align-items: flex-start;">
                    <div>
                        <img src="${pageContext.request.contextPath}/${supplier.image}" alt="Supplier Image"
                             style="max-width: 280px; border-radius: 12px; border: 3px solid #ddd; box-shadow: 0 6px 12px rgba(0,0,0,0.1);">
                    </div>

                    <div style="flex: 1;">
                        <h2 class="page-title" style="margin-bottom: 20px;">Supplier Detail</h2>

                        <div class="form-group"><label class="form-label">ID:</label> ${supplier.id}</div>
                        <div class="form-group"><label class="form-label">Name:</label> ${supplier.supName}</div>
                        <div class="form-group"><label class="form-label">Password:</label> ${supplier.password}</div>
                        <div class="form-group"><label class="form-label">Email:</label> ${supplier.email}</div>
                        <div class="form-group"><label class="form-label">Phone:</label> ${supplier.phone}</div>
                        <div class="form-group"><label class="form-label">Address:</label> ${supplier.address}</div>
                        <div class="form-group"><label class="form-label">Status:</label>
                            <span class="status-badge ${supplier.status ? 'active' : 'inactive'}">
                                <c:choose>
                                    <c:when test="${supplier.status}">Active</c:when>
                                    <c:otherwise>Inactive</c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <div class="btn-group" style="margin-top: 30px;">
                            <a href="${pageContext.request.contextPath}/admin/supplier/list" class="btn btn-secondary">← Back to List</a>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card text-center">
                    <div class="alert alert-warning">Supplier not found!</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
