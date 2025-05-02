package dao;

import model.SanPham;
import model.DanhMuc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    private Connection conn;

<<<<<<< HEAD
    public SanPhamDAO() {
        this.conn = DBConnection.getConnection();
    }

=======
    // Constructor không tham số
    public SanPhamDAO() {
        this.conn = DBConnection.getConnection(); // Kết nối tự động lấy từ DBConnection
    }

    // Constructor có tham số Connection (dành cho các trường hợp khác cần kết nối từ ngoài vào)
>>>>>>> github-main
    public SanPhamDAO(Connection conn) {
        this.conn = conn;
    }

<<<<<<< HEAD
    // Lấy sản phẩm theo ID
    public SanPham getById(int id) {
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, " +
                     "sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
                     "WHERE sp.maSanPham = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SanPham sanPham = new SanPham();
                    sanPham.setMaSanPham(rs.getInt("maSanPham"));
                    sanPham.setTenSanPham(rs.getString("tenSanPham"));
                    sanPham.setChiTiet(rs.getString("chiTiet"));
                    sanPham.setGiaGoc(rs.getDouble("giaGoc"));
                    sanPham.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
                    sanPham.setTinhTrang(rs.getString("tinhTrang"));
                    sanPham.setSoLuongTonKho(rs.getInt("soLuongTonKho"));
                    sanPham.setHinhAnh(rs.getString("hinhAnh"));

                    DanhMuc danhMuc = new DanhMuc(
                        rs.getInt("maDanhMuc"),
                        rs.getString("tenDanhMuc"),
                        rs.getString("moTa")
                    );
                    sanPham.setDanhMuc(danhMuc);
                    
                    return sanPham;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả sản phẩm (đã có)
    public List<SanPham> getAll() {
        List<SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, " +
                     "sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc";

        try (Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sanPhams.add(mapResultSetToSanPham(rs));
=======
    // Phương thức lấy tất cả sản phẩm
 // Phương thức lấy tất cả sản phẩm
    public List<SanPham> getAll() {
        List<SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, sp.tinhTrang, " +
                     "sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSanPham(rs.getInt("maSanPham"));
                sanPham.setTenSanPham(rs.getString("tenSanPham"));
                sanPham.setChiTiet(rs.getString("chiTiet"));
                sanPham.setGiaGoc(rs.getDouble("giaGoc"));
                sanPham.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
                sanPham.setTinhTrang(rs.getString("tinhTrang"));
                sanPham.setSoLuongTonKho(rs.getInt("soLuongTonKho"));
                sanPham.setHinhAnh(rs.getString("hinhAnh"));

                // Lấy thông tin danh mục từ bảng danh_muc và tạo đối tượng DanhMuc
                int maDanhMuc = rs.getInt("maDanhMuc");
                String tenDanhMuc = rs.getString("tenDanhMuc");
                String moTa = rs.getString("moTa");

                // Sử dụng constructor đầy đủ của DanhMuc
                DanhMuc danhMuc = new DanhMuc(maDanhMuc, tenDanhMuc, moTa);
                sanPham.setDanhMuc(danhMuc);

                sanPhams.add(sanPham);
>>>>>>> github-main
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPhams;
    }
<<<<<<< HEAD

    // Lấy tổng số sản phẩm (đã có)
=======
>>>>>>> github-main
    public int getTotalSanPham() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM san_pham";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

<<<<<<< HEAD
    // Lấy sản phẩm phân trang (đã có)
    public List<SanPham> getSanPhams(int offset, int limit) {
        List<SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, " +
                     "sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
=======

    public List<SanPham> getSanPhams(int offset, int limit) {
        List<SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, sp.tinhTrang, " +
                     "sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
>>>>>>> github-main
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
                     "LIMIT ? OFFSET ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);

<<<<<<< HEAD
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sanPhams.add(mapResultSetToSanPham(rs));
                }
=======
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSanPham(rs.getInt("maSanPham"));
                sanPham.setTenSanPham(rs.getString("tenSanPham"));
                sanPham.setChiTiet(rs.getString("chiTiet"));
                sanPham.setGiaGoc(rs.getDouble("giaGoc"));
                sanPham.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
                sanPham.setTinhTrang(rs.getString("tinhTrang"));
                sanPham.setSoLuongTonKho(rs.getInt("soLuongTonKho"));
                sanPham.setHinhAnh(rs.getString("hinhAnh"));

                DanhMuc danhMuc = new DanhMuc(
                    rs.getInt("maDanhMuc"),
                    rs.getString("tenDanhMuc"),
                    rs.getString("moTa")
                );
                sanPham.setDanhMuc(danhMuc);

                sanPhams.add(sanPham);
>>>>>>> github-main
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPhams;
    }
<<<<<<< HEAD

    // Phương thức hỗ trợ ánh xạ ResultSet thành đối tượng SanPham
    private SanPham mapResultSetToSanPham(ResultSet rs) throws SQLException {
        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(rs.getInt("maSanPham"));
        sanPham.setTenSanPham(rs.getString("tenSanPham"));
        sanPham.setChiTiet(rs.getString("chiTiet"));
        sanPham.setGiaGoc(rs.getDouble("giaGoc"));
        sanPham.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
        sanPham.setTinhTrang(rs.getString("tinhTrang"));
        sanPham.setSoLuongTonKho(rs.getInt("soLuongTonKho"));
        sanPham.setHinhAnh(rs.getString("hinhAnh"));

        DanhMuc danhMuc = new DanhMuc(
            rs.getInt("maDanhMuc"),
            rs.getString("tenDanhMuc"),
            rs.getString("moTa")
        );
        sanPham.setDanhMuc(danhMuc);
        
        return sanPham;
    }

    // Đóng kết nối (đã có)
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
=======
    // Phương thức đóng kết nối
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close(); // Đóng kết nối
>>>>>>> github-main
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> github-main
