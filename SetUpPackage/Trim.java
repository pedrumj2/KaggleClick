package SetUpPackage;

import java.sql.*;

/**
 * Created by Pedrum on 12/22/2016.
 */
public class Trim {


    public static void trim(Statement __stmt) throws SQLException{
        addIDColPageView(__stmt);
        trimPageViews(__stmt);
        trimEvents(__stmt);
        trimClicksTrain(__stmt);


    }
    //adds primary key column to Page View table
    private static void addIDColPageView(Statement __stmt) {
        Boolean _res;

        String query = "ALTER TABLE kaggle.page_views_sample \n" +
                "ADD COLUMN idView INT UNSIGNED NOT NULL AUTO_INCREMENT first,\n" +
                "ADD PRIMARY KEY (idView asc);";

        try{
            _res = __stmt.execute(query);
        }
        catch(java.sql.SQLException ex) {
            System.out.println("Could not add primary key to table page_views_sample");
        }
    }


    //reduces the rows in the page view table
    private static void trimPageViews(Statement __stmt) throws  SQLException{
        int _rowCount = 1000000;
        Boolean _res;
        String query = "delete from kaggle.page_views_sample\n" +
                "where  idView > " + _rowCount + ";";
        _res = __stmt.execute(query);

    }

    //reduces the rows in the events table
    private static void trimEvents(Statement __stmt) throws  SQLException{
        int _rowCount = 1000000;
        Boolean _res;
        String query = "delete from kaggle.events\n" +
                "where  display_id > " + _rowCount + ";";
        _res = __stmt.execute(query);

    }

    //reduces the rows in the Clicks_Train table
    private static void trimClicksTrain(Statement __stmt) throws  SQLException{
        int _rowCount = 666666;
        Boolean _res;
        String query = "delete from kaggle.clicks_train\n" +
                "where  display_id > " + _rowCount + ";";
        _res = __stmt.execute(query);

    }



}
