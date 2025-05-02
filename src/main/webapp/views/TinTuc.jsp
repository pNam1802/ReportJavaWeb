<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Danh sách Tin Tức</title>

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
</head>
<body>
    <header>
        <section class="header">
            <div class="top-header">
                <div class="container">
                    <div class="row align-items-center">
                        <div class="col-md-3">
                            <div class="logo">
                                <img src="<%=request.getContextPath() + "/images/logo.png"%>" />
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="search">
                                <form class="form-inline justify-content-center row">
                                    <div class="form-search col-10">
                                        <input class="form-control w-100" type="search"
                                               placeholder="Search" aria-label="Search">
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
                            <button class="btn btn-cart">
                                <i class="fa fa-cart-plus mr-2" aria-hidden="true"></i> <span>Giỏ hàng</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
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
                                <li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/san-pham">Trang chủ</a></li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
                                       role="button" data-toggle="dropdown" aria-haspopup="true"
                                       aria-expanded="false"> Sản phẩm </a>
                                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <a class="dropdown-item" href="#">Sofa</a>
                                        <a class="dropdown-item" href="#">Bàn trà</a>
                                        <a class="dropdown-item" href="#">Tủ Giường</a>
                                        <a class="dropdown-item" href="#">Bàn ăn</a>
                                        <a class="dropdown-item" href="#">Ghế thư giãn</a>
                                    </div>
                                </li>
                                <li class="nav-item"><a class="nav-link" href="#">Giới Thiệu</a></li>
                                <li class="nav-item"><a class="nav-link" href="#">Sản phẩm đã giao</a></li>
                                <li class="nav-item active"><a class="nav-link" href="#">Tin tức<span class="sr-only">(current)</span></a></li>
                                <li class="nav-item"><a class="nav-link" href="#">Facebook</a></li>
                                <li class="nav-item"><a class="nav-link" href="#">Liên hệ</a></li>
                            </ul>
                        </div>
                    </nav>
                </div>
            </div>
            <!-- Kết thúc thanh menu -->
        </section>
    </header>
    <!-- Kết thúc header -->

    <div class="main-wrapper">
        <section class="news">
            <h3 class="title-page">TIN TỨC</h3>
            <div class="container">
                <div class="list-box row">
                    <%
                    List<TinTuc> tinTucs = (List<TinTuc>) request.getAttribute("tinTucs");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    if (tinTucs != null) {
                        for (TinTuc tinTuc : tinTucs) {
                    %>
                    <div class="col-md-4">
                        <div class="box-item">
                            <div class="content-box">
                                <h4>
                                    <a href="<%=request.getContextPath()%>/chi-tiet-tin-tuc?maTinTuc=<%=tinTuc.getMaTinTuc()%>">
                                        <%=tinTuc.getTieuDe()%>
                                    </a>
                                </h4>
                                <p class="mb-0">
                                    Ngày đăng: <%=dateFormat.format(tinTuc.getNgayDang())%>
                                </p>
                                <p>
                                    <%=tinTuc.getNoiDung().length() > 100 ? tinTuc.getNoiDung().substring(0, 100) + "..." : tinTuc.getNoiDung()%>
                                </p>
                            </div>
                            <a href="<%=request.getContextPath()%>/chi-tiet-tin-tuc?maTinTuc=<%=tinTuc.getMaTinTuc()%>" class="add-to-cart">Xem chi tiết</a>
                        </div>
                    </div>
                    <%
                        }
                    }
                    %>
                </div>
            </div>
            <!-- Phân trang -->
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <%
                    Integer currentPage = (Integer) request.getAttribute("currentPage");
                    Integer totalPages = (Integer) request.getAttribute("totalPages");

                    if (currentPage == null) currentPage = 1;
                    if (totalPages == null) totalPages = 1;

                    // Nút trang trước
                    if (currentPage > 1) {
                    %>
                    <li class="page-item"><a class="page-link"
                                             href="tin-tuc?page=<%=currentPage - 1%>">«</a></li>
                    <%
                    }

                    // Các số trang
                    for (int i = 1; i <= totalPages; i++) {
                    %>
                    <li class="page-item <%=(i == currentPage ? "active" : "")%>">
                        <a class="page-link" href="tin-tuc?page=<%=i%>"><%=i%></a>
                    </li>
                    <%
                    }

                    // Nút trang sau
                    if (currentPage < totalPages) {
                    %>
                    <li class="page-item"><a class="page-link"
                                             href="tin-tuc?page=<%=currentPage + 1%>">»</a></li>
                    <%
                    }
                    %>
                </ul>
            </nav>
            <!-- Kết thúc phân trang -->
        </section>
    </div>

    <footer>
        <div class="container">
            <div class="row contact">
                <div class="col-md-3">
                    <h4>Isofa</h4>
                    <ul>
                        <li>Công ty chuyên sản xuất kinh doanh sofa hiện đại với thiết kế từ Italia với hơn 20 năm kinh nghiệm</li>
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
                        <li><a href="https://www.facebook.com/profile.php?id=100022420882328">Facebook</a></li>
                        <li><a href="">Zalo</a></li>
                    </ul>
                </div>
            </div>
            <div class="row address-store">
                <div class="col-md-4">
                    <h4>
                        <i class="fa fa-map-marker mr-2" aria-hidden="true"></i> SIÊU THỊ NỘI THẤT 1
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

    <!-- jQuery (BẮT BUỘC - Bootstrap 4 cần jQuery) -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <!-- Popper.js (BẮT BUỘC - để dropdown, tooltip hoạt động) -->
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
    <!-- Bootstrap JS (file chính của Bootstrap) -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
</body>
</html>