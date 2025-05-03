<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.*" %>
<%@ page import="dao.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
:root {
	--primary-color: #000000;
	--secondary-color: #f25a29;
}

body {
	background-color: #f8f9fa;
	font-family: 'Arial', sans-serif;
}

.header {
	background-color: var(--primary-color);
	color: white;
	padding: 15px 0;
	margin-bottom: 30px;
}

.form-section {
	background-color: white;
	padding: 25px;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.cart-section {
	background-color: white;
	padding: 25px;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.form-control:focus {
	border-color: var(--secondary-color);
	box-shadow: 0 0 0 0.25rem rgba(242, 90, 41, 0.25);
}

.btn-primary {
	background-color: var(--primary-color);
	border-color: var(--primary-color);
}

.btn-primary:hover {
	background-color: #333333;
	border-color: #333333;
}

.btn-check:checked+.btn-outline-primary {
	background-color: var(--secondary-color);
	border-color: var(--secondary-color);
	color: white;
}

.btn-outline-primary {
	color: var(--secondary-color);
	border-color: var(--secondary-color);
}

.btn-outline-primary:hover {
	background-color: var(--secondary-color);
	border-color: var(--secondary-color);
	color: white;
}

.total-price {
	color: var(--secondary-color);
	font-weight: bold;
	font-size: 1.2rem;
}

.product-item {
	border-bottom: 1px solid #eee;
	padding: 10px 0;
}

.product-item:last-child {
	border-bottom: none;
}
</style>
</head>
<body>
<div class="container mt-4">
    <form action="<%=request.getContextPath()%>/dat-hang" method="post">
        <div class="row">

            <!-- THÔNG TIN KHÁCH HÀNG -->
            <div class="col-md-6 mb-4">
                <div class="form-section">
                    <h3 class="mb-4 text-secondary">Thông tin khách hàng</h3>
                    <div class="mb-3">
                        <label class="form-label">Họ và tên</label>
                        <input type="text" class="form-control" name="fullName" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="tel" class="form-control" name="phone" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Địa chỉ</label>
                        <textarea class="form-control" name="address" rows="3" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Ghi chú</label>
                        <textarea class="form-control" name="note" rows="2"></textarea>
                    </div>
                </div>
            </div>

            <!-- GIỎ HÀNG -->
            <div class="col-md-6 mb-4">
    <div class="cart-section">
        <h3 class="mb-4 text-secondary fw-bold">Giỏ hàng của bạn</h3>

        <%
            GioHang gioHang = (GioHang) request.getAttribute("gioHang");
            double tongCong = 0;

            if (gioHang != null) {
                for (Map.Entry<Integer, GioHangItem> entry : gioHang.getDanhSach().entrySet()) {
                    GioHangItem gioHangItem = entry.getValue();
                  //tạo SanPhamDAO để sử dụng phương thức lấy đối tượng sản phẩm thông qua ID để có thể gọi số lượng tồn kho
					SanPhamDAO sanPhamDAO = new SanPhamDAO();
					int id = gioHangItem.getMaSanPham();
					SanPham sanPham = sanPhamDAO.getById(id);                  
                    int soLuong = gioHangItem.getSoLuong();     // Lấy số lượng từ GioHangItem
                    double donGia = sanPham.getGiaKhuyenMai();  // Lấy giá khuyến mãi của sản phẩm
                    double thanhTien = donGia * soLuong;        // Tính thành tiền
                    tongCong += thanhTien;                      // Cộng dồn tổng cộng
                    String thanhTienFormatted = String.format("%,.0f", thanhTien);
        %>
            <div class="product-item d-flex align-items-center justify-content-between mb-3">
                <div class="d-flex align-items-center">
                    <img src="<%=request.getContextPath()%>/images/<%=sanPham.getHinhAnh()%>" 
                         class="rounded me-3" style="width: 80px; height: 80px; object-fit: cover;" 
                         alt="<%=sanPham.getTenSanPham()%>">
                    <div>
                        <h6 class="mb-1"><%=sanPham.getTenSanPham()%></h6>
                        <small>Giá: <%=String.format("%,.0f", donGia)%> VNĐ</small><br>
                        <small>Số lượng: <%=soLuong%></small>
                    </div>
                </div>
                <div>
                    <span class="fw-bold text-primary"><%=thanhTienFormatted%> VNĐ</span>
                </div>

                <!-- Hidden fields để gửi dữ liệu -->
                <input type="hidden" name="maSanPham[]" value="<%=sanPham.getMaSanPham()%>">
			    <input type="hidden" name="tenSanPham[]" value="<%=sanPham.getTenSanPham()%>">
			    <input type="hidden" name="soLuong[]" value="<%=soLuong%>">
			    <input type="hidden" name="donGia[]" value="<%=donGia%>">
			    <input type="hidden" name="thanhTien[]" value="<%=thanhTien%>">
            </div>
        <%
                } // end for
            } else {
        %>
            <p>Không có sản phẩm nào trong giỏ hàng.</p>
        <%
            }
        %>

        <!-- Tổng tiền -->
        <div class="d-flex justify-content-between align-items-center my-4 p-3 bg-light rounded">
            <h5 class="mb-0">Tổng cộng:</h5>
            <h5 class="total-price mb-0"><%=String.format("%,.0f", tongCong)%> VNĐ</h5>
            <input type="hidden" name="tongCong" value="<%=tongCong%>">
        </div>

        <!-- Phương thức thanh toán -->
        <div class="mb-4">
            <h5 class="mb-3 fw-bold">Phương thức thanh toán</h5>
            <div class="btn-group w-100" role="group">
                <input type="radio" class="btn-check" name="paymentMethod" id="cash" value="cash" checked>
                <label class="btn btn-outline-primary" for="cash">Tiền mặt</label>

                <input type="radio" class="btn-check" name="paymentMethod" id="banking" value="banking">
                <label class="btn btn-outline-primary" for="banking">Chuyển khoản</label>
            </div>
        </div>

        <!-- Nút đặt hàng -->
        <button type="submit" class="btn btn-primary btn-lg w-100" name="action" value="GioHangThanhToan">ĐẶT HÀNG</button>
    </div>
</div>

        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
