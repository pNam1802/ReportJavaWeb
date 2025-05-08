<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.SanPham" %>
<%@ page import="model.DanhMuc" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Sản phẩm - iSofa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .action-buttons {
            display: flex;
            gap: 5px;
            justify-content: center; /* Căn giữa nút Sửa và Xóa */
        }
    </style>
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-success text-white">
            <h3 class="mb-0">Quản lý Sản phẩm</h3>
        </div>
        <div class="card-body">

            <!-- Form sửa sản phẩm (ẩn ban đầu, hiển thị ở đầu khi nhấp Sửa) -->
            <div class="collapse" id="editProductForm">
                <div class="card card-body mt-3">
                    <h5>Sửa Sản phẩm</h5>
                    <form action="${pageContext.request.contextPath}/admin-san-pham" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" id="editMaSanPham" name="maSanPham" value="">
                        <div class="mb-3">
                            <label for="editTenSanPham" class="form-label">Tên sản phẩm</label>
                            <input type="text" class="form-control" id="editTenSanPham" name="tenSanPham" required>
                        </div>
                        <div class="mb-3">
                            <label for="editMaDanhMuc" class="form-label">Danh mục</label>
                            <select class="form-select" id="editMaDanhMuc" name="maDanhMuc" required>
                                <option value="">Chọn danh mục</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="editGiaGoc" class="form-label">Giá gốc (Đồng)</label>
                            <input type="number" step="0.01" class="form-control" id="editGiaGoc" name="giaGoc" required>
                        </div>
                        <div class="mb-3">
                            <label for="editGiaKhuyenMai" class="form-label">Giá khuyến mãi (Đồng)</label>
                            <input type="number" step="0.01" class="form-control" id="editGiaKhuyenMai" name="giaKhuyenMai">
                        </div>
                        <div class="mb-3">
                            <label for="editTinhTrang" class="form-label">Tình trạng</label>
                            <input type="text" class="form-control" id="editTinhTrang" name="tinhTrang">
                        </div>
                        <div class="mb-3">
                            <label for="editSoLuongTonKho" class="form-label">Số lượng tồn kho</label>
                            <input type="number" class="form-control" id="editSoLuongTonKho" name="soLuongTonKho" required>
                        </div>
                        <div class="mb-3">
                            <label for="editHinhAnh" class="form-label">Hình ảnh</label>
                            <input type="file" class="form-control" id="editHinhAnh" name="hinhAnh" accept="image/*">
                            <small id="currentImage"></small>
                        </div>
                        <div class="mb-3">
                            <label for="editChiTiet" class="form-label">Chi tiết</label>
                            <textarea class="form-control" id="editChiTiet" name="chiTiet" rows="4"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Cập nhật Sản phẩm</button>
                        <button type="button" class="btn btn-secondary" data-bs-toggle="collapse" data-bs-target="#editProductForm">Hủy</button>
                    </form>
                </div>
            </div>

            <!-- Nút để mở form thêm sản phẩm -->
            <div class="mb-4" id="addButtonContainer">
                <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#addProductForm" aria-expanded="false" aria-controls="addProductForm">
                    Thêm Sản phẩm
                </button>
            </div>

            <!-- Form thêm sản phẩm (ẩn ban đầu, trượt xuống khi nhấp nút) -->
            <div class="collapse" id="addProductForm">
                <div class="card card-body mt-3">
                    <h5>Thêm Sản phẩm</h5>
                    <form action="${pageContext.request.contextPath}/admin-san-pham" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="add">
                        <div class="mb-3">
                            <label for="tenSanPham" class="form-label">Tên sản phẩm</label>
                            <input type="text" class="form-control" id="tenSanPham" name="tenSanPham" required>
                        </div>
                        <div class="mb-3">
                            <label for="maDanhMuc" class="form-label">Danh mục</label>
                            <select class="form-select" id="maDanhMuc" name="maDanhMuc" required>
                                <option value="">Chọn danh mục</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="giaGoc" class="form-label">Giá gốc (Đồng)</label>
                            <input type="number" step="0.01" class="form-control" id="giaGoc" name="giaGoc" required>
                        </div>
                        <div class="mb-3">
                            <label for="giaKhuyenMai" class="form-label">Giá khuyến mãi (Đồng)</label>
                            <input type="number" step="0.01" class="form-control" id="giaKhuyenMai" name="giaKhuyenMai">
                        </div>
                        <div class="mb-3">
                            <label for="tinhTrang" class="form-label">Tình trạng</label>
                            <input type="text" class="form-control" id="tinhTrang" name="tinhTrang">
                        </div>
                        <div class="mb-3">
                            <label for="soLuongTonKho" class="form-label">Số lượng tồn kho</label>
                            <input type="number" class="form-control" id="soLuongTonKho" name="soLuongTonKho" required>
                        </div>
                        <div class="mb-3">
                            <label for="hinhAnh" class="form-label">Hình ảnh</label>
                            <input type="file" class="form-control" id="hinhAnh" name="hinhAnh" accept="image/*" required>
                        </div>
                        <div class="mb-3">
                            <label for="chiTiet" class="form-label">Chi tiết</label>
                            <textarea class="form-control" id="chiTiet" name="chiTiet" rows="4"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Thêm Sản phẩm</button>
                        <button type="button" class="btn btn-secondary" data-bs-toggle="collapse" data-bs-target="#addProductForm">Hủy</button>
                    </form>
                </div>
            </div>

            <!-- Bảng danh sách sản phẩm -->
            <h5>Danh sách Sản phẩm</h5>
            <table class="table table-bordered table-hover">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Tên sản phẩm</th>
                        <th>Danh mục</th>
                        <th>Giá gốc</th>
                        <th>Giá khuyến mãi</th>
                        <th>Tình trạng</th>
                        <th>Số lượng tồn kho</th>
                        <th>Hình ảnh</th>
                        <th>Chi tiết</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<SanPham> sanPhams = (List<SanPham>) request.getAttribute("sanPhams");
                        if (sanPhams != null && !sanPhams.isEmpty()) {
                            for (SanPham sp : sanPhams) {
                    %>
                        <tr>
                            <td><%= sp.getMaSanPham() %></td>
                            <td><%= sp.getTenSanPham() != null ? sp.getTenSanPham() : "" %></td>
                            <td><%= sp.getDanhMuc() != null ? sp.getDanhMuc().getTenDanhMuc() : "" %></td>
                            <td><%= sp.getGiaGoc() %></td>
                            <td><%= sp.getGiaKhuyenMai() > 0 ? sp.getGiaKhuyenMai() : "" %></td>
                            <td><%= sp.getTinhTrang() != null ? sp.getTinhTrang() : "" %></td>
                            <td><%= sp.getSoLuongTonKho() %></td>
                            <td>
                                <% if (sp.getHinhAnh() != null && !sp.getHinhAnh().isEmpty()) { %>
                                    <img src="${pageContext.request.contextPath}/<%= sp.getHinhAnh() %>" alt="<%= sp.getTenSanPham() != null ? sp.getTenSanPham() : "" %>" width="50">
                                <% } %>
                            </td>
                            <td><%= sp.getChiTiet() != null ? sp.getChiTiet() : "" %></td>
                            <td class="action-buttons">
                                <button class="btn btn-sm btn-primary edit-btn" 
                                        data-id="<%= sp.getMaSanPham() %>"
                                        data-ten="<%= sp.getTenSanPham() != null ? sp.getTenSanPham() : "" %>"
                                        data-danhmuc="<%= sp.getDanhMuc() != null ? sp.getDanhMuc().getTenDanhMuc() : "" %>"
                                        data-giagoc="<%= sp.getGiaGoc() %>"
                                        data-giakhuyenmai="<%= sp.getGiaKhuyenMai() > 0 ? sp.getGiaKhuyenMai() : "" %>"
                                        data-tinhtrang="<%= sp.getTinhTrang() != null ? sp.getTinhTrang() : "" %>"
                                        data-soluongtonkho="<%= sp.getSoLuongTonKho() %>"
                                        data-hinhanh="<%= sp.getHinhAnh() != null ? sp.getHinhAnh() : "" %>"
                                        data-chitiet="<%= sp.getChiTiet() != null ? sp.getChiTiet() : "" %>"
                                        data-danhmucid="<%= sp.getDanhMuc() != null ? sp.getDanhMuc().getMaDanhMuc() : -1 %>">
                                        Sửa
                                </button>
                                <a href="${pageContext.request.contextPath}/admin-san-pham?action=delete&id=<%= sp.getMaSanPham() %>" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">Xóa</a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="10" class="text-center">Không có sản phẩm để hiển thị.</td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>

            <!-- Quay lại Dashboard -->
            <a href="${pageContext.request.contextPath}/views/AdminDashboard.jsp" class="btn btn-dark mt-3">Quay lại Dashboard</a>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Tạo mảng danh mục
    const danhMucs = [
        <% 
            List<DanhMuc> danhMucList = (List<DanhMuc>) request.getAttribute("danhMucs");
            if (danhMucList != null) {
                for (int i = 0; i < danhMucList.size(); i++) {
                    DanhMuc dm = danhMucList.get(i);
        %>
            {
                maDanhMuc: <%= dm.getMaDanhMuc() %>,
                tenDanhMuc: "<%= dm.getTenDanhMuc() != null ? dm.getTenDanhMuc().replace("\"", "\\\"") : "" %>"
            }<%= i < danhMucList.size() - 1 ? "," : "" %>
        <% 
                }
            }
        %>
    ];

    // Hiển thị danh mục trong dropdown (Thêm)
    const danhMucSelect = document.getElementById('maDanhMuc');
    danhMucs.forEach(dm => {
        const option = document.createElement('option');
        option.value = dm.maDanhMuc;
        option.textContent = dm.tenDanhMuc;
        danhMucSelect.appendChild(option);
    });

    // Hiển thị danh mục trong dropdown (Sửa)
    const editDanhMucSelect = document.getElementById('editMaDanhMuc');
    danhMucs.forEach(dm => {
        const option = document.createElement('option');
        option.value = dm.maDanhMuc;
        option.textContent = dm.tenDanhMuc;
        editDanhMucSelect.appendChild(option);
    });

    // Xử lý sự kiện sửa sản phẩm
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function() {
            // Lấy dữ liệu từ các thuộc tính data-*
            const maSanPham = this.getAttribute('data-id');
            const tenSanPham = this.getAttribute('data-ten');
            const danhMuc = this.getAttribute('data-danhmuc');
            const giaGoc = this.getAttribute('data-giagoc');
            const giaKhuyenMai = this.getAttribute('data-giakhuyenmai');
            const tinhTrang = this.getAttribute('data-tinhtrang');
            const soLuongTonKho = this.getAttribute('data-soluongtonkho');
            const hinhAnh = this.getAttribute('data-hinhanh');
            const chiTiet = this.getAttribute('data-chitiet');
            const danhMucId = this.getAttribute('data-danhmucid');

            // Điền dữ liệu vào form
            document.getElementById('editMaSanPham').value = maSanPham;
            document.getElementById('editTenSanPham').value = tenSanPham;
            document.getElementById('editGiaGoc').value = giaGoc;
            document.getElementById('editGiaKhuyenMai').value = giaKhuyenMai;
            document.getElementById('editTinhTrang').value = tinhTrang;
            document.getElementById('editSoLuongTonKho').value = soLuongTonKho;
            document.getElementById('currentImage').innerHTML = hinhAnh ? `<img src="${hinhAnh}" alt="${tenSanPham}" width="50" class="mt-2">` : '';
            document.getElementById('editChiTiet').value = chiTiet;

            // Chọn danh mục trong dropdown
            Array.from(editDanhMucSelect.options).forEach(option => {
                option.selected = option.value == danhMucId;
            });

            // Mở form sửa và ẩn nút Thêm
            const editCollapse = new bootstrap.Collapse(document.getElementById('editProductForm'), { toggle: true });
            document.getElementById('addButtonContainer').style.display = 'none';
            editCollapse._element.addEventListener('hidden.bs.collapse', () => {
                document.getElementById('addButtonContainer').style.display = 'block';
            });
        });
    });

    // Xử lý sự kiện thêm sản phẩm
    document.querySelector('[data-bs-target="#addProductForm"]').addEventListener('click', function() {
        // Mở form thêm và ẩn nút Thêm
        const addCollapse = new bootstrap.Collapse(document.getElementById('addProductForm'), { toggle: true });
        document.getElementById('addButtonContainer').style.display = 'none';
        addCollapse._element.addEventListener('hidden.bs.collapse', () => {
            document.getElementById('addButtonContainer').style.display = 'block';
        });
    });
</script>

</body>
</html>