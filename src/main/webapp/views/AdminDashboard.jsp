<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard - iSofa</title>

    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        :root {
            --primary-color: #f97316; /* Orange */
            --secondary-color: #1e40af; /* Dark Blue */
            --bg-color: #f3f4f6; /* Light Gray */
            --card-bg: #ffffff; /* White */
            --text-color: #1f2937; /* Dark Gray */
        }

        body {
            background-color: var(--bg-color);
            color: var(--text-color);
            font-family: 'Inter', sans-serif;
        }

        .sidebar {
            transition: transform 0.3s ease-in-out;
        }

        .sidebar-hidden {
            transform: translateX(-100%);
        }

        .card {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }

        .btn-primary {
            background-color: var(--primary-color);
            color: white;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #ea580c;
        }

        .btn-secondary {
            background-color: var(--secondary-color);
            color: white;
            transition: background-color 0.3s ease;
        }

        .btn-secondary:hover {
            background-color: #1e3a8a;
        }
    </style>
</head>
<body class="min-h-screen flex">
    <!-- Sidebar -->
    <div class="sidebar fixed top-0 left-0 h-full w-64 bg-white shadow-lg p-6 flex flex-col justify-between sidebar-hidden lg:translate-x-0 z-50">
        <div>
            <h2 class="text-2xl font-bold text-gray-800 mb-8">iSofa Admin</h2>
            <nav class="space-y-2">
                <a href="${pageContext.request.contextPath}/san-pham" class="flex items-center p-2 text-gray-700 hover:bg-gray-100 rounded">
                    <i class="fas fa-box mr-2"></i> Trang sản phẩm
                </a>
                <a href="${pageContext.request.contextPath}/admin-san-pham" class="flex items-center p-2 text-gray-700 hover:bg-gray-100 rounded">
                    <i class="fas fa-cubes mr-2"></i> Quản lý Sản phẩm
                </a>
                <a href="${pageContext.request.contextPath}/admin/nguoi-dung" class="flex items-center p-2 text-gray-700 hover:bg-gray-100 rounded">
                    <i class="fas fa-users mr-2"></i> Quản lý Người dùng
                </a>
                <a href="${pageContext.request.contextPath}/don-hang" class="flex items-center p-2 text-gray-700 hover:bg-gray-100 rounded">
                    <i class="fas fa-shopping-cart mr-2"></i> Quản lý Đơn hàng
                </a>
                <a href="${pageContext.request.contextPath}/QuanLyTinTuc?page=1" class="flex items-center p-2 text-gray-700 hover:bg-gray-100 rounded">
                    <i class="fas fa-newspaper mr-2"></i> Quản lý Tin tức
                </a>
                <a href="${pageContext.request.contextPath}/admin-khuyen-mai" class="flex items-center p-2 text-gray-700 hover:bg-gray-100 rounded">
                    <i class="fas fa-tags mr-2"></i> Quản lý Khuyến mãi
                </a>
            </nav>
        </div>
        <a href="${pageContext.request.contextPath}/logout-admin" class="flex items-center p-2 text-red-600 hover:bg-red-100 rounded">
            <i class="fas fa-sign-out-alt mr-2"></i> Đăng xuất
        </a>
    </div>

    <!-- Main Content -->
    <div class="flex-1 p-6 lg:ml-64">
        <div class="container mx-auto">
            <!-- Header -->
            <header class="flex justify-between items-center mb-8">
                <h1 class="text-3xl font-bold text-gray-800">Trang Quản Trị</h1>
                <button onclick="toggleSidebar()" class="lg:hidden p-2 rounded bg-gray-200">
                    <i class="fas fa-bars"></i>
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
                    <i class="fas fa-box fa-2x text-orange-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Sản phẩm</h4>
                        <p class="text-gray-600">Xem và quản lý danh sách sản phẩm.</p>
                        <a href="${pageContext.request.contextPath}/san-pham" class="btn-primary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="fas fa-users fa-2x text-blue-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Người dùng</h4>
                        <p class="text-gray-600">Quản lý thông tin người dùng.</p>
                        <a href="${pageContext.request.contextPath}/admin/nguoi-dung" class="btn-secondary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="fas fa-shopping-cart fa-2x text-orange-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Đơn hàng</h4>
                        <p class="text-gray-600">Theo dõi và xử lý đơn hàng.</p>
                        <a href="${pageContext.request.contextPath}/don-hang" class="btn-primary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="fas fa-newspaper fa-2x text-blue-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Tin tức</h4>
                        <p class="text-gray-600">Quản lý bài viết và tin tức.</p>
                        <a href="${pageContext.request.contextPath}/QuanLyTinTuc?page=1" class="btn-secondary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
                    </div>
                </div>
                <div class="card bg-white rounded-lg shadow-lg p-6 flex items-center">
                    <i class="fas fa-tags fa-2x text-orange-500 mr-4"></i>
                    <div>
                        <h4 class="text-lg font-semibold">Khuyến mãi</h4>
                        <p class="text-gray-600">Tạo và quản lý chương trình khuyến mãi.</p>
                        <a href="${pageContext.request.contextPath}/admin-khuyen-mai" class="btn-primary mt-2 inline-block px-4 py-2 rounded">Truy cập</a>
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