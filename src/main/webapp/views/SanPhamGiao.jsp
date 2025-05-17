<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.DanhGiaSanPham" %>
<%
    List<DanhGiaSanPham> danhGiaList = (List<DanhGiaSanPham>) request.getAttribute("danhGiaList");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đánh giá sản phẩm đã giao</title>
    <!-- Bootstrap CSS từ CDN -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
	integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
	crossorigin="anonymous">
<!-- Font Awesome để dùng icon -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- Style của chính mình -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">




   
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
								<a href="<%=request.getContextPath()%>/san-pham">
									<img src="<%=request.getContextPath() + "/images/logo.png"%>" alt="Logo" />
								</a>
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
						<div class="col-md-2 d-flex align-items-center justify-content-end">
    						<form action="<%=request.getContextPath()%>/giohang" method="post">
        						<button type="submit" class="btn btn-cart d-flex align-items-center">
            						<i class="fa fa-cart-plus mr-2" aria-hidden="true"></i> 
            						<span class="d-inline-block">Giỏ hàng</span>
        						</button>
    						</form>
    						<button class="btn btn-user ml-2" data-toggle="modal" data-target="#adminLoginModal">
        						<i class="fa fa-user-circle" aria-hidden="true"></i>
    						</button>
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
								<li class="nav-item"><a class="nav-link"
									href="views/GioiThieu.jsp">Giới Thiệu</a></li>
								<li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/san-pham?action=daGiao">Sản
										phẩm đã giao</a></li>
								<li class="nav-item active"><a class="nav-link"
									href="<%=request.getContextPath()%>/tin-tuc">Tin tức<span
										class="sr-only">(current)</span></a></li>
								<li class="nav-item"><a class="nav-link" href="https://www.facebook.com/isofafurniture">Facebook</a>
								</li>
								<li class="nav-item"><a class="nav-link" href="views/LienHe.jsp">Liên
										hệ</a></li>
							</ul>
						</div>
					</nav>
				</div>
			</div>
			<!-- Kết thúc thanh menu -->
		</section>
	</header>
<div class="container py-4">
  <h3 class="title-page text-center mb-4">SẢN PHẨM ĐÃ GIAO</h3>

    <% if (danhGiaList != null && !danhGiaList.isEmpty()) {
        for (DanhGiaSanPham dg : danhGiaList) { %>
            <div class="card mb-4 shadow-sm border-left-orange">
                <div class="card-body">
                    <h5 class="card-title main-color">
                        <%= dg.getTenSanPham() %> - <%= dg.getTenNguoiDung() %>
                    </h5>
                    <p class="mb-2">
                        <span class="star">
                            <% for (int i = 0; i < dg.getDiemDanhGia(); i++) { %>★<% } %>
                        </span>
                    </p>
                    <p class="card-text"><%= dg.getNoiDung() %></p>
                    <p class="text-muted small">
                        Ngày đánh giá: <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(dg.getNgayDanhGia()) %>
                    </p>
                </div>
            </div>
    <%  }
    } else { %>
        <div class="alert alert-info text-center">
            Chưa có đánh giá nào.
        </div>
    <% } %>
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
<!-- Modal thông báo -->
	<div class="modal fade" id="adminLoginModal" tabindex="-1" role="dialog" aria-labelledby="adminLoginModalLabel" aria-hidden="true">
    	<div class="modal-dialog" role="document">
        	<div class="modal-content">
            	<div class="modal-header">
                	<h5 class="modal-title" id="adminLoginModalLabel">Thông báo</h5>
                	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    	<span aria-hidden="true">&times;</span>
                	</button>
            	</div>
            	<div class="modal-body">
                	Bạn chỉ có thể thực hiện chức năng này khi là admin. Bạn vẫn muốn tiếp tục?
            	</div>
            	<div class="modal-footer">
                	<a href="<%=request.getContextPath()%>/login-admin" class="btn btn-primary">Đăng nhập</a>
                	<button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
            	</div>
        	</div>
    	</div>
	</div>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<!-- Popper -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
<!-- Bootstrap 4 JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>

</body>
</html>
