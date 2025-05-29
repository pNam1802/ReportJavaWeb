package interfaces;

import java.util.List;

import model.DanhMuc;

public interface IDanhMuc {
    boolean add(DanhMuc danhMuc);
    boolean update(DanhMuc danhMuc);
    boolean delete(int maDanhMuc);
    List<DanhMuc> getAll();
    DanhMuc getId(int maDanhMuc);
    DanhMuc getTenDanhMuc(String tenDanhMuc);
}