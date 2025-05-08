<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="model.DonHang"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.net.URLEncoder"%>

<%
    List<DonHang> donHangs = (List<DonHang>) request.getAttribute("donHangs");
    String trangThaiChon = request.getParameter("trangThai");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý đơn hàng</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container mt-5">
		<h2 class="mb-4">Quản lý đơn hàng</h2>

		<!-- Phần lọc đơn hàng -->
		<form action="${pageContext.request.contextPath}/don-hang"
			method="get" class="row g-3 mb-4">

			<div class="col-auto">
				<select name="trangThai" class="form-select">
					<option value="">Tất cả đơn hàng</option>
					<option value="Đang xử lý"
						<%= "Đang xử lý".equals(trangThaiChon) ? "selected" : "" %>>Đang
						xử lý</option>
					<option value="Đã giao"
						<%= "Đã giao".equals(trangThaiChon) ? "selected" : "" %>>Đã
						giao</option>
					<option value="Đã hủy"
						<%= "Đã hủy".equals(trangThaiChon) ? "selected" : "" %>>Đã
						hủy</option>
				</select>
			</div>
			<div class="col-auto">
				<button type="submit" class="btn btn-primary">Lọc</button>
			</div>
		</form>

		<!-- Phần bảng đơn hàng -->
		<table class="table table-bordered table-hover">
			<thead class="table-dark">
				<tr>
					<th>Mã đơn</th>
					<th>Ngày lập</th>
					<th>Trạng thái</th>
					<th>Tổng tiền</th>
					<th>Mã người dùng</th>
					<th>Hành động</th>
				</tr>
			</thead>
			<tbody>
				<%
                    if (donHangs != null && !donHangs.isEmpty()) {
                        for (DonHang dh : donHangs) {
                            String trangThaiMoi = URLEncoder.encode("Đã hủy", "UTF-8");
                %>
				<tr>
					<td><%= dh.getMaDonHang() %></td>
					<td><%= sdf.format(dh.getNgayLap()) %></td>
					<td><%= dh.getTrangThai() %></td>
					<td><%= currencyFormatter.format(dh.getTongTien()) %></td>
					<td><%= dh.getMaNguoiDung() %></td>
					<td style="display: flex; gap: 5px;"><a
						href="don-hang?action=chitiet&maDonHang=<%= dh.getMaDonHang() %>"
						class="btn btn-info btn-sm">Chi tiết</a>

						<form action="don-hang" method="post">
							<input type="hidden" name="action" value="huy"> <input
								type="hidden" name="maDonHang" value="<%= dh.getMaDonHang() %>">
							<input type="hidden" name="trangThaiMoi"
								value="<%= trangThaiMoi %>">
							<button type="submit" class="btn btn-danger btn-sm"
								onclick="return confirm('Bạn chắc chắn muốn hủy đơn này?')">Hủy</button>
						</form></td>

				</tr>
				<%
                        }
                    } else {
                %>
				<tr>
					<td colspan="6" class="text-center text-muted">Không có đơn
						hàng nào phù hợp</td>
				</tr>
				<%
                    }
                %>
			</tbody>
		</table>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
