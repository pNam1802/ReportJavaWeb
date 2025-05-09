package dao;

import model.KhuyenMai;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminKhuyenMaiDAO {
    private Connection conn;

    public AdminKhuyenMaiDAO() {
        this.conn = DBConnection.getConnection();
        if (this.conn == null) {
            throw new RuntimeException("Database connection is null");
        }
    }

    public AdminKhuyenMaiDAO(Connection conn) {
        this.conn = conn;
    }

    private void checkConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DBConnection.getConnection();
            if (conn == null) {
                throw new RuntimeException("Failed to re-establish database connection");
            }
        }
    }

    public void add(KhuyenMai km) {
        String sql = "INSERT INTO khuyen_mai (maSanPham, ngayBatDau, ngayKetThuc, giaKhuyenMai) VALUES (?, ?, ?, ?)";
        try {
            checkConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, km.getMaSanPham());
                ps.setDate(2, new Date(km.getNgayBatDau().getTime()));
                ps.setDate(3, new Date(km.getNgayKetThuc().getTime()));
                ps.setDouble(4, km.getGiaKhuyenMai());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add promotion", e);
        }
    }

    public void update(KhuyenMai km) {
        String sql = "UPDATE khuyen_mai SET maSanPham = ?, ngayBatDau = ?, ngayKetThuc = ?, giaKhuyenMai = ? WHERE maKhuyenMai = ?";
        try {
            checkConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, km.getMaSanPham());
                ps.setDate(2, new Date(km.getNgayBatDau().getTime()));
                ps.setDate(3, new Date(km.getNgayKetThuc().getTime()));
                ps.setDouble(4, km.getGiaKhuyenMai());
                ps.setInt(5, km.getMaKhuyenMai());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update promotion", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM khuyen_mai WHERE maKhuyenMai = ?";
        try {
            checkConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete promotion", e);
        }
    }

    public KhuyenMai getById(int id) {
        String sql = "SELECT * FROM khuyen_mai WHERE maKhuyenMai = ?";
        try {
            checkConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        KhuyenMai km = new KhuyenMai();
                        km.setMaKhuyenMai(rs.getInt("maKhuyenMai"));
                        km.setMaSanPham(rs.getInt("maSanPham"));
                        km.setNgayBatDau(rs.getDate("ngayBatDau"));
                        km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                        km.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
                        return km;
                    }
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public List<KhuyenMai> getAll() {
        List<KhuyenMai> khuyenMais = new ArrayList<>();
        String sql = "SELECT * FROM khuyen_mai";
        try {
            checkConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    KhuyenMai km = new KhuyenMai();
                    km.setMaKhuyenMai(rs.getInt("maKhuyenMai"));
                    km.setMaSanPham(rs.getInt("maSanPham"));
                    km.setNgayBatDau(rs.getDate("ngayBatDau"));
                    km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                    km.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
                    khuyenMais.add(km);
                }
            }
        } catch (SQLException e) {
            return khuyenMais;
        }
        return khuyenMais;
    }

    public int getTotalPromotions() {
        String sql = "SELECT COUNT(*) FROM khuyen_mai";
        try {
            checkConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    public List<KhuyenMai> getPromotions(int offset, int limit) {
        List<KhuyenMai> khuyenMais = new ArrayList<>();
        String sql = "SELECT * FROM khuyen_mai ORDER BY maKhuyenMai ASC LIMIT ? OFFSET ?";
        try {
            checkConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        KhuyenMai km = new KhuyenMai();
                        km.setMaKhuyenMai(rs.getInt("maKhuyenMai"));
                        km.setMaSanPham(rs.getInt("maSanPham"));
                        km.setNgayBatDau(rs.getDate("ngayBatDau"));
                        km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                        km.setGiaKhuyenMai(rs.getDouble("giaKhuyenMai"));
                        khuyenMais.add(km);
                    }
                }
            }
        } catch (SQLException e) {
            return khuyenMais;
        }
        return khuyenMais;
    }

    public void updateExpiredPromotions() {
        String sqlUpdate = "UPDATE san_pham sp " +
                          "SET sp.giaKhuyenMai = sp.giaGoc " +
                          "WHERE EXISTS (" +
                          "    SELECT 1 FROM khuyen_mai km " +
                          "    WHERE km.maSanPham = sp.maSanPham " +
                          "    AND km.ngayKetThuc < CURDATE()" +
                          "    AND NOT EXISTS (" +
                          "        SELECT 1 FROM khuyen_mai km2 " +
                          "        WHERE km2.maSanPham = km.maSanPham " +
                          "        AND km2.ngayBatDau <= CURDATE() " +
                          "        AND km2.ngayKetThuc >= CURDATE()" +
                          "    )" +
                          ")";
        String sqlDelete = "DELETE FROM khuyen_mai WHERE ngayKetThuc < CURDATE()";
        try {
            checkConnection();
            // Update giaKhuyenMai to giaGoc for products with only expired promotions
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.executeUpdate();
            }
            // Optionally delete expired promotions
            try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
                psDelete.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update expired promotions", e);
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            // Ignore or handle silently
        }
    }
}