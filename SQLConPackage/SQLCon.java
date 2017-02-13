package SQLConPackage; /**
 * Created by Pedrum on 12/11/2016.
 */

import java.sql.*;

//handles connections to our database
public class SQLCon {
    public Statement stmt;
    private Connection connection;
    private String password = "fafdRE$3";
    private String dbIP = "130.211.218.53";
    private String dbName = "kaggle";

    public enum SQLRET{
        SUCCESS,
        FAIL
    }


    public SQLCon() throws SQLException {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + dbIP + ":3306/" +dbName+"?useSSL=false", "root", password);
            stmt = connection.createStatement();
            stmt.setQueryTimeout(360000);
        }
        catch(java.sql.SQLException ex) {
            System.out.println("Database not connected!");
            throw ex;
        }
        catch(ClassNotFoundException ex){
            System.out.println(ex);

        }


    }


    public void close() throws SQLException {

        stmt.close();
        connection.close();


    }

}
