package dao;

import model.DonHang;
import model.ChiTietDonHang;
import java.sql.*;
import java.util.List;

public class DatHangDAO {

    // Thêm mới hoặc lấy người dùng từ thông tin
    public int themHoacLayNguoiDung(String fullName, String phone, String email, String address) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT maNguoiDung FROM nguoi_dung WHERE email = ? OR sdt = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, phone);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("maNguoiDung");
            } else {
                String insertSql = "INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, fullName);
                insertStatement.setString(2, phone);
                insertStatement.setString(3, email);
                insertStatement.setString(4, address);
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return -1;
    }

    // Xử lý toàn bộ quy trình đặt hàng
    public boolean placeOrder(DonHang donHang, List<ChiTietDonHang> dsChiTiet) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Kiểm tra số lượng tồn kho
            for (ChiTietDonHang ct : dsChiTiet) {
                String checkStockSql = "SELECT soLuongTonKho FROM san_pham WHERE maSanPham = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkStockSql);
                checkStmt.setInt(1, ct.getMaSanPham());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int soLuongTonKho = rs.getInt("soLuongTonKho");
                    if (soLuongTonKho < ct.getSoLuong()) {
                        conn.rollback();
                        return false; // Không đủ tồn kho
                    }
                } else {
                    conn.rollback();
                    return false; // Sản phẩm không tồn tại
                }
            }

            // 2. Thêm đơn hàng
            String insertOrderSql = "INSERT INTO don_hang (ngayLap, trangThai, tongTien, maNguoiDung) VALUES (?, ?, ?, ?)";
            PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setDate(1, new java.sql.Date(donHang.getNgayLap().getTime()));
            orderStmt.setString(2, donHang.getTrangThai());
            orderStmt.setDouble(3, donHang.getTongTien());
            orderStmt.setInt(4, donHang.getMaNguoiDung());
            orderStmt.executeUpdate();

            ResultSet rs = orderStmt.getGeneratedKeys();
            int maDonHang = rs.next() ? rs.getInt(1) : -1;
            if (maDonHang == -1) {
                conn.rollback();
                return false;
            }

            // 3. Thêm chi tiết đơn hàng
            String insertDetailSql = "INSERT INTO chi_tiet_don_hang (maDonHang, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";
            PreparedStatement detailStmt = conn.prepareStatement(insertDetailSql);
            for (ChiTietDonHang ct : dsChiTiet) {
                detailStmt.setInt(1, maDonHang);
                detailStmt.setInt(2, ct.getMaSanPham());
                detailStmt.setInt(3, ct.getSoLuong());
                detailStmt.setDouble(4, ct.getDonGia());
                detailStmt.addBatch();
            }
            detailStmt.executeBatch();

            // 4. Cập nhật số lượng tồn kho
            String updateStockSql = "UPDATE san_pham SET soLuongTonKho = soLuongTonKho - ? WHERE maSanPham = ?";
            PreparedStatement stockStmt = conn.prepareStatement(updateStockSql);
            for (ChiTietDonHang ct : dsChiTiet) {
                stockStmt.setInt(1, ct.getSoLuong());
                stockStmt.setInt(2, ct.getMaSanPham());
                stockStmt.addBatch();
            }
            stockStmt.executeBatch();

            // 5. Xóa giỏ hàng
            String deleteCartSql = "DELETE FROM chi_tiet_gio_hang WHERE maGioHang = (SELECT maGioHang FROM gio_hang WHERE maNguoiDung = ?)";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteCartSql);
            deleteStmt.setInt(1, donHang.getMaNguoiDung());
            deleteStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}