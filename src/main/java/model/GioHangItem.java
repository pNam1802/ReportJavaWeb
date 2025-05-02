package model;

public class GioHangItem {
    private int maSanPham;
    private String tenSanPham;
    private double gia;
    private int soLuong;

    // Constructor
    public GioHangItem(int maSanPham, String tenSanPham, double gia, int soLuong) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    // Getter và Setter
    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    // Phương thức tính giá trị của món hàng (gia * soLuong)
    public double getThanhTien() {
        return gia * soLuong;
    }

    // Phương thức cập nhật số lượng sản phẩm trong giỏ hàng
    public void capNhatSoLuong(int soLuongMoi) {
        this.soLuong = soLuongMoi;
    }
}
