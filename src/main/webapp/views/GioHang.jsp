<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ Hàng</title>
    <!-- Liên kết tới Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
   <style>
        /* Đặt màu chủ đạo cho các phần */
        .bg-custom {
            background-color: #f25a29 !important;
        }
        .text-custom {
            color: #f25a29 !important;
            text-align: center;
        }
        .btn-custom {
            background-color: #f25a29;
            color: white;
        }
        .btn-custom:hover {
            background-color: #e04c1b;
        }
        .card-custom {
            border: 1px solid #f25a29;
        }
        .card-header-custom {
            background-color: #333333;
            color: white;
        }
        .card-footer-custom {
            background-color: #333333;
            color: white;
        }
        .table th, .table td {
            vertical-align: middle;
        }
        .table th {
            background-color: #333333;
            color: white;
        }
        .table td {
            background-color: #f4f4f4;
        }
    </style>
</head>
<body>
	<div class="container mt-5">
    <div class="row">
        <!-- Phần bên trái: Quản lý giỏ hàng -->
        <div class="col-md-8">
            <h2 class="text-custom">Quản lý Giỏ Hàng</h2>

            <c:if test="${empty gioHang.items}">
                <div class="alert alert-warning">Giỏ hàng của bạn hiện đang trống!</div>
            </c:if>

            <c:if test="${not empty gioHang.items}">
                <table class="table table-bordered">
                    <thead class="bg-custom">
                        <tr>
                            <th scope="col">Hình ảnh</th>
                            <th scope="col">Sản phẩm</th>
                            <th scope="col">Giá</th>
                            <th scope="col">Số lượng</th>
                            <th scope="col">Thành tiền</th>
                            <th scope="col">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${gioHang.items}">
                            <tr>
                                <td>
                                    <img src="${item.hinhAnh}" alt="${item.tenSanPham}" class="img-fluid" style="width: 100px; height: auto;">
                                </td>
                                <td>${item.tenSanPham}</td>
                                <td>${item.gia}</td>
                                <td>
                                    <!-- Form cập nhật số lượng sản phẩm -->
                                    <form action="giohang" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="capNhat">
                                        <input type="hidden" name="maSanPham" value="${item.maSanPham}">
                                        <input type="number" name="soLuongMoi" value="${item.soLuong}" min="1" class="form-control w-50">
                                        <button type="submit" class="btn btn-custom btn-sm mt-2">Cập nhật</button>
                                    </form>
                                </td>
                                <td>${item.thanhTien}</td>
                                <td>
                                    <!-- Form xóa sản phẩm -->
                                    <form action="giohang" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="xoa">
                                        <input type="hidden" name="maSanPham" value="${item.maSanPham}">
                                        <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>

        <!-- Phần bên phải: Tổng giỏ hàng và thanh toán -->
        <div class="col-md-4">
            <h2 class="text-custom">Tổng giỏ hàng</h2>
            <div class="card card-custom">
                <div class="card-header card-header-custom">
                    Tổng tiền
                </div>
                <div class="card-body">
                    <h5 class="card-title"><strong>${gioHang.tongTien}</strong></h5>
                    <a href="thanh-toan.jsp" class="btn btn-custom w-100">Tiến hành thanh toán</a>
                </div>
                <div class="card-footer card-footer-custom">
                    Cảm ơn bạn đã mua sắm tại cửa hàng chúng tôi.
                </div>
            </div>
        </div>
    </div>
</div>


</body>
<!-- Liên kết tới Bootstrap JS và các dependency -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

</html>