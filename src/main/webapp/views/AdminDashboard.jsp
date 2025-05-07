<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Bàn Ghế Sofa</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0">Trang Quản Trị - Admin Dashboard</h3>
        </div>
        <div class="card-body">
            <p class="lead">Chào mừng <strong>Admin</strong> đến với hệ thống quản trị Bàn Ghế Sofa.</p>

            <div class="row g-3 mt-4">
                <div class="col-md-6">
                    <a href="danh-muc" class="btn btn-outline-primary w-100">Quản lý Danh mục sản phẩm</a>
                </div>
                <div class="col-md-6">
                    <a href="san-pham" class="btn btn-outline-success w-100">Quản lý Sản phẩm</a>
                </div>
                <div class="col-md-6">
                    <a href="nguoi-dung" class="btn btn-outline-warning w-100">Quản lý Người dùng</a>
                </div>
                <div class="col-md-6">
                    <a href="don-hang" class="btn btn-outline-danger w-100">Quản lý Đơn hàng</a>
                </div>
                <div class="col-md-6">
                    <a href="tin-tuc" class="btn btn-outline-secondary w-100">Quản lý Tin tức</a>
                </div>
                <div class="col-md-6">
                    <a href="khuyen-mai" class="btn btn-outline-info w-100">Quản lý Khuyến mãi</a>
                </div>
                <div class="col-md-12 mt-3">
                    <a href="index.jsp" class="btn btn-dark w-100">Đăng xuất</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS (nếu bạn cần tương tác động sau này) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
