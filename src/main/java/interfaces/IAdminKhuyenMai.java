package interfaces;

import model.KhuyenMai;
import java.util.List;

public interface IAdminKhuyenMai {
    void add(KhuyenMai km);
    void update(KhuyenMai km);
    void delete(int id);
    KhuyenMai getById(int id);
    List<KhuyenMai> getAll();
    int getTotalPromotions();
    List<KhuyenMai> getPromotions(int offset, int limit);
    void updateExpiredPromotions();
    void closeConnection();
}