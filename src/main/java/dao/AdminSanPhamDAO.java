package dao;

import interfaces.IAdminSanPham;
import model.SanPham;
import model.DanhMuc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminSanPhamDAO implements IAdminSanPham {
    public void add(SanPham sanPham) throws SQLException {
        String sql = "INSERT INTO san_pham (maSanPham, tenSanPham, idDanhMuc, giaGoc, giaKhuyenMai, tinhTrang, soLuongTonKho, hinhAnh, chiTiet) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sanPham.getMaSanPham());
            ps.setString(2, sanPham.getTenSanPham());
            ps.setInt(3, sanPham.getDanhMuc().getMaDanhMuc());
            ps.setDouble(4, sanPham.getGiaGoc());
            ps.setDouble(5, sanPham.getGiaGoc()); // Giá khuyến mãi bằng giá gốc
            ps.setString(6, sanPham.getTinhTrang());
            ps.setInt(7, sanPham.getSoLuongTonKho());
            ps.setString(8, sanPham.getHinhAnh());
            ps.setString(9, sanPham.getChiTiet());
            ps.executeUpdate();
            System.out.println("Thêm sản phẩm thành công.");
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            throw e;
        }
    }

    public void update(SanPham sanPham) throws SQLException {
        String sql = "UPDATE san_pham SET tenSanPham = ?, idDanhMuc = ?, giaGoc = ?, giaKhuyenMai = ?, tinhTrang = ?, soLuongTonKho = ?, hinhAnh = ?, chiTiet = ? WHERE maSanPham = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sanPham.getTenSanPham());
            ps.setInt(2, sanPham.getDanhMuc().getMaDanhMuc());
            ps.setDouble(3, sanPham.getGiaGoc());
            ps.setDouble(4, sanPham.getGiaKhuyenMai());
            ps.setString(5, sanPham.getTinhTrang());
            ps.setInt(6, sanPham.getSoLuongTonKho());
            ps.setString(7, sanPham.getHinhAnh());
            ps.setString(8, sanPham.getChiTiet());
            ps.setInt(9, sanPham.getMaSanPham());
            ps.executeUpdate();
            System.out.println("Chỉnh sửa thành công.");
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            throw e;
        }
    }

    public void delete(int maSanPham) throws SQLException {
        String checkOrderSql = "SELECT COUNT(*) FROM chi_tiet_don_hang WHERE maSanPham = ?";
        String checkCartSql = "SELECT COUNT(*) FROM chi_tiet_gio_hang WHERE maSanPham = ?";
        String checkTinTucSql = "SELECT maTinTuc FROM tin_tuc WHERE maSanPham = ?";
        String checkKhuyenMaiSql = "SELECT maKhuyenMai FROM khuyen_mai WHERE maSanPham = ?";
        String deleteSql = "DELETE FROM san_pham WHERE maSanPham = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra chi tiết đơn hàng
            try (PreparedStatement ps = conn.prepareStatement(checkOrderSql)) {
                ps.setInt(1, maSanPham);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Không thể xóa sản phẩm vì sản phẩm đang được sử dụng trong đơn hàng.");
                    }
                }
            }
            // Kiểm tra chi tiết giỏ hàng
            try (PreparedStatement ps = conn.prepareStatement(checkCartSql)) {
                ps.setInt(1, maSanPham);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Không thể xóa sản phẩm vì sản phẩm đang có trong giỏ hàng.");
                    }
                }
            }
         // Kiểm tra tin tức liên quan đến sản phẩm
            List<Integer> tinTucIds = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(checkTinTucSql)) {
                ps.setInt(1, maSanPham);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        tinTucIds.add(rs.getInt("maTinTuc"));
                    }
                }
            }
            if (!tinTucIds.isEmpty()) {
                String tinTucIdList = String.join(", ", tinTucIds.stream().map(String::valueOf).toList());
                throw new SQLException("Không thể xóa sản phẩm vì đang được sử dụng trong tin tức. Mã tin tức là: " + tinTucIdList);
            }
            // Kiểm tra khuyến mãi
            List<Integer> khuyenMaiIds = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(checkKhuyenMaiSql)) {
                ps.setInt(1, maSanPham);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                    	khuyenMaiIds.add(rs.getInt("maKhuyenMai"));
                    }
                }
            }
            if (!khuyenMaiIds.isEmpty()) {
                String khuyenMaiIdList = String.join(", ", khuyenMaiIds.stream().map(String::valueOf).toList());
                throw new SQLException("Vui lòng xóa khuyến mãi ra khỏi sản phẩm. Mã khuyến mãi là: " + khuyenMaiIdList);
            }
            
            // Xóa sản phẩm
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setInt(1, maSanPham);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            throw e;
        }
    }
}