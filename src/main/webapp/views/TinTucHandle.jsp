<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.TinTuc, model.SanPham, dao.QuanLyTinTucDAO, java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý Tin tức - Admin</title>
<!-- Bootstrap 5 CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<!-- Tailwind CSS -->
<script src="https://cdn.tailwindcss.com"></script>

<!-- Custom CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminStyles.css">
    <style>
        .truncate {
            max-width: 250px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            display: inline-block;
        }
        .table th, .table td { vertical-align: middle; }
        .alert-dismissible { margin-bottom: 20px; }
        .pagination .page-link { margin: 0 2px; border-radius: 5px; }
        .pagination .page-item.active .page-link { background-color: #007bff; border-color: #007bff; }
        .action-buttons { min-width: 120px; }
    </style>
</head>
<body class="min-h-screen flex">
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
    <!-- content main -->
    <div class="flex-1 p-6 lg:ml-64">
     <div class="container mx-auto">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h3 class="mb-0">Quản lý Tin tức</h3>
            </div>
            <div class="card-body">
                <!-- Hiển thị thông báo -->
                <% 
                    String message = (String) request.getAttribute("message");
                    String messageType = (String) request.getAttribute("messageType");
                    if (message != null) {
                %>
                    <div class="alert alert-<%=messageType%> alert-dismissible fade show" role="alert">
                        <%=message%>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                <% } %>

                <!-- Form thêm/sửa tin tức -->
                <form action="<%=request.getContextPath()%>/QuanLyTinTuc" method="post" class="mb-4">
                    <% 
                        TinTuc tinTuc = (TinTuc) request.getAttribute("tinTuc");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    %>
                    <input type="hidden" name="action" value="<%= tinTuc != null ? "edit" : "add" %>">
                    <% if (tinTuc != null) { %>
                        <input type="hidden" name="maTinTuc" value="<%=tinTuc.getMaTinTuc()%>">
                    <% } %>
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label for="tieuDe" class="form-label">Tiêu đề</label>
                            <input type="text" class="form-control" id="tieuDe" name="tieuDe" value="<%= tinTuc != null ? tinTuc.getTieuDe() : "" %>" placeholder="Nhập tiêu đề" required>
                        </div>
                        <div class="col-md-3">
                            <label for="noiDung" class="form-label">Nội dung</label>
                            <textarea class="form-control" id="noiDung" name="noiDung" rows="3" placeholder="Nhập nội dung" required><%= tinTuc != null ? tinTuc.getNoiDung() : "" %></textarea>
                        </div>
                        <div class="col-md-2">
                            <label for="ngayDang" class="form-label">Ngày đăng</label>
                            <input type="date" class="form-control" id="ngayDang" name="ngayDang" value="<%= tinTuc != null ? dateFormat.format(tinTuc.getNgayDang()) : "" %>" required>
                        </div>
                        <div class="col-md-2">
                            <label for="maSanPham" class="form-label">Sản phẩm</label>
                            <select class="form-select" id="maSanPham" name="maSanPham" required>
                                <option value="" disabled <%= tinTuc == null ? "selected" : "" %>>Chọn sản phẩm</option>
                                <% 
                                    List<SanPham> products = (List<SanPham>) request.getAttribute("products");
                                    if (products == null || products.isEmpty()) {
                                        request.setAttribute("message", "Không có sản phẩm nào trong cơ sở dữ liệu.");
                                        request.setAttribute("messageType", "warning");
                                    } else {
                                        for (SanPham product : products) {
                                %>
                                    <option value="<%=product.getMaSanPham()%>" <%= tinTuc != null && tinTuc.getMaSanPham() == product.getMaSanPham() ? "selected" : "" %>><%=product.getTenSanPham()%></option>
                                <% 
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        <div class="col-md-2 d-flex align-items-end">
                            <button type="submit" class="btn btn-success w-100"><%= tinTuc != null ? "Cập nhật" : "Thêm" %></button>
                            <% if (tinTuc != null) { %>
                                <a href="<%=request.getContextPath()%>/QuanLyTinTuc" class="btn btn-secondary ms-2">Hủy</a>
                            <% } %>
                        </div>
                    </div>
                </form>

                <!-- Danh sách tin tức -->
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 5%;">STT</th>
                                <th style="width: 20%;">Tiêu đề</th>
                                <th style="width: 30%;">Nội dung</th>
                                <th style="width: 15%;">Ngày đăng</th>
                                <th style="width: 15%;">Sản phẩm</th>
                                <th style="width: 15%;">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                List<TinTuc> tinTucs = (List<TinTuc>) request.getAttribute("tinTucs");
                                Integer currentPage = (Integer) request.getAttribute("currentPage");
                                if (currentPage == null) currentPage = 1;
                                int pageSize = 6;
                                System.out.println("JSP: tinTucs size: " + (tinTucs != null ? tinTucs.size() : "null"));
                                if (tinTucs != null && !tinTucs.isEmpty()) {
                                    int index = (currentPage - 1) * pageSize + 1;
                                    for (TinTuc tinTucItem : tinTucs) {
                            %>
                                <tr>
                                    <td><%=index++%></td>
                                    <td><%=tinTucItem.getTieuDe()%></td>
                                    <td><span class="truncate"><%=tinTucItem.getNoiDung()%></span></td>
                                    <td><%=dateFormat.format(tinTucItem.getNgayDang())%></td>
                                    <td>
                                        <% 
                                            if (products != null) {
                                                for (SanPham product : products) {
                                                    if (product.getMaSanPham() == tinTucItem.getMaSanPham()) {
                                                        out.print(product.getTenSanPham());
                                                        break;
                                                    }
                                                }
                                            } else {
                                                out.print("Không có sản phẩm");
                                            }
                                        %>
                                    </td>
                                    <td class="action-buttons">
                                        <a href="<%=request.getContextPath()%>/QuanLyTinTuc?action=edit&maTinTuc=<%=tinTucItem.getMaTinTuc()%>" class="btn btn-primary btn-sm me-1">Sửa</a>
                                        <form action="<%=request.getContextPath()%>/QuanLyTinTuc" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="maTinTuc" value="<%=tinTucItem.getMaTinTuc()%>">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</button>
                                        </form>
                                    </td>
                                </tr>
                            <% 
                                    }
                                } else {
                            %>
                                <tr>
                                    <td colspan="6" class="text-center">Không có tin tức nào.</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>

                <!-- Phân trang -->
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <% 
                            Integer totalPages = (Integer) request.getAttribute("totalPages");
                            if (totalPages == null) totalPages = 1;
                            if (currentPage > 1) {
                        %>
                            <li class="page-item">
                                <a class="page-link" href="<%=request.getContextPath()%>/QuanLyTinTuc?page=<%=currentPage - 1%>">Trước</a>
                            </li>
                        <% 
                            }
                            int startPage = Math.max(1, currentPage - 2);
                            int endPage = Math.min(totalPages, currentPage + 2);
                            for (int i = startPage; i <= endPage; i++) {
                        %>
                            <li class="page-item <%= i == currentPage ? "active" : "" %>">
                                <a class="page-link" href="<%=request.getContextPath()%>/QuanLyTinTuc?page=<%=i%>"><%=i%></a>
                            </li>
                        <% 
                            }
                            if (currentPage < totalPages) {
                        %>
                            <li class="page-item">
                                <a class="page-link" href="<%=request.getContextPath()%>/QuanLyTinTuc?page=<%=currentPage + 1%>">Sau</a>
                            </li>
                        <% } %>
                    </ul>
                </nav>

                <a href="<%=request.getContextPath()%>/views/AdminDashboard.jsp" class="btn btn-secondary mt-3">Quay lại Dashboard</a>
            </div>
        </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>