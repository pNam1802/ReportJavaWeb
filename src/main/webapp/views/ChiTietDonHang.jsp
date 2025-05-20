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
<%
    String updateStatus = request.getParameter("updateStatus");
    String removeStatus = request.getParameter("removeStatus");
    String quantityStatus = request.getParameter("quantityStatus");

    String message = null;
    String alertClass = null;

    if ("success".equals(updateStatus)) {
        message = "Cập nhật thông tin đơn hàng thành công!";
        alertClass = "bg-green-500";
    } else if ("fail".equals(updateStatus)) {
        message = "Cập nhật thông tin đơn hàng thất bại!";
        alertClass = "bg-red-500";
    } else if ("success".equals(removeStatus)) {
        message = "Sản phẩm đã được xóa thành công!";
        alertClass = "bg-green-500";
    } else if ("fail".equals(removeStatus)) {
        message = "Xóa sản phẩm thất bại!";
        alertClass = "bg-red-500";
    } else if ("success".equals(quantityStatus)) {
        message = "Cập nhật số lượng thành công!";
        alertClass = "bg-green-500";
    } else if ("fail".equals(quantityStatus)) {
        message = "Cập nhật số lượng thất bại!";
        alertClass = "bg-red-500";
    }
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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/adminStyles.css">
</head>
<body class="min-h-screen flex">
   <%-- THÔNG BÁO --%>
<% if (message != null) { %>
    <div id="notification"
         class="fixed top-4 right-4 z-50 p-4 rounded-md shadow-md text-white <%= alertClass %>">
        <%= message %>
    </div>
    <script>
        setTimeout(() => {
            const notif = document.getElementById("notification");
            if (notif) notif.style.display = "none";
        }, 3000);
    </script>
<% } %>

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
                            <i class="fas fa-save mr-2"></i>Cập nhật thông tin
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
                                    <input type="hidden" name="action" value="removeProduct">
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
                <a href="<%=request.getContextPath() %>/don-hang" class="btn-primary px-4 py-2 rounded-md flex items-center inline-flex">
                    <i class="fas fa-arrow-left mr-2"></i>Trở về Quản lý Đơn hàng
                </a>
            </div>
        </div>
    </div>
<script>
    function showToast(msg, type = 'success') {
        const alertDiv = document.createElement("div");
        alertDiv.className = `fixed top-4 right-4 z-50 p-4 rounded-md shadow-md text-white ${type == 'success' ? 'bg-green-500' : 'bg-red-500'}`;
        alertDiv.innerText = msg;
        document.body.appendChild(alertDiv);
        setTimeout(() => alertDiv.remove(), 3000);
    }
</script>

    <script>
        function toggleSidebar() {
            document.querySelector('.sidebar').classList.toggle('sidebar-hidden');
        }
    </script>
</body>
</html>