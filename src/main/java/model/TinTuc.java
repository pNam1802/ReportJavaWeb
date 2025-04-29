package model;

import java.util.Date;

public class TinTuc {
    private int maTinTuc;
    private String tieuDe;
    private String noiDung;
    private Date ngayDang;
    private int maSanPham;

    public TinTuc() {}

    public TinTuc(int maTinTuc, String tieuDe, String noiDung, Date ngayDang, int maSanPham) {
        this.maTinTuc = maTinTuc;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayDang = ngayDang;
        this.maSanPham = maSanPham;
    }

    public int getMaTinTuc() {
        return maTinTuc;
    }

    public void setMaTinTuc(int maTinTuc) {
        this.maTinTuc = maTinTuc;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(Date ngayDang) {
        this.ngayDang = ngayDang;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }
}
