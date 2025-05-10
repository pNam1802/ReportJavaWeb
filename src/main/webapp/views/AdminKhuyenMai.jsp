<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.KhuyenMai, model.SanPham"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý Khuyến mãi - Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminStyles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminSanPham.css">
</head>
<body class="bg-light">
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

<!-- Main content -->
<div class="main-container">
    <div class="container mt-5">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h3 class="mb-0">Quản lý Khuyến mãi</h3>
            </div>
            <div class="card-body">
                <!-- Form thêm/sửa khuyến mãi -->
                <form action="<%=request.getContextPath()%>/admin-khuyen-mai" method="post" class="mb-4">
                    <% KhuyenMai khuyenMai = (KhuyenMai) request.getAttribute("khuyenMai"); %>
                    <input type="hidden" name="action" value="<%= khuyenMai != null ? "update" : "add" %>">
                    <% if (khuyenMai != null) { %>
                        <input type="hidden" name="maKhuyenMai" value="<%=khuyenMai.getMaKhuyenMai()%>">
                    <% } %>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="maSanPham" class="form-label">Sản phẩm</label>
                            <select class="form-select" id="maSanPham" name="maSanPham" required>
                                <option value="">Chọn sản phẩm</option>
                                <% 
                                    List<SanPham> sanPhams = (List<SanPham>) request.getAttribute("sanPhams");
                                    if (sanPhams != null) {
                                        for (SanPham sp : sanPhams) {
                                            String selected = (khuyenMai != null && khuyenMai.getMaSanPham() == sp.getMaSanPham()) ? "selected" : "";
                                %>
                                    <option value="<%= sp.getMaSanPham() %>" <%= selected %>><%= sp.getTenSanPham() != null ? sp.getTenSanPham() : "" %></option>
                                <% 
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="ngayBatDau" class="form-label">Ngày bắt đầu</label>
                            <input type="date" class="form-control" id="ngayBatDau" name="ngayBatDau" value="<%= khuyenMai != null ? khuyenMai.getNgayBatDau() : "" %>" required>
                        </div>
                        <div class="col-md-6">
                            <label for="ngayKetThuc" class="form-label">Ngày kết thúc</label>
                            <input type="date" class="form-control" id="ngayKetThuc" name="ngayKetThuc" value="<%= khuyenMai != null ? khuyenMai.getNgayKetThuc() : "" %>" required>
                        </div>
                        <div class="col-md-6">
                            <label for="giaKhuyenMai" class="form-label">Giá khuyến mãi (Đồng)</label>
                            <input type="number" step="0.01" class="form-control" id="giaKhuyenMai" name="giaKhuyenMai" value="<%= khuyenMai != null ? khuyenMai.getGiaKhuyenMai() : "" %>" required>
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-success"><%= khuyenMai != null ? "Cập nhật" : "Thêm" %> Khuyến mãi</button>
                            <% if (khuyenMai != null) { %>
                                <a href="<%=request.getContextPath()%>/admin-khuyen-mai" class="btn btn-secondary">Hủy</a>
                            <% } %>
                        </div>
                    </div>
                </form>

                <!-- Danh sách khuyến mãi -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Sản phẩm</th>
                            <th>Ngày bắt đầu</th>
                            <th>Ngày kết thúc</th>
                            <th>Giá khuyến mãi</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<KhuyenMai> khuyenMais = (List<KhuyenMai>) request.getAttribute("khuyenMais");
                            if (khuyenMais != null) {
                                for (KhuyenMai km : khuyenMais) {
                                    SanPham sp = sanPhams != null ? sanPhams.stream()
                                        .filter(s -> s.getMaSanPham() == km.getMaSanPham())
                                        .findFirst()
                                        .orElse(null) : null;
                        %>
                        <tr>
                            <td><%= km.getMaKhuyenMai() %></td>
                            <td><%= sp != null && sp.getTenSanPham() != null ? sp.getTenSanPham() : "N/A" %></td>
                            <td><%= km.getNgayBatDau() %></td>
                            <td><%= km.getNgayKetThuc() %></td>
                            <td><%= km.getGiaKhuyenMai() %></td>
                            <td>
                                <a href="<%=request.getContextPath()%>/admin-khuyen-mai?action=edit&id=<%=km.getMaKhuyenMai()%>" class="btn bg-primary text-white btn-sm">Sửa</a>
                                <form action="<%=request.getContextPath()%>/admin-khuyen-mai" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="maKhuyenMai" value="<%=km.getMaKhuyenMai()%>">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</button>
                                </form>
                            </td>
                        </tr>
                        <% 
                                }
                            }
                        %>
                    </tbody>
                </table>

                <!-- Phân trang -->
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <% 
                            Integer currentPage = (Integer) request.getAttribute("currentPage");
                            Integer totalPages = (Integer) request.getAttribute("totalPages");
                            if (currentPage == null) currentPage = 1;
                            if (totalPages == null) totalPages = 1;

                            if (currentPage > 1) {
                        %>
                        <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/admin-khuyen-mai?page=<%=currentPage - 1%>">«</a></li>
                        <% 
                            }
                            for (int i = 1; i <= totalPages; i++) {
                        %>
                        <li class="page-item <%= (i == currentPage ? "active" : "") %>">
                            <a class="page-link" href="<%=request.getContextPath()%>/admin-khuyen-mai?page=<%=i%>"><%=i%></a>
                        </li>
                        <% 
                            }
                            if (currentPage < totalPages) {
                        %>
                        <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/admin-khuyen-mai?page=<%=currentPage + 1%>">»</a></li>
                        <% 
                            }
                        %>
                    </ul>
                </nav>

                <!-- Thông báo lỗi -->
                <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
                <% if (errorMessage != null) { %>
                    <div class="alert alert-danger"><%=errorMessage%></div>
                <% } %>

                <!-- Nút quay lại Dashboard -->
                <a href="<%=request.getContextPath()%>/views/AdminDashboard.jsp" class="btn btn-secondary mt-3">Quay lại Dashboard</a>
            </div> <!-- card-body -->
        </div> <!-- card -->
    </div> <!-- container -->
</div> <!-- main-container -->

<script>
    function toggleSidebar() {
        document.querySelector('.sidebar').classList.toggle('sidebar-hidden');
    }
</script>
<!-- Bootstrap 5 JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>