<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.NguoiDung"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý người dùng - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
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
        <a href="${pageContext.request.contextPath}/admin-dashboard" class="nav-link ${request.getServletPath() eq '/admin-dashboard' ? 'active' : ''}">
            <i class="bi bi-house"></i> Tổng quan
        </a>
        <a href="${pageContext.request.contextPath}/san-pham" class="nav-link ${request.getServletPath() eq '/san-pham' ? 'active' : ''}">
            <i class="bi bi-box"></i> Trang sản phẩm
        </a>
        <a href="${pageContext.request.contextPath}/thong-ke" class="nav-link ${request.getServletPath() eq '/admin-dashboard' ? 'active' : ''}">
            <i class="bi bi-graph-up"></i> Thống kê
	    </a>
        <a href="${pageContext.request.contextPath}/admin-san-pham" class="nav-link ${request.getServletPath() eq '/admin-san-pham' ? 'active' : ''}">
            <i class="bi bi-box-seam"></i> Quản lý Sản phẩm
        </a>
        <a href="${pageContext.request.contextPath}/admin/nguoi-dung" class="nav-link ${request.getServletPath() eq '/admin/nguoi-dung' ? 'active' : ''}">
            <i class="bi bi-people"></i> Quản lý Người dùng
        </a>
        <a href="${pageContext.request.contextPath}/don-hang" class="nav-link ${request.getServletPath() eq '/don-hang' ? 'active' : ''}">
            <i class="bi bi-cart-check"></i> Quản lý Đơn hàng
        </a>
        <a href="${pageContext.request.contextPath}/QuanLyTinTuc?page=1" class="nav-link ${request.getServletPath() eq '/QuanLyTinTuc' ? 'active' : ''}">
            <i class="bi bi-newspaper"></i> Quản lý Tin tức
        </a>
        <a href="${pageContext.request.contextPath}/admin-khuyen-mai" class="nav-link ${request.getServletPath() eq '/admin-khuyen-mai' ? 'active' : ''}">
            <i class="bi bi-tag"></i> Quản lý Khuyến mãi
        </a>
        <a href="${pageContext.request.contextPath}/logout-admin" class="nav-link ${request.getServletPath() eq '/logout-admin' ? 'active' : ''}">
            <i class="bi bi-box-arrow-right"></i> Đăng xuất
        </a>
    </div>
</div>

<!-- Main content -->
<div class="main-container">
    <div class="container mt-5">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h3 class="mb-0">Quản lý Người Dùng</h3>
            </div>
            <div class="card-body">
                <!-- Form thêm/sửa người dùng -->
                <div class="mb-4">
                    <h4><%= request.getAttribute("nguoiDung") != null ? "Cập nhật" : "Thêm" %> Người Dùng</h4>
                    <form action="<%=request.getContextPath()%>/admin/nguoi-dung" method="post" class="mb-4">
                        <% NguoiDung nguoiDung = (NguoiDung) request.getAttribute("nguoiDung"); %>
                        <input type="hidden" name="action" value="<%= nguoiDung != null ? "update" : "add" %>">
                        <% if (nguoiDung != null) { %>
                            <input type="hidden" name="maNguoiDung" value="<%=nguoiDung.getMaNguoiDung()%>">
                        <% } %>
                        <input type="hidden" name="role" value="<%= request.getAttribute("role") != null ? request.getAttribute("role") : "" %>">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="hoTen" class="form-label">Họ Tên</label>
                                <input type="text" class="form-control" id="hoTen" name="hoTen" value="<%= nguoiDung != null ? nguoiDung.getHoTen() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="sdt" class="form-label">Số Điện Thoại</label>
                                <input type="text" class="form-control" id="sdt" name="sdt" value="<%= nguoiDung != null ? nguoiDung.getSdt() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" value="<%= nguoiDung != null ? nguoiDung.getEmail() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="diaChi" class="form-label">Địa Chỉ</label>
                                <input type="text" class="form-control" id="diaChi" name="diaChi" value="<%= nguoiDung != null ? nguoiDung.getDiaChi() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="tenDangNhap" class="form-label">Tên Đăng Nhập</label>
                                <input type="text" class="form-control" id="tenDangNhap" name="tenDangNhap" value="<%= nguoiDung != null ? nguoiDung.getTenDangNhap() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="matKhau" class="form-label">Mật Khẩu</label>
                                <input type="password" class="form-control" id="matKhau" name="matKhau" value="<%= nguoiDung != null ? nguoiDung.getMatKhau() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="vaiTro" class="form-label">Vai Trò</label>
                                <select class="form-control" id="vaiTro" name="vaiTro" required>
                                    <option value="user" <%= nguoiDung != null && "user".equals(nguoiDung.getVaiTro()) ? "selected" : "" %>>Người dùng</option>
                                    <option value="admin" <%= nguoiDung != null && "admin".equals(nguoiDung.getVaiTro()) ? "selected" : "" %>>Quản trị viên</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-success"><%= nguoiDung != null ? "Cập nhật" : "Thêm" %> Người Dùng</button>
                                <% if (nguoiDung != null) { %>
                                    <a href="<%=request.getContextPath()%>/admin/nguoi-dung<%= request.getAttribute("role") != null ? "?role=" + request.getAttribute("role") : "" %>" class="btn btn-secondary">Hủy</a>
                                <% } %>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Form danh sách và xóa người dùng -->
                <div>
                    <h4>Danh Sách Người Dùng</h4>
                    <div class="d-flex justify-content-end mb-3">
                        <form action="<%=request.getContextPath()%>/admin/nguoi-dung" method="get" class="d-flex align-items-center">
                            <label for="roleFilter" class="form-label me-2 mb-0">Lọc theo vai trò:</label>
                            <select class="form-control w-auto" id="roleFilter" name="role" onchange="this.form.submit()">
                                <option value="" <%= request.getAttribute("role") == null || "".equals(request.getAttribute("role")) ? "selected" : "" %>>Tất cả</option>
                                <option value="admin" <%= "admin".equals(request.getAttribute("role")) ? "selected" : "" %>>Quản trị viên</option>
                                <option value="user" <%= "user".equals(request.getAttribute("role")) ? "selected" : "" %>>Người dùng</option>
                            </select>
                        </form>
                    </div>
                    <form action="<%=request.getContextPath()%>/admin/nguoi-dung" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="role" value="<%= request.getAttribute("role") != null ? request.getAttribute("role") : "" %>">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th><input type="checkbox" id="selectAll" onclick="toggleSelectAll()"></th>
                                    <th>Họ Tên</th>
                                    <th>Số Điện Thoại</th>
                                    <th>Email</th>
                                    <th>Địa Chỉ</th>
                                    <th>Tên Đăng Nhập</th>
                                    <th>Vai Trò</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                List<NguoiDung> nguoiDungs = (List<NguoiDung>) request.getAttribute("nguoiDungs");
                                if (nguoiDungs != null) {
                                    for (NguoiDung nd : nguoiDungs) {
                                %>
                                <tr>
                                    <td><input type="checkbox" name="maNguoiDungs" value="<%=nd.getMaNguoiDung()%>"></td>
                                    <td><%=nd.getHoTen()%></td>
                                    <td><%=nd.getSdt()%></td>
                                    <td><%=nd.getEmail()%></td>
                                    <td><%=nd.getDiaChi()%></td>
                                    <td><%=nd.getTenDangNhap()%></td>
                                    <td><%=nd.getVaiTro()%></td>
                                    <td>
                                        <a href="<%=request.getContextPath()%>/admin/nguoi-dung?action=edit&maNguoiDung=<%=nd.getMaNguoiDung()%><%= request.getAttribute("role") != null ? "&role=" + request.getAttribute("role") : "" %>" class="btn bg-primary text-white btn-sm">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                    </td>
                                </tr>
                                <%
                                    }
                                }
                                %>
                            </tbody>
                        </table>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc muốn xóa các người dùng đã chọn?')">Xóa All</button>
                        </div>
                    </form>

                    <!-- Phân trang -->
                    <nav aria-label="Page navigation">
                        <ul class="pagination justify-content-center">
                            <%
                            Integer currentPage = (Integer) request.getAttribute("currentPage");
                            Integer totalPages = (Integer) request.getAttribute("totalPages");
                            String role = (String) request.getAttribute("role");
                            if (currentPage == null) currentPage = 1;
                            if (totalPages == null) totalPages = 1;

                            String roleParam = role != null && !role.isEmpty() ? "&role=" + role : "";
                            if (currentPage > 1) {
                            %>
                            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/admin/nguoi-dung?page=<%=currentPage - 1%><%=roleParam%>">«</a></li>
                            <%
                            }
                            for (int i = 1; i <= totalPages; i++) {
                            %>
                            <li class="page-item <%= (i == currentPage ? "active" : "") %>">
                                <a class="page-link" href="<%=request.getContextPath()%>/admin/nguoi-dung?page=<%=i%><%=roleParam%>"><%=i%></a>
                            </li>
                            <%
                            }
                            if (currentPage < totalPages) {
                            %>
                            <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/admin/nguoi-dung?page=<%=currentPage + 1%><%=roleParam%>">»</a></li>
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

                    <a href="<%=request.getContextPath()%>/views/AdminDashboard.jsp" class="btn btn-secondary mt-3">Quay lại Dashboard</a>
                </div>
            </div> <!-- card-body -->
        </div> <!-- card -->
    </div> <!-- container -->
</div> <!-- main-container -->

<script>
    function toggleSidebar() {
        document.querySelector('.sidebar').classList.toggle('sidebar-hidden');
    }

    function toggleSelectAll() {
        const selectAllCheckbox = document.getElementById('selectAll');
        const checkboxes = document.getElementsByName('maNguoiDungs');
        checkboxes.forEach(checkbox => checkbox.checked = selectAllCheckbox.checked);
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>