<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.DonHang" %>
<%@ page import="java.text.DecimalFormat" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa Đơn Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Sửa Đơn Hàng</h2>
        <%
            DonHang donHang = (DonHang) request.getAttribute("donHang");
            DecimalFormat formatter = new DecimalFormat("#,###.##");

            if (donHang != null) {
        %>
        <form action="${pageContext.request.contextPath}/don-hang" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="maDonHang" value="<%= donHang.getMaDonHang() %>">

            <div class="mb-3">
                <label for="trangThai" class="form-label">Trạng Thái</label>
                <select class="form-select" id="trangThai" name="trangThai" required>
                    <option value="Chờ xử lý" <%= donHang.getTrangThai().equals("Chờ xử lý") ? "selected" : "" %>>Chờ xử lý</option>
                    <option value="Đang giao" <%= donHang.getTrangThai().equals("Đang giao") ? "selected" : "" %>>Đang giao</option>
                    <option value="Hoàn thành" <%= donHang.getTrangThai().equals("Hoàn thành") ? "selected" : "" %>>Hoàn thành</option>
                    <option value="Hủy" <%= donHang.getTrangThai().equals("Hủy") ? "selected" : "" %>>Hủy</option>
                </select>
            </div>

            <div class="mb-3">
                <label for="tongTien" class="form-label">Tổng Tiền</label>
                <input type="text" class="form-control" id="tongTien" name="tongTien" 
                       value="<%= formatter.format(donHang.getTongTien()) %>" required>
            </div>

            <div class="mb-3">
                <label for="maNguoiDung" class="form-label">Mã Người Dùng</label>
                <input type="number" class="form-control" id="maNguoiDung" name="maNguoiDung" 
                       value="<%= donHang.getMaNguoiDung() %>" required>
            </div>

            <button type="submit" class="btn btn-primary">Cập Nhật</button>
            <a href="${pageContext.request.contextPath}/don-hang" class="btn btn-secondary">Quay Lại</a>
        </form>
        <% } else { %>
            <div class="alert alert-danger">Không tìm thấy đơn hàng!</div>
        <% } %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
