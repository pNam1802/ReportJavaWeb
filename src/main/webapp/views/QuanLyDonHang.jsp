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
    <!-- Font Awesome for icons (used in filter and action buttons) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
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
                        <option value="Chờ xử lý" <%= "Chờ xử lý".equals(trangThaiChon) ? "selected" : "" %>>Chờ xử lý</option>
                        <option value="Hoàn thành" <%= "Hoàn thành".equals(trangThaiChon) ? "selected" : "" %>>Hoàn thành</option>
                        <option value="Hủy" <%= "Hủy".equals(trangThaiChon) ? "selected" : "" %>>Đã hủy</option>
                    </select>
                </div>
                <button type="submit" class="btn-primary px-4 py-2 rounded-md flex items-center bg-blue-500 text-white hover:bg-blue-600">
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
                                <span class="px-2 py-1 rounded text-sm <%= dh.getTrangThai().equals("Chờ xử lý") ? "bg-yellow-100 text-yellow-800" : dh.getTrangThai().equals("Đang giao") ? "bg-blue-100 text-blue-800" : dh.getTrangThai().equals("Hoàn thành") ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800" %>">
                                    <%= dh.getTrangThai() %>
                                </span>
                            </td>
                            <td class="p-3"><%= currencyFormatter.format(dh.getTongTien()) %></td>
                            <td class="p-3"><%= dh.getMaNguoiDung() %></td>
                            <td class="p-3 flex gap-2">
                                <a href="don-hang?action=chitiet&maDonHang=<%= dh.getMaDonHang() %>" class="btn-info px-3 py-1 rounded text-sm flex items-center bg-blue-500 text-white hover:bg-blue-600">
                                    <i class="fas fa-eye mr-1"></i>Chi tiết
                                </a>
                                <form action="don-hang" method="post">
                                    <input type="hidden" name="action" value="huy">
                                    <input type="hidden" name="maDonHang" value="<%= dh.getMaDonHang() %>">
                                    <input type="hidden" name="trangThaiMoi" value="<%= trangThaiMoi %>">
                                    <button type="submit" class="btn-danger px-3 py-1 rounded text-sm flex items-center bg-red-500 text-white hover:bg-red-600" onclick="return confirm('Bạn chắc chắn muốn hủy đơn này?')">
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

                <!-- Phân trang -->
                <div class="flex justify-between items-center mt-4">
                    <div>
                        <p class="text-gray-600">
                            Trang <%= request.getAttribute("currentPage") != null ? request.getAttribute("currentPage") : 1 %> / 
                            <%= request.getAttribute("totalPages") != null ? request.getAttribute("totalPages") : 1 %>
                        </p>
                    </div>
                    <div class="flex gap-2">
                        <%
                            int currentPage = request.getAttribute("currentPage") != null ? (Integer) request.getAttribute("currentPage") : 1;
                            int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : 1;
                            String trangThai = request.getParameter("trangThai");
                            String baseUrl = request.getContextPath() + "/don-hang?";
                            if (trangThai != null && !trangThai.isEmpty()) {
                                baseUrl += "trangThai=" + URLEncoder.encode(trangThai, "UTF-8") + "&";
                            }

                            // Nút Trang trước
                            if (currentPage > 1) {
                        %>
                        <a href="<%= baseUrl %>page=<%= currentPage - 1 %>" class="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300 text-gray-800">Trước</a>
                        <%
                            }

                            // Các số trang
                            int startPage = Math.max(1, currentPage - 2);
                            int endPage = Math.min(totalPages, currentPage + 2);
                            for (int i = startPage; i <= endPage; i++) {
                        %>
                        <a href="<%= baseUrl %>page=<%= i %>" class="px-3 py-1 rounded <%= i == currentPage ? "bg-blue-500 text-white" : "bg-gray-200 hover:bg-gray-300 text-gray-800" %>"><%= i %></a>
                        <%
                            }

                            // Nút Trang sau
                            if (currentPage < totalPages) {
                        %>
                        <a href="<%= baseUrl %>page=<%= currentPage + 1 %>" class="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300 text-gray-800">Sau</a>
                        <%
                            }
                        %>
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