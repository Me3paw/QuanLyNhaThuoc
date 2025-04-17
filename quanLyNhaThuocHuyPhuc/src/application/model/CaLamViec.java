package application.model;

import java.time.LocalDate;

public class CaLamViec {
    private String maCa;
    private LocalDate ngayLam;
    private String gioBatDau;
    private String gioKetThuc;
    private String nhanVien;
    private String ghiChu;

    // Constructor nhận các tham số đúng theo yêu cầu
    public CaLamViec(String maCa, LocalDate ngayLam, String gioBatDau, String gioKetThuc, String nhanVien, String ghiChu) {
        this.maCa = maCa;
        this.ngayLam = ngayLam;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.nhanVien = nhanVien;
        this.ghiChu = ghiChu;
    }

    // Getter và Setter
    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public LocalDate getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(LocalDate ngayLam) {
        this.ngayLam = ngayLam;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public String getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(String gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(String nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "CaLamViec{" +
                "maCa='" + maCa + '\'' +
                ", ngayLam=" + ngayLam +
                ", gioBatDau='" + gioBatDau + '\'' +
                ", gioKetThuc='" + gioKetThuc + '\'' +
                ", nhanVien='" + nhanVien + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
