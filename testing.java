import java.sql.*;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Jorgen on 27/04/15.
 */
public class testing {
    public static void main(String[] args) throws Exception{
        System.out.println(java.net.Inet4Address.getLocalHost().getHostAddress());
        Class.forName("com.mysql.jdbc.Driver");
//        Connection con = DriverManager.getConnection( "jdbc:mysql://" + java.net.Inet4Address.getLocalHost().getHostAddress() + ":3306/test1", "root", "123qweasd");
//
//        Connection con2 = DriverManager.getConnection( "jdbc:mysql://" + java.net.Inet4Address.getLocalHost().getHostAddress() + ":3306/test2", "root", "123qweasd");

//        Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/test1", "root", "123qweasd");

        Connection con2 = DriverManager.getConnection( "jdbc:mysql://10.0.1.7:8889/Bil", "db", "db");

        String stm = "SELECT * FROM biler";


        PreparedStatement ps = con2.prepareStatement(stm);
        ResultSet rs  = ps.executeQuery();
        while(rs.next()){
            System.out.println(rs.getString("farge"));
        }

        stm = "INSERT INTO test1 VALUES(11, 'hei')";
//        Statement st = con.createState

//
//        con.setAutoCommit(falsea);
//        PreparedStatement ps1 = con.prepareStatement(stm);
//
//        ps1.execute();
//
//        showMessageDialog(null, "hei!");
//
//        con.commit();*/
    }
}
