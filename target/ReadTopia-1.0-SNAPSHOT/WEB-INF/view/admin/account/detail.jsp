<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="form-container bg-white shadow p-5 rounded mx-auto" style="max-width: 600px; margin-top: 50px;">
            <h2 class="fw-bold text-center mb-4">👁 View Account Detail</h2>

            <form>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text" class="form-input" value="${account.username}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">Date of Birth</label>
                        <input type="date" class="form-input" value="${account.dob}" readonly />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">First Name</label>
                        <input type="text" class="form-input" value="${account.firstName}" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">Last Name</label>
                        <input type="text" class="form-input" value="${account.lastName}" readonly />
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-input" value="${account.email}" readonly />
                </div>

                <div class="form-group">
                    <label class="form-label">Phone</label>
                    <input type="text" class="form-input" value="${account.phone}" readonly />
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Role</label>
                        <input type="text" class="form-input" readonly
                               value="<c:choose>
                                        <c:when test='${account.role == 0}'>Admin</c:when>
                                        <c:when test='${account.role == 1}'>Customer</c:when>
                                        <c:otherwise>Unknown</c:otherwise>
                                      </c:choose>"/>
                        <!-- Lưu ý: JSTL không hoạt động bên trong attribute value. Thay bằng cách khác bên dưới -->
                    </div>
                    <div class="form-group">
                        <label class="form-label">Sex</label>
                        <div class="radio-group">
                            <label class="radio-item">
                                <input type="radio" name="sex" disabled
                                    <c:if test="${account.sex == 0}">checked</c:if> /> Female
                            </label>
                            <label class="radio-item">
                                <input type="radio" name="sex" disabled
                                    <c:if test="${account.sex == 1}">checked</c:if> /> Male
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Address</label>
                    <textarea class="form-input form-textarea" readonly>${account.address}</textarea>
                </div>

                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Back to List</a>
                </div>
            </form>
        </div>
    </div>
</div>
