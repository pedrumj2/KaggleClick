package SetUpPackage;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Statement;

import SQLConPackage.SQLCon;
import Timer.Timer;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Pedrum on 12/23/2016.
 */


public class Main {
    public static void  RunSetUp(Statement __stmt) throws SQLException{

       // Trim.trim(__stmt);
       // alter_page_views_sample(__stmt);
       setUpColumns(__stmt);
        //Timer.start();
      //  General.dataTypeAdjust(__stmt);
       // Timer.end("General Data Type Adjustment: ");
       // Timer.start();
        //General.addIndices(__stmt);
        //Timer.end("General indice addition: ");
      //  mergePageViews(__stmt);

    }

    //
    private static void alter_page_views_sample(Statement __stmt) {

        Boolean _res;
        String query = "ALTER TABLE `kaggle`.`page_views_sample` \n" +
                "CHANGE COLUMN `uuid` `uuid` CHAR(30) NULL DEFAULT NULL ,\n" +
                "CHANGE COLUMN `document_id` `document_id` INT UNSIGNED NOT NULL ,\n" +
                "CHANGE COLUMN `timestamp` `timestamp` INT NULL DEFAULT NULL ,\n" +
                "CHANGE COLUMN `platform` `platform` INT NULL DEFAULT NULL ,\n" +
                "CHANGE COLUMN `geo_location` `geo_location` CHAR(15) NULL DEFAULT NULL ,\n" +
                "CHANGE COLUMN `traffic_source` `traffic_source` INT NULL DEFAULT NULL ,\n" +
                "ADD COLUMN `location` INT UNSIGNED NOT NULL DEFAULT 1 AFTER `traffic_source`,\n" +
                "ADD COLUMN `user` INT UNSIGNED NOT NULL DEFAULT 1 AFTER `location`,\n" +
                "ADD COLUMN `trafficSource` INT UNSIGNED NOT NULL DEFAULT 1 AFTER `user`,\n" +
                "ADD COLUMN `platform2` INT UNSIGNED NOT NULL DEFAULT 1 AFTER `trafficSource`,\n" +
                "ADD COLUMN `document` INT UNSIGNED NOT NULL DEFAULT 1 AFTER `platform2`;\n;";

        try{
            _res = __stmt.execute(query);

        }
        catch(java.sql.SQLException ex) {

            System.out.println("Could not alter table page_views_sample");
            System.out.println(ex);

        }


    }

    //Reduces the size of the some tables to make calculation faster
    private static void setUpColumns(Statement __stmt) throws SQLException {

     /*   Timer.start();
        Users.SetUp(__stmt);
        Timer.end("User setup time: ");
        Timer.start();
        Location.SetUp(__stmt);
        Timer.end("Location setup time: ");
        Timer.start();
        Document.SetUp(__stmt);
        Timer.end("Document setup time: ");
        Timer.start();*/
        General.SetUp(__stmt);
      //  Timer.end("General setup time: ");


    }

    private static List<String> generatePostfixStrings() {
        List<String> _postFix;
        _postFix = new ArrayList<String>();
        for (char c = 'b'; c <= 'u'; c++) {
            _postFix.add(Character.toString(c));
        }

        return _postFix;



    }

    private static void mergePageViews(Statement __stmt) throws SQLException {
        List<String> _postFix;
        _postFix = generatePostfixStrings();

        renameFirstPageView(__stmt);
        for (int i = 0; i < _postFix.size(); i++){
            insertIntoPageViewsSample(__stmt, _postFix.get(i));
            dropSplitTable(__stmt, _postFix.get(i));
        }


    }
    private static SQLCon.SQLRET dropSplitTable(Statement __stmt, String __postFix) throws SQLException {

        Boolean _res;
        String query = "drop table kaggle.split_page_views_a" + __postFix + ";\n";
        try{
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        }
        catch(java.sql.SQLException ex) {
            System.out.println("tables split view a" + __postFix + " does not exist for dropping");
            return SQLCon.SQLRET.FAIL;
        }
    }


    private static SQLCon.SQLRET insertIntoPageViewsSample(Statement __stmt, String __postFix) throws SQLException {

        Boolean _res;
        String query = " insert into page_views_sample\n" +
                "    SELECT * FROM kaggle.split_page_views_a" + __postFix + ";\n";
        try{
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        }
        catch(java.sql.SQLException ex) {
            System.out.println("table split a" + __postFix + " does not exist for inserting");
            return SQLCon.SQLRET.FAIL;
        }
    }

    private static SQLCon.SQLRET renameFirstPageView(Statement __stmt) throws SQLException {

        Boolean _res;
        String query = "ALTER TABLE `kaggle`.`split_page_views_aa` \n" +
                "RENAME TO  `kaggle`.`page_views_sample` ;\n";

        try{
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        }
        catch(java.sql.SQLException ex) {
            System.out.println("Could not rename split_page_views_aa");
            return SQLCon.SQLRET.FAIL;
        }
    }

}
