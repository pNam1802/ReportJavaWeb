<%@ page import="java.util.List"%>
<%@ page import="model.RevenueData.Revenue"%>
<%@ page import="model.TopProductData.Product"%>
<%@ page import="model.OrderStatusData.Status"%>
<%@ page import="model.RatingData.Rating"%>
<%@ page import="model.InventoryData.Inventory"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thống kê - iSofa</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <!-- Chart.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminStyles.css">
</head>
<body class="min-h-screen flex bg-gray-100">
    <!-- Sidebar -->
    <div class="sidebar fixed top-0 left-0 h-full sidebar-hidden lg:translate-x-0 z-50">
    <div class="header">
        <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="logo" onerror="this.src='${pageContext.request.contextPath}/images/default-logo.png';">
        <h4 class="mb-0">Trang Quản Trị</h4>
        <small>Admin Dashboard</small>
    </div>
    <div class="nav flex-column">
        <a href="${pageContext.request.contextPath}/admin-dashboard" 
           class="nav-link ${request.getServletPath() eq '/admin-dashboard' ? 'active' : ''}">
           <i class="bi bi-house"></i> Tổng quan
        </a>
        <a href="${pageContext.request.contextPath}/san-pham" 
           class="nav-link ${request.getServletPath() eq '/san-pham' ? 'active' : ''}">
           <i class="bi bi-box"></i> Trang sản phẩm
        </a>
        <a href="${pageContext.request.contextPath}/thong-ke" 
            class="nav-link ${request.getServletPath() eq '/admin-dashboard' ? 'active' : ''}">
            <i class="bi bi-graph-up"></i> Thống kê
	    </a>
        <a href="${pageContext.request.contextPath}/admin-san-pham" 
           class="nav-link ${request.getServletPath() eq '/admin-san-pham' ? 'active' : ''}">
           <i class="bi bi-box-seam"></i> Quản lý Sản phẩm
        </a>
        <a href="${pageContext.request.contextPath}/admin/nguoi-dung" 
           class="nav-link ${request.getServletPath() eq '/admin/nguoi-dung' ? 'active' : ''}">
           <i class="bi bi-people"></i> Quản lý Người dùng
        </a>
        <a href="${pageContext.request.contextPath}/don-hang" 
           class="nav-link ${request.getServletPath() eq '/don-hang' ? 'active' : ''}">
           <i class="bi bi-cart-check"></i> Quản lý Đơn hàng
        </a>
        <a href="${pageContext.request.contextPath}/QuanLyTinTuc?page=1" 
           class="nav-link ${request.getServletPath() eq '/QuanLyTinTuc' ? 'active' : ''}">
           <i class="bi bi-newspaper"></i> Quản lý Tin tức
        </a>
        <a href="${pageContext.request.contextPath}/admin-khuyen-mai" 
           class="nav-link ${request.getServletPath() eq '/admin-khuyen-mai' ? 'active' : ''}">
           <i class="bi bi-tag"></i> Quản lý Khuyến mãi
        </a>
        <a href="${pageContext.request.contextPath}/logout-admin" 
           class="nav-link ${request.getServletPath() eq '/logout-admin' ? 'active' : ''}">
           <i class="bi bi-box-arrow-right"></i> Đăng xuất
        </a>
    </div>
