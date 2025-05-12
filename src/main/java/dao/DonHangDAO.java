package dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import interfaces.IQuanLyDonHang;
import model.*;

public class DonHangDAO implements IQuanLyDonHang {
    private Connection connection;

    public DonHangDAO() {
        this.connection = DBConnection.getConnection();
    }

    public List<DonHang> getAllDonHang() throws SQLException {
        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT dh.* FROM don_hang dh";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                DonHang donHang = new DonHang(
                    rs.getInt("maDonHang"),
                    rs.getDate("ngayLap"),
                    rs.getString("trangThai"),
                    rs.getDouble("tongTien"),
                    rs.getInt("maNguoiDung")
                );
                donHangs.add(donHang);
            }
        }

        return donHangs;
    }

    public List<DonHang> getDonHangsByTrangThai(String trangThai) throws SQLException {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được null hoặc rỗng");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT * FROM don_hang WHERE trangThai = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trangThai);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DonHang donHang = new DonHang(
                        rs.getInt("maDonHang"),                      
                        rs.getDate("ngayLap"),
                        rs.getString("trangThai"),
                        rs.getDouble("tongTien"),
                        rs.getInt("maNguoiDung")
                    );
                    donHangs.add(donHang);
                }
            }
        }   
        return donHangs;
    }

    public List<DonHang> getDonHangsByTrangThaiThanhToan(String trangThaiThanhToan) throws SQLException {
        if (trangThaiThanhToan == null || trangThaiThanhToan.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái thanh toán không được null hoặc rỗng");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String query = """
            SELECT dh.*
            FROM don_hang dh
            JOIN thanh_toan tt ON dh.maDonHang = tt.maDonHang
            WHERE tt.trangThai = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trangThaiThanhToan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DonHang donHang = new DonHang(
                        rs.getInt("maDonHang"),
                        rs.getDate("ngayLap"),
                        rs.getString("trangThai"),
                        rs.getDouble("tongTien"),
                        rs.getInt("maNguoiDung")
                    );
                    donHangs.add(donHang);
                }
            }
        }

        return donHangs;
    }

    public List<DonHang> getDonHangsByNgayLap(Date ngayLap) throws SQLException {
        if (ngayLap == null) {
            throw new IllegalArgumentException("Ngày lập không được null");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT * FROM don_hang WHERE ngayLap = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, ngayLap);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DonHang donHang = new DonHang(
                        rs.getInt("maDonHang"),
                        rs.getDate("ngayLap"),
                        rs.getString("trangThai"),
                        rs.getDouble("tongTien"),
                        rs.getInt("maNguoiDung")
                    );
                    donHangs.add(donHang);
                }
            }
        }

        return donHangs;
    }

    public List<DonHang> getDonHangsByMaNguoiDung(int maNguoiDung) throws SQLException {
        if (maNguoiDung <= 0) {
            throw new IllegalArgumentException("Mã người dùng không hợp lệ");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT * FROM don_hang WHERE maNguoiDung = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maNguoiDung);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DonHang donHang = new DonHang(
                        rs.getInt("maDonHang"),
                        rs.getDate("ngayLap"),
                        rs.getString("trangThai"),
                        rs.getDouble("tongTien"),
                        rs.getInt("maNguoiDung")
                    );
                    donHangs.add(donHang);
                }
            }
        }

        return donHangs;
    }

    public List<SanPhamInDonHang> getSanPhamInDonHang(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        List<SanPhamInDonHang> sanPhamList = new ArrayList<>();
        String query = "SELECT sp.tenSanPham, cth.soLuong, cth.donGia " +
                      "FROM chi_tiet_don_hang cth " +
                      "JOIN san_pham sp ON cth.maSanPham = sp.maSanPham " +
                      "WHERE cth.maDonHang = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maDonHang);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tenSanPham = rs.getString("tenSanPham");
                    int soLuong = rs.getInt("soLuong");
                    double gia = rs.getDouble("donGia");
                    sanPhamList.add(new SanPhamInDonHang(tenSanPham, soLuong, gia));
                }
            }
        }

        return sanPhamList;
    }

    public List<DonHangWithUser> getDonHangWithUser(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        List<DonHangWithUser> donHangWithUsers = new ArrayList<>();
        String query = "SELECT dh.maDonHang, dh.ngayLap, dh.trangThai, th.trangThai AS trangThaiThanhToan, dh.tongTien, " +
                      "nd.diaChi, nd.hoTen AS tenNguoiDung " +
                      "FROM don_hang dh " +
                      "INNER JOIN nguoi_dung nd ON dh.maNguoiDung = nd.maNguoiDung " +
                      "INNER JOIN thanh_toan th ON dh.maDonHang = th.maDonHang " +
                      "WHERE dh.maDonHang = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maDonHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    // Nếu không có kết quả, in thông báo và trả về danh sách trống
                    System.out.println("Không có kết quả cho mã đơn hàng: " + maDonHang);
                    return donHangWithUsers;  // Trả về danh sách trống
                }

                // Nếu có kết quả, xử lý dữ liệu
                while (rs.next()) {
                    System.out.println("Đơn hàng đã tìm thấy: " + rs.getInt("maDonHang"));
                    DonHangWithUser donHangWithUser = new DonHangWithUser(
                        rs.getInt("maDonHang"),
                        rs.getDate("ngayLap"),
                        rs.getString("trangThai"),
                        rs.getString("trangThaiThanhToan"),
                        rs.getDouble("tongTien"),
                        rs.getString("diaChi"),
                        rs.getString("tenNguoiDung"),
                        getSanPhamInDonHang(rs.getInt("maDonHang"))  // Giả sử phương thức này trả về danh sách sản phẩm
                    );
                    donHangWithUsers.add(donHangWithUser);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi truy vấn cơ sở dữ liệu", e);
        }

        return donHangWithUsers;
    }

    public void updateTrangThaiDonHang(int maDonHang, String trangThaiMoi) throws SQLException {
        if (maDonHang <= 0 || trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng hoặc trạng thái mới không hợp lệ");
        }

        String query = "UPDATE don_hang SET trangThai = ? WHERE maDonHang = ?";
        System.out.print("đến đây vẫn ổn");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trangThaiMoi);
            stmt.setInt(2, maDonHang);
            System.out.print("trang thai don hang moi: " + trangThaiMoi + "he");
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy đơn hàng với mã: " + maDonHang);
            }
        }
    }

    public String getTrangThaiDonHang(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        String query = "SELECT trangThai FROM don_hang WHERE maDonHang = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maDonHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("trangThai");
                }
                throw new SQLException("Không tìm thấy đơn hàng với mã: " + maDonHang);
            }
        }
    }

    public void updatePaymentStatus(int maDonHang, String thanhToanTrangThai) throws SQLException {
        if (maDonHang <= 0 || thanhToanTrangThai == null || thanhToanTrangThai.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng hoặc trạng thái thanh toán không hợp lệ");
        }

        String sql = "UPDATE thanh_toan SET trangThai = ? WHERE maDonHang = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, thanhToanTrangThai);
            stmt.setInt(2, maDonHang);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy thanh toán cho đơn hàng: " + maDonHang);
            }
        }
    }


    public void addChiTietDonHang(int maDonHang, int maSanPham, int soLuong, Double donGia) throws SQLException {
        if (maDonHang <= 0 || maSanPham <= 0 || soLuong <= 0 || donGia == null || donGia <= 0) {
            throw new IllegalArgumentException("Dữ liệu đầu vào không hợp lệ");
        }

        String query = "INSERT INTO chi_tiet_don_hang (maDonHang, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maDonHang);
            stmt.setInt(2, maSanPham);
            stmt.setInt(3, soLuong);
            stmt.setDouble(4, donGia);
            stmt.executeUpdate();
        }
    }

    public void updateDiaChiNguoiDungTheoMaDonHang(int maDonHang, String diaChiMoi) throws SQLException {
        if (maDonHang <= 0 || diaChiMoi == null || diaChiMoi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng hoặc địa chỉ mới không hợp lệ");
        }

        String sql = """
            UPDATE nguoi_dung 
            SET diaChi = ? 
            WHERE maNguoiDung = (
                SELECT maNguoiDung 
                FROM don_hang 
                WHERE maDonHang = ?
            )
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, diaChiMoi);
            stmt.setInt(2, maDonHang);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy đơn hàng hoặc người dùng liên quan đến mã: " + maDonHang);
            }
        }
    }

    public List<ChiTietDonHang> getChiTietDonHang(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();
        String query = "SELECT * FROM chi_tiet_don_hang WHERE maDonHang = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maDonHang);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietDonHang chiTiet = new ChiTietDonHang(
                        rs.getInt("maDonHang"),
                        rs.getInt("maSanPham"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia")
                    );
                    chiTietDonHangs.add(chiTiet);
                }
            }
        }

        return chiTietDonHangs;
    }

    public void huyDonHang(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        String updateDonHangQuery = "UPDATE don_hang SET trangThai = 'Đã hủy' WHERE maDonHang = ?";
        String getChiTietQuery = "SELECT maSanPham, soLuong FROM chi_tiet_don_hang WHERE maDonHang = ?";
        String updateKhoQuery = "UPDATE san_pham SET soLuongTonKho = soLuongTonKho + ? WHERE maSanPham = ?";

        // Bắt đầu giao dịch
        try {
            connection.setAutoCommit(false);

            // Bước 1: Cập nhật trạng thái đơn hàng thành 'Đã hủy'
            try (PreparedStatement stmt = connection.prepareStatement(updateDonHangQuery)) {
                stmt.setInt(1, maDonHang);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không tìm thấy đơn hàng với mã: " + maDonHang);
                }
            }

            // Bước 2: Lấy chi tiết các sản phẩm trong đơn hàng để cập nhật lại số lượng kho
            try (PreparedStatement stmt = connection.prepareStatement(getChiTietQuery)) {
                stmt.setInt(1, maDonHang);
                try (ResultSet rs = stmt.executeQuery()) {
                    // Bước 3: Cập nhật lại số lượng sản phẩm trong kho
                    while (rs.next()) {
                        int maSanPham = rs.getInt("maSanPham");
                        int soLuong = rs.getInt("soLuong");

                        try (PreparedStatement stmtUpdateKho = connection.prepareStatement(updateKhoQuery)) {
                            stmtUpdateKho.setInt(1, soLuong); // Thêm vào kho
                            stmtUpdateKho.setInt(2, maSanPham);
                            stmtUpdateKho.executeUpdate();
                        }
                    }
                }
            }

            connection.commit(); // Xác nhận giao dịch
        } catch (SQLException e) {
            connection.rollback(); // Hoàn tác nếu có lỗi
            throw e; // Ném lại lỗi để có thể xử lý ở nơi khác (ví dụ: Controller)
        } finally {
            connection.setAutoCommit(true); // Khôi phục chế độ auto-commit
        }
    }
    

}