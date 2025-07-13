<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <!-- Tiêu đề -->
        <div class="page-header">
            <h1 class="page-title"> Category Management</h1>
        </div>

        <!-- Chỉ hiển thị ô tìm kiếm nếu có hơn 10 danh mục -->
        <c:if test="${fn:length(categoryList) > 10}">
            <div class="toolbar d-flex justify-content-between align-items-center mb-3">
                <form action="${pageContext.request.contextPath}/admin/category/list" method="get" class="d-flex gap-2">
                    <input 
                        type="text" 
                        name="keyword" 
                        class="form-control search-box" 
                        placeholder=" Search category name..." 
                        value="${fn:escapeXml(keyword)}" />
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>

                <button class="btn btn-success" onclick="location.href = '${pageContext.request.contextPath}/admin/category/add'">
                    <i class="fas fa-plus"></i> Add Category
                </button>
            </div>
        </c:if>

        <!-- Nếu danh mục <= 10 thì chỉ hiển thị nút Add -->
        <c:if test="${fn:length(categoryList) <= 10}">
            <div class="d-flex justify-content-end mb-3">
                <button class="btn btn-success" onclick="location.href = '${pageContext.request.contextPath}/admin/category/add'">
                    <i class="fas fa-plus"></i> Add Category
                </button>
            </div>
        </c:if>

        <!-- Thông báo thành công -->
        <c:if test="${not empty sessionScope.success}">
            <div class="success-message mt-3">
                <i class="fas fa-check-circle"></i> ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>

        <!-- Hiển thị số kết quả tìm được -->
        <c:if test="${not empty categoryList}">
            <div class="text-muted text-end mb-2">
                Found <strong>${fn:length(categoryList)}</strong> result(s).
            </div>
        </c:if>

        <!-- Bảng danh sách -->
        <c:if test="${not empty categoryList}">
            <div class="table-container mt-3">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th style="width: 10%;" class="text-center">ID</th>
                            <th class="text-center">Name</th>
                            <th style="width: 20%;" class="text-center">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cat" items="${categoryList}">
                            <tr>
                                <td class="text-center"><c:out value="${cat.categoryID}" /></td>
                                <td class="text-center"><c:out value="${cat.categoryName}" /></td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/admin/category/detail?id=${cat.categoryID}" class="btn btn-icon btn-info" title="View">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cat.categoryID}" class="btn btn-icon btn-warning" title="Edit">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/category/delete?id=${cat.categoryID}"
                                       onclick="return confirm('Are you sure you want to delete this category?');"
                                       class="btn btn-icon btn-danger" title="Delete">
                                        <i class="fas fa-trash-alt"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <!-- Không có dữ liệu -->
        <c:if test="${empty categoryList}">
            <div class="alert alert-info mt-4 text-center">
                No categories found in the system.
            </div>
        </c:if>
    </div>
</div>
