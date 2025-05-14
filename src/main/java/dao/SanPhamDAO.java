package dao;

import interfaces.ISanPham;
import model.SanPham;
import model.DanhGiaSanPham;
import model.DanhMuc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO implements ISanPham {
    private Connection conn;

    public SanPhamDAO() {
        this.conn = DBConnection.getConnection();
    }

    public SanPhamDAO(Connection conn) {
        this.conn = conn;
        if (conn == null) {
            System.out.println("⚠️ Kết nối MySQL thất bại (conn == null)");
        } else {
            System.out.println("✅ Kết nối MySQL thành công");
        }

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
                    return mapResultSetToSanPham(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả sản phẩm
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
            System.err.println("Error fetching all products: " + e.getMessage());
            e.printStackTrace();
        }
        return sanPhams;
    }

    // Lấy tổng số sản phẩm
    public int getTotalSanPham() {
        String sql = "SELECT COUNT(*) FROM san_pham";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting products: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Lấy sản phẩm phân trang
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
            System.err.println("Error fetching products by page: " + e.getMessage());
            e.printStackTrace();
        }
        return sanPhams;
    }
    
    // Tìm kiếm sản phẩm theo tên
    public List<SanPham> searchByName(String keyword, int offset, int limit) {
        List<SanPham> sanPhams = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, " +
                     "sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
                     "WHERE sp.tenSanPham LIKE ? " +
                     "LIMIT ? OFFSET ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sanPhams.add(mapResultSetToSanPham(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching products by name: " + e.getMessage());
            e.printStackTrace();
        }
        return sanPhams;
    }

    // Lấy tổng số sản phẩm theo tên
    public int getTotalSanPhamByName(String keyword) {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE tenSanPham LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting products by name: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Lấy sản phẩm theo danh mục (có phân trang)
    public List<SanPham> getSanPhamTheoDanhMuc(int maDanhMuc, int offset, int limit) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, " +
                     "sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
                     "WHERE sp.idDanhMuc = ? " +
                     "LIMIT ? OFFSET ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDanhMuc);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSanPham(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products by category: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy tổng số sản phẩm theo danh mục
    public int getTotalSanPhamTheoDanhMuc(int maDanhMuc) {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE idDanhMuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDanhMuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting products by category: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<DanhGiaSanPham> layDanhSachDanhGiaDaGiao() {
        List<DanhGiaSanPham> danhSach = new ArrayList<>();
        String sql = """
            SELECT sp.tenSanPham, nd.hoTen, dg.diemDanhGia, dg.noiDung, dg.ngayDanhGia
					FROM danh_gia dg
					JOIN san_pham sp ON dg.maSanPham = sp.maSanPham
					JOIN nguoi_dung nd ON dg.maNguoiDung = nd.maNguoiDung
					JOIN don_hang dh ON dh.maNguoiDung = nd.maNguoiDung
					WHERE dh.trangThai = 'Hoàn thành';
					        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DanhGiaSanPham dg = new DanhGiaSanPham();
                dg.setTenSanPham(rs.getString("tenSanPham"));
                dg.setTenNguoiDung(rs.getString("hoTen"));
                dg.setDiemDanhGia(rs.getInt("diemDanhGia"));
                dg.setNoiDung(rs.getString("noiDung"));
                dg.setNgayDanhGia(rs.getDate("ngayDanhGia"));
                danhSach.add(dg);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product reviews: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    // Phương thức ánh xạ ResultSet thành đối tượng SanPham
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

        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setMaDanhMuc(rs.getInt("maDanhMuc"));
        danhMuc.setTenDanhMuc(rs.getString("tenDanhMuc"));
        danhMuc.setMoTa(rs.getString("moTa"));
        sanPham.setDanhMuc(danhMuc);

        return sanPham;
    }

    // Đóng kết nối
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}