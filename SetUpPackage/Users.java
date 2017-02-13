package SetUpPackage;

import SQLConPackage.SQLCon;

import java.sql.*;
/**
 * Created by Pedrum on 12/22/2016.
 */
public class Users {

    public static void SetUp(Statement __stmt) throws SQLException {

        createUsersTable(__stmt);
        if (insUsersColInPageViews(__stmt) == SQLCon.SQLRET.SUCCESS){

            Utility.addIndex(__stmt, "page_views_sample", "pageview1_idx", "user");
        }
    }

    private static void createUsersTable(Statement __stmt) throws  SQLException{
        Utility _utility;
        _utility = new Utility();
        _utility.currentTableColumn = "uuid";
        _utility.currentTable = "events";
        _utility.currentTableNewColumn = "user";
        _utility.newTableColumn = "userNum";
        _utility.newTable = "users";
        _utility.SetUp(__stmt, "CHAR(30)");
    }

    private static SQLCon.SQLRET insUsersColInPageViews(Statement __stmt) throws  SQLException{
        Utility _utility;
        _utility = new Utility();
        _utility.currentTableColumn = "uuid";
        _utility.currentTable = "page_views_sample";
        _utility.currentTableNewColumn = "user";
        _utility.newTableColumn = "userNum";
        _utility.newTable = "users";
        return _utility.SetUpColumn(__stmt);
    }


}
