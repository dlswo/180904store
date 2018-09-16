package org.injae;

import org.injae.domain.MenuVO;
import org.injae.domain.StoreVO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    public List<StoreVO> getAll(){
        String sql = "select * from tbl_store order by sname";
        List<StoreVO> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try{
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@10.10.10.91:1521:XE",
                    "zerock",
                    "12345678");
            System.out.println(con);
            stmt = con.prepareStatement(sql);

            rs = stmt.executeQuery();

            while(rs.next()){
                StoreVO vo = new StoreVO();
                vo.setSno(rs.getInt("sno"));
                vo.setLat(rs.getDouble("lat"));
                vo.setLng(rs.getDouble("lng"));
                vo.setSname(rs.getString("sname"));
                vo.setGubun(rs.getString("gubun"));
                vo.setImg(rs.getString("img"));
                list.add(vo);
            }

            System.out.println(sql);

            int count = stmt.executeUpdate();

            System.out.println(count);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            if (rs != null) {     try {     rs.close();} catch (Exception e) {}}
            if (stmt != null) {     try {         stmt.close();   } catch (Exception e) { }}
            if (con != null) {     try {     con.close();} catch (Exception e) {}}



        }//end finally


        return list;
    }
}
