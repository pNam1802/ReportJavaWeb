package dao;

import model.SanPham;
import model.DanhMuc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    private Connection conn;

    public SanPhamDAO() {
        this.conn = DBConnection.getConnection();
    }

    public SanPhamDAO(Connection conn) {
        this.conn = conn;
    }

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPhams;
    }

    // Lấy tổng số sản phẩm (đã có)
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

    // Lấy sản phẩm phân trang (đã có)
    public List<SanPham> getSanPhams(int offset, int limit) {
        List<SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, " +
                     "sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
                     "LIMIT ? OFFSET ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sanPhams.add(mapResultSetToSanPham(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPhams;
    }

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}