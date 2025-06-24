<%-- 
    Document   : add
    Created on : May 29, 2025, 7:27:07 AM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/head.jsp" />

<!DOCTYPE html>
<html>
    <head>
        <title>Add Notification</title>
        <!-- Quill Rich Text Editor -->
        <link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="mb-4 text-center">Add Notification</h2>

            <form action="${pageContext.request.contextPath}/admin/notification/add" method="post" class="card p-4 shadow-sm" onsubmit="syncDescription()">
                <div class="mb-3">
                    <label for="title" class="form-label">Title:</label>
                    <input type="text" name="title" id="title" class="form-control" placeholder="Title of notification" required>
                </div>

                <div class="mb-3">
                    <label for="receiver" class="form-label"><span class="text-danger">*</span> Receiver:</label>
                    <select name="receiver" id="receiver" class="form-select" required>
                        <option value="0">All</option>
                        <option value="1">Admin</option>
                        <option value="2">Staff</option>
                        <option value="3">Customer</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label"><span class="text-danger">*</span> Description:</label>
                    <div id="editor" style="height: 150px;"></div>
                    <input type="hidden" name="description" id="description">
                </div>

                <div class="d-flex justify-content-between mt-4">
                    <button type="submit" class="btn btn-success">Submit</button>
                    <button type="reset" class="btn btn-light">Reset</button>
                </div>
            </form>

            <c:if test="${not empty error}">
                <div class="alert alert-danger mt-3">${error}</div>
            </c:if>
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
    </body>
</html>

