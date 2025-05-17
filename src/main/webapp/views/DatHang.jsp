<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.*"%>
<%
// Null check cho sanPham và soLuong
SanPham sanPham = (SanPham) request.getAttribute("sanPham");
Integer soLuong = (Integer) request.getAttribute("soLuong");
if (sanPham == null || soLuong == null) {
    response.sendRedirect(request.getContextPath() + "/trang-chu?error=missingData");
    return;
}
double giaKhuyenMai = sanPham.getGiaKhuyenMai();
double tongTien = giaKhuyenMai * soLuong;
String tongTienFormatted = String.format("%,.0f", tongTien);

// Lấy dữ liệu từ session nếu có lỗi
String fullName = (String) session.getAttribute("fullName");
String phone = (String) session.getAttribute("phone");
String email = (String) session.getAttribute("email");
String address = (String) session.getAttribute("address");
String note = (String) session.getAttribute("note");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Đặt Hàng</title>
	<!-- Bootstrap CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<!-- Font Awesome CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
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

		.btn-close-cart {
    		display: flex;
    		align-items: center;
    		justify-content: center;
    		width: 30px;
    		height: 30px;
    		background-color: var(--secondary-color);
    		color: white;
    		border-radius: 50%;
    		text-decoration: none;
    		font-size: 16px;
    		transition: all 0.3s ease;
		}
		.btn-close-cart:hover {
    		background-color: #d94a20;
    		transform: scale(1.1);
		}
		.btn-close-cart i {
    		line-height: 1;
		}
	</style>
</head>
<body>
    <div class="container">
        <form action="<%=request.getContextPath()%>/dat-hang" method="post" id="orderForm">
            <input type="hidden" name="action" value="datHang">
            <div class="row">
                <!-- Điền thông tin -->
                <div class="col-md-6 mb-4">
                    <div class="form-section">
                        <h3 class="mb-4" style="color: var(--secondary-color);">Thông tin khách hàng</h3>
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Họ và tên</label>
                            <input type="text" class="form-control" name="fullName" value="<%= fullName != null ? fullName : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="phone" class="form-label">Số điện thoại</label>
                            <input type="tel" class="form-control" name="phone" value="<%= phone != null ? phone : "" %>" pattern="[0-9]{10,11}" title="Số điện thoại phải có 10-11 chữ số" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" name="email" value="<%= email != null ? email : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="address" class="form-label">Địa chỉ</label>
                            <textarea class="form-control" name="address" rows="3" required><%= address != null ? address : "" %></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="note" class="form-label">Ghi chú</label>
                            <textarea class="form-control" name="note" rows="2"><%= note != null ? note : "" %></textarea>
                        </div>
                    </div>
                </div>

                <!-- Giỏ hàng -->
                <div class="col-md-6 mb-4">
                    <div class="cart-section p-4 bg-light rounded shadow-sm">
                        <h3 class="mb-4 d-flex justify-content-between align-items-center flex-nowrap" style="color: var(--secondary-color); font-weight: bold;">
    						<span>Giỏ hàng của bạn</span>
    						<a href="<%=request.getContextPath()%>/san-pham" class="btn-close-cart" aria-label="Close">
        						<i class="fa fa-times" aria-hidden="true"></i>
    						</a>
						</h3>
                        <div class="product-list mb-4">
                            <div class="product-item d-flex align-items-center justify-content-between p-3 bg-white rounded mb-2 shadow-sm">
                                <div class="d-flex align-items-center">
                                    <img src="<%=request.getContextPath() + "/images/" + sanPham.getHinhAnh()%>" alt="<%=sanPham.getTenSanPham()%>" class="img-fluid rounded me-3" style="width: 80px; height: 80px; object-fit: cover;">
                                    <div>
                                        <h6 class="mb-0 fw-bold"><%=sanPham.getTenSanPham()%></h6>
                                    </div>
                                </div>
                                <div class="text-center">
                                    <label class="fw-medium">Số lượng: <%=soLuong%></label>
                                </div>
                                <div class="text-end">
                                    <label class="fw-bold text-primary"><%=tongTienFormatted%></label>
                                </div>
                            </div>
                        </div>

                        <!-- Tổng tiền -->
                        <div class="d-flex justify-content-between align-items-center mb-4 p-3 bg-white rounded shadow-sm">
                            <h5 class="mb-0 fw-bold">Tổng tiền:</h5>
                            <h5 class="total-price mb-0 fw-bold text-primary"><%=tongTienFormatted%></h5>
                        </div>

                        <!-- Phương thức thanh toán -->
                        <div class="payment-method mb-4">
                            <h5 class="mb-3 fw-bold">Phương thức thanh toán</h5>
                            <div class="btn-group w-100" role="group">
                                <input type="radio" class="btn-check" name="paymentMethod" id="cash" autocomplete="off" checked>
                                <label class="btn btn-outline-primary rounded-start" for="cash" style="transition: all 0.3s;">Tiền mặt</label>
                                <input type="radio" class="btn-check" name="paymentMethod" id="banking" autocomplete="off">
                                <label class="btn btn-outline-primary rounded-end" for="banking" style="transition: all 0.3s;">Chuyển khoản</label>
                            </div>                       
                        </div>

                        <!-- Trường ẩn -->
                        <input type="hidden" name="maSanPham" value="<%=sanPham.getMaSanPham()%>">
                        <input type="hidden" name="tenSanPham" value="<%=sanPham.getTenSanPham()%>">
                        <input type="hidden" name="hinhAnh" value="<%=sanPham.getHinhAnh()%>">
                        <input type="hidden" name="soLuong" value="<%=soLuong%>">
                        <input type="hidden" name="tongTien" value="<%=tongTien%>">

                        <!-- Nút đặt hàng -->
                        <button type="submit" class="btn btn-primary btn-lg w-100 py-3 fw-bold" style="background-color: var(--primary-color); border: none; transition: all 0.3s;">
                            ĐẶT HÀNG
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Kiểm tra định dạng phía client
        document.getElementById('orderForm').addEventListener('submit', function(event) {
            const phone = document.querySelector('input[name="phone"]').value;
            const email = document.querySelector('input[name="email"]').value;
            const phoneRegex = /^[0-9]{10,11}$/;
            const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;

            if (!phoneRegex.test(phone)) {
                alert('Số điện thoại phải có 10-11 chữ số.');
                event.preventDefault();
                return;
            }

            if (!emailRegex.test(email)) {
                alert('Email không hợp lệ.');
                event.preventDefault();
                return;
            }
        });
    </script>
</body>
</html>