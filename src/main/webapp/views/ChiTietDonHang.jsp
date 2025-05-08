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

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container py-5">

    <% if (donHang != null) { %>
        <!-- FORM CẬP NHẬT -->
        <form action="<%= request.getContextPath() %>/don-hang" method="post" class="card shadow-sm mb-5">
            <input type="hidden" name="action" value="updateInfo">
            <input type="hidden" name="maDonHang" value="<%= donHang.getMaDonHang() %>">

            <div class="card-header bg-primary text-white fs-5">Thông tin đơn hàng</div>
            <div class="card-body">
                <div class="mb-3 row">
                    <label class="col-sm-3 fw-bold">Mã đơn hàng:</label>
                    <div class="col-sm-9"><%= donHang.getMaDonHang() %></div>
                </div>
                <div class="mb-3 row">
                    <label class="col-sm-3 fw-bold">Tên người dùng:</label>
                    <div class="col-sm-9"><%= donHang.getTenNguoiDung() %></div>
                </div>
                <div class="mb-3 row">
                    <label class="col-sm-3 fw-bold">Ngày đặt:</label>
                    <div class="col-sm-9"><%= sdf.format(donHang.getNgayLap()) %></div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Trạng thái:</label>
                    <select name="trangThai" class="form-select">
                        <option <%= donHang.getTrangThai().equals("Chờ xử lý") ? "selected" : "" %>>Chờ xử lý</option>
                        <option <%= donHang.getTrangThai().equals("Đang giao") ? "selected" : "" %>>Đang giao</option>
                        <option <%= donHang.getTrangThai().equals("Hoàn thành") ? "selected" : "" %>>Hoàn thành</option>
                        <option <%= donHang.getTrangThai().equals("Hủy") ? "selected" : "" %>>Hủy</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Trạng thái thanh toán:</label>
                    <select name="trangThaiThanhToan" class="form-select">
                        <option <%= donHang.getTrangThaiThanhToan().equals("Chưa thanh toán") ? "selected" : "" %>>Chưa thanh toán</option>
                        <option <%= donHang.getTrangThaiThanhToan().equals("Đã thanh toán") ? "selected" : "" %>>Đã thanh toán</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Địa chỉ:</label>
                    <input type="text" name="diaChi" class="form-control" value="<%= donHang.getDiaChi() %>">
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-success">Cập nhật thông tin</button>
                </div>
            </div>
        </form>
    <% } %>

    <!-- DANH SÁCH SẢN PHẨM -->
    <div class="card shadow-sm">
        <div class="card-header bg-dark text-white fs-5">Danh sách sản phẩm trong đơn hàng</div>
        <div class="card-body p-0">
            <table class="table table-bordered table-hover m-0">
                <thead class="table-light text-center">
                <tr>
                    <th>#</th>
                    <th>Tên sản phẩm</th>
                    <th style="width: 180px;">Số lượng</th>
                    <th>Giá</th>
                    <th>Tổng</th>
                    <th>Hành động</th>
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
                <tr>
                    <td class="text-center"><%= index++ %></td>
                    <td><%= matchedSanPham.getTenSanPham() %></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/don-hang" method="post" class="d-flex">
                            <input type="hidden" name="action" value="updateQuantity">
                            <input type="hidden" name="maDonHang" value="<%= donHang.getMaDonHang() %>">
                            <input type="hidden" name="maSanPham" value="<%= matchedSanPham.getMaSanPham() %>">
                            <input type="number" name="soLuong" value="<%= item.getSoLuong() %>" min="1"
                                   class="form-control form-control-sm me-2">
                            <button type="submit" class="btn btn-sm btn-outline-primary">Sửa</button>
                        </form>
                    </td>
                    <td><%= String.format("%,.0f", item.getGia()) %> đ</td>
                    <td><%= String.format("%,.0f", thanhTien) %> đ</td>
                    <td class="text-center">
                        <form action="<%= request.getContextPath() %>/don-hang" method="post"
                              onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này không?');">
                            <input type="hidden" name="action" value="deleteProduct">
                            <input type="hidden" name="maDonHang" value="<%= donHang.getMaDonHang() %>">
                            <input type="hidden" name="maSanPham" value="<%= matchedSanPham.getMaSanPham() %>">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Xóa</button>
                        </form>
                    </td>
                </tr>
                <% }}} %>
                <tr class="fw-bold bg-light">
                    <td colspan="4" class="text-end">Tổng cộng:</td>
                    <td><%= String.format("%,.0f", tongTien) %> đ</td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

</div>
</body>
</html>
