package model;

import java.util.Date;

public class DanhGiaSanPham {
    private String tenSanPham;
    private String tenNguoiDung;
    private int diemDanhGia;
    private String noiDung;
    private Date ngayDanhGia;

    public DanhGiaSanPham() {}

    public DanhGiaSanPham(String tenSanPham, String tenNguoiDung, int diemDanhGia, String noiDung, Date ngayDanhGia) {
        this.tenSanPham = tenSanPham;
        this.tenNguoiDung = tenNguoiDung;
        this.diemDanhGia = diemDanhGia;
        this.noiDung = noiDung;
        this.ngayDanhGia = ngayDanhGia;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public int getDiemDanhGia() {
        return diemDanhGia;
    }

    public void setDiemDanhGia(int diemDanhGia) {
        this.diemDanhGia = diemDanhGia;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(Date ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }

    @Override
    public String toString() {
        return "Đánh giá [" + tenSanPham + " - " + tenNguoiDung + "]: " + diemDanhGia + "★ - " + noiDung;
    }
}
