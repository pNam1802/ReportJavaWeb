package interfaces;

import java.sql.SQLException;

import model.SanPham;

public interface IAdminSanPham {
    void add(SanPham sanPham) throws SQLException;
    void update(SanPham sanPham) throws SQLException;
    void delete(int maSanPham) throws SQLException;
}