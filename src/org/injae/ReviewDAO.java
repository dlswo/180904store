package org.injae;

import org.injae.domain.ReviewVO;
import org.injae.domain.ScoreVO;
import org.injae.domain.StoreVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<ReviewVO> getReviews(int sno){
        List<ReviewVO> list = new ArrayList<>();
        String sql = "select * from tbl_review \n" +
                "where mno in (select mno from tbl_menu where sno = ?)\n" +
                "order by rno desc";
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
            stmt.setInt(1,sno);
            rs = stmt.executeQuery();

            while(rs.next()) {
                ReviewVO vo = new ReviewVO();
                vo.setRno(rs.getInt("rno"));
                vo.setmno(rs.getInt("mno"));
                vo.setMid(rs.getString("mid"));
                vo.setCmt(rs.getString("cmt"));
                vo.setReviewDate(rs.getDate("reviewDate"));
                vo.setMname(rs.getString("mname"));

                list.add(vo);
            }



        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            if (rs != null) {     try {     rs.close();} catch (Exception e) {}}
            if (stmt != null) {     try {         stmt.close();   } catch (Exception e) { }}
            if (con != null) {     try {     con.close();} catch (Exception e) {}}



        }//end finally


        return list;
    }

    public List<ScoreVO> getScores(int sno){
        List<ScoreVO> list = new ArrayList<>();
        String sql = "select s.score, nvl(cnt,0) cnt,\n" +
                "    round(nvl(cnt,0) / ( sum(nvl(cnt,0)) over() ) * 100) pnt\n" +
                "from \n" +
                "  (select round(score) score ,count(rno) cnt\n" +
                "    from\n" +
                "      (select mno from tbl_menu where sno = ?) menu,\n" +
                "      tbl_review review\n" +
                "    where menu.mno = review.mno\n" +
                "    group by round(score) ) review, tbl_score s\n" +
                "where s.score = review.score(+)\n" +
                "order by s.score desc";
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
            stmt.setInt(1,sno);
            rs = stmt.executeQuery();

            while(rs.next()) {
                ScoreVO vo = new ScoreVO();
                vo.setScore(rs.getInt("score"));
                vo.setCnt(rs.getInt("cnt"));
                vo.setPnt(rs.getInt("pnt"));

                list.add(vo);
            }



        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            if (rs != null) {     try {     rs.close();} catch (Exception e) {}}
            if (stmt != null) {     try {         stmt.close();   } catch (Exception e) { }}
            if (con != null) {     try {     con.close();} catch (Exception e) {}}



        }//end finally



        return list;
    }

    public void add(ReviewVO vo){

        // 순서

        Connection con = null;
        PreparedStatement stmt = null;
        String sql =
                "insert into tbl_review ( rno, mid, mno, score, cmt) values (seq_review.nextval, ?, ?, ?, ?)";
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@10.10.10.91:1521:XE",
                    "zerock",
                    "12345678");
            System.out.println(con);
            stmt = con.prepareStatement(sql);
            //mid, mno, score
            stmt.setString(1,vo.getMid());
            stmt.setInt(2,vo.getmno());
            stmt.setDouble(3,vo.getScore());
            stmt.setString(4,vo.getCmt());

            System.out.println(sql);

            int count = stmt.executeUpdate();

            System.out.println(count);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {
                    }
                }

            }//end finally

            // 1.Connection 맺기


            // 2. Statement SQL 전달

            // 3. Statement 실행

            // 4. close
        }

    } // end method
}//end class

