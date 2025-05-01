package model;

public class GioHang {
    private int maGioHang;
    private int maNguoiDung;

    public GioHang() {}

    public GioHang(int maGioHang, int maNguoiDung) {
        this.maGioHang = maGioHang;
        this.maNguoiDung = maNguoiDung;
    }

    public int getMaGioHang() {
        return maGioHang;
    }

    public void setMaGioHang(int maGioHang) {
        this.maGioHang = maGioHang;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }
}
