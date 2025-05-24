package entity;

import java.time.LocalDate;

public class LoThuoc {
	 	private String maLoThuoc;
	    private String maThuoc;
	    private int soLuongNhap;
	    private LocalDate ngayNhap;
	    private LocalDate hanSuDung;
	    private double giaNhap;
	    private String ghiChu;
	    
	    public LoThuoc() {
			this("", "", 0, LocalDate.now(), LocalDate.now(), 0.0, "");
		}
		public LoThuoc(String maLoThuoc, String maThuoc, int soLuongNhap, LocalDate ngayNhap, LocalDate hanSuDung,
				double giaNhap, String ghiChu) {
			
			this.maLoThuoc = maLoThuoc;
			this.maThuoc = maThuoc;
			this.soLuongNhap = soLuongNhap;
			this.ngayNhap = ngayNhap;
			this.hanSuDung = hanSuDung;
			this.giaNhap = giaNhap;
			this.ghiChu = ghiChu;
		}
		public String getMaLoThuoc() {
			return maLoThuoc;
		}
		public void setMaLoThuoc(String maLoThuoc) {
			this.maLoThuoc = maLoThuoc;
		}
		public String getMaThuoc() {
			return maThuoc;
		}
		public void setMaThuoc(String maThuoc) {
			this.maThuoc = maThuoc;
		}
		public int getSoLuongNhap() {
			return soLuongNhap;
		}
		public void setSoLuongNhap(int soLuongNhap) {
			this.soLuongNhap = soLuongNhap;
		}
		public LocalDate getNgayNhap() {
			return ngayNhap;
		}
		public void setNgayNhap(LocalDate ngayNhap) {
			this.ngayNhap = ngayNhap;
		}
		public LocalDate getHanSuDung() {
			return hanSuDung;
		}
		public void setHanSuDung(LocalDate hanSuDung) {
			this.hanSuDung = hanSuDung;
		}
		public double getGiaNhap() {
			return giaNhap;
		}
		public void setGiaNhap(double giaNhap) {
			this.giaNhap = giaNhap;
		}
		public String getGhiChu() {
			return ghiChu;
		}
		public void setGhiChu(String ghiChu) {
			this.ghiChu = ghiChu;
		}
		@Override
		public String toString() {
			return "LoThuoc [maLoThuoc=" + maLoThuoc + ", maThuoc=" + maThuoc + ", soLuongNhap=" + soLuongNhap
					+ ", ngayNhap=" + ngayNhap + ", hanSuDung=" + hanSuDung + ", giaNhap=" + giaNhap + ", ghiChu="
					+ ghiChu + "]";
		}
	    
		
}
