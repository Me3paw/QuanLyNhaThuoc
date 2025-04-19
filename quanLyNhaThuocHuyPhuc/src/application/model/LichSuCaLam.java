package application.model;

import java.time.LocalDate;

public class LichSuCaLam {
    private String maNhanVien;
    private LocalDate ngayLam;
    private int caLam;

    public LichSuCaLam(String maNhanVien, LocalDate LocalDate, int caLam) {
        this.maNhanVien = maNhanVien;
        this.ngayLam = LocalDate;
        this.caLam = caLam;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public LocalDate getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(LocalDate ngayLam) {
        this.ngayLam = ngayLam;
    }

    public int getCaLam() {
        return caLam;
    }

    public void setCaLam(int caLam) {
        this.caLam = caLam;
    }
}
