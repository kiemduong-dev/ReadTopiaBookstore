<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="form-container bg-white shadow p-5 rounded mx-auto" style="max-width: 600px; margin-top: 50px;">
            <h2 class="fw-bold text-center mb-4">✏️ Edit Account</h2>

            <!-- Hiển thị lỗi nếu có -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger"><c:out value="${error}" /></div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/admin/account/edit">
                <div class="form-group">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" value="<c:out value='${account.username}' />" class="form-input" readonly />
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">First Name</label>
                        <input type="text" name="firstName" value="<c:out value='${account.firstName}' />" class="form-input" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label">Last Name</label>
                        <input type="text" name="lastName" value="<c:out value='${account.lastName}' />" class="form-input" required />
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" value="<c:out value='${account.email}' />" class="form-input" required />
                </div>

                <div class="form-group">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" value="<c:out value='${account.phone}' />" class="form-input" />
                </div>

                <div class="form-group">
                    <label class="form-label">Address</label>
                    <textarea name="address" class="form-textarea"><c:out value="${account.address}" /></textarea>
                </div>

                <div class="form-group">
                    <label class="form-label">Date of Birth</label>
                    <input type="date" name="dob" class="form-input" value="<c:out value='${account.dob}' />" />
                </div>

                <div class="form-group">
                    <label class="form-label">Sex</label>
                    <div class="radio-group">
                        <label class="radio-item">
                            <input type="radio" name="sex" value="1" <c:if test="${account.sex == 1}">checked</c:if> /> Male
                            </label>
                            <label class="radio-item">
                                <input type="radio" name="sex" value="0" <c:if test="${account.sex == 0}">checked</c:if> /> Female
                            </label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Role</label>
                        <select name="role" class="form-select">
                            <option value="0" <c:if test="${account.role == 0}">selected</c:if>>Admin</option>
                        <option value="1" <c:if test="${account.role == 1}">selected</c:if>>Customer</option>
                        <option value="2" <c:if test="${account.role == 2}">selected</c:if>>Staff</option>
                        </select>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">Update Account</button>
                        <a href="${pageContext.request.contextPath}/admin/account/list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>
