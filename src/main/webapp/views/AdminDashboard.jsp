
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h3 class="mb-0">Admin Dashboard</h3>
            </div>
            <div class="card-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <a href="<%=request.getContextPath()%>/admin/danh-muc" class="btn btn-outline-primary w-100">Quản lý Danh mục sản phẩm</a>
                    </div>
                    <div class="col-md-6">
                        <a href="<%=request.getContextPath()%>/admin/san-pham" class="btn btn-outline-success w-100">Quản lý Sản phẩm</a>
                    </div>
                    <div class="col-md-6">
                        <a href="<%=request.getContextPath()%>/admin/nguoi-dung" class="btn btn-outline-warning w-100">Quản lý Người dùng</a>
                    </div>
                    <div class="col-md-6">
                        <a href="<%=request.getContextPath()%>/admin/don-hang" class="btn btn-outline-danger w-100">Quản lý Đơn hàng</a>
                    </div>
                    <div class="col-md-6">
                        <a href="<%=request.getContextPath()%>/admin/tin-tuc" class="btn btn-outline-secondary w-100">Quản lý Tin tức</a>
                    </div>
                    <div class="col-md-6">
                        <a href="<%=request.getContextPath()%>/admin/khuyen-mai" class="btn btn-outline-info w-100">Quản lý Khuyến mãi</a>
                    </div>
                    <div class="col-12">
                        <a href="<%=request.getContextPath()%>/logout-admin" class="btn btn-dark w-100">Đăng xuất</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
