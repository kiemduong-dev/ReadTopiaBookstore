<%-- 
    Document   : add
    Created on : Jun 17, 2025, 11:40:22 AM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/includes/head.jsp" />
<html>
    <head>
        <title>Add Promotion</title>
    </head>
    <body class="bg-light">

        <div class="container mt-5">
            <h2 class="mb-4">Add New Promotion</h2>

            <form method="post" action="add" class="row g-3">
                <div class="col-md-6">
                    <label for="proName" class="form-label">Name</label>
                    <input type="text" class="form-control" name="proName" id="proName" required>
                </div>

                <div class="col-md-6">
                    <label for="proCode" class="form-label">Code</label>
                    <input type="text" class="form-control" name="proCode" id="proCode" required>
                </div>

                <div class="col-md-4">
                    <label for="discount" class="form-label">Discount (%)</label>
                    <input type="number" step="0.01" class="form-control" name="discount" id="discount" required>
                </div>

                <div class="col-md-4">
                    <label for="startDate" class="form-label">Start Date</label>
                    <input type="date" class="form-control" name="startDate" id="startDate" required>
                </div>

                <div class="col-md-4">
                    <label for="endDate" class="form-label">End Date</label>
                    <input type="date" class="form-control" name="endDate" id="endDate" required>
                </div>

                <div class="col-md-4">
                    <label for="quantity" class="form-label">Quantity</label>
                    <input type="number" class="form-control" name="quantity" id="quantity" required>
                </div>

                <div class="col-md-4">
                    <label for="proStatus" class="form-label">Status</label>
                    <select class="form-select" name="proStatus" id="proStatus">
                        <option value="1" selected>Active</option>
                        <option value="0">Inactive</option>
                    </select>
                </div>

                <div class="col-12 mt-4">
                    <button type="submit" class="btn btn-success">Add Promotion</button>
                    <a href="list" class="btn btn-secondary ms-2">Back to List</a>
                </div>
            </form>
        </div>

    </body>
</html>

