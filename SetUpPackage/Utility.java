package SetUpPackage;

import SQLConPackage.SQLCon;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pedrum on 12/22/2016.
 */
public class Utility {
    //this is the table with the column we are trying to make into a distinct table
    public String currentTable;
    //this is the new table that rsults from the column in the current table
    public String newTable;
    public String newTableColumn;
    public String currentTableColumn;
    public String currentTableNewColumn;


    public  void SetUp(Statement __stmt,String __type ) throws SQLException {

        SetUpTable(__stmt, __type);
        SetUpColumn(__stmt);


    }
    public  SQLCon.SQLRET SetUpColumn(Statement __stmt ) throws SQLException {
        SQLCon.SQLRET _res;
        _res = insCol(__stmt);
        //failure occurs when the column already exists
        if ((_res == SQLCon.SQLRET.SUCCESS) || currentTable.equals("page_views_sample")){
            fillCol(__stmt);
            delNULLLinks(__stmt);
            delOldCol(__stmt);
        }
        return _res;
    }

    public  void SetUpTable(Statement __stmt,String __type ) throws SQLException {
        //failure occurs when the table already exists
        if (createTable(__stmt, __type) == SQLCon.SQLRET.SUCCESS){
            insNULLRec(__stmt);
            fillTable(__stmt);
        }
    }

    public static void addIndex(Statement __stmt, String __table, String __index, String __field){
        Boolean _res;
        String query = "ALTER TABLE `kaggle`.`"+__table+"` \n" +
                "ADD INDEX `"+__index+"` (`"+__field+"` ASC);\n";
        try{
            _res = __stmt.execute(query);
        }
        catch(SQLException ex){
            System.out.println("Could not add indice to "+__table);
            System.out.println(ex);
        }
    }
    public static void changeColumnType(Statement __stmt, String __table, String __column,
                                        String __type) throws SQLException{
        Boolean _res;
        String query = "ALTER TABLE `kaggle`.`"+__table+"`\n" +
                "    CHANGE COLUMN `"+__column+"` `"+__column+"` "+__type+" NULL;";
        _res = __stmt.execute(query);

    }

    private SQLCon.SQLRET createTable(Statement __stmt, String __type) {
        Boolean _res;

        String query = "CREATE TABLE `kaggle`.`" + newTable + "` (\n" +
                "  `id" + newTable +"` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  `" + newTableColumn +"` "+ __type + " NOT NULL,\n" +
                "  PRIMARY KEY (`id" + newTable +"`),\n" +
                "  INDEX `INDEX1` (`" + newTableColumn +"` ASC));;";

        try{
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        }
        catch(java.sql.SQLException ex) {
            //this can happen if the table already exists
            System.out.println("Could not create table: " + newTable);
            System.out.println(ex);
            return SQLCon.SQLRET.FAIL;
        }

    }

    private   void insNULLRec(Statement __stmt) throws  SQLException{
        Boolean _res;
        String query = "INSERT INTO `kaggle`.`" + newTable + "` (`" + newTableColumn +"`) VALUES ( '0');";
        _res = __stmt.execute(query);
    }


    //fills the users table
    private   void fillTable(Statement __stmt) throws  SQLException{
        Boolean _res;
        String query;


             query = "insert into " + newTable +" (" + newTableColumn +") \n" +
                    "SELECT distinct " + currentTableColumn +"  FROM kaggle." + currentTable +"\n" +
                     "where " + currentTableColumn + " <> \"\";";



        _res = __stmt.execute(query);

    }


    private SQLCon.SQLRET insCol(Statement __stmt) {
        Boolean _res;
        String query = "ALTER TABLE `kaggle`.`" + currentTable +"`\n" +
                "    ADD COLUMN `" + currentTableNewColumn +"` INT NOT NULL DEFAULT 1 ;";

        try{
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        }
        catch(java.sql.SQLException ex) {
            System.out.println("Could not create column: " + currentTableNewColumn + "\nIn table: " +
                    currentTable );
            System.out.println(ex);
            return SQLCon.SQLRET.FAIL;
        }

    }

    //creates users table
    private   void fillCol(Statement __stmt) throws SQLException{
        Boolean _res;
        String query = "update kaggle." + currentTable + " \n" +
                "inner join " + newTable +"\n" +
                "on " + newTable +"." + newTableColumn +" = " + currentTable +"." + currentTableColumn +"\n" +
                "set "+currentTable+"."+currentTableNewColumn+" = "+newTable+".id"+newTable+";";


        _res = __stmt.execute(query);
    }

    //deletes uuid column from input table
    private   void delOldCol(Statement __stmt) throws SQLException{
        Boolean _res;
        String query = "ALTER TABLE `kaggle`.`"+currentTable+"` \n" +
                "DROP COLUMN `"+currentTableColumn+"`;\n;";
        _res = __stmt.execute(query);

    }

    //if for some reason any of the values are linked to the NULL value
    // in the new table this fnction removes them
    private   void delNULLLinks(Statement __stmt) throws SQLException{
        Boolean _res;
        String query = "delete  FROM kaggle." + currentTable + "\n" +
                "where " + currentTableNewColumn +  " = 1;";
        _res = __stmt.execute(query);

    }











}
