package dao;

import model.DonHang;
import model.ChiTietDonHang;
import java.sql.*;
import java.util.List;

public class DatHangDAO {

    private Connection connection;

    public DatHangDAO() {
        this.connection = DBConnection.getConnection();
    }

    // Thêm mới hoặc lấy người dùng từ thông tin
    public int themHoacLayNguoiDung(String fullName, String phone, String email, String address) throws SQLException {
        String sql = "SELECT maNguoiDung FROM nguoi_dung WHERE email = ? AND sdt = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, phone);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("maNguoiDung");
                }
            }
        }

        String insertSql = "INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, fullName);
            insertStatement.setString(2, phone);
            insertStatement.setString(3, email);
            insertStatement.setString(4, address);
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }

        return -1;
    }

    // Xử lý toàn bộ quy trình đặt hàng
    public boolean placeOrder(DonHang donHang, List<ChiTietDonHang> dsChiTiet) throws SQLException {
        try {
            connection.setAutoCommit(false);

            // 1. Thêm đơn hàng
            String insertOrderSql = "INSERT INTO don_hang (ngayLap, trangThai, tongTien, maNguoiDung) VALUES (?, ?, ?, ?)";
            try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setDate(1, new java.sql.Date(donHang.getNgayLap().getTime()));
                orderStmt.setString(2, donHang.getTrangThai());
                orderStmt.setDouble(3, donHang.getTongTien());
                orderStmt.setInt(4, donHang.getMaNguoiDung());
                orderStmt.executeUpdate();

                try (ResultSet rs = orderStmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        connection.rollback();
                        return false;
                    }
                    int maDonHang = rs.getInt(1);
                    donHang.setMaDonHang(maDonHang);
                }
            }

            // 2. Thêm chi tiết đơn hàng
            String insertDetailSql = "INSERT INTO chi_tiet_don_hang (maDonHang, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";
            try (PreparedStatement detailStmt = connection.prepareStatement(insertDetailSql)) {
                for (ChiTietDonHang ct : dsChiTiet) {
                    detailStmt.setInt(1, donHang.getMaDonHang());
                    detailStmt.setInt(2, ct.getMaSanPham());
                    detailStmt.setInt(3, ct.getSoLuong());
                    detailStmt.setDouble(4, ct.getDonGia());
                    detailStmt.addBatch();
                }
                detailStmt.executeBatch();
            }

            // 3. Cập nhật số lượng tồn kho
            String updateStockSql = "UPDATE san_pham SET soLuongTonKho = soLuongTonKho - ? WHERE maSanPham = ?";
            try (PreparedStatement stockStmt = connection.prepareStatement(updateStockSql)) {
                for (ChiTietDonHang ct : dsChiTiet) {
                    stockStmt.setInt(1, ct.getSoLuong());
                    stockStmt.setInt(2, ct.getMaSanPham());
                    stockStmt.addBatch();
                }
                int[] updateCounts = stockStmt.executeBatch();
                for (int count : updateCounts) {
                    if (count == 0) {
                        connection.rollback();
                        return false; // Sản phẩm không tồn tại hoặc lỗi cập nhật
                    }
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}