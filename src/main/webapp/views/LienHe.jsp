<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Liên hệ</title>
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
	href="${pageContext.request.contextPath}/css/style.css">
<style>
:root {
	--primary-color: #f25a29;
}

body {
	font-family: Arial, sans-serif;
}

.page-title {
	color: var(--primary-color);
	text-align: center;
	margin-top: 30px;
	margin-bottom: 30px;
}

.contact-info p {
	margin: 5px 0;
}

.contact-card {
	background-color: #fff4f0;
	border: 1px solid #f25a29;
	padding: 20px;
	border-radius: 10px;
}

iframe {
	border-radius: 10px;
	border: none;
	width: 100%;
	height: 450px;
}

.btn-primary {
	background-color: var(--primary-color);
	border-color: var(--primary-color);
}

.btn-primary:hover {
	background-color: #d94f22;
	border-color: #d94f22;
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
											value="<%=request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>">
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
									aria-expanded="false"> Sản phẩm </a>
									<div class="dropdown-menu" aria-labelledby="navbarDropdown">
										<a class="dropdown-item" href="#">Sofa</a> <a
											class="dropdown-item" href="#">Bàn trà</a> <a
											class="dropdown-item" href="#">Tủ Giường</a> <a
											class="dropdown-item" href="#">Bàn ăn</a> <a
											class="dropdown-item" href="#">Ghế thư giãn</a>
									</div></li>
								<li class="nav-item"><a class="nav-link" href="GioiThieu.jsp">giới thiệu</a></li>

								<li class="nav-item"><a class="nav-link"
									href="<%=request.getContextPath()%>/san-pham?action=daGiao">Sản
										phẩm đã giao</a></li>
								<li class="nav-item active"><a class="nav-link"
									href="<%=request.getContextPath()%>/tin-tuc">Tin tức<span
										class="sr-only"></span></a></li>
								<li class="nav-item"><a class="nav-link"
									href="https://www.facebook.com/isofafurniture">Facebook</a></li>
								<li class="nav-item"><a class="nav-link"
									href="#">Liên hệ</a></li>
							</ul>
						</div>
					</nav>
				</div>
			</div>
			<!-- Kết thúc thanh menu -->
		</section>
	</header>

	<div class="container">
		<h1 class="page-title">Liên hệ với chúng tôi</h1>

		<div class="row mb-5">
			<div class="col-md-6">
				<div class="contact-card">
					<h4>Thông tin liên hệ</h4>
					<div class="contact-info">
						<p>
							<strong>Địa chỉ:</strong> Nguyên Hòa, Phù Cừ, Hưng Yên
						</p>
						<p>
							<strong>Điện thoại:</strong> 0123 456 789
						</p>
						<p>
							<strong>Email:</strong> lienhe@banghesofa.vn
						</p>
						<p>
							<strong>Giờ làm việc:</strong> Thứ 2 - Thứ 7: 8:00 - 17:00
						</p>
					</div>
				</div>
			</div>
			<div>
				<iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d29862.641045613094!2d106.22565790775522!3d20.67648882668459!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3135eb5bd81e6cb7%3A0x2c751faca03cff2b!2zTmd1ecOqbiBIb8OgLCBQaMO5IEPhu6ssIEjGsG5nIFnDqm4sIFZp4buHdCBOYW0!5e0!3m2!1svi!2s!4v1746414629571!5m2!1svi!2s" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
			</div>
		</div>




	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
