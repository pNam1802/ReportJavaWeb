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
    <title>Quản lý Đơn hàng - iSofa</title>

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

        .table-container {
            background-color: var(--card-bg);
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            overflow-x: auto;
        }

        .btn-primary {
            background-color: var(--primary-color);
            color: white;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #ea580c;
        }

        .btn-danger {
            background-color: #dc2626;
            color: white;
            transition: background-color 0.3s ease;
        }

        .btn-danger:hover {
            background-color: #b91c1c;
        }

        .btn-info {
            background-color: #0284c7;
            color: white;
            transition: background-color 0.3s ease;
        }

        .btn-info:hover {
            background-color: #026ba4;
        }

        .form-select, .form-control {
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-select:focus, .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(249, 115, 22, 0.25);
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
                <a href="${pageContext.request.contextPath}/don-hang" class="flex items-center p-2 text-gray-700 bg-gray-100 rounded">
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