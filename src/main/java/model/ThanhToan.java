package model;

import java.util.Date;

public class ThanhToan {
    private int maThanhToan;
    private int maDonHang;
    private String hinhThucThanhToan;
    private String trangThai;
    private Date ngayThanhToan;

    public ThanhToan() {}

    public ThanhToan(int maThanhToan, int maDonHang, String hinhThucThanhToan, String trangThai, Date ngayThanhToan) {
        this.maThanhToan = maThanhToan;
        this.maDonHang = maDonHang;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.trangThai = trangThai;
        this.ngayThanhToan = ngayThanhToan;
    }

    public int getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(int maThanhToan) {
        this.maThanhToan = maThanhToan;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getHinhThucThanhToan() {
        return hinhThucThanhToan;
    }

    public void setHinhThucThanhToan(String hinhThucThanhToan) {
        this.hinhThucThanhToan = hinhThucThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }
}
