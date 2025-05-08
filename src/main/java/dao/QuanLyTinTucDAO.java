package dao;

import model.TinTuc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuanLyTinTucDAO {
    // Lớp Product nội bộ
    public static class Product {
        private int maSanPham;
        private String tenSanPham;

        public Product(int maSanPham, String tenSanPham) {
            this.maSanPham = maSanPham;
            this.tenSanPham = tenSanPham;
        }

        public int getMaSanPham() {
            return maSanPham;
        }

        public String getTenSanPham() {
            return tenSanPham;
        }
    }

    // Kiểm tra bảng tồn tại
    private boolean checkTableExists(Connection conn, String tableName) {
        try {
            System.out.println("DAO: Checking existence of table: " + tableName);
            ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
            boolean exists = rs.next();
            System.out.println("DAO: Table " + tableName + " exists: " + exists);
            if (!exists) {
                // Thử với chữ thường
                rs = conn.getMetaData().getTables(null, null, tableName.toLowerCase(), null);
                exists = rs.next();
                System.out.println("DAO: Table " + tableName.toLowerCase() + " exists: " + exists);
                if (!exists) {
                    // Thử với chữ hoa
                    rs = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null);
                    exists = rs.next();
                    System.out.println("DAO: Table " + tableName.toUpperCase() + " exists: " + exists);
                }
            }
            return exists;
        } catch (SQLException e) {
            System.err.println("DAO: Error checking table existence for " + tableName + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tin tức theo trang
    public List<TinTuc> getTinTucByPage(int page, int pageSize) {
        System.out.println("DAO: Entering getTinTucByPage with page=" + page + ", pageSize=" + pageSize);
        List<TinTuc> tinTucs = new ArrayList<>();
        String sql = "SELECT * FROM tin_tuc ORDER BY maTinTuc DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DAO: Database connection established for getTinTucByPage");
            if (!checkTableExists(conn, "tin_tuc")) {
                System.err.println("DAO: Table tin_tuc does not exist");
                return tinTucs; // Trả về danh sách rỗng
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, pageSize);
                stmt.setInt(2, (page - 1) * pageSize);
                System.out.println("DAO: Executing query: " + sql + " with pageSize=" + pageSize + ", offset=" + ((page - 1) * pageSize));
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        TinTuc news = new TinTuc();
                        news.setMaTinTuc(rs.getInt("maTinTuc"));
                        news.setTieuDe(rs.getString("tieuDe"));
                        news.setNoiDung(rs.getString("noiDung"));
                        news.setNgayDang(rs.getDate("ngayDang"));
                        news.setMaSanPham(rs.getInt("maSanPham"));
                        tinTucs.add(news);
                    }
                    System.out.println("DAO: Fetched " + tinTucs.size() + " news items for page " + page);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error fetching news: " + e.getMessage());
            e.printStackTrace();
            return tinTucs; // Trả về danh sách rỗng
        } catch (Exception e) {
            System.err.println("DAO: Unexpected error in getTinTucByPage: " + e.getMessage());
            e.printStackTrace();
            return tinTucs;
        }
        return tinTucs;
    }

    // Đếm tổng số tin tức
    public int getTotalTinTuc() {
        System.out.println("DAO: Entering getTotalTinTuc");
        String sql = "SELECT COUNT(*) FROM tin_tuc";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DAO: Database connection established for getTotalTinTuc");
            if (!checkTableExists(conn, "tin_tuc")) {
                System.err.println("DAO: Table tin_tuc does not exist");
                return 0;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                System.out.println("DAO: Executing query: " + sql);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        System.out.println("DAO: Total news count: " + count);
                        return count;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error counting news: " + e.getMessage());
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            System.err.println("DAO: Unexpected error in getTotalTinTuc: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
        System.out.println("DAO: No count returned, defaulting to 0");
        return 0;
    }

    // Lấy tin tức theo ID
    public TinTuc getTinTucById(int maTinTuc) {
        System.out.println("DAO: Entering getTinTucById with maTinTuc=" + maTinTuc);
        String sql = "SELECT * FROM tin_tuc WHERE maTinTuc = ?";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DAO: Database connection established for getTinTucById");
            if (!checkTableExists(conn, "tin_tuc")) {
                System.err.println("DAO: Table tin_tuc does not exist");
                return null;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, maTinTuc);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        TinTuc news = new TinTuc();
                        news.setMaTinTuc(rs.getInt("maTinTuc"));
                        news.setTieuDe(rs.getString("tieuDe"));
                        news.setNoiDung(rs.getString("noiDung"));
                        news.setNgayDang(rs.getDate("ngayDang"));
                        news.setMaSanPham(rs.getInt("maSanPham"));
                        System.out.println("DAO: Fetched news with ID: " + maTinTuc);
                        return news;
                    } else {
                        System.out.println("DAO: No news found with ID: " + maTinTuc);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error fetching news by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("DAO: Unexpected error in getTinTucById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    // Thêm tin tức
    public void addTinTuc(TinTuc news) {
        System.out.println("DAO: Entering addTinTuc with title: " + news.getTieuDe());
        String sql = "INSERT INTO tin_tuc (tieuDe, noiDung, ngayDang, maSanPham) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DAO: Database connection established for addTinTuc");
            if (!checkTableExists(conn, "tin_tuc")) {
                System.err.println("DAO: Table tin_tuc does not exist");
                throw new SQLException("Bảng tin_tuc không tồn tại!");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, news.getTieuDe());
                stmt.setString(2, news.getNoiDung());
                stmt.setDate(3, new java.sql.Date(news.getNgayDang().getTime()));
                stmt.setInt(4, news.getMaSanPham());
                stmt.executeUpdate();
                System.out.println("DAO: Added news with title: " + news.getTieuDe());
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error adding news: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm tin tức: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("DAO: Unexpected error in addTinTuc: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm tin tức: " + e.getMessage());
        }
    }

    // Sửa tin tức
    public void updateTinTuc(TinTuc news) {
        System.out.println("DAO: Entering updateTinTuc with ID: " + news.getMaTinTuc());
        String sql = "UPDATE tin_tuc SET tieuDe = ?, noiDung = ?, ngayDang = ?, maSanPham = ? WHERE maTinTuc = ?";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DAO: Database connection established for updateTinTuc");
            if (!checkTableExists(conn, "tin_tuc")) {
                System.err.println("DAO: Table tin_tuc does not exist");
                throw new SQLException("Bảng tin_tuc không tồn tại!");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, news.getTieuDe());
                stmt.setString(2, news.getNoiDung());
                stmt.setDate(3, new java.sql.Date(news.getNgayDang().getTime()));
                stmt.setInt(4, news.getMaSanPham());
                stmt.setInt(5, news.getMaTinTuc());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("DAO: No news found to update with ID: " + news.getMaTinTuc());
                    throw new SQLException("Không tìm thấy tin tức để cập nhật!");
                } else {
                    System.out.println("DAO: Updated news with ID: " + news.getMaTinTuc());
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error updating news: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật tin tức: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("DAO: Unexpected error in updateTinTuc: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật tin tức: " + e.getMessage());
        }
    }

    // Xóa tin tức
    public void deleteTinTuc(int maTinTuc) {
        System.out.println("DAO: Entering deleteTinTuc with ID: " + maTinTuc);
        String sql = "DELETE FROM tin_tuc WHERE maTinTuc = ?";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DAO: Database connection established for deleteTinTuc");
            if (!checkTableExists(conn, "tin_tuc")) {
                System.err.println("DAO: Table tin_tuc does not exist");
                throw new SQLException("Bảng tin_tuc không tồn tại!");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, maTinTuc);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("DAO: No news found to delete with ID: " + maTinTuc);
                    throw new SQLException("Không tìm thấy tin tức để xóa!");
                } else {
                    System.out.println("DAO: Deleted news with ID: " + maTinTuc);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error deleting news: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa tin tức: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("DAO: Unexpected error in deleteTinTuc: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa tin tức: " + e.getMessage());
        }
    }

    // Lấy danh sách sản phẩm
    public List<Product> getAllProducts() {
        System.out.println("DAO: Entering getAllProducts");
        List<Product> products = new ArrayList<>();
        String sql = "SELECT maSanPham, tenSanPham FROM san_pham";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DAO: Database connection established for getAllProducts");
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(rs.getInt("maSanPham"), rs.getString("tenSanPham")));
                }
                System.out.println("DAO: Fetched " + products.size() + " products");
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error fetching products: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("DAO: Unexpected error in getAllProducts: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }
        return products;
    }
}