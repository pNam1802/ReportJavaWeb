package interfaces;

import model.TinTuc;
import model.SanPham;
import java.util.List;

public interface IQuanLyTinTuc {
    void addTinTuc(TinTuc news);
    void updateTinTuc(TinTuc news);
    void deleteTinTuc(int maTinTuc);
    TinTuc getTinTucById(int maTinTuc);
    List<TinTuc> getTinTucByPage(int page, int pageSize);
    int getTotalTinTuc();
    List<SanPham> getAllProducts();
}