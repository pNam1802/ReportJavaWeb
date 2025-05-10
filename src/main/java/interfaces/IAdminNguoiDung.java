package interfaces;

import java.sql.SQLException;
import java.util.List;
import model.NguoiDung;

public interface IAdminNguoiDung {
    List<NguoiDung> getNguoiDungsByPage(int page, int pageSize) throws SQLException;
    int getTotalNguoiDungs() throws SQLException;
    NguoiDung getNguoiDungById(int maNguoiDung) throws SQLException;
    void addNguoiDung(NguoiDung nguoiDung) throws SQLException;
    void updateNguoiDung(NguoiDung nguoiDung) throws SQLException;
    void deleteNguoiDung(int maNguoiDung) throws SQLException;
}