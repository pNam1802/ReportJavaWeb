package interfaces;

import model.SanPham;
import model.DanhGiaSanPham;
import java.util.List;

public interface ISanPham {
    SanPham getById(int id);
    List<SanPham> getAll();
    int getTotalSanPham();
    List<SanPham> getSanPhams(int offset, int limit);
    List<SanPham> searchByName(String keyword, int offset, int limit);
    int getTotalSanPhamByName(String keyword);
    List<SanPham> getSanPhamTheoDanhMuc(int maDanhMuc, int offset, int limit);
    int getTotalSanPhamTheoDanhMuc(int maDanhMuc);
    List<DanhGiaSanPham> layDanhSachDanhGiaDaGiao();
    void closeConnection();
}