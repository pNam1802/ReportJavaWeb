package interfaces;

import java.util.List;

import model.SanPham;
import model.TinTuc;

public interface IQuanLyTinTuc {
    void addTinTuc(TinTuc news);
    void updateTinTuc(TinTuc news);
    void deleteTinTuc(int maTinTuc);
    TinTuc getTinTucById(int maTinTuc);
    List<TinTuc> getTinTucByPage(int page, int pageSize);
    int getTotalTinTuc();
    List<SanPham> getAllProducts();
}