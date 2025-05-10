package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NguoiDung;
import dao.DBConnection;
import interfaces.IAdminNguoiDung;

public class NguoiDungDAO implements IAdminNguoiDung {
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

                    list.add(nguoiDung);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
            throw e;
        }
        return list;
    }

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
                    return nguoiDung;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void addNguoiDung(NguoiDung nguoiDung) throws SQLException {
        String sql = "INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nguoiDung.getHoTen());
            ps.setString(2, nguoiDung.getSdt());
            ps.setString(3, nguoiDung.getEmail());
            ps.setString(4, nguoiDung.getDiaChi());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            throw e;
        }
    }

    public void updateNguoiDung(NguoiDung nguoiDung) throws SQLException {
        String sql = "UPDATE nguoi_dung SET hoTen = ?, sdt = ?, email = ?, diaChi = ?, tenDangNhap = ?, matKhau = ?, vaiTro = ? WHERE maNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Gán giá trị cho hoTen
            if (nguoiDung.getHoTen() != null) {
                ps.setString(1, nguoiDung.getHoTen());
            } else {
                ps.setNull(1, java.sql.Types.VARCHAR);
            }

            // Gán giá trị cho sdt
            if (nguoiDung.getSdt() != null) {
                ps.setString(2, nguoiDung.getSdt());
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            // Gán giá trị cho email
            if (nguoiDung.getEmail() != null) {
                ps.setString(3, nguoiDung.getEmail());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }

            // Gán giá trị cho diaChi
            if (nguoiDung.getDiaChi() != null) {
                ps.setString(4, nguoiDung.getDiaChi());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            // Gán NULL cho tenDangNhap
            ps.setNull(5, java.sql.Types.VARCHAR);

            // Gán NULL cho matKhau
            ps.setNull(6, java.sql.Types.VARCHAR);

            // Gán NULL cho vaiTro
            ps.setNull(7, java.sql.Types.VARCHAR);

            // Gán giá trị cho maNguoiDung
            ps.setInt(8, nguoiDung.getMaNguoiDung());

            // Thực thi câu lệnh
            ps.executeUpdate();
        }
    }

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
}
