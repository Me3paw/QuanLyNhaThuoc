package application.database;

import application.model.NhaCungCap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class NhaCungCapDAO {
	
	
	public List<NhaCungCap> getAllTenNhaCungCap() {
        List<NhaCungCap> listNCC = new ArrayList<>();
        String sql = "SELECT maNhaCungCap, tenNhaCungCap FROM NhaCungCap"; 

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maNCC = rs.getString("maNhaCungCap");
                String tenNCC = rs.getString("tenNhaCungCap");
                NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC,"","","");
                listNCC.add(ncc);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNCC;
    }

}
