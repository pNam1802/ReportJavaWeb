package model;

import java.util.Date;

public class DonHang {
    private int maDonHang;
    private Date ngayLap;
    private String trangThai;
    private double tongTien;
    private int maNguoiDung;

    public DonHang() {}

    public DonHang(int maDonHang, Date ngayLap, String trangThai, double tongTien, int maNguoiDung) {
        this.maDonHang = maDonHang;
        this.ngayLap = ngayLap;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.maNguoiDung = maNguoiDung;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }
}
