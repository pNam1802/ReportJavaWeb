package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import interfaces.IAdminNguoiDung;
import model.NguoiDung;

public class NguoiDungDAO implements IAdminNguoiDung {
    @Override
	public List<NguoiDung> getNguoiDungsByPage(int page, int pageSize) throws SQLException {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoi_dung ORDER BY maNguoiDung ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NguoiDung nguoiDung = new NguoiDung();
                    nguoiDung.setMaNguoiDung(rs.getInt("maNguoiDung"));
                    nguoiDung.setHoTen(rs.getString("hoTen"));
                    nguoiDung.setSdt(rs.getString("sdt"));
                    nguoiDung.setEmail(rs.getString("email"));
                    nguoiDung.setDiaChi(rs.getString("diaChi"));
                    nguoiDung.setTenDangNhap(rs.getString("tenDangNhap"));
                    nguoiDung.setMatKhau(rs.getString("matKhau"));
                    nguoiDung.setVaiTro(rs.getString("vaiTro"));
                    list.add(nguoiDung);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
            throw e;
        }
        return list;
    }

    @Override
	public List<NguoiDung> getNguoiDungsByRoleAndPage(String vaiTro, int page, int pageSize) throws SQLException {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoi_dung WHERE vaiTro = ? ORDER BY maNguoiDung ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vaiTro);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NguoiDung nguoiDung = new NguoiDung();
                    nguoiDung.setMaNguoiDung(rs.getInt("maNguoiDung"));
                    nguoiDung.setHoTen(rs.getString("hoTen"));
                    nguoiDung.setSdt(rs.getString("sdt"));
                    nguoiDung.setEmail(rs.getString("email"));
                    nguoiDung.setDiaChi(rs.getString("diaChi"));
                    nguoiDung.setTenDangNhap(rs.getString("tenDangNhap"));
                    nguoiDung.setMatKhau(rs.getString("matKhau"));
                    nguoiDung.setVaiTro(rs.getString("vaiTro"));
                    list.add(nguoiDung);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users by role: " + e.getMessage());
            throw e;
        }
        return list;
    }

    @Override
	public int getTotalNguoiDungs() throws SQLException {
        String sql = "SELECT COUNT(*) FROM nguoi_dung";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    @Override
	public int getTotalNguoiDungsByRole(String vaiTro) throws SQLException {
        String sql = "SELECT COUNT(*) FROM nguoi_dung WHERE vaiTro = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vaiTro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting users by role: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    @Override
	public NguoiDung getNguoiDungById(int maNguoiDung) throws SQLException {
        String sql = "SELECT * FROM nguoi_dung WHERE maNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNguoiDung);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NguoiDung nguoiDung = new NguoiDung();
                    nguoiDung.setMaNguoiDung(rs.getInt("maNguoiDung"));
                    nguoiDung.setHoTen(rs.getString("hoTen"));
                    nguoiDung.setSdt(rs.getString("sdt"));
                    nguoiDung.setEmail(rs.getString("email"));
                    nguoiDung.setDiaChi(rs.getString("diaChi"));
                    nguoiDung.setTenDangNhap(rs.getString("tenDangNhap"));
                    nguoiDung.setMatKhau(rs.getString("matKhau"));
                    nguoiDung.setVaiTro(rs.getString("vaiTro"));
                    return nguoiDung;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID: " + e.getMessage());
            throw e;
        }
        return null;
    }

    @Override
	public void addNguoiDung(NguoiDung nguoiDung) throws SQLException {
        String sql = "INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi, tenDangNhap, matKhau, vaiTro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nguoiDung.getHoTen());
            ps.setString(2, nguoiDung.getSdt());
            ps.setString(3, nguoiDung.getEmail());
            ps.setString(4, nguoiDung.getDiaChi());
            ps.setString(5, nguoiDung.getTenDangNhap());
            ps.setString(6, nguoiDung.getMatKhau());
            ps.setString(7, nguoiDung.getVaiTro());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            throw e;
        }
    }

    @Override
	public void updateNguoiDung(NguoiDung nguoiDung) throws SQLException {
        String sql = "UPDATE nguoi_dung SET hoTen = ?, sdt = ?, email = ?, diaChi = ?, tenDangNhap = ?, matKhau = ?, vaiTro = ? WHERE maNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nguoiDung.getHoTen());
            ps.setString(2, nguoiDung.getSdt());
            ps.setString(3, nguoiDung.getEmail());
            ps.setString(4, nguoiDung.getDiaChi());
            ps.setString(5, nguoiDung.getTenDangNhap());
            ps.setString(6, nguoiDung.getMatKhau());
            ps.setString(7, nguoiDung.getVaiTro());
            ps.setInt(8, nguoiDung.getMaNguoiDung());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e;
        }
    }

    @Override
	public void deleteNguoiDung(int maNguoiDung) throws SQLException {
        String sql = "DELETE FROM nguoi_dung WHERE maNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNguoiDung);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw e;
        }
    }

    public boolean validateAdminLogin(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM nguoi_dung WHERE tenDangNhap = ? AND matKhau = ? AND vaiTro = 'admin'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}