package model;

public class SanPhamInDonHang {
    private String tenSanPham;
    private int soLuong;
    private double gia;

    // Constructor
    public SanPhamInDonHang(String tenSanPham, int soLuong, double gia) {
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    // Getters and Setters
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
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

