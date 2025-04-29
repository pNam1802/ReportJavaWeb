package model;

import java.util.Date;

public class KhuyenMai {
    private int maKhuyenMai;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private double giaKhuyenMai;
    private int maSanPham;

    public KhuyenMai() {}

    public KhuyenMai(int maKhuyenMai, Date ngayBatDau, Date ngayKetThuc, double giaKhuyenMai, int maSanPham) {
        this.maKhuyenMai = maKhuyenMai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giaKhuyenMai = giaKhuyenMai;
        this.maSanPham = maSanPham;
    }

    public int getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(int maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(double giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }
}
