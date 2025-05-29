package interfaces;

import java.util.List;

import model.KhuyenMai;

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