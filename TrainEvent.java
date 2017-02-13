import SQLConPackage.SQLCon;

import java.sql.SQLException;
import java.sql.Statement;
import SQLConPackage.SQLCon;

/**
 * Created by Pedrum on 12/23/2016.
 */
public class TrainEvent {

    public static void CreateTraining(Statement __stmt) throws SQLException {
        if (CreateTable(__stmt) == SQLCon.SQLRET.SUCCESS) {
            Insert1(__stmt);
        }
        CreatePastViewTables(__stmt);
    }

    private static SQLCon.SQLRET CreateTable(Statement __stmt) {
        Boolean _res;

        String query = "CREATE TABLE `kaggle`.`tempCalc1` (\n" +
                "  `idtempCalc1` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  `ad_id` int UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `document` int UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `display_id` int UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `clicked` TINYINT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `campaign` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `advertiser` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `doc_source` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `doc_publisher` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `user` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `timestamp` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `platform` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `loc1` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `loc2` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  `loc3` INT UNSIGNED NOT NULL DEFAULT 1,\n" +
                "  PRIMARY KEY (`idtempCalc1`), \n" +
                " INDEX `INDEX1` (`display_id` ASC));";

        try {
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        } catch (java.sql.SQLException ex) {
            System.out.println("Could not create table: tempCalc1");
            return SQLCon.SQLRET.FAIL;
        }
    }

    private static void CreatePastViewTables(Statement __stmt){
        for (int i = 1; i <=1; i++){
            CreateSinglePastViewTable(__stmt, i);
        }
    }
    private static SQLCon.SQLRET CreateSinglePastViewTable(Statement __stmt, int i) {
        Boolean _res;

        String query = "CREATE TABLE `kaggle`.`pastViews" + i + "` (\n" +
                "            `idpastViews" + i + "` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "            `idTempCalc` INT UNSIGNED NOT NULL,\n" +
                "   `source` INT UNSIGNED NOT NULL,\n" +
                "  `publisher` INT UNSIGNED NOT NULL,\n" +
                "  `timestamp` INT UNSIGNED NOT NULL,\n" +
                "   `trafficSource` INT UNSIGNED NOT NULL,\n" +
                "  `platform` DOUBLE NOT NULL,\n" +
                "  `T1` DOUBLE NOT NULL,\n" +
                "  `T2` DOUBLE NOT NULL,\n" +
                "  `T3` DOUBLE NOT NULL,\n" +
                "  `catSimilarity` DOUBLE NOT NULL,\n" +
                "  `topicSimilarity` DOUBLE UNSIGNED NOT NULL,\n" +
                "  `entitySimilarity` DOUBLE UNSIGNED NOT NULL,\n" +
                "    PRIMARY KEY (`idpastViews" + i + "`),\n" +
                "    INDEX `pastView" + i + "Index1_idx` (`idTempCalc` ASC),\n" +
                "    CONSTRAINT `pastView" + i +"Index1`\n" +
                "    FOREIGN KEY (`idTempCalc`)\n" +
                "    REFERENCES `kaggle`.`tempCalc1` (`idtempCalc1`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE RESTRICT);";

        try {
            _res = __stmt.execute(query);
            return SQLCon.SQLRET.SUCCESS;
        } catch (java.sql.SQLException ex) {
            System.out.println("Could not create table: tempCalc1");
            return SQLCon.SQLRET.FAIL;
        }
    }






    //inserting into the training table will be done in seperate steps
    private static void Insert1(Statement __stmt) throws SQLException {
        Boolean _res;

        String query = "insert into tempCalc1 (ad_id, document, display_id, clicked, campaign, advertiser, doc_source, doc_publisher, user, timestamp, platform, loc1, loc2, loc3)\n" +
                "(select clicks_train.ad_id, events.document, events.display_id, clicks_train.clicked, promoted_content.campaign, \n" +
                "\tpromoted_content.advertiser, documents_meta.source, documents_meta.publisher, events.user, events.timestamp, events.platform, locations.T1, \n" +
                "    locations.T2, locations.T3 from clicks_train\n" +
                "inner join promoted_content \n" +
                "on promoted_content.ad_id = clicks_train.ad_id\n" +
                "inner join events\n" +
                "on events.display_id = clicks_train.display_id\n" +
                "inner join locations\n" +
                "on locations.idlocations = events.location\n" +
                "inner join documents_meta\n" +
                "on documents_meta.document = events.document)";

        __stmt.execute(query);

    }
}
