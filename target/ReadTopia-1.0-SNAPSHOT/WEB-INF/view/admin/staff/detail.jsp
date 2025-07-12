<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<div class="main-content">
    <div class="content-area">
        <div class="page-header">
            <h1 class="page-title">View Staff Detail</h1>
        </div>

        <form>
            <div class="form-group">
                <label>Staff ID:</label>
                <input type="text" value="${staff.staffID}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Username:</label>
                <input type="text" value="${staff.username}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>First Name:</label>
                <input type="text" value="${staff.firstName}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" value="${staff.lastName}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Date of Birth:</label>
                <input type="text"
                       value="<c:choose><c:when test='${not empty staff.dob}'><fmt:formatDate value='${staff.dob}' pattern='dd/MM/yyyy'/></c:when><c:otherwise>Not specified</c:otherwise></c:choose>"
                               readonly class="form-input" />
                       </div>

                       <div class="form-group">
                           <label>Email:</label>
                               <input type="email" value="${staff.email}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Phone:</label>
                <input type="text" value="${staff.phone}" readonly class="form-input" />
            </div>

            <div class="form-group">
                <label>Sex:</label>
                <select class="form-select" disabled>
                    <option value="1" ${staff.sex == 1 ? 'selected' : ''}>Male</option>
                    <option value="0" ${staff.sex == 0 ? 'selected' : ''}>Female</option>
                </select>
            </div>

            <div class="form-group">
                <label>Role:</label>
                <select class="form-select" disabled>
                    <option value="2" ${staff.role == 2 ? 'selected' : ''}>Seller Staff</option>
                    <option value="3" ${staff.role == 3 ? 'selected' : ''}>Warehouse Staff</option>
                </select>
            </div>

            <div class="form-group">
                <label>Address:</label>
                <textarea class="form-input" readonly>${staff.address}</textarea>
            </div>

            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/admin/staff/list" class="btn btn-secondary">Back to List</a>
            </div>
        </form>
    </div>
</div>
