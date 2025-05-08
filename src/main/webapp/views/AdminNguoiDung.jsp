
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.NguoiDung"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý Người Dùng - Admin</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h3 class="mb-0">Quản lý Người Dùng</h3>
            </div>
            <div class="card-body">
                <!-- Form thêm/sửa người dùng -->
                <form action="<%=request.getContextPath()%>/admin/nguoi-dung" method="post" class="mb-4">
                    <% NguoiDung nguoiDung = (NguoiDung) request.getAttribute("nguoiDung"); %>
                    <input type="hidden" name="action" value="<%= nguoiDung != null ? "update" : "add" %>">
                    <% if (nguoiDung != null) { %>
                        <input type="hidden" name="maNguoiDung" value="<%=nguoiDung.getMaNguoiDung()%>">
                    <% } %>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="hoTen" class="form-label">Họ Tên</label>
                            <input type="text" class="form-control" id="hoTen" name="hoTen" value="<%= nguoiDung != null ? nguoiDung.getHoTen() : "" %>" required>
                        </div>
                        <div class="col-md-6">
                            <label for="sdt" class="form-label">Số Điện Thoại</label>
                            <input type="text" class="form-control" id="sdt" name="sdt" value="<%= nguoiDung != null ? nguoiDung.getSdt() : "" %>" required>
                        </div>
                        <div class="col-md-6">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" value="<%= nguoiDung != null ? nguoiDung.getEmail() : "" %>" required>
                        </div>
                        <div class="col-md-6">
                            <label for="diaChi" class="form-label">Địa Chỉ</label>
                            <input type="text" class="form-control" id="diaChi" name="diaChi" value="<%= nguoiDung != null ? nguoiDung.getDiaChi() : "" %>" required>
                        </div>
                            </select>
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-success"><%= nguoiDung != null ? "Cập nhật" : "Thêm" %> Người Dùng</button>
                            <% if (nguoiDung != null) { %>
                                <a href="<%=request.getContextPath()%>/admin/nguoi-dung" class="btn btn-secondary">Hủy</a>
                            <% } %>
                        </div>
                    </div>
                </form>

                <!-- Danh sách người dùng -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Họ Tên</th>
                            <th>Số Điện Thoại</th>
                            <th>Email</th>
                            <th>Địa chỉ</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        List<NguoiDung> nguoiDungs = (List<NguoiDung>) request.getAttribute("nguoiDungs");
                        if (nguoiDungs != null) {
                            for (NguoiDung nd : nguoiDungs) {
                        %>
                        <tr>
                            <td><%=nd.getHoTen()%></td>
                            <td><%=nd.getSdt()%></td>
                            <td><%=nd.getEmail()%></td>
                            <td><%=nd.getDiaChi()%></td>
                         <%--    <td><%=nd.getTenDangNhap()%></td>
                            <td><%=nd.getVaiTro()%></td> --%>
                            <td>
                                <a href="<%=request.getContextPath()%>/admin/nguoi-dung?action=edit&maNguoiDung=<%=nd.getMaNguoiDung()%>" class="btn btn-primary btn-sm">Sửa</a>
                                <form action="<%=request.getContextPath()%>/admin/nguoi-dung" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="maNguoiDung" value="<%=nd.getMaNguoiDung()%>">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</button>
                                </form>
                            </td>
                        </tr>
                        <%
                            }
                        }
                        %>
                    </tbody>
                </table>

                <!-- Phân trang -->
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <%
                        Integer currentPage = (Integer) request.getAttribute("currentPage");
                        Integer totalPages = (Integer) request.getAttribute("totalPages");
                        if (currentPage == null) currentPage = 1;
                        if (totalPages == null) totalPages = 1;

                        if (currentPage > 1) {
                        %>
                        <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/admin/nguoi-dung?page=<%=currentPage - 1%>">«</a></li>
                        <%
                        }
                        for (int i = 1; i <= totalPages; i++) {
                        %>
                        <li class="page-item <%= (i == currentPage ? "active" : "") %>">
                            <a class="page-link" href="<%=request.getContextPath()%>/admin/nguoi-dung?page=<%=i%>"><%=i%></a>
                        </li>
                        <%
                        }
                        if (currentPage < totalPages) {
                        %>
                        <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/admin/nguoi-dung?page=<%=currentPage + 1%>">»</a></li>
                        <%
                        }
                        %>
                    </ul>
                </nav>

                <!-- Thông báo lỗi -->
                <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
                <% if (errorMessage != null) { %>
                    <div class="alert alert-danger"><%=errorMessage%></div>
                <% } %>
            </div>
        </div>
        <a href="<%=request.getContextPath()%>/views/AdminDashboard.jsp" class="btn btn-secondary mt-3">Quay lại Dashboard</a>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
