<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giới thiệu về Isofa</title>
   <!-- Bootstrap CSS từ CDN -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
	integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
	crossorigin="anonymous">
<!-- Font Awesome để dùng icon -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- Style của chính mình -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/style.css">
    <style>
        :root {
            --main-color: #f25a29;
        }
        .text-main {
            color: var(--main-color) !important;
        }
        .bg-main {
            background-color: var(--main-color) !important;
        }
        .border-main {
            border-color: var(--main-color) !important;
        }
        .btn-main {
            background-color: var(--main-color);
            color: white;
            border: none;
        }
        .btn-main:hover {
            background-color: #d94f24;
        }
    </style>
</head>
<body class="bg-light">
<header>
		<section class="header">
			<!-- Thanh navbar -->
			<div class="top-header">
				<div class="container">
					<div class="row align-items-center">
						<div class="col-md-3">
							<div class="logo">
								<img src="<%=request.getContextPath() + "/images/logo.png"%>"
									alt="Logo" />
							</div>
						</div>
						<div class="col-md-4">
							<div class="search">
								<form action="<%=request.getContextPath()%>/san-pham"
									method="get" class="form-inline justify-content-center row">
									<input type="hidden" name="action" value="timKiem">
									<div class="form-search col-10">
										<input class="form-control w-100" type="search" name="keyword"
											placeholder="Tìm kiếm" aria-label="Search"
											value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>">
										<button type="submit" aria-label="Tìm kiếm">
											<i class="fa fa-search"></i>
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
										<a class="dropdown-item" href="<%=request.getContextPath()%>/san-pham?danhMuc=sofa">Sofa</a>
										<a class="dropdown-item" href="<%=request.getContextPath()%>/san-pham?danhMuc=ban-tra">Bàn trà</a>
										<a class="dropdown-item" href="<%=request.getContextPath()%>/san-pham?danhMuc=tu-giuong">Tủ Giường</a>
										<a class="dropdown-item" href="<%=request.getContextPath()%>/san-pham?danhMuc=ban-an">Bàn ăn</a>
										<a class="dropdown-item" href="<%=request.getContextPath()%>/san-pham?danhMuc=ghe-thu-gian">Ghế thư giãn</a>
									</div></li>
								<li class="nav-item"><a class="nav-link"
									href="#">Giới Thiệu</a></li>
								<li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/san-pham?action=daGiao">Sản
										phẩm đã giao</a></li>
								<li class="nav-item active"><a class="nav-link"
									href="<%=request.getContextPath()%>/tin-tuc">Tin tức<span
										class="sr-only">(current)</span></a></li>
								<li class="nav-item"><a class="nav-link" href="https://www.facebook.com/isofafurniture">Facebook</a>
								</li>
								<li class="nav-item"><a class="nav-link" href="LienHe.jsp">Liên hệ</a></li>

							</ul>
						</div>
					</nav>
				</div>
			</div>
			<!-- Kết thúc thanh menu -->
		</section>
	</header>

    <div class="container mt-5">
        <div class="card shadow p-4 border-main">
           <h3 class="title-page text-center mb-4">GIỚI THIỆU</h3>
            <p><strong>Isofa</strong> là công ty tiên phong trong lĩnh vực nội thất và thiết kế không gian sống hiện đại. Với sứ mệnh mang đến sự tinh tế, tiện nghi và phong cách cho mọi tổ ấm, Isofa không ngừng sáng tạo và cải tiến sản phẩm để đáp ứng nhu cầu ngày càng đa dạng của khách hàng.</p>

            <p>Chúng tôi cung cấp các dòng sản phẩm nội thất cao cấp như ghế sofa, bàn ghế phòng khách, phòng ăn, văn phòng,... với thiết kế hiện đại, chất lượng bền bỉ và giá cả hợp lý.</p>

            <p>Với đội ngũ nhân viên chuyên nghiệp và tận tâm, Isofa cam kết mang đến trải nghiệm mua sắm và dịch vụ hậu mãi tốt nhất cho khách hàng trên toàn quốc.</p>

            <p class="fw-bold">Hãy cùng Isofa biến không gian sống của bạn trở thành nơi lý tưởng để thư giãn và thể hiện phong cách riêng!</p>

            <a href="<%=request.getContextPath() %>/san-pham" class="btn btn-main mt-3">Về trang chủ</a>
        </div>
    </div>
<footer>
		<div class="container">
			<div class="row contact">
				<div class="col-md-3">
					<h4>Isofa</h4>
					<ul>
						<li>Công ty chuyên sản xuất kinh doanh sofa hiện đại với
							thiết kế từ Italia với hơn 20 năm kinh nghiệm</li>
					</ul>
				</div>
				<div class="col-md-3">
					<h4>Về Chúng Tôi</h4>
					<ul>
						<li>Lịch sử hình thành</li>
						<li>Triết lý kinh doanh</li>
						<li>Nghiên cứu và phát triển</li>
						<li>Năng lực và nhân sự</li>
					</ul>
				</div>
				<div class="col-md-3">
					<h4>Chính sách</h4>
					<ul>
						<li>Chính sách vận chuyển</li>
						<li>Chính sách bảo hành</li>
						<li>Chính sách đổi trả</li>
						<li>Chính sách thanh toán</li>
					</ul>
				</div>
				<div class="col-md-3 p-0">
					<h4>Kết Nối Với AQ</h4>
					<ul>
						<li><a
							href="https://www.facebook.com/profile.php?id=100022420882328">Facebook</a></li>
						<li><a href="#">Zalo</a></li>
					</ul>
				</div>
			</div>
			<div class="row address-store">
				<div class="col-md-4">
					<h4>
						<i class="fa fa-map-marker me-2" aria-hidden="true"></i> SIÊU THỊ
						NỘI THẤT 1
					</h4>
					<ul>
						<li>Địa chỉ: 205-207 Tôn Đức Thắng - TP. Đà Nẵng</li>
						<li>Hotline: 07777777</li>
						<li>Email: hntnghia.20it8@vku.udn.vn</li>
					</ul>
				</div>
			</div>
		</div>
	</footer>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