</div>

    <!-- Main Content -->
    <div class="flex-1 ml-0 lg:ml-64 p-6 transition-all">
        <div class="container mx-auto">
            <div class="flex justify-between items-center mb-6">
                <h1 class="text-2xl font-bold">Tổng quan thống kê</h1>
                <a href="${pageContext.request.contextPath}/thong-ke?export=pdf" 
                   class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                   Xuất PDF
                </a>
                <a href="${pageContext.request.contextPath}/admin-dashboard?export=excel" 
                   class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                   Xuất Excel
                </a>
            </div>

            <!-- Dashboard Grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Doanh thu theo tháng -->
                <div class="bg-white p-4 rounded-lg shadow">
                    <h2 class="text-lg font-semibold mb-4">Doanh thu theo tháng</h2>
                    <canvas id="revenueChart"></canvas>
                </div>

                <!-- Sản phẩm bán chạy -->
                <div class="bg-white p-4 rounded-lg shadow">
                    <h2 class="text-lg font-semibold mb-4">Top 5 sản phẩm bán chạy</h2>
                    <canvas id="topProductsChart"></canvas>
                </div>

                <!-- Trạng thái đơn hàng -->
                <div class="bg-white p-4 rounded-lg shadow">
                    <h2 class="text-lg font-semibold mb-4">Tỷ lệ trạng thái đơn hàng</h2>
                    <canvas id="orderStatusChart"></canvas>
                </div>

                <!-- Đánh giá sản phẩm -->
                <div class="bg-white p-4 rounded-lg shadow">
                    <h2 class="text-lg font-semibold mb-4">Phân bố điểm đánh giá</h2>
                    <canvas id="ratingChart"></canvas>
                </div>

                <!-- Tồn kho theo danh mục -->
                <div class="bg-white p-4 rounded-lg shadow">
                    <h2 class="text-lg font-semibold mb-4">Tồn kho theo danh mục</h2>
                    <canvas id="inventoryChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript for Charts -->
    <script>
        // Toggle Sidebar
        function toggleSidebar() {
            document.querySelector('.sidebar').classList.toggle('sidebar-hidden');
        }

        // Chart.js Configurations
        // 1. Doanh thu theo tháng
        new Chart(document.getElementById('revenueChart'), {
            type: 'bar',
            data: {
                labels: [<% 
                    List<Revenue> revenueData = (List<Revenue>) request.getAttribute("revenueData");
                    for (int i = 0; i < revenueData.size(); i++) {
                        out.print("'" + revenueData.get(i).getMonth() + "'");
                        if (i < revenueData.size() - 1) out.print(",");
                    }
                %>],
                datasets: [{
                    label: 'Doanh thu (VND)',
                    data: [<% 
                        for (int i = 0; i < revenueData.size(); i++) {
                            out.print(revenueData.get(i).getAmount());
                            if (i < revenueData.size() - 1) out.print(",");
                        }
                    %>],
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: 'Doanh thu (VND)' } },
                    x: { title: { display: true, text: 'Tháng' } }
                },
                plugins: { title: { display: true, text: 'Doanh thu theo tháng' } }
            }
        });

        // 2. Top 5 sản phẩm bán chạy
        new Chart(document.getElementById('topProductsChart'), {
            type: 'pie',
            data: {
                labels: [<% 
                    List<Product> topProducts = (List<Product>) request.getAttribute("topProducts");
                    for (int i = 0; i < topProducts.size(); i++) {
                        out.print("'" + topProducts.get(i).getName() + "'");
                        if (i < topProducts.size() - 1) out.print(",");
                    }
                %>],
                datasets: [{
                    label: 'Số lượng bán',
                    data: [<% 
                        for (int i = 0; i < topProducts.size(); i++) {
                            out.print(topProducts.get(i).getQuantity());
                            if (i < topProducts.size() - 1) out.print(",");
                        }
                    %>],
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.6)',
                        'rgba(54, 162, 235, 0.6)',
                        'rgba(255, 206, 86, 0.6)',
                        'rgba(75, 192, 192, 0.6)',
                        'rgba(153, 102, 255, 0.6)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                plugins: { title: { display: true, text: 'Top 5 sản phẩm bán chạy' } }
            }
        });

        // 3. Tỷ lệ trạng thái đơn hàng
        new Chart(document.getElementById('orderStatusChart'), {
            type: 'doughnut',
            data: {
                labels: [<% 
                    List<Status> orderStatusData = (List<Status>) request.getAttribute("orderStatusData");
                    for (int i = 0; i < orderStatusData.size(); i++) {
                        out.print("'" + orderStatusData.get(i).getStatus() + "'");
                        if (i < orderStatusData.size() - 1) out.print(",");
                    }
                %>],
                datasets: [{
                    label: 'Tỷ lệ đơn hàng',
                    data: [<% 
                        for (int i = 0; i < orderStatusData.size(); i++) {
                            out.print(orderStatusData.get(i).getPercentage());
                            if (i < orderStatusData.size() - 1) out.print(",");
                        }
                    %>],
                    backgroundColor: [
                    	 'rgba(255, 159, 64, 0.6)',     // Cam nhạt
                    	    'rgba(75, 192, 192, 0.6)',     // Xanh ngọc
                    	    'rgba(255, 99, 132, 0.6)',     // Hồng
                    	    'rgba(54, 162, 235, 0.6)',     // Xanh dương
                    	    'rgba(153, 102, 255, 0.6)'     // Tím nhạt
                    ],
                    borderColor: [
                    	'rgba(255, 159, 64, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(153, 102, 255, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                plugins: { title: { display: true, text: 'Tỷ lệ trạng thái đơn hàng' } }
            }
        });

        // 4. Phân bố điểm đánh giá
        new Chart(document.getElementById('ratingChart'), {
            type: 'bar',
            data: {
                labels: [<% 
                    List<Rating> ratingData = (List<Rating>) request.getAttribute("ratingData");
                    for (int i = 0; i < ratingData.size(); i++) {
                        out.print("'" + ratingData.get(i).getRating() + "'");
                        if (i < ratingData.size() - 1) out.print(",");
                    }
                %>],
                datasets: [{
                    label: 'Số lượng đánh giá',
                    data: [<% 
                        for (int i = 0; i < ratingData.size(); i++) {
                            out.print(ratingData.get(i).getCount());
                            if (i < ratingData.size() - 1) out.print(",");
                        }
                    %>],
                    backgroundColor: 'rgba(153, 102, 255, 0.6)',
                    borderColor: 'rgba(153, 102, 255, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                indexAxis: 'y',
                scales: {
                    x: { beginAtZero: true, title: { display: true, text: 'Số lượng đánh giá' } },
                    y: { title: { display: true, text: 'Điểm đánh giá' } }
                },
                plugins: { title: { display: true, text: 'Phân bố điểm đánh giá sản phẩm' } }
            }
        });

        // 5. Tồn kho theo danh mục
        new Chart(document.getElementById('inventoryChart'), {
            type: 'bar',
            data: {
                labels: [<% 
                    List<Inventory> inventoryData = (List<Inventory>) request.getAttribute("inventoryData");
                    for (int i = 0; i < inventoryData.size(); i++) {
                        out.print("'" + inventoryData.get(i).getCategory() + "'");
                        if (i < inventoryData.size() - 1) out.print(",");
                    }
                %>],
                datasets: [{
                    label: 'Số lượng tồn kho',
                    data: [<% 
                        for (int i = 0; i < inventoryData.size(); i++) {
                            out.print(inventoryData.get(i).getStock());
                            if (i < inventoryData.size() - 1) out.print(",");
                        }
                    %>],
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: 'Số lượng tồn kho' } },
                    x: { title: { display: true, text: 'Danh mục' } }
                },
                plugins: { title: { display: true, text: 'Tồn kho theo danh mục' } }
            }
        });
    </script>
</body>
</html>