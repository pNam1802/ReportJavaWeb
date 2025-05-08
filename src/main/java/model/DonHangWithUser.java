package model;

import java.sql.Date;
import java.util.List;

public class DonHangWithUser {
    private int maDonHang;
    private Date ngayLap;
    private String trangThai;
    private String trangThaiThanhToan;  // Thêm trạng thái thanh toán
    private Double tongTien;
    private String diaChi;
    private String tenNguoiDung;  // Thêm tên người dùng
    private List<SanPhamInDonHang> sanPhamList; // Danh sách sản phẩm

    // Constructor
    public DonHangWithUser(int maDonHang, Date ngayLap, String trangThai, String trangThaiThanhToan, Double tongTien, String diaChi, String tenNguoiDung, List<SanPhamInDonHang> sanPhamList) {
        this.maDonHang = maDonHang;
        this.ngayLap = ngayLap;
        this.trangThai = trangThai;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.tongTien = tongTien;
        this.diaChi = diaChi;
        this.tenNguoiDung = tenNguoiDung;  // Khởi tạo tên người dùng
        this.sanPhamList = sanPhamList;
    }

    // Getters and Setters
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

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;  // Getter cho trạng thái thanh toán
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;  // Setter cho trạng thái thanh toán
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;  // Getter cho tên người dùng
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;  // Setter cho tên người dùng
    }

    public List<SanPhamInDonHang> getSanPhamList() {
        return sanPhamList;
    }

    public void setSanPhamList(List<SanPhamInDonHang> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }
}
