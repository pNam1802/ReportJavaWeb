package dao;

import interfaces.IQuanLyTinTuc;
import model.TinTuc;
import model.SanPham;
import model.DanhMuc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuanLyTinTucDAO implements IQuanLyTinTuc {

    // Lấy danh sách tin tức theo trang
    public List<TinTuc> getTinTucByPage(int page, int pageSize) {
        List<TinTuc> tinTucs = new ArrayList<>();
        String sql = "SELECT * FROM tin_tuc ORDER BY ngayDang DESC LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TinTuc tinTuc = new TinTuc();
                tinTuc.setMaTinTuc(rs.getInt("maTinTuc"));
                tinTuc.setTieuDe(rs.getString("tieuDe"));
                tinTuc.setNoiDung(rs.getString("noiDung"));
                tinTuc.setNgayDang(rs.getDate("ngayDang"));
                tinTuc.setMaSanPham(rs.getInt("maSanPham"));
                tinTucs.add(tinTuc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving tin tuc: " + e.getMessage(), e);
        }
        return tinTucs;
    }

    // Lấy tổng số tin tức
    public int getTotalTinTuc() {
        String sql = "SELECT COUNT(*) FROM tin_tuc";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error counting tin tuc: " + e.getMessage(), e);
        }
        return 0;
    }

    // Lấy tin tức theo ID
    public TinTuc getTinTucById(int maTinTuc) {
        String sql = "SELECT * FROM tin_tuc WHERE maTinTuc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maTinTuc);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TinTuc tinTuc = new TinTuc();
                tinTuc.setMaTinTuc(rs.getInt("maTinTuc"));
                tinTuc.setTieuDe(rs.getString("tieuDe"));
                tinTuc.setNoiDung(rs.getString("noiDung"));
                tinTuc.setNgayDang(rs.getDate("ngayDang"));
                tinTuc.setMaSanPham(rs.getInt("maSanPham"));
                return tinTuc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving tin tuc by id: " + e.getMessage(), e);
        }
        return null;
    }

    // Thêm tin tức
    public void addTinTuc(TinTuc news) {
        String sql = "INSERT INTO tin_tuc (tieuDe, noiDung, ngayDang, maSanPham) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, news.getTieuDe());
            stmt.setString(2, news.getNoiDung());
            stmt.setDate(3, new java.sql.Date(news.getNgayDang().getTime()));
            stmt.setInt(4, news.getMaSanPham());
            stmt.executeUpdate();
            System.out.println("DAO: Added news with title: " + news.getTieuDe());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding tin tuc: " + e.getMessage(), e);
        }
    }

    // Sửa tin tức
    public void updateTinTuc(TinTuc news) {
        String sql = "UPDATE tin_tuc SET tieuDe = ?, noiDung = ?, ngayDang = ?, maSanPham = ? WHERE maTinTuc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, news.getTieuDe());
            stmt.setString(2, news.getNoiDung());
            stmt.setDate(3, new java.sql.Date(news.getNgayDang().getTime()));
            stmt.setInt(4, news.getMaSanPham());
            stmt.setInt(5, news.getMaTinTuc());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No news found to update with ID: " + news.getMaTinTuc());
            }
            System.out.println("DAO: Updated news with ID: " + news.getMaTinTuc());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating tin tuc: " + e.getMessage(), e);
        }
    }

    // Xóa tin tức
    public void deleteTinTuc(int maTinTuc) {
        String sql = "DELETE FROM tin_tuc WHERE maTinTuc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maTinTuc);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No news found to delete with ID: " + maTinTuc);
            }
            System.out.println("DAO: Deleted news with ID: " + maTinTuc);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting tin tuc: " + e.getMessage(), e);
        }
    }

    // Lấy danh sách sản phẩm
    public List<SanPham> getAllProducts() {
        List<SanPham> products = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.chiTiet, sp.giaGoc, sp.giaKhuyenMai, sp.tinhTrang, sp.soLuongTonKho, sp.hinhAnh, sp.idDanhMuc, dm.maDanhMuc, dm.tenDanhMuc, dm.moTa " +
                     "FROM san_pham sp LEFT JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SanPham product = new SanPham();
                product.setMaSanPham(rs.getInt("maSanPham"));
                product.setTenSanPham(rs.getString("tenSanPham"));
                product.setChiTiet(rs.getString("chiTiet"));
                product.setGiaGoc(rs.getDouble("giaGoc"));
                product.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
                product.setTinhTrang(rs.getString("tinhTrang"));
                product.setSoLuongTonKho(rs.getInt("soLuongTonKho"));
                product.setHinhAnh(rs.getString("hinhAnh"));
                int idDanhMuc = rs.getInt("idDanhMuc");
                if (!rs.wasNull()) {
                    DanhMuc danhMuc = new DanhMuc();
                    danhMuc.setMaDanhMuc(rs.getInt("maDanhMuc"));
                    danhMuc.setTenDanhMuc(rs.getString("tenDanhMuc"));
                    danhMuc.setMoTa(rs.getString("moTa"));
                    product.setDanhMuc(danhMuc);
                }
                products.add(product);
            }
            System.out.println("DAO: Fetched " + products.size() + " products");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving products: " + e.getMessage(), e);
        }
        return products;
    }
}