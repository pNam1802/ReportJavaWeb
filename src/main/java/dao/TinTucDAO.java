package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.TinTuc;

public class TinTucDAO {

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
    public TinTuc getTinTucById(int maTinTuc) {
        String sql = "SELECT * FROM TinTuc WHERE maTinTuc = ?";
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
        return null; // Trả về null nếu không tìm thấy tin tức
    }
}