package PetCare.PetCare.Connections;




import java.sql.*;

public class DBLoader {

    public static ResultSet executeSQL(String sql) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver Loading Done");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/petcare", "root", "1485");
        System.out.println("connection done");
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Statement Done");
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Statement Created");

        return rs;
    }

}
