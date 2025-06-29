<%-- 
    Document   : add
    Created on : May 29, 2025, 7:27:07 AM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<!DOCTYPE html>
<html>
    <head>
        <title>Add Notification</title>
        <!-- Quill Rich Text Editor -->
        <link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
    </head>
    <body>
        <div class="main-content">
            <div class="content-area">
                <div class="page-header">
                    <h2 class="page-title text-center">Add Notification</h2>
                    <p class="page-subtitle text-center">Create a new notification for selected user roles.</p>
                </div>

                <form action="${pageContext.request.contextPath}/admin/notification/add" method="post" class="card" onsubmit="syncDescription()">
                    <div class="form-group">
                        <label for="title" class="form-label">Title:</label>
                        <input type="text" name="title" id="title" class="form-input" placeholder="Title of notification" required>
                    </div>

                    <div class="form-group">
                        <label for="receiver" class="form-label">Receiver: <span style="color:red">*</span></label>
                        <select name="receiver" id="receiver" class="form-select" required>
                            <option value="0">Admin</option>
                            <option value="1">Customer</option>
                            <option value="2">Seller Staff</option>
                            <option value="3">Warehouse Staff</option>
                            <option value="4">All</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Description: <span style="color:red">*</span></label>
                        <div id="editor" style="height: 150px;"></div>
                        <input type="hidden" name="description" id="description">
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-paper-plane"></i> Submit
                        </button>
                        <button type="reset" class="btn btn-secondary">
                            <i class="fas fa-undo"></i> Reset
                        </button>
                    </div>
                </form>

                <c:if test="${not empty error}">
                    <div class="success-message" style="background: #f8d7da; color: #721c24;">
                        ${error}
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Quill JS -->
        <script src="https://cdn.quilljs.com/1.3.6/quill.js"></script>
        <script>
                    var quill = new Quill('#editor', {
                        theme: 'snow'
                    });

                    function syncDescription() {
                        document.getElementById('description').value = quill.root.innerHTML;
                    }
        </script>

        <!-- Optional: Font Awesome for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </body>
</html>
