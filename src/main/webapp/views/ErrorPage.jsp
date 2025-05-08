<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lỗi - iSofa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-danger text-white">
            <h3 class="mb-0">Đã xảy ra lỗi</h3>
        </div>
        <div class="card-body">
            <p class="text-danger">Lỗi: <%= request.getAttribute("error") != null ? request.getAttribute("error") : "Không xác định lỗi" %></p>
            <% if ("Cannot delete product because it is linked to an order.".equals(request.getAttribute("error"))) { %>
                <p class="text-muted">Sản phẩm này đã được liên kết với một đơn hàng và không thể xóa. Vui lòng kiểm tra lại hoặc liên hệ quản trị viên.</p>
            <% } %>
            <a href="${pageContext.request.contextPath}/admin-san-pham?action=list" class="btn btn-primary mt-3">Quay lại danh sách sản phẩm</a>
            <a href="${pageContext.request.contextPath}/views/AdminDashboard.jsp" class="btn btn-dark mt-3">Quay lại Dashboard</a>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>