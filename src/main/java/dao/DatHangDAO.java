package dao;

import model.DonHang;
import model.ChiTietDonHang;
import model.NguoiDung;
import java.sql.*;
import java.util.List;

public class DatHangDAO {
    private Connection connection;

    public DatHangDAO() {
        connection = DBConnection.getConnection(); // Giả sử DBConnection đã có sẵn
    }

    // Thêm mới hoặc lấy người dùng từ thông tin
    public int themHoacLayNguoiDung(String fullName, String phone, String email, String address) {
        String sql = "SELECT maNguoiDung FROM nguoi_dung WHERE email = ? OR sdt = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, phone);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
            	// nếu người dùng tồn tại thì return luôn
                return resultSet.getInt("maNguoiDung");
            } else {
            	//còn chưa thì phải insert vào rồi mới return
                String insertSql = "INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStatement.setString(1, fullName);
                    insertStatement.setString(2, phone);
                    insertStatement.setString(3, email);;
                    insertStatement.setString(4, address);
                    int rowsInserted = insertStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    // Thêm đơn hàng
    public int themDonHang(DonHang donHang) {
        String sql = "INSERT INTO don_hang (ngayLap, trangThai, tongTien, maNguoiDung) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(donHang.getNgayLap().getTime()));
            stmt.setString(2, donHang.getTrangThai());
            stmt.setDouble(3, donHang.getTongTien());
            stmt.setInt(4, donHang.getMaNguoiDung());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Thêm chi tiết đơn hàng
    public void themChiTietDonHang(int maDonHang, List<ChiTietDonHang> dsChiTiet) throws SQLException {
        String sql = "INSERT INTO chi_tiet_don_hang (maDonHang, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (ChiTietDonHang ct : dsChiTiet) {
                stmt.setInt(1, maDonHang);
                stmt.setInt(2, ct.getMaSanPham());
                stmt.setInt(3, ct.getSoLuong());
                stmt.setDouble(4, ct.getDonGia());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }


 // Cập nhật tồn kho sản phẩm
    public void capNhatSoLuongSanPham(int maSanPham, int soLuongDat) throws SQLException {
        String sql = "UPDATE san_pham SET soLuongTonKho = soLuongTonKho - ? WHERE maSanPham = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, soLuongDat);
            stmt.setInt(2, maSanPham);
            stmt.executeUpdate();
        }
    }
}
