package dao;

import model.SanPham;
import model.DanhGiaSanPham;
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
            System.out.println("Executing searchByName with keyword: " + keyword);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sanPhams.add(mapResultSetToSanPham(rs));
                }
                System.out.println("Found " + sanPhams.size() + " products for keyword: " + keyword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPhams;
    }


    // Lấy tổng số sản phẩm theo tên
    public int getTotalSanPhamByName(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM san_pham WHERE tenSanPham LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
                System.out.println("Total products for keyword " + keyword + ": " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    // Lấy sản phẩm theo danh mục (có phân trang)
    public List<SanPham> getSanPhamTheoDanhMuc(int maDanhMuc, int offset, int limit) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, " +
                     "sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
                     "WHERE sp.idDanhMuc = ? " +
                     "LIMIT ? OFFSET ?"; // Thêm LIMIT và OFFSET cho phân trang

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,  maDanhMuc);	 // Set mã danh mục vào câu truy vấn
        	ps.setInt(2, limit);         // Set giới hạn số lượng sản phẩm
            ps.setInt(3, offset);        // Set vị trí bắt đầu (offset)

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = mapResultSetToSanPham(rs);
                    list.add(sp); // Thêm sản phẩm vào danh sách
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy tổng số sản phẩm theo danh mục
    public int getTotalSanPhamTheoDanhMuc(int maDanhMuc) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM san_pham sp " +
                     "JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
                     "WHERE sp.idDanhMuc = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDanhMuc);  // Set mã danh mục vào câu truy vấn
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public List<DanhGiaSanPham> layDanhSachDanhGiaDaGiao() {
        List<DanhGiaSanPham> danhSach = new ArrayList<>();

        String sql = """
            SELECT sp.tenSanPham, nd.hoTen, dg.diemDanhGia, dg.noiDung, dg.ngayDanhGia
            FROM danh_gia dg
            JOIN san_pham sp ON dg.maSanPham = sp.maSanPham
            JOIN nguoi_dung nd ON dg.maNguoiDung = nd.maNguoiDung
            JOIN don_hang dh ON dh.maNguoiDung = nd.maNguoiDung
            JOIN chi_tiet_don_hang ct ON ct.maDonHang = dh.maDonHang AND ct.maSanPham = sp.maSanPham
            WHERE dh.trangThai = 'Đã giao'
            ORDER BY dg.ngayDanhGia DESC;
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
            e.printStackTrace();
        }

        return danhSach;
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
