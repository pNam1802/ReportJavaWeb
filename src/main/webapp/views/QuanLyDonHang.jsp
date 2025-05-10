<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="model.DonHang"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.net.URLEncoder"%>

<%
    List<DonHang> donHangs = (List<DonHang>) request.getAttribute("donHangs");
    String trangThaiChon = request.getParameter("trangThai");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
%>

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
            width: 250px;
            background: #ffffff;
            padding: 20px 10px;
            border-right: 1px solid #e0e0e0;
            height: 100vh;
            position: fixed;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease-in-out;
            overflow-y: auto;
        }

        .sidebar-hidden {
            transform: translateX(-100%);
        }

        .sidebar .header {          
            color: white;
            padding-left: 20px;
            text-align: center;
            transition: transform 0.3s;
        }

        .sidebar .header:hover {
            transform: scale(1.02);
        }

        .sidebar .logo {
            max-width: 100px;
            margin-bottom: 10px;
        }

        .sidebar .nav-link {
            display: flex;
            align-items: center;
            padding: 12px 15px;
            margin-bottom: 10px;
            border-radius: 8px;
            color: #333;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .sidebar .nav-link i {
            margin-right: 10px;
            font-size: 1.2rem;
        }

        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background: #007bff;
            color: white;
            transform: translateX(5px);
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

        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
            }
            .flex-1 {
                margin-left: 0 !important;
                padding: 15px;
            }
        }
    </style>
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
                <h1 class="text-3xl font-bold text-gray-800">Quản lý Đơn hàng</h1>
                <button onclick="toggleSidebar()" class="lg:hidden p-2 rounded bg-gray-200">
                    <i class="fas fa-bars"></i>
                </button>
            </header>

            <!-- Filter Form -->
            <form action="${pageContext.request.contextPath}/don-hang" method="get" class="flex flex-col sm:flex-row gap-4 mb-6">
                <div class="flex-1">
                    <select name="trangThai" class="form-select w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none">
                        <option value="">Tất cả đơn hàng</option>
                        <option value="Đang xử lý" <%= "Đang xử lý".equals(trangThaiChon) ? "selected" : "" %>>Đang xử lý</option>
                        <option value="Đã giao" <%= "Đã giao".equals(trangThaiChon) ? "selected" : "" %>>Đã giao</option>
                        <option value="Đã hủy" <%= "Đã hủy".equals(trangThaiChon) ? "selected" : "" %>>Đã hủy</option>
                    </select>
                </div>
                <button type="submit" class="btn-primary px-4 py-2 rounded-md flex items-center">
                    <i class="fas fa-filter mr-2"></i>Lọc
                </button>
            </form>

            <!-- Order Table -->
            <div class="table-container p-4">
                <table class="w-full text-left">
                    <thead class="bg-gray-800 text-white">
                        <tr>
                            <th class="p-3">Mã đơn</th>
                            <th class="p-3">Ngày lập</th>
                            <th class="p-3">Trạng thái</th>
                            <th class="p-3">Tổng tiền</th>
                            <th class="p-3">Mã người dùng</th>
                            <th class="p-3">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (donHangs != null && !donHangs.isEmpty()) {
                                for (DonHang dh : donHangs) {
                                    String trangThaiMoi = URLEncoder.encode("Đã hủy", "UTF-8");
                        %>
                        <tr class="border-b hover:bg-gray-50">
                            <td class="p-3"><%= dh.getMaDonHang() %></td>
                            <td class="p-3"><%= sdf.format(dh.getNgayLap()) %></td>
                            <td class="p-3">
                                <span class="px-2 py-1 rounded text-sm <%= dh.getTrangThai().equals("Đang xử lý") ? "bg-yellow-100 text-yellow-800" : dh.getTrangThai().equals("Đã giao") ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800" %>">
                                    <%= dh.getTrangThai() %>
                                </span>
                            </td>
                            <td class="p-3"><%= currencyFormatter.format(dh.getTongTien()) %></td>
                            <td class="p-3"><%= dh.getMaNguoiDung() %></td>
                            <td class="p-3 flex gap-2">
                                <a href="don-hang?action=chitiet&maDonHang=<%= dh.getMaDonHang() %>" class="btn-info px-3 py-1 rounded text-sm flex items-center">
                                    <i class="fas fa-eye mr-1"></i>Chi tiết
                                </a>
                                <form action="don-hang" method="post">
                                    <input type="hidden" name="action" value="huy">
                                    <input type="hidden" name="maDonHang" value="<%= dh.getMaDonHang() %>">
                                    <input type="hidden" name="trangThaiMoi" value="<%= trangThaiMoi %>">
                                    <button type="submit" class="btn-danger px-3 py-1 rounded text-sm flex items-center" onclick="return confirm('Bạn chắc chắn muốn hủy đơn này?')">
                                        <i class="fas fa-trash mr-1"></i>Hủy
                                    </button>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="6" class="p-3 text-center text-gray-500">Không có đơn hàng nào phù hợp</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
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