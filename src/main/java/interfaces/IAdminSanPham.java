package interfaces;

import model.SanPham;
import java.sql.SQLException;

public interface IAdminSanPham {
    void add(SanPham sanPham) throws SQLException;
    void update(SanPham sanPham) throws SQLException;
    void delete(int maSanPham) throws SQLException;
}