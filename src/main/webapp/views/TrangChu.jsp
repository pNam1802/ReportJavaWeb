<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.SanPham"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>Danh sách Sản Phẩm</title>

	<!-- Bootstrap CSS từ CDN -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
		integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
	<!-- Bootstrap CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<!-- Font Awesome để dùng icon -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
	<!-- Style của chính mình -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
								<li class="nav-item"><a class="nav-link" href="views/LienHe.jsp">Liên hệ</a></li>
							</ul>
						</div>
					</nav>
				</div>
			</div>
			<!-- Kết thúc thanh menu -->
		</section>
	</header>
	
	<!-- Banner -->
	<div id="bannerCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
  		<div class="carousel-indicators">
    		<button type="button" data-bs-target="#bannerCarousel" data-bs-slide-to="0" class="active" aria-label="Slide 1"></button>
    		<button type="button" data-bs-target="#bannerCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
    		<button type="button" data-bs-target="#bannerCarousel" data-bs-slide-to="2" aria-label="Slide 3"></button>
  		</div>
  		<div class="carousel-inner">
    		<div class="carousel-item active">
      			<img src="images/banner1.jpg" class="d-block w-100" alt="Banner 1">
    		</div>
    		<div class="carousel-item">
      			<img src="images/banner2.jpg" class="d-block w-100" alt="Banner 2">
      			<div class="carousel-caption">
        			<h5>THIẾT KẾ TINH TẾ</h5>
        			<h5>SANG TRỌNG ÊM ÁI</h5>
      			</div>
    		</div>
    		<div class="carousel-item">
      			<img src="images/banner3.jpg" class="d-block w-100" alt="Banner 3">
      			<div class="carousel-caption">
        			<h5>ĐẲNG CẤP TỪ MỌI GÓC NHÌN</h5>
      			</div>
    		</div>
  		</div>
	</div>

	<!--  -->
	<div class="main-wrapper">
		<section class="product-sales">
			<h3 class="title-page text-center mb-4">SẢN PHẨM</h3>
			<div class="container">
				<div class="row">
					<%
                    List<SanPham> sanPhams = (List<SanPham>) request.getAttribute("sanPhams");
                    if (sanPhams != null && !sanPhams.isEmpty()) {
                        for (SanPham sanPham : sanPhams) {
                    %>
					<div class="col-md-4 mb-4">
						<div class="box-item border rounded p-3 position-relative">
							<!-- Phần để xem chi tiết sản phẩm -->
							<div class="img-box">
								<a
									href="<%=request.getContextPath()%>/san-pham?action=chiTiet&id=<%=sanPham.getMaSanPham()%>">
									<img
									src="<%=sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()
                                        ? request.getContextPath() + "/images/" + sanPham.getHinhAnh()
                                        : request.getContextPath() + "/images/default.jpg"%>"
									alt="<%=sanPham.getTenSanPham()%>" class="img-fluid">
								</a>
							</div>

							<div class="content-box mt-2">
								<h4>
									<a
										href="<%=request.getContextPath()%>/san-pham?action=chiTiet&id=<%=sanPham.getMaSanPham()%>">
										<%=sanPham.getTenSanPham()%>
									</a>
								</h4>
								<p class="mb-0">
									Giá:
									<%=String.format("%,.0f", sanPham.getGiaKhuyenMai())%>
									đ
								</p>
							</div>

							<%
                            if (sanPham.getGiaGoc() > sanPham.getGiaKhuyenMai()) {
                                double phanTramGiam = 100.0 * (sanPham.getGiaGoc() - sanPham.getGiaKhuyenMai()) / sanPham.getGiaGoc();
                            %>
							<div
								class="sale-report position-absolute top-0 end-0 bg-danger text-white p-1 rounded">
								<span>Giảm <%=(int) phanTramGiam%>%
								</span>
							</div>
							<%
                            }
                            %>


						</div>
					</div>
					<%
                        }
                    } else {
                    %>
					<div class="col-12">
						<p class="text-center">Không có sản phẩm nào để hiển thị.</p>
					</div>
					<%
                    }
                    %>
				</div>
			</div>

			<!-- Phân trang -->
			<nav aria-label="Page navigation">
				<ul class="pagination justify-content-center mt-4">
					<%
                    Integer currentPage = (Integer) request.getAttribute("currentPage");
                    Integer totalPages = (Integer) request.getAttribute("totalPages");

                    if (currentPage == null) currentPage = 1;
                    if (totalPages == null) totalPages = 1;

                    // Nút trang trước
                    if (currentPage > 1) {
                    %>
					<li class="page-item"><a class="page-link"
						href="<%=request.getContextPath()%>/san-pham?page=<%=currentPage - 1%>">«</a>
					</li>
					<%
                    }

                    // Các số trang
                    for (int i = 1; i <= totalPages; i++) {
                    %>
					<li class="page-item <%= (i == currentPage) ? "active" : "" %>">
						<a class="page-link"
						href="<%=request.getContextPath()%>/san-pham?page=<%=i%>"><%=i%></a>
					</li>
					<%
                    }

                    // Nút trang sau
                    if (currentPage < totalPages) {
                    %>
					<li class="page-item"><a class="page-link"
						href="<%=request.getContextPath()%>/san-pham?page=<%=currentPage + 1%>">»</a>
					</li>
					<%
                    }
                    %>
				</ul>
			</nav>
		</section>
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

	<!-- Bootstrap 5.3 JS -->
	<!-- jQuery (BẮT BUỘC - Bootstrap 4 cần jQuery) -->
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<!-- Popper.js (BẮT BUỘC - để dropdown, tooltip hoạt động) -->
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
	<!-- Bootstrap JS (file chính của Bootstrap) -->
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
