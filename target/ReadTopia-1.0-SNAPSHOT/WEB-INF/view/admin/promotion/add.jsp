<%-- 
    Document   : add
    Created on : Jun 17, 2025, 11:40:22 AM
    Author     : ngtua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/includes/head-admin.jsp" />
<jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

<html>
    <head>
        <title>Add Promotion</title>
    </head>
    <body>
        <div class="main-content">
            <div class="content-area">
                <div class="page-header">
                    <h2 class="page-title">Add New Promotion</h2>
                    <p class="page-subtitle">Create a new promotion code for campaigns</p>
                </div>

                <form method="post" action="add">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="proName" class="form-label">Name</label>
                            <input type="text" class="form-input" name="proName" id="proName" required>
                        </div>

                        <div class="form-group">
                            <label for="proCode" class="form-label">Code</label>
                            <input type="text" class="form-input" name="proCode" id="proCode" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="discount" class="form-label">Discount (%)</label>
                            <input type="number" step="0.01" class="form-input" name="discount" id="discount" required>
                        </div>

                        <div class="form-group">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-input" name="startDate" id="startDate" required>
                        </div>

                        <div class="form-group">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-input" name="endDate" id="endDate" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="quantity" class="form-label">Quantity</label>
                            <input type="number" class="form-input" name="quantity" id="quantity" required>
                        </div>

                        <div class="form-group">
                            <label for="proStatus" class="form-label">Status</label>
                            <select class="form-select" name="proStatus" id="proStatus">
                                <option value="1" selected>Active</option>
                                <option value="0">Inactive</option>
                            </select>
                        </div>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add Promotion
                        </button>
                        <a href="list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
