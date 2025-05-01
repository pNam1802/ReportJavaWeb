package model;

public class ChiTietGioHang {
    private int maGioHang;
    private int maSanPham;
    private int soLuong;
    private double gia;

    public ChiTietGioHang() {}

    public ChiTietGioHang(int maGioHang, int maSanPham, int soLuong, double gia) {
        this.maGioHang = maGioHang;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    public int getMaGioHang() {
        return maGioHang;
    }

    public void setMaGioHang(int maGioHang) {
        this.maGioHang = maGioHang;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
