package kollus.jwt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcDriver {

    public void connection(){
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";

        String url = "jdbc:mysql://183.111.198.193:3305/kollus_base";
        String user = "root";
        String pw = "catzzang1@3$";

        try {
            //1. JDBC 드라이버 로딩
            Class.forName(driver);

            // 2. Connection 생성
            con = DriverManager.getConnection(url, user, pw);        //데이터베이스 연결

            System.out.println("[Database 연결 성공]");

        } catch (SQLException e) {

            System.out.println("[SQL Error : " + e.getMessage() +"]");

        } catch (ClassNotFoundException e1) {

            System.out.println("[JDBC Connector Driver Error : " + e1.getMessage() + "]");
        } finally {
            //Connection 사용 후 Close
            if(con != null) {
                try {
                    con.close();
                } catch (Exception e) {

                }
            }
        }

    }

}
