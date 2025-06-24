<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/head-admin.jsp" />

<body>
    <jsp:include page="/WEB-INF/includes/sidebar-admin.jsp" />

    <div class="main-content">
        <!-- Dashboard Page -->
        <div id="dashboard" class="page active">
            <div class="content-area">
                <div class="page-header">
                    <h1 class="page-title">Revenue Report This Month</h1>
                </div>

                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon revenue">
                            <i class="fas fa-dollar-sign"></i>
                        </div>
                        <div class="stat-value">₫<c:out value="${totalRevenue}" /></div>
                        <div class="stat-label">Total Revenue</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon orders">
                            <i class="fas fa-shopping-cart"></i>
                        </div>
                        <div class="stat-value"><c:out value="${totalOrders}" /></div>
                        <div class="stat-label">Total Orders</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon users">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-value"><c:out value="${totalUsers}" /></div>
                        <div class="stat-label">Total Users</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon books">
                            <i class="fas fa-book"></i>
                        </div>
                        <div class="stat-value"><c:out value="${totalBooks}" /></div>
                        <div class="stat-label">Total Books</div>
                    </div>
                </div>

                <div class="card">
                    <div class="chart-container">
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>

                <div class="card">
                    <h3 class="card-title">Books Sold</h3>
                    <div class="table-container">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Title Book</th>
                                    <th>Author</th>
                                    <th>Publisher</th>
                                    <th>Sold</th>
                                    <th>Price</th>
                                    <th>Total Price</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="book" items="${topBooks}">
                                    <tr>
                                        <td><c:out value="${book.title}" /></td>
                                        <td><c:out value="${book.author}" /></td>
                                        <td><c:out value="${book.publisher}" /></td>
                                        <td><c:out value="${book.sold}" /></td>
                                        <td><c:out value="${book.price}" /> VND</td>
                                        <td><c:out value="${book.price * book.sold}" /> VND</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Nhúng JS -->
    <script src="${pageContext.request.contextPath}/assets/js/admin-main.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <script>
        // Chuyển List<String> và List<Integer> truyền từ Servlet thành JS Array thủ công để không bị lỗi
        const revenueLabels = [
        <c:forEach items="${revenueLabels}" var="label" varStatus="status">
        '${label}'<c:if test="${!status.last}">,</c:if>
        </c:forEach>
        ];

        const revenueData = [
        <c:forEach items="${revenueData}" var="data" varStatus="status">
            ${data}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
        ];

        // Vẽ chart doanh thu 7 ngày gần đây
        const ctxRevenue = document.getElementById('revenueChart').getContext('2d');
        const revenueChart = new Chart(ctxRevenue, {
            type: 'line',
            data: {
                labels: revenueLabels,
                datasets: [{
                        label: 'Revenue (VND)',
                        data: revenueData,
                        borderColor: '#2196f3',
                        backgroundColor: 'rgba(33, 150, 243, 0.1)',
                        tension: 0.4
                    }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return value.toLocaleString() + ' VNĐ';
                            }
                        }
                    }
                }
            }
        });
    </script>
</body>
</html>
