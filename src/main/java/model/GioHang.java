package model;

import java.util.HashMap;
import java.util.Map;

public class GioHang {
    // Giả sử gioHang là một Map<Integer, GioHangItem>, với Integer là mã sản phẩm
    private Map<Integer, GioHangItem> danhSach;

    public GioHang() {
        this.danhSach = new HashMap<>();
    }

    // Phương thức lấy danh sách các mặt hàng trong giỏ
    public Map<Integer, GioHangItem> getDanhSach() {
        return danhSach;
    }

    // Thêm sản phẩm vào giỏ
    public void themSanPham(GioHangItem item) {
        danhSach.put(item.getMaSanPham(), item);
    }

    // Xóa sản phẩm khỏi giỏ
    public void xoaSanPham(int maSanPham) {
        danhSach.remove(maSanPham);
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    public void capNhatSoLuong(int maSanPham, int soLuongMoi) {
        GioHangItem item = danhSach.get(maSanPham);
        if (item != null) {
            item.setSoLuong(soLuongMoi);
        }
    }
    public double getTongTien() {
        double tongTien = 0.0;
        for (GioHangItem item : danhSach.values()) {
            tongTien += item.getThanhTien();
        }
        return tongTien;
    }

}
