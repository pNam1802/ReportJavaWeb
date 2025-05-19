package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import interfaces.IQuanLyDonHang;
import model.*;

public class DonHangDAO implements IQuanLyDonHang {
    private Connection connection;

    public DonHangDAO() {
        this.connection = DBConnection.getConnection();
    }


    // Phương thức ánh xạ ResultSet sang DonHang để tái sử dụng
    private DonHang mapResultSetToDonHang(ResultSet rs) throws SQLException {
        return new DonHang(
            rs.getInt("maDonHang"),
            rs.getDate("ngayLap"),
            rs.getString("trangThai"),
            rs.getDouble("tongTien"),
            rs.getInt("maNguoiDung")
        );
    }

    // Lấy tất cả đơn hàng với phân trang
    public List<DonHang> getAllDonHang(int offset, int limit) throws SQLException {
        List<DonHang> donHangs = new ArrayList<>();
        String sql = "SELECT * FROM don_hang ORDER BY ngayLap DESC LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    donHangs.add(mapResultSetToDonHang(rs));
                }
            }
        }
        return donHangs;
    }

    // Lấy đơn hàng theo trạng thái với phân trang
    public List<DonHang> getDonHangsByTrangThai(String trangThai, int offset, int limit) throws SQLException {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được null hoặc rỗng");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String sql = "SELECT * FROM don_hang WHERE trangThai = ? ORDER BY ngayLap DESC LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    donHangs.add(mapResultSetToDonHang(rs));
                }
            }
        }
        return donHangs;
    }

    // Đếm tổng số đơn hàng
    public int getTotalDonHangs() throws SQLException {
        String sql = "SELECT COUNT(*) FROM don_hang";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm tổng số đơn hàng theo trạng thái
    public int getTotalDonHangsByTrangThai(String trangThai) throws SQLException {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được null hoặc rỗng");
        }

        String sql = "SELECT COUNT(*) FROM don_hang WHERE trangThai = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Lấy tất cả đơn hàng (không phân trang)
    public List<DonHang> getAllDonHang() throws SQLException {
        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT * FROM don_hang ORDER BY ngayLap DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                donHangs.add(mapResultSetToDonHang(rs));
            }
        }
        return donHangs;
    }

    // Lấy đơn hàng theo trạng thái (không phân trang)
    public List<DonHang> getDonHangsByTrangThai(String trangThai) throws SQLException {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được null hoặc rỗng");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT * FROM don_hang WHERE trangThai = ? ORDER BY ngayLap DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trangThai);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    donHangs.add(mapResultSetToDonHang(rs));
                }
            }
        }
        return donHangs;
    }

    // Lấy đơn hàng theo trạng thái thanh toán
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
            ORDER BY dh.ngayLap DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trangThaiThanhToan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    donHangs.add(mapResultSetToDonHang(rs));
                }
            }
        }
        return donHangs;
    }

    // Lấy đơn hàng theo ngày lập
    public List<DonHang> getDonHangsByNgayLap(Date ngayLap) throws SQLException {
        if (ngayLap == null) {
            throw new IllegalArgumentException("Ngày lập không được null");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT * FROM don_hang WHERE ngayLap = ? ORDER BY ngayLap DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, ngayLap);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    donHangs.add(mapResultSetToDonHang(rs));
                }
            }
        }
        return donHangs;
    }

    // Lấy đơn hàng theo mã người dùng
    public List<DonHang> getDonHangsByMaNguoiDung(int maNguoiDung) throws SQLException {
        if (maNguoiDung <= 0) {
            throw new IllegalArgumentException("Mã người dùng không hợp lệ");
        }

        List<DonHang> donHangs = new ArrayList<>();
        String query = "SELECT * FROM don_hang WHERE maNguoiDung = ? ORDER BY ngayLap DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maNguoiDung);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    donHangs.add(mapResultSetToDonHang(rs));
                }
            }
        }
        return donHangs;
    }

    // Lấy danh sách sản phẩm trong đơn hàng
    public List<SanPhamInDonHang> getSanPhamInDonHang(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        List<SanPhamInDonHang> sanPhamList = new ArrayList<>();
        String query = """
            SELECT sp.tenSanPham, cth.soLuong, cth.donGia
            FROM chi_tiet_don_hang cth
            JOIN san_pham sp ON cth.maSanPham = sp.maSanPham
            WHERE cth.maDonHang = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maDonHang);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sanPhamList.add(new SanPhamInDonHang(
                        rs.getString("tenSanPham"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia")
                    ));
                }
            }
        }
        return sanPhamList;
    }

    // Lấy thông tin đơn hàng kèm thông tin người dùng
    public List<DonHangWithUser> getDonHangWithUser(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        List<DonHangWithUser> donHangWithUsers = new ArrayList<>();
        String query = """
            SELECT dh.maDonHang, dh.ngayLap, dh.trangThai, th.trangThai AS trangThaiThanhToan, dh.tongTien,
                   nd.diaChi, nd.hoTen AS tenNguoiDung
            FROM don_hang dh
            INNER JOIN nguoi_dung nd ON dh.maNguoiDung = nd.maNguoiDung
            INNER JOIN thanh_toan th ON dh.maDonHang = th.maDonHang
            WHERE dh.maDonHang = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, maDonHang);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DonHangWithUser donHangWithUser = new DonHangWithUser(
                        rs.getInt("maDonHang"),
                        rs.getDate("ngayLap"),
                        rs.getString("trangThai"),
                        rs.getString("trangThaiThanhToan"),
                        rs.getDouble("tongTien"),
                        rs.getString("diaChi"),
                        rs.getString("tenNguoiDung"),
                        getSanPhamInDonHang(rs.getInt("maDonHang"))
                    );
                    donHangWithUsers.add(donHangWithUser);
                }
            }
        }
        return donHangWithUsers;
    }

    // Cập nhật trạng thái đơn hàng
    public void updateTrangThaiDonHang(int maDonHang, String trangThaiMoi) throws SQLException {
        if (maDonHang <= 0 || trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng hoặc trạng thái mới không hợp lệ");
        }

        String query = "UPDATE don_hang SET trangThai = ? WHERE maDonHang = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trangThaiMoi);
            stmt.setInt(2, maDonHang);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy đơn hàng với mã: " + maDonHang);
            }
        }
    }

    // Lấy trạng thái đơn hàng
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

    // Cập nhật trạng thái thanh toán
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

    // Thêm chi tiết đơn hàng
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

    // Cập nhật địa chỉ người dùng theo mã đơn hàng
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

    // Lấy chi tiết đơn hàng
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
                    chiTietDonHangs.add(new ChiTietDonHang(
                        rs.getInt("maDonHang"),
                        rs.getInt("maSanPham"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia")
                    ));
                }
            }
        }
        return chiTietDonHangs;
    }

    // Hủy đơn hàng
    public void huyDonHang(int maDonHang) throws SQLException {
        if (maDonHang <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ");
        }

        String updateDonHangQuery = "UPDATE don_hang SET trangThai = 'Đã hủy' WHERE maDonHang = ?";
        String getChiTietQuery = "SELECT maSanPham, soLuong FROM chi_tiet_don_hang WHERE maDonHang = ?";
        String updateKhoQuery = "UPDATE san_pham SET soLuongTonKho = soLuongTonKho + ? WHERE maSanPham = ?";

        try {
            connection.setAutoCommit(false);

            // Cập nhật trạng thái đơn hàng
            try (PreparedStatement stmt = connection.prepareStatement(updateDonHangQuery)) {
                stmt.setInt(1, maDonHang);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không tìm thấy đơn hàng với mã: " + maDonHang);
                }
            }

            // Lấy chi tiết đơn hàng để cập nhật kho
            try (PreparedStatement stmt = connection.prepareStatement(getChiTietQuery)) {
                stmt.setInt(1, maDonHang);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int maSanPham = rs.getInt("maSanPham");
                        int soLuong = rs.getInt("soLuong");
                        try (PreparedStatement stmtUpdateKho = connection.prepareStatement(updateKhoQuery)) {
                            stmtUpdateKho.setInt(1, soLuong);
                            stmtUpdateKho.setInt(2, maSanPham);
                            stmtUpdateKho.executeUpdate();
                        }
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}