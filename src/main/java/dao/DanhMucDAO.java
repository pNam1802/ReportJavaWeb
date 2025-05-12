package dao;

import interfaces.IDanhMuc;
import model.DanhMuc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import interfaces.IAdminSanPham;

public class DanhMucDAO implements IDanhMuc {
    private Connection conn;

    public DanhMucDAO() {
        this.conn = DBConnection.getConnection(); // Lấy kết nối từ DBConnection
    }

    // Phương thức thêm DanhMuc
    public boolean add(DanhMuc danhMuc) {
        String sql = "INSERT INTO danh_muc (tenDanhMuc, moTa) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, danhMuc.getTenDanhMuc());
            ps.setString(2, danhMuc.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật DanhMuc
    public boolean update(DanhMuc danhMuc) {
        String sql = "UPDATE danh_muc SET tenDanhMuc = ?, moTa = ? WHERE maDanhMuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, danhMuc.getTenDanhMuc());
            ps.setString(2, danhMuc.getMoTa());
            ps.setInt(3, danhMuc.getMaDanhMuc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa DanhMuc
    public boolean delete(int maDanhMuc) {
        String sql = "DELETE FROM danh_muc WHERE maDanhMuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDanhMuc);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức tìm tất cả DanhMuc
    public List<DanhMuc> getAll() {
        List<DanhMuc> danhMucs = new ArrayList<>();
        String sql = "SELECT * FROM danh_muc";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DanhMuc danhMuc = new DanhMuc();
                danhMuc.setMaDanhMuc(rs.getInt("maDanhMuc"));
                danhMuc.setTenDanhMuc(rs.getString("tenDanhMuc"));
                danhMuc.setMoTa(rs.getString("moTa"));
                danhMucs.add(danhMuc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhMucs;
    }

    // Phương thức tìm kiếm DanhMuc theo maDanhMuc
    public DanhMuc getId(int maDanhMuc) {
        String sql = "SELECT * FROM danh_muc WHERE maDanhMuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDanhMuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DanhMuc danhMuc = new DanhMuc();
                    danhMuc.setMaDanhMuc(rs.getInt("maDanhMuc"));
                    danhMuc.setTenDanhMuc(rs.getString("tenDanhMuc"));
                    danhMuc.setMoTa(rs.getString("moTa"));
                    return danhMuc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public DanhMuc getTenDanhMuc(String tenDanhMuc) {
        String sql = "SELECT * FROM danh_muc WHERE tenDanhMuc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDanhMuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DanhMuc danhMuc = new DanhMuc();
                    danhMuc.setMaDanhMuc(rs.getInt("maDanhMuc"));
                    danhMuc.setTenDanhMuc(rs.getString("tenDanhMuc"));
                    danhMuc.setMoTa(rs.getString("moTa"));
                    return danhMuc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
