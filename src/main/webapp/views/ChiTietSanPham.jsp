<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.SanPham"%>
<% SanPham sanPham = (SanPham) request.getAttribute("sanPham"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><%= sanPham != null ? sanPham.getTenSanPham() : "Chi tiết sản phẩm" %>
	- Chi tiết sản phẩm</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container py-5">
		<div class="row">
			<!-- Phần hình ảnh sản phẩm (bên trái) -->
			<div class="col-md-6">
				<div class="border rounded p-3 bg-white">
					<img
						src="${pageContext.request.contextPath}/images/<%= sanPham != null && sanPham.getHinhAnh() != null ? sanPham.getHinhAnh() : "default.jpg" %>"
						alt="<%= sanPham != null ? sanPham.getTenSanPham() : "Sản phẩm" %>"
						class="img-fluid">
				</div>
			</div>

			<!-- Phần thông tin sản phẩm (bên phải) -->
			<div class="col-md-6">
				<h1 class="fw-bold mb-4"><%= sanPham != null ? sanPham.getTenSanPham() : "Sản phẩm không tồn tại" %></h1>

				<!-- Danh mục sản phẩm -->
				<div class="mb-3 d-flex">
					<span class="fw-semibold" style="min-width: 120px;">Danh
						mục:</span> <span><%= sanPham != null && sanPham.getDanhMuc() != null ? sanPham.getDanhMuc().getTenDanhMuc() : "Không có danh mục" %></span>


				</div>

				<!-- Giá sản phẩm -->
				<div class="my-4">
					<span class="fs-3 fw-bold text-danger"> <%= sanPham != null ? String.format("%,.0f", sanPham.getGiaKhuyenMai()) : "0" %>
						₫
					</span>
					<% if (sanPham != null && sanPham.getGiaGoc() > sanPham.getGiaKhuyenMai()) { %>
					<span class="text-decoration-line-through text-muted fs-5 ms-2">
						<%= String.format("%,.0f", sanPham.getGiaGoc()) %> ₫
					</span> <span class="badge bg-danger text-white ms-2"> Giảm <%= String.format("%.0f", 100 - (sanPham.getGiaKhuyenMai() / sanPham.getGiaGoc()) * 100) %>%
					</span>
					<% } %>
				</div>

				<!-- Tình trạng -->
				<div class="mb-3 d-flex">
					<span class="fw-semibold" style="min-width: 120px;">Tình
						trạng:</span>
					<% if (sanPham != null && sanPham.getSoLuongTonKho() > 0) { %>
					<span class="text-success fw-semibold">Còn hàng (<%= sanPham.getSoLuongTonKho() %>
						sản phẩm)
					</span>
					<% } else { %>
					<span class="text-danger fw-semibold">Hết hàng</span>
					<% } %>
				</div>
				<!-- Mô tả ngắn -->
				<div class="mb-3 d-flex">
					<span class="fw-semibold" style="min-width: 120px;">Mô tả:</span> <span><%= sanPham != null && sanPham.getChiTiet() != null ? sanPham.getChiTiet() : "Không có mô tả" %></span>
				</div>
				<!-- form đặt hàng  chuyển sang trang SanPhamController-->
				<form action="<%=request.getContextPath()%>/san-pham" method="post"
					class="mt-2">
					<!-- Chọn số lượng -->
					<div class="d-flex align-items-center mb-4">
						<span class="fw-semibold" style="min-width: 120px;">Số
							lượng:</span>
						<div class="input-group" style="width: 150px;">
							<button class="btn btn-outline-secondary minus" type="button">-</button>
							<!-- truyền số lương-->
							<input type="number" name="soLuong"
								class="form-control text-center quantity-input" value="1"
								min="1"
								max="<%= sanPham != null ? sanPham.getSoLuongTonKho() : 1 %>">
							<button class="btn btn-outline-secondary plus" type="button">+</button>
						</div>
					</div>

					<!-- Truyền mã sản phẩm -->
					<input type="hidden" name="id" value="<%=sanPham.getMaSanPham()%>">					
					<div class="d-flex gap-2" style="width: 100%;">
						<button type="submit" name="action" value="datHang"
							class="btn btn-primary btn-lg w-100 py-3 fw-bold"
							style="background-color: #000000; border: none;">Đặt
							hàng</button>
						<button type="submit" name="action" value="themVaoGioHang"
							class="btn btn-outline-dark btn-lg w-100 py-3 fw-bold">
							Thêm vào giỏ</button>
					</div>
				</form>

			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script>
        // Xử lý tăng giảm số lượng
        document.querySelector('.minus').addEventListener('click', function() {
            const input = document.querySelector('.quantity-input');
            if (parseInt(input.value) > 1) {
                input.value = parseInt(input.value) - 1;
            }
        });
        
        document.querySelector('.plus').addEventListener('click', function() {
            const input = document.querySelector('.quantity-input');
            const max = parseInt(input.getAttribute('max'));
            if (parseInt(input.value) < max) {
                input.value = parseInt(input.value) + 1;
            }
        });
        
        // Xử lý thêm vào giỏ hàng
        document.querySelector('.btn-add-to-cart').addEventListener('click', function() {
            const quantity = document.querySelector('.quantity-input').value;
            // Gọi API hoặc submit form thêm vào giỏ hàng
            alert('Đã thêm ' + quantity + ' sản phẩm vào giỏ hàng');
            // Có thể thêm logic gửi request POST tới servlet với action=addToCart
        });
        
        // Xử lý mua ngay
        document.querySelector('.btn-buy-now').addEventListener('click', function() {
            const quantity = document.querySelector('.quantity-input').value;
            // Chuyển đến trang thanh toán
            window.location.href = '${pageContext.request.contextPath}/san-pham?action=chiTiet&id=<%= sanPham != null ? sanPham.getMaSanPham() : "" %>&quantity=' + quantity;
        });
    </script>
</body>
</html>