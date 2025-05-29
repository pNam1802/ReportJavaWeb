<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Objects"%>



<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard - iSofa</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminStyles.css">
</head>
<body class="min-h-screen flex">
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
    <div class="flex-1 p-6 lg:ml-64">
        <div class="container mx-auto">
            <!-- Header -->
            <header class="flex justify-between items-center mb-8">
                <h1 class="text-3xl font-bold text-gray-800">Trang Quản Trị</h1>
                <button onclick="toggleSidebar()" class="lg:hidden p-2 rounded bg-gray-200">
                    <i class="bi bi-list"></i>
                </button>
            </header>

            <!-- Welcome Card -->
            <div class="card bg-white rounded-lg shadow-lg p-6 mb-8">
                <h3 class="text-xl font-semibold text-gray-800">Chào mừng Admin</h3>
                <p class="text-gray-600">Quản lý hệ thống iSofa một cách dễ dàng và hiệu quả.</p>
            </div>

            <!-- Dashboard Cards -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="bi bi-box fa-2x text-orange-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Sản phẩm</h4>
                        <p class="text-gray-600">Xem và quản lý danh sách sản phẩm.</p>
                        <a href="${pageContext.request.contextPath}/admin-san-pham" class="btn-primary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="bi bi-people fa-2x text-blue-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Người dùng</h4>
                        <p class="text-gray-600">Quản lý thông tin người dùng.</p>
                        <a href="${pageContext.request.contextPath}/admin/nguoi-dung" class="btn-secondary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="bi bi-cart-check fa-2x text-orange-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Đơn hàng</h4>
                        <p class="text-gray-600">Theo dõi và xử lý đơn hàng.</p>
                        <a href="${pageContext.request.contextPath}/don-hang" class="btn-primary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="bi bi-graph-up fa-2x text-orange-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Thống kê</h4>
                        <p class="text-gray-600">Thống kê doanh thu hàng tháng và các sản phẩm bán chạy.</p>
                        <a href="${pageContext.request.contextPath}/thong-ke" class="btn-secondary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="bi bi-newspaper fa-2x text-blue-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Tin tức</h4>
                        <p class="text-gray-600">Quản lý bài viết và tin tức.</p>
                        <a href="${pageContext.request.contextPath}/QuanLyTinTuc?page=1" class="btn-primary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="bi bi-tag fa-2x text-orange-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Khuyến mãi</h4>
                        <p class="text-gray-600">Tạo và quản lý chương trình khuyến mãi.</p>
                        <a href="${pageContext.request.contextPath}/admin-khuyen-mai" class="btn-secondary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function toggleSidebar() {
            document.querySelector('.sidebar').classList.toggle('sidebar-hidden');
        }
    </script>
</body>
</html>