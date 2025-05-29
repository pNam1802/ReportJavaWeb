package interfaces;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import model.ChiTietDonHang;
import model.DonHang;
import model.DonHangWithUser;
import model.SanPhamInDonHang;

public interface IQuanLyDonHang {
    List<DonHang> getAllDonHang() throws SQLException;

    List<DonHang> getDonHangsByTrangThai(String trangThai) throws SQLException;

    List<DonHang> getDonHangsByTrangThaiThanhToan(String trangThaiThanhToan) throws SQLException;

    List<DonHang> getDonHangsByNgayLap(Date ngayLap) throws SQLException;

    List<DonHang> getDonHangsByMaNguoiDung(int maNguoiDung) throws SQLException;

    List<SanPhamInDonHang> getSanPhamInDonHang(int maDonHang) throws SQLException;

    List<DonHangWithUser> getDonHangWithUser(int maDonHang) throws SQLException;

    void updateTrangThaiDonHang(int maDonHang, String trangThaiMoi) throws SQLException;

    String getTrangThaiDonHang(int maDonHang) throws SQLException;

    void updatePaymentStatus(int maDonHang, String thanhToanTrangThai) throws SQLException;

    void addChiTietDonHang(int maDonHang, int maSanPham, int soLuong, Double donGia) throws SQLException;

    void updateDiaChiNguoiDungTheoMaDonHang(int maDonHang, String diaChiMoi) throws SQLException;

    List<ChiTietDonHang> getChiTietDonHang(int maDonHang) throws SQLException;

    void huyDonHang(int maDonHang) throws SQLException;
}
