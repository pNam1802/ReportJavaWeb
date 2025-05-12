package dao;

import model.DonHang;
import model.ChiTietDonHang;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;



public class DatHangDAO {

    private static final Logger LOGGER = Logger.getLogger(DatHangDAO.class.getName());
    private final Connection connection;

    //Các trạng thái cố định dùng trong hệ thống
    
    public static class TrangThai {
        public static final String CHUA_THANH_TOAN = "Chưa thanh toán";
        public static final String DA_THANH_TOAN = "Đã thanh toán";
        public static final String DANG_XU_LY = "Chờ xử lý";
    }

    //Các hình thức thanh toán
    
    public static class HinhThucThanhToan {
        public static final String COD = "COD";
    }

    //Khởi tạo DatHangDAO với kết nối cơ sở dữ liệu

    public DatHangDAO() {
        this.connection = DBConnection.getConnection();
    }

    //Thêm mới hoặc lấy mã người dùng dựa trên thông tin cung cấp.

    public int themHoacLayNguoiDung(String fullName, String phone, String email, String address) throws SQLException {
        if (fullName == null || phone == null || email == null || address == null) {
            throw new IllegalArgumentException("Thông tin người dùng không được để trống!");
        }

        // Kiểm tra người dùng đã tồn tại
        String selectSql = "SELECT maNguoiDung FROM nguoi_dung WHERE email = ? AND sdt = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setString(1, email);
            statement.setString(2, phone);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("maNguoiDung");
                }
            }
        }

        // Thêm người dùng mới
        String insertSql = "INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, fullName);
            insertStatement.setString(2, phone);
            insertStatement.setString(3, email);
            insertStatement.setString(4, address);
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Không thể thêm người dùng mới!");
            }
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Không thể lấy mã người dùng mới!");
            }
        }
    }

    //Thêm thông tin thanh toán cho đơn hàng.
     
    public boolean addPayment(DonHang donHang) throws SQLException {
        if (donHang == null || donHang.getMaDonHang() <= 0) {
            throw new IllegalArgumentException("Đơn hàng không hợp lệ!");
        }

        String insertPaymentSql = "INSERT INTO thanh_toan (maDonHang, hinhThucThanhToan, trangThai, ngayThanhToan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertPaymentSql)) {
            stmt.setInt(1, donHang.getMaDonHang());
            stmt.setString(2, HinhThucThanhToan.COD); // Thanh toán khi giao hàng
            stmt.setString(3, TrangThai.CHUA_THANH_TOAN); // Chưa thanh toán
            stmt.setNull(4, Types.DATE); // Ngày thanh toán để null vì chưa thanh toán
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Lỗi khi thêm thanh toán: " + e.getMessage());
            throw e;
        }
    }

    // Xử lý toàn bộ quy trình đặt hàng, bao gồm thêm đơn hàng, chi tiết đơn hàng, cập nhật tồn kho, và thanh toán.
   
    public boolean placeOrder(DonHang donHang, List<ChiTietDonHang> dsChiTiet) throws SQLException {
        if (donHang == null || dsChiTiet == null || dsChiTiet.isEmpty()) {
            throw new IllegalArgumentException("Đơn hàng hoặc chi tiết đơn hàng không hợp lệ!");
        }

        try {
            connection.setAutoCommit(false);

            // Kiểm tra tồn kho
            checkStockAvailability(dsChiTiet);

            // Thêm đơn hàng
            int maDonHang = insertOrder(donHang);
            donHang.setMaDonHang(maDonHang);

            // Thêm chi tiết đơn hàng
            insertOrderDetails(maDonHang, dsChiTiet);

            // Cập nhật tồn kho
            updateStock(dsChiTiet);

            // Thêm thanh toán
            addPayment(donHang);

            connection.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.severe("Lỗi khi đặt hàng: " + e.getMessage());
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //Kiểm tra số lượng tồn kho của các sản phẩm trong đơn hàng.

    private void checkStockAvailability(List<ChiTietDonHang> dsChiTiet) throws SQLException {
        String checkStockSql = "SELECT soLuongTonKho FROM san_pham WHERE maSanPham = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkStockSql)) {
            for (ChiTietDonHang ct : dsChiTiet) {
                checkStmt.setInt(1, ct.getMaSanPham());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int soLuongTonKho = rs.getInt("soLuongTonKho");
                        if (soLuongTonKho < ct.getSoLuong()) {
                            throw new SQLException("Sản phẩm " + ct.getMaSanPham() + " không đủ tồn kho!");
                        }
                    } else {
                        throw new SQLException("Sản phẩm " + ct.getMaSanPham() + " không tồn tại!");
                    }
                }
            }
        }
    }

    //Thêm đơn hàng vào cơ sở dữ liệu.
     
    private int insertOrder(DonHang donHang) throws SQLException {
        String sql = "INSERT INTO don_hang (ngayLap, trangThai, tongTien, maNguoiDung) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(donHang.getNgayLap().getTime()));
            stmt.setString(2, donHang.getTrangThai() != null ? donHang.getTrangThai() : TrangThai.DANG_XU_LY);
            stmt.setDouble(3, donHang.getTongTien());
            stmt.setInt(4, donHang.getMaNguoiDung());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Không thể lấy mã đơn hàng!");
            }
        } catch (SQLException e) {
            LOGGER.severe("Lỗi khi thêm đơn hàng: " + e.getMessage());
            throw e;
        }
    }

    //Thêm danh sách chi tiết đơn hàng vào cơ sở dữ liệu.
 
    private void insertOrderDetails(int maDonHang, List<ChiTietDonHang> dsChiTiet) throws SQLException {
        String sql = "INSERT INTO chi_tiet_don_hang (maDonHang, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (ChiTietDonHang ct : dsChiTiet) {
                stmt.setInt(1, maDonHang);
                stmt.setInt(2, ct.getMaSanPham());
                stmt.setInt(3, ct.getSoLuong());
                stmt.setDouble(4, ct.getDonGia());
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            for (int result : results) {
                if (result == Statement.EXECUTE_FAILED) {
                    throw new SQLException("Thêm chi tiết đơn hàng thất bại!");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Lỗi khi thêm chi tiết đơn hàng: " + e.getMessage());
            throw e;
        }
    }

    // Cập nhật số lượng tồn kho dựa trên chi tiết đơn hàng.
 
    private void updateStock(List<ChiTietDonHang> dsChiTiet) throws SQLException {
        String sql = "UPDATE san_pham SET soLuongTonKho = soLuongTonKho - ? WHERE maSanPham = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (ChiTietDonHang ct : dsChiTiet) {
                stmt.setInt(1, ct.getSoLuong());
                stmt.setInt(2, ct.getMaSanPham());
                stmt.addBatch();
            }
            int[] updateCounts = stmt.executeBatch();
            for (int count : updateCounts) {
                if (count == 0) {
                    throw new SQLException("Cập nhật tồn kho thất bại!");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Lỗi khi cập nhật tồn kho: " + e.getMessage());
            throw e;
        }
    }
}