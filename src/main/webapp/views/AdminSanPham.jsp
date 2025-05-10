<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.SanPham, model.DanhMuc, java.text.NumberFormat, java.util.Locale" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý sản phẩm - Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/adminStyles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/adminSanPham.css">
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

    <div class="flex-1" style="margin-left: 250px; padding: 20px;">
        <div class="container mt-5">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h3 class="mb-0">Quản lý sản phẩm</h3>
                </div>
                <div class="card-body">
                    <!-- Error Message -->
                    <% String errorMessage = (String) request.getAttribute("errorMessage");
                       if (errorMessage != null && !errorMessage.isEmpty()) { %>
                        <div class="alert alert-danger"><%= errorMessage %></div>
                    <% } %>

                    <!-- Add Product Button -->
                    <button id="addProductButton" class="btn btn-success mb-3">
                        <i class="fas fa-plus me-2"></i>Thêm sản phẩm
                    </button>

                    <!-- Form thêm/sửa sản phẩm -->
                    <div id="productForm" class="collapse mb-4 <%= request.getAttribute("sanPham") != null ? "show" : "" %>">
                        <% SanPham sanPham = (SanPham) request.getAttribute("sanPham"); %>
                        <form action="<%= request.getContextPath() %>/admin-san-pham" method="post" enctype="multipart/form-data" class="mb-4">
                            <input type="hidden" name="action" value="<%= sanPham != null ? "update" : "add" %>">
                            <% if (sanPham != null) { %>
                                <input type="hidden" name="maSanPham" value="<%= sanPham.getMaSanPham() %>">
                                <input type="hidden" name="giaKhuyenMai" value="<%= sanPham.getGiaKhuyenMai() %>">
                            <% } %>
                            <div class="row g-2">
                                <div class="col-6">
                                    <label for="maSanPham" class="form-label">Mã sản phẩm</label>
                                    <input type="number" class="form-control" id="maSanPham" name="maSanPham" value="<%= sanPham != null ? sanPham.getMaSanPham() : (request.getAttribute("maSanPham") != null ? request.getAttribute("maSanPham") : "") %>" <%= sanPham != null ? "readonly" : "" %> required>
                                    <div id="maSanPhamError" class="invalid-feedback">
                                        <%= request.getAttribute("maSanPhamError") != null ? request.getAttribute("maSanPhamError") : "" %>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <label for="tenSanPham" class="form-label">Tên sản phẩm</label>
                                    <input type="text" class="form-control" id="tenSanPham" name="tenSanPham" value="<%= sanPham != null ? sanPham.getTenSanPham() : (request.getAttribute("tenSanPham") != null ? request.getAttribute("tenSanPham") : "") %>" required>
                                </div>

                                <div class="col-6">
                                    <label for="maDanhMuc" class="form-label">Danh mục</label>
                                    <select class="form-control" id="maDanhMuc" name="maDanhMuc" required>
                                        <% List<DanhMuc> danhMucs = (List<DanhMuc>) request.getAttribute("danhMucs");
                                           String selectedMaDanhMuc = (String) request.getAttribute("maDanhMuc");
                                           if (danhMucs != null) {
                                               for (DanhMuc danhMuc : danhMucs) { %>
                                                   <option value="<%= danhMuc.getMaDanhMuc() %>" <%= (sanPham != null && danhMuc.getMaDanhMuc() == sanPham.getDanhMuc().getMaDanhMuc()) || (selectedMaDanhMuc != null && selectedMaDanhMuc.equals(String.valueOf(danhMuc.getMaDanhMuc()))) ? "selected" : "" %>>
                                                       <%= danhMuc.getTenDanhMuc() %>
                                                   </option>
                                        <%     }
                                           } %>
                                    </select>
                                </div>
                                <div class="col-6">
                                    <label for="giaGoc" class="form-label">Giá gốc (Đồng)</label>
                                    <input type="number" step="0.01" class="form-control" id="giaGoc" name="giaGoc" value="<%= sanPham != null ? sanPham.getGiaGoc() : (request.getAttribute("giaGoc") != null ? request.getAttribute("giaGoc") : "") %>" required>
                                </div>

                                <div class="col-6">
                                    <label for="tinhTrang" class="form-label">Tình trạng</label>
                                    <input type="text" class="form-control" id="tinhTrang" name="tinhTrang" value="<%= sanPham != null ? sanPham.getTinhTrang() : (request.getAttribute("tinhTrang") != null ? request.getAttribute("tinhTrang") : "") %>" required>
                                </div>
                                <div class="col-6">
                                    <label for="soLuongTonKho" class="form-label">Số lượng tồn kho</label>
                                    <input type="number" class="form-control" id="soLuongTonKho" name="soLuongTonKho" value="<%= sanPham != null ? sanPham.getSoLuongTonKho() : (request.getAttribute("soLuongTonKho") != null ? request.getAttribute("soLuongTonKho") : "") %>" required>
                                </div>

                                <div class="col-6">
                                    <label for="hinhAnh" class="form-label">Hình ảnh</label>
                                    <input type="file" class="form-control" id="hinhAnh" name="hinhAnh" accept="image/*">
                                    <% if (sanPham != null && sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) { %>
                                        <img src="<%= request.getContextPath() %>/images/<%= sanPham.getHinhAnh() %>" alt="Hình ảnh sản phẩm" class="img-thumbnail mt-2" style="max-width: 100px;">
                                    <% } %>
                                </div>
                                <div class="col-6">
                                    <label for="chiTiet" class="form-label">Chi tiết</label>
                                    <textarea class="form-control" id="chiTiet" name="chiTiet" rows="4"><%= sanPham != null && sanPham.getChiTiet() != null ? sanPham.getChiTiet() : (request.getAttribute("chiTiet") != null ? request.getAttribute("chiTiet") : "") %></textarea>
                                </div>

                                <div class="col-12">
                                    <button type="submit" class="btn btn-success"><%= sanPham != null ? "Cập nhật" : "Thêm" %> sản phẩm</button>
                                    <% if (sanPham != null) { %>
                                        <a href="<%= request.getContextPath() %>/admin-san-pham" class="btn btn-secondary">Hủy</a>
                                    <% } else { %>
                                        <button type="button" id="cancelFormButton" class="btn btn-secondary">Hủy</button>
                                    <% } %>
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- Product Table -->
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên sản phẩm</th>
                                <th>Danh mục</th>
                                <th>Giá gốc</th>
                                <th>Giá khuyến mãi</th>
                                <th>Tình trạng</th>
                                <th>Số lượng tồn kho</th>
                                <th>Hình ảnh</th>
                                <th>Chi Tiết</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% List<SanPham> sanPhams = (List<SanPham>) request.getAttribute("sanPhams");
                               NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                               if (sanPhams != null) {
                                   for (SanPham sp : sanPhams) { %>
                                       <tr>
                                           <td><%= sp.getMaSanPham() %></td>
                                           <td><%= sp.getTenSanPham() %></td>
                                           <td><%= sp.getDanhMuc().getTenDanhMuc() %></td>
                                           <td><%= currencyFormat.format(sp.getGiaGoc()) %></td>
                                           <td><%= currencyFormat.format(sp.getGiaKhuyenMai()) %></td>
                                           <td><%= sp.getTinhTrang() %></td>
                                           <td><%= sp.getSoLuongTonKho() %></td>
                                           <td>
                                               <% if (sp.getHinhAnh() != null && !sp.getHinhAnh().isEmpty()) { %>
                                                   <img src="<%= request.getContextPath() %>/images/<%= sp.getHinhAnh() %>" alt="Hình ảnh sản phẩm" class="img-thumbnail" style="max-width: 50px;">
                                               <% } %>
                                           </td>
                                           <td><%= sp.getChiTiet() %></td>
                                           <td class="action-buttons">
                                               <a href="<%= request.getContextPath() %>/admin-san-pham?action=edit&maSanPham=<%= sp.getMaSanPham() %>" class="btn bg-primary text-white btn-sm">Sửa</a>
                                               <form action="<%= request.getContextPath() %>/admin-san-pham" method="post" style="display: inline;" onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">
                                                   <input type="hidden" name="action" value="delete">
                                                   <input type="hidden" name="maSanPham" value="<%= sp.getMaSanPham() %>">
                                                   <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
                                               </form>
                                           </td>
                                       </tr>
                            <%     }
                               } %>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <% Integer currentPage = (Integer) request.getAttribute("currentPage");
                       Integer totalPages = (Integer) request.getAttribute("totalPages");
                       if (currentPage == null) currentPage = 1;
                       if (totalPages == null) totalPages = 1;
                       if (currentPage != null && totalPages != null) { %>
                           <nav aria-label="Page navigation">
                               <ul class="pagination justify-content-center">
                                   <% if (currentPage > 1) { %>
                                       <li class="page-item">
                                           <a class="page-link" href="<%= request.getContextPath() %>/admin-san-pham?page=<%= currentPage - 1 %>" aria-label="Previous">
                                               <span aria-hidden="true">«</span>
                                           </a>
                                       </li>
                                   <% } %>
                                   <% for (int i = 1; i <= totalPages; i++) { %>
                                       <li class="page-item <%= i == currentPage ? "active" : "" %>">
                                           <a class="page-link" href="<%= request.getContextPath() %>/admin-san-pham?page=<%= i %>"><%= i %></a>
                                       </li>
                                   <% } %>
                                   <% if (currentPage < totalPages) { %>
                                       <li class="page-item">
                                           <a class="page-link" href="<%= request.getContextPath() %>/admin-san-pham?page=<%= currentPage + 1 %>" aria-label="Next">
                                               <span aria-hidden="true">»</span>
                                           </a>
                                       </li>
                                   <% } %>
                               </ul>
                           </nav>
                    <% } %>
                </div>
            </div>
            <a href="<%= request.getContextPath() %>/views/AdminDashboard.jsp" class="btn btn-secondary mt-3">Quay lại Dashboard</a>
        </div>

        <!-- Bootstrap 5 JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Custom JS -->
        <script>
            window.contextPath = '<%= request.getContextPath() %>';   
        </script>
        <script src="<%= request.getContextPath() %>/js/adminSanPham.js"></script>
    </div>
</body>
</html>