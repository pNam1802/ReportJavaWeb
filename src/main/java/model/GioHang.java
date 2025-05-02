package model;

import java.util.ArrayList;
import java.util.List;

public class GioHang {
    private List<GioHangItem> items;

    public GioHang() {
        items = new ArrayList<>();
    }

    // Thêm sản phẩm vào giỏ hàng
    public void themSanPham(GioHangItem item) {
        boolean exist = false;
        for (GioHangItem gioHangItem : items) {
            if (gioHangItem.getMaSanPham() == item.getMaSanPham()) {
                // Nếu sản phẩm đã có trong giỏ, chỉ cần cập nhật số lượng
                gioHangItem.capNhatSoLuong(gioHangItem.getSoLuong() + item.getSoLuong());
                exist = true;
                break;
            }
        }
        if (!exist) {
            items.add(item);
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void xoaSanPham(int maSanPham) {
        items.removeIf(item -> item.getMaSanPham() == maSanPham);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void capNhatSoLuong(int maSanPham, int soLuongMoi) {
        for (GioHangItem item : items) {
            if (item.getMaSanPham() == maSanPham) {
                item.capNhatSoLuong(soLuongMoi);
                break;
            }
        }
    }

    // Tính tổng giá trị giỏ hàng
    public double getTongTien() {
        double tongTien = 0;
        for (GioHangItem item : items) {
            tongTien += item.getThanhTien();
        }
        return tongTien;
    }

    // Lấy tất cả các món hàng trong giỏ
    public List<GioHangItem> getItems() {
        return items;
    }

    // Kiểm tra xem giỏ có rỗng không
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
