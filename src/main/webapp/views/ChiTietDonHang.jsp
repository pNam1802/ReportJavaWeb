<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat, model.DonHangWithUser, model.SanPhamInDonHang" %>
<%@ page import="model.SanPham" %>
<%
    List<DonHangWithUser> donHangWithUsers = (List<DonHangWithUser>) request.getAttribute("donHangWithUsers");
    DonHangWithUser donHang = (donHangWithUsers != null && !donHangWithUsers.isEmpty()) ? donHangWithUsers.get(0) : null;

    List<SanPham> allSanPham = (List<SanPham>) request.getAttribute("danhSachSanPham");

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    double tongTien = 0;
    int index = 1;
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chi tiết Đơn hàng - iSofa</title>

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

        .btn-outline-primary {
            border-color: var(--primary-color);
            color: var(--primary-color);
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        .btn-outline-primary:hover {
            background-color: var(--primary-color);
            color: white;
        }

        .btn-outline-danger {
            border-color: #dc2626;
            color: #dc2626;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        .btn-outline-danger:hover {
            background-color: #dc2626;
            color: white;
        }

        .form-select, .form-control {
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-select:focus, .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(249, 115, 22, 0.25);
        }

        .input-sm {
            width: 200px; /* Compact input size */
        }

        .number-input {
            width: 80px; /* Compact number input size */
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
                <h1 class="text-3xl font-bold text-gray-800">Chi tiết Đơn hàng</h1>
                <button onclick="toggleSidebar()" class="lg:hidden p-2 rounded bg-gray-200">
                    <i class="fas fa-bars"></i>
                </button>
            </header>

            <% if (donHang != null) { %>
            <!-- Order Info Form -->
            <div class="card bg-white rounded-lg shadow-lg p-6 mb-6">
                <h3 class="text-xl font-semibold text-gray-800 mb-4">Thông tin Đơn hàng</h3>
                <form action="<%= request.getContextPath() %>/don-hang" method="post">
                    <input type="hidden" name="action" value="updateInfo">
                    <input type="hidden" name="maDonHang" value="<%= donHang.getMaDonHang() %>">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-700"><i class="fas fa-id-badge mr-2"></i>Mã đơn hàng</label>
                            <p class="mt-1 text-gray-600"><%= donHang.getMaDonHang() %></p>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700"><i class="fas fa-user mr-2"></i>Tên người dùng</label>
                            <p class="mt-1 text-gray-600"><%= donHang.getTenNguoiDung() %></p>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700"><i class="fas fa-calendar-alt mr-2"></i>Ngày đặt</label>
                            <p class="mt-1 text-gray-600"><%= sdf.format(donHang.getNgayLap()) %></p>
                        </div>
                    </div>
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-3">
                        <div>
                            <label class="block text-sm font-medium text-gray-700"><i class="fas fa-info-circle mr-2"></i>Trạng thái</label>
                            <p class="mt-1 text-gray-600 mb-2">
                                <span class="px-2 py-1 rounded text-sm <%= donHang.getTrangThai().equals("Chờ xử lý") ? "bg-yellow-100 text-yellow-800" : donHang.getTrangThai().equals("Đang giao") ? "bg-blue-100 text-blue-800" : donHang.getTrangThai().equals("Hoàn thành") ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800" %>">
                                    <%= donHang.getTrangThai() %>
                                </span>
                            </p>
                            <select name="trangThai" class="form-select input-sm px-3 py-2 border border-gray-300 rounded-md focus:outline-none">
                                <option <%= donHang.getTrangThai().equals("Chờ xử lý") ? "selected" : "" %>>Chờ xử lý</option>
                                <option <%= donHang.getTrangThai().equals("Đang giao") ? "selected" : "" %>>Đang giao</option>
                                <option <%= donHang.getTrangThai().equals("Hoàn thành") ? "selected" : "" %>>Hoàn thành</option>
                                <option <%= donHang.getTrangThai().equals("Hủy") ? "selected" : "" %>>Hủy</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700"><i class="fas fa-credit-card mr-2"></i>Trạng thái thanh toán</label>
                            <p class="mt-1 text-gray-600 mb-2">
                                <span class="px-2 py-1 rounded text-sm <%= donHang.getTrangThaiThanhToan().equals("Chưa thanh toán") ? "bg-red-100 text-red-800" : "bg-green-100 text-green-800" %>">
                                    <%= donHang.getTrangThaiThanhToan() %>
                                </span>
                            </p>
                            <select name="trangThaiThanhToan" class="form-select input-sm px-3 py-2 border border-gray-300 rounded-md focus:outline-none">
                                <option <%= donHang.getTrangThaiThanhToan().equals("Chưa thanh toán") ? "selected" : "" %>>Chưa thanh toán</option>
                                <option <%= donHang.getTrangThaiThanhToan().equals("Đã thanh toán") ? "selected" : "" %>>Đã thanh toán</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700"><i class="fas fa-map-marker-alt mr-2"></i>Địa chỉ</label>
                            <input type="text" name="diaChi" class="form-control input-sm px-3 py-2 border border-gray-300 rounded-md focus:outline-none" value="<%= donHang.getDiaChi() %>">
                        </div>
                    </div>
                    <div class="text-end mt-4">
                        <button type="submit" class="btn-primary px-4 py-2 rounded-md flex items-center">
                            <i classquirrel fas fa-save mr-2"></i>Cập nhật thông tin
                        </button>
                    </div>
                </form>
            </div>
            <% } %>

            <!-- Product List Table -->
            <div class="table-container p-4">
                <h3 class="text-xl font-semibold text-gray-800 mb-4">Danh sách Sản phẩm trong Đơn hàng</h3>
                <table class="w-full text-left">
                    <thead class="bg-gray-800 text-white">
                        <tr>
                            <th class="p-3">#</th>
                            <th class="p-3">Tên sản phẩm</th>
                            <th class="p-3">Số lượng</th>
                            <th class="p-3">Giá</th>
                            <th class="p-3">Tổng</th>
                            <th class="p-3">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (donHang != null && donHang.getSanPhamList() != null && allSanPham != null) {
                                for (SanPhamInDonHang item : donHang.getSanPhamList()) {
                                    SanPham matchedSanPham = null;
                                    for (SanPham sp : allSanPham) {
                                        if (sp.getTenSanPham().equalsIgnoreCase(item.getTenSanPham())) {
                                            matchedSanPham = sp;
                                            break;
                                        }
                                    }

                                    if (matchedSanPham != null) {
                                        double thanhTien = item.getSoLuong() * item.getGia();
                                        tongTien += thanhTien;
                        %>
                        <tr class="border-b hover:bg-gray-50">
                            <td class="p-3 text-center"><%= index++ %></td>
                            <td class="p-3"><%= matchedSanPham.getTenSanPham() %></td>
                            <td class="p-3">
                                <form action="<%= request.getContextPath() %>/don-hang" method="post" class="flex items-center gap-2">
                                    <input type="hidden" name="action" value="updateQuantity">
                                    <input type="hidden" name="maDonHang" value="<%= donHang.getMaDonHang() %>">
                                    <input type="hidden" name="maSanPham" value="<%= matchedSanPham.getMaSanPham() %>">
                                    <input type="number" name="soLuong" value="<%= item.getSoLuong() %>" min="1"
                                           class="form-control number-input px-2 py-1 border border-gray-300 rounded-md focus:outline-none">
                                    <button type="submit" class="btn-outline-primary px-3 py-1 rounded text-sm flex items-center">
                                        <i class="fas fa-edit mr-1"></i>Sửa
                                    </button>
                                </form>
                            </td>
                            <td class="p-3"><%= String.format("%,.0f", item.getGia()) %> đ</td>
                            <td class="p-3"><%= String.format("%,.0f", thanhTien) %> đ</td>
                            <td class="p-3 text-center">
                                <form action="<%= request.getContextPath() %>/don-hang" method="post"
                                      onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này không?');">
                                    <input type="hidden" name="action" value="deleteProduct">
                                    <input type="hidden" name="maDonHang" value="<%= donHang.getMaDonHang() %>">
                                    <input type="hidden" name="maSanPham" value="<%= matchedSanPham.getMaSanPham() %>">
                                    <button type="submit" class="btn-outline-danger px-3 py-1 rounded text-sm flex items-center mx-auto">
                                        <i class="fas fa-trash mr-1"></i>Xóa
                                    </button>
                                </form>
                            </td>
                        </tr>
                        <% }}} %>
                        <tr class="font-semibold bg-gray-50">
                            <td colspan="4" class="p-3 text-right">Tổng cộng:</td>
                            <td class="p-3"><%= String.format("%,.0f", tongTien) %> đ</td>
                            <td class="p-3"></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Back Button -->
            <div class="text-end mt-6">
                <a href="<%= request.getContextPath() %>/don-hang" class="btn-primary px-4 py-2 rounded-md flex items-center inline-flex">
                    <i class="fas fa-arrow-left mr-2"></i>Trở về Quản lý Đơn hàng
                </a>
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