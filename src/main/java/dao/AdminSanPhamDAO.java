package dao;

import model.SanPham;
import java.sql.*;
import java.util.logging.Logger;

public class AdminSanPhamDAO {
    private static final Logger LOGGER = Logger.getLogger(AdminSanPhamDAO.class.getName());
    private Connection conn;

    public AdminSanPhamDAO() {
        this.conn = DBConnection.getConnection();
        if (this.conn == null) {
            LOGGER.severe("Failed to establish database connection");
            throw new RuntimeException("Database connection is null");
        }
    }

    public AdminSanPhamDAO(Connection conn) {
        this.conn = conn;
    }

    private void checkConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DBConnection.getConnection();
            if (conn == null) {
                LOGGER.severe("Failed to re-establish database connection");
                throw new RuntimeException("Failed to re-establish database connection");
            }
        }
    }

    public void add(SanPham sp) {
        String sql = "INSERT INTO san_pham (tenSanPham, idDanhMuc, giaGoc, giaKhuyenMai, tinhTrang, soLuongTonKho, hinhAnh, chiTiet) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSanPham());
            ps.setInt(2, sp.getDanhMuc().getMaDanhMuc());
            ps.setDouble(3, sp.getGiaGoc());
            ps.setDouble(4, sp.getGiaKhuyenMai());
            ps.setString(5, sp.getTinhTrang());
            ps.setInt(6, sp.getSoLuongTonKho());
            ps.setString(7, sp.getHinhAnh());
            ps.setString(8, sp.getChiTiet());
            ps.executeUpdate();
            LOGGER.info("Added product: " + sp.getTenSanPham());
        } catch (SQLException e) {
            LOGGER.severe("Error adding product: " + e.getMessage());
            throw new RuntimeException("Failed to add product", e);
        }
    }

    public void update(SanPham sp) {
        String sql = "UPDATE san_pham SET tenSanPham = ?, idDanhMuc = ?, giaGoc = ?, giaKhuyenMai = ?, tinhTrang = ?, soLuongTonKho = ?, hinhAnh = ?, chiTiet = ? WHERE maSanPham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSanPham());
            ps.setInt(2, sp.getDanhMuc().getMaDanhMuc());
            ps.setDouble(3, sp.getGiaGoc());
            ps.setDouble(4, sp.getGiaKhuyenMai());
            ps.setString(5, sp.getTinhTrang());
            ps.setInt(6, sp.getSoLuongTonKho());
            ps.setString(7, sp.getHinhAnh());
            ps.setString(8, sp.getChiTiet());
            ps.setInt(9, sp.getMaSanPham());
            ps.executeUpdate();
            LOGGER.info("Updated product with ID: " + sp.getMaSanPham());
        } catch (SQLException e) {
            LOGGER.severe("Error updating product: " + e.getMessage());
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public boolean canDelete(int id) {
        String sql = "SELECT COUNT(*) FROM chi_tiet_don_hang WHERE maSanPham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Không thể xóa vì có liên kết
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error checking delete eligibility: " + e.getMessage());
        }
        return true;
    }

    public void delete(int id) {
        try {
            checkConnection();
            conn.setAutoCommit(false);

            if (!canDelete(id)) {
                throw new RuntimeException("Cannot delete product because it is linked to an order.");
            }

            String sql = "DELETE FROM san_pham WHERE maSanPham = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.info("Deleted product with ID: " + id);
                }
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                LOGGER.severe("Rolled back transaction due to error: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                LOGGER.severe("Error during rollback: " + rollbackEx.getMessage());
            }
            LOGGER.severe("Error deleting product: " + e.getMessage());
            throw new RuntimeException("Failed to delete product: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.severe("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException e) {
            LOGGER.severe("Error closing connection: " + e.getMessage());
        }
    }
}