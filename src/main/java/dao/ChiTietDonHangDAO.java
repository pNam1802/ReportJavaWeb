package dao;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import model.*;
public class ChiTietDonHangDAO {

    private Connection connection;
    
    public ChiTietDonHangDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Xóa một sản phẩm khỏi đơn hàng
     * @param maDonHang mã đơn hàng
     * @param maSanPham mã sản phẩm cần xóa
     * @throws SQLException
     */
    public void xoaSanPhamKhoiDonHang(int maDonHang, int maSanPham) throws SQLException {
        String deleteSql = "DELETE FROM chi_tiet_don_hang WHERE maDonHang = ? AND maSanPham = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, maDonHang);
            stmt.setInt(2, maSanPham);
            stmt.executeUpdate();
        }

        // Cập nhật lại tổng tiền sau khi xóa
        capNhatTongTienDonHang(maDonHang);
    }

    /**
     * Cập nhật tổng tiền của đơn hàng dựa trên các sản phẩm còn lại
     * @param maDonHang mã đơn hàng cần cập nhật
     * @throws SQLException
     */
    public void capNhatTongTienDonHang(int maDonHang) throws SQLException {
        String sumSql = "SELECT SUM(soLuong * donGia) AS tongTien FROM chi_tiet_don_hang WHERE maDonHang = ?";
        double tongTien = 0.0;

        try (PreparedStatement stmt = connection.prepareStatement(sumSql)) {
            stmt.setInt(1, maDonHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tongTien = rs.getDouble("tongTien");
                }
            }
        }

        String updateSql = "UPDATE don_hang SET tongTien = ? WHERE maDonHang = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setDouble(1, tongTien);
            stmt.setInt(2, maDonHang);
            stmt.executeUpdate();
        }
    }
    public void capNhatSoLuong(int maDonHang, int maSanPham, int soLuong) {
        String sql = "UPDATE chi_tiet_don_hang SET soLuong = ? WHERE maDonHang = ? AND maSanPham = ?";
        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, soLuong);
            ps.setInt(2, maDonHang);
            ps.setInt(3, maSanPham);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
