<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="model.GioHang"%>
<%@ page import="model.GioHangItem"%>
<%@ page import="dao.SanPhamDAO"%>
<%@ page import="model.SanPham"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="java.util.Locale"%>
<!DOCTYPE html>
<html lang="vi">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Giỏ Hàng</title>
	
	<!-- Bootstrap CSS từ CDN -->
	<link rel="stylesheet"
		href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
		integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
	<!-- Font Awesome để dùng icon -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
	<!-- Style của chính mình -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">

	<style> /* Custom color scheme */ .btn-primary, .btn-success {
		background-color: #f25a29 !important;
		border-color: #f25a29 !important;
	}

	.btn-primary:hover, .btn-success:hover {
		background-color: #e04a1a !important;
		border-color: #e04a1a !important;
	}

	.btn-outline-secondary {
		color: #f25a29;
		border-color: #f25a29;
	}

	.btn-outline-secondary:hover {
		background-color: #f25a29;
		color: white;
	}

	.text-primary {
		color: #f25a29 !important;
	}

	.card-header, .bg-dark {
		background-color: #3d3e42 !important;
	}

	.border-primary {
		border-color: #f25a29 !important;
	}

	.alert-warning {
		background-color: #fff3cd;
		border-color: #ffeeba;
		color: #856404;
	}
	</style>
</head>
<body>
	<header>
		<section class="header">
			<!-- Thanh navbar -->
			<div class="top-header">
				<div class="container">
					<div class="row align-items-center">
						<div class="col-md-3">
							<div class="logo">
								<a href="<%=request.getContextPath()%>/san-pham">
									<img src="<%=request.getContextPath() + "/images/logo.png"%>" alt="Logo" />
								</a>
							</div>
						</div>
						<div class="col-md-4">
							<div class="search">
								<form class="form-inline justify-content-center row">
									<div class="form-search col-10">
										<input class="form-control w-100" type="search"
											placeholder="Tìm kiếm" aria-label="Search">
										<button class="btn btn-search p-o" type="submit">
											<i class="fa fa-search" aria-hidden="true"></i>
										</button>
									</div>
								</form>
							</div>
						</div>
						<div class="col-md-3">
							<div class="contact">
								<h4>Hotline hỗ trợ</h4>
								<h5>0348363413 - 077777777</h5>
							</div>
						</div>
						<div class="col-md-2">
							<form action="<%=request.getContextPath()%>/giohang"
								method="post">
								<button type="submit" class="btn btn-cart">
									<i class="fa fa-cart-plus mr-2" aria-hidden="true"></i> <span>Giỏ
										hàng</span>
								</button>
							</form>
						</div>
					</div>
				</div>
			</div>
			<!-- Kết thúc thanh navbar -->

			<!-- Thanh menu -->
			<div class="menu-wrapper">
				<div class="container">
					<nav class="navbar navbar-expand-lg p-0">
						<button class="navbar-toggler" type="button"
							data-toggle="collapse" data-target="#navbarSupportedContent"
							aria-controls="navbarSupportedContent" aria-expanded="false"
							aria-label="Toggle navigation">
							<span class="navbar-toggler-icon"></span>
						</button>
						<div class="collapse navbar-collapse" id="navbarSupportedContent">
							<ul class="navbar-nav">
								<li class="nav-item"><a class="nav-link"
									href="<%=request.getContextPath()%>/san-pham">Trang chủ</a></li>
								<li class="nav-item dropdown"><a
                                    class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
                                    role="button" data-toggle="dropdown" aria-haspopup="true"
                                    aria-expanded="false"> Danh mục </a>
                                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <a class="dropdown-item" href="<%=request.getContextPath()%>/danh-muc?tenDanhMuc=Sofa">Sofa</a>
                                        <a class="dropdown-item" href="<%=request.getContextPath()%>/danh-muc?tenDanhMuc=Bàn trà">Bàn trà</a>
                                        <a class="dropdown-item" href="<%=request.getContextPath()%>/danh-muc?tenDanhMuc=Bàn ăn">Bàn ăn</a>
                                        <a class="dropdown-item" href="<%=request.getContextPath()%>/danh-muc?tenDanhMuc=Tủ Giường">Tủ Giường</a>
                                        <a class="dropdown-item" href="<%=request.getContextPath()%>/danh-muc?tenDanhMuc=Ghế thư giãn">Ghế thư giãn</a>
                                    </div></li>
								<li class="nav-item"><a class="nav-link" href="#">Giới
										Thiệu</a></li>
								<li class="nav-item"><a class="nav-link" href="#">Sản
										phẩm đã giao</a></li>
								<li class="nav-item active"><a class="nav-link"
									href="<%=request.getContextPath()%>/tin-tuc">Tin tức<span
										class="sr-only">(current)</span></a></li>
								<li class="nav-item"><a class="nav-link" href="#">Facebook</a>
								</li>
								<li class="nav-item"><a class="nav-link" href="#">Liên
										hệ</a></li>
							</ul>
						</div>
					</nav>
				</div>
			</div>
			<!-- Kết thúc thanh menu -->
		</section>
	</header>
	<div class="container py-5">
		<div class="row g-4">
			<!-- Thêm gutter (khoảng cách) giữa các cột -->
			<!-- PHẦN TRÁI: Danh sách sản phẩm -->
			<div class="col-lg-8">
				<div class="card shadow-sm">
					<!-- Bọc phần trái trong card để đồng bộ với phần phải -->
					<div class="card-body">
						<h3 class="mb-4">Giỏ hàng của bạn</h3>

						<%
						// tạo biến gioHang để lấy gioHang trong session đã được truyền từ GioHangController
						GioHang gioHang = (GioHang) session.getAttribute("gioHang");
						double tongTien = 0;
						NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

						if (gioHang == null || gioHang.getDanhSach().isEmpty()) {
						%>
						<div class="alert alert-warning">Giỏ hàng của bạn đang
							trống.</div>
						<%
						// Danh Sach được lưu trữ Map<Integer, GioHangItem> .values() ta được danh sách GioHangItem
						} else {
						for (GioHangItem item : gioHang.getDanhSach().values()) {
							double thanhTien = item.getGia() * item.getSoLuong();
							tongTien += thanhTien;
						%>
						<div class="card mb-3 shadow-sm">
							<div
								class="card-body d-flex justify-content-between align-items-center flex-wrap">
								<div>
									<h5 class="card-title mb-1"><%=item.getTenSanPham()%></h5>
									<p class="mb-1 text-muted">
										Mã SP:
										<%=item.getMaSanPham()%></p>
									<p class="mb-1 text-muted">
										Giá:
										<%=currencyFormat.format(item.getGia())%></p>
									<p class="mb-1 text-muted">
										Thành tiền:
										<%=currencyFormat.format(thanhTien)%></p>
								</div>

								<div class="d-flex align-items-center gap-2">
									<form action="giohang" method="post"
										class="d-flex align-items-center">
										<input type="hidden" name="action" value="capNhat"> <input
											type="hidden" name="maSanPham"
											value="<%=item.getMaSanPham()%>">

										<button type="submit" name="soLuongMoi"
											value="<%=item.getSoLuong() - 1%>"
											class="btn btn-outline-secondary"
											<%=item.getSoLuong() <= 1 ? "disabled" : ""%>>-</button>

										<input type="text" value="<%=item.getSoLuong()%>" readonly
											class="form-control text-center mx-1" style="width: 60px;">
										<%
										//tạo SanPhamDAO để sử dụng phương thức lấy đối tượng sản phẩm thông qua ID để có thể gọi số lượng tồn kho
										SanPhamDAO sanPhamDAO = new SanPhamDAO();
										int id = item.getMaSanPham();
										SanPham sanPham = sanPhamDAO.getById(id);
										%>

										<button type="submit" name="soLuongMoi"
											value="<%=item.getSoLuong() + 1%>"
											class="btn btn-outline-secondary"
											<%=item.getSoLuong() >= sanPham.getSoLuongTonKho() ? "disabled" : ""%>>+</button>
									</form>

									<form action="giohang" method="post">
										<input type="hidden" name="action" value="xoa"> <input
											type="hidden" name="maSanPham"
											value="<%=item.getMaSanPham()%>">
										<button type="submit" class="btn btn-dark ms-2">
											<i class="bi bi-trash"></i> Xóa
										</button>
									</form>
								</div>
							</div>
						</div>
						<%
						}
						%>
						<div class="d-flex justify-content-between mt-4">
							<a href="<%=request.getContextPath()%>/san-pham"
								class="btn btn-outline-secondary"> <i
								class="bi bi-arrow-left"></i> Tiếp tục mua hàng
							</a>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>

			<!-- PHẦN PHẢI: Tóm tắt và Thanh toán -->
			<div class="col-lg-4">
				<div class="card shadow-sm sticky-top" style="top: 20px;">
					<!-- Thêm sticky-top để cố định khi cuộn -->
					<div class="card-body">
						<h4 class="card-title mb-3">Tóm tắt đơn hàng</h4>

						<div class="d-flex justify-content-between mb-2">
							<span>Tạm tính:</span> <span><%=currencyFormat.format(tongTien)%></span>
						</div>

						<hr>

						<div class="d-flex justify-content-between fw-bold mb-4">
							<span>Tổng cộng:</span> <span class="text-primary"><%=currencyFormat.format(tongTien)%></span>
						</div>
						<!-- đưa sang trang GioHangController để xửa lý -->

						<form action="<%=request.getContextPath()%>/giohang"
							method="post">
							<button type="submit" class="btn btn-dark w-100 py-2" name="action" value="ThanhToan">
								<i class="bi bi-credit-card"></i> Thanh toán
							</button>
						</form>


					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Bootstrap JS -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
			integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
			crossorigin="anonymous"></script>
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
</body>
</html>
