package SetUpPackage;

import SQLConPackage.SQLCon;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pedrum on 12/22/2016.
 */
public class Location {
    public static void SetUp(Statement __stmt) throws SQLException {
        //failure occurs when the table already exists
        if (createLocation(__stmt) == SQLCon.SQLRET.SUCCESS) {
            insNULLLocation(__stmt);
            fillLocations(__stmt);
            deleteEmptyLocations(__stmt);

        }


        //failure occurs when the table already exists
        if (createTi(__stmt, 1) == SQLCon.SQLRET.SUCCESS){
            insNULLTi(__stmt, 1);
            fillTi(__stmt, 1);
            fillTiCol(__stmt, 1);

        }

        //failure occurs when the table already exists
        if (createTi(__stmt, 2) == SQLCon.SQLRET.SUCCESS){
            insNULLTi(__stmt, 2);
            fillTi(__stmt, 2);
            fillTiCol(__stmt, 2);
        }


        //failure occurs when the table already exists
        if (createTi(__stmt, 3) == SQLCon.SQLRET.SUCCESS){
            insNULLTi(__stmt, 3);
            fillTi(__stmt, 3);
            fillTiCol(__stmt, 3);
        }

        SetLocationColumnInPageViews(__stmt);
        SetLocationColumnInEvents(__stmt);
    }

    private static void SetLocationColumnInPageViews(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "geo_location";
        catSetUp.currentTable = "page_views_sample";
        catSetUp.currentTableNewColumn = "location";
        catSetUp.newTableColumn = "strLocation";
        catSetUp.newTable = "locations";
        catSetUp.SetUpColumn(__stmt);
    }

    private static void SetLocationColumnInEvents(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "geo_location";
        catSetUp.currentTable = "events";
        catSetUp.currentTableNewColumn = "location";
        catSetUp.newTableColumn = "strLocation";
        catSetUp.newTable = "locations";
        catSetUp.SetUpColumn(__stmt);
    }



    //creates location table
    private static SQLCon.SQLRET createLocation(Statement __stmt) {
        Boolean _res;

        String query = "CREATE TABLE `kaggle`.`locations` (\n" +
                "  `idlocations` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  `strLocation` CHAR(20) NOT NULL,\n" +
                "  `T1` INT NOT NULL DEFAULT 1,\n" +
                "  `T2` INT NOT NULL DEFAULT 1,\n" +
                "  `T3` INT NOT NULL DEFAULT 1,\n" +
                "  PRIMARY KEY (`idlocations`),\n" +
                "  INDEX `INDEX1` (`strLocation` ASC));\n;";

        try{
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        }
        catch(java.sql.SQLException ex) {

            System.out.println("Could not create table: locations");

            return SQLCon.SQLRET.FAIL;
        }

    }

    //insert single NULL Location in Locations table
    private  static void insNULLLocation(Statement __stmt) throws  SQLException{
        Boolean _res;
        String query = "INSERT INTO `kaggle`.`locations` (`strLocation`) VALUES ( '');";
        _res = __stmt.execute(query);

    }
    //fills the locations table
    private  static void fillLocations(Statement __stmt) throws  SQLException{
        Boolean _res;
        String query = "insert  into kaggle.locations (strLocation)\n" +
                "select distinct geo_location from kaggle.events;";
        _res = __stmt.execute(query);

    }

    //deletes empty rows. Some rows will endup with junk values. We will assign all these
    //to the NULL index created.
    private  static void deleteEmptyLocations(Statement __stmt) throws  SQLException{
        Boolean _res;
        String query = "delete FROM kaggle.locations\n" +
                "where (strLocation = \"\" and idLocations <> 1)\n" +
                "or (strLocation = \"--\")\n" +
                "or (strLocation = \"-->-->0\");;";
        _res = __stmt.execute(query);
    }


    //creates Ti table
    private static SQLCon.SQLRET createTi(Statement __stmt, int i) {
        Boolean _res;

        String query = "CREATE TABLE `kaggle`.`loc_t" + i + "` (\n" +
                "  `idloc_t" + i + "` INT UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,\n" +
                "  `strT" + i + "` CHAR(2) NOT NULL,\n" +
                "  PRIMARY KEY (`idloc_t" + i + "`),\n" +
                "  INDEX `INDEX1` (`strT" + i + "` ASC));\n;";

        try{
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        }
        catch(java.sql.SQLException ex) {
            System.out.println("Could not create table: loc_T" + i);
            return SQLCon.SQLRET.FAIL;
        }

    }

    //insert single NULL strTi in Ti tables
    private  static void insNULLTi(Statement __stmt, int i) throws  SQLException{
        Boolean _res;
        String query = "insert into kaggle.loc_t1 (strT1) values('');;";
        _res = __stmt.execute(query);
    }

    //fills the locations table
    private  static void fillTi(Statement __stmt, int i) throws  SQLException{
        Boolean _res;
        int _startIndex =1 + (i-1)*3;
        String query = "insert into kaggle.loc_t" + i + "(strT" + i + ")\n" +
                "select d from \n" +
                "(\n" +
                "select distinct substr(locations.strLocation, " +_startIndex + ", 2) as d from locations\n" +
                ") as t\n" +
                "where d <> \"\";";
        _res = __stmt.execute(query);
    }


    //fills the Ti columns in the location table
    private  static void fillTiCol(Statement __stmt, int i) throws SQLException{
        Boolean _res;
        int _startIndex =1 + (i-1)*3;
        String query = "update kaggle.locations\n" +
                "inner join\n" +
                "(select * from \n" +
                "(\n" +
                "\tselect distinct substr(locations.strLocation, " + _startIndex + ", 2) as d, T" + i + ", idlocations from kaggle.locations\n" +
                ") as t\n" +
                "inner join loc_t" + i + "\n" +
                "on loc_t" + i + ".strT" + i + " = t.d\n" +
                ")as e\n" +
                "on e.idlocations = locations.idlocations\n" +
                "set locations.T" + i + " = e.idloc_t" + i +";";


        _res = __stmt.execute(query);
    }

}
