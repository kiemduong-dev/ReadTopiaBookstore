<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <h1 class="page-title">Supplier Management</h1>

        <div class="toolbar">
            <a href="${pageContext.request.contextPath}/admin/supplier/add" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Supplier
            </a>

            <form action="${pageContext.request.contextPath}/admin/supplier/search" method="get" style="display: flex; gap: 10px;">
                <input type="text" name="query" placeholder="Search by name..." 
                       value="${param.query != null ? param.query : ''}" class="search-box" />
                <button type="submit" class="btn btn-secondary">Search</button>
            </form>
        </div>

        <c:choose>
            <c:when test="${empty suppliers}">
                <p class="text-muted" style="text-align:center;">No supplier found.</p>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Phone</th>
                                <th>Address</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${suppliers}">
                                <tr>
                                    <td><c:out value="${s.id}" /></td>
                                    <td><c:out value="${s.supName}" /></td>
                                    <td><c:out value="${s.phone}" /></td>
                                    <td><c:out value="${s.address}" /></td>
                                    <td>
                                        <span class="status-badge ${s.status ? 'active' : 'inactive'}">
                                            ${s.status ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/supplier/detail?id=${s.id}" 
                                           class="btn-icon btn-info" title="Detail">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/supplier/edit?id=${s.id}" 
                                           class="btn-icon btn-warning" title="Edit">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/supplier/delete?id=${s.id}" 
                                           class="btn-icon btn-danger" 
                                           onclick="return confirm('Are you sure you want to delete this supplier?')" 
                                           title="Delete">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
