package SetUpPackage;

import SQLConPackage.SQLCon;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pedrum on 12/22/2016.
 */
public class Document {

        public static void SetUp(Statement __stmt) throws SQLException {
            Utility catSetUp;
            //failure occurs when the table alread exists
            if (createDocuments(__stmt) == SQLCon.SQLRET.SUCCESS){
                insNULLDoc(__stmt);
                fillDocs(__stmt);
            }

            catSetUp =factoryGenSetUp();

            docColsSetUp(catSetUp, __stmt);
            ChangeConfType(__stmt);
            addIndices(__stmt);

        }

        private static void addIndices(Statement __stmt){
            Utility.addIndex(__stmt, "documents_entities", "INDEX1", "document");
            Utility.addIndex(__stmt, "documents_meta", "INDEX1", "document");
            Utility.addIndex(__stmt, "documents_topics", "INDEX1", "document");
            Utility.addIndex(__stmt, "documents_categories", "INDEX1", "document");
        }


        private static void docColsSetUp(Utility __catSetUp, Statement __stmt) throws SQLException{
            __catSetUp.currentTable = "documents_categories";
            __catSetUp.SetUpColumn(__stmt);
            __catSetUp.currentTable = "documents_entities";
            __catSetUp.SetUpColumn(__stmt);
            __catSetUp.currentTable = "documents_meta";
            __catSetUp.SetUpColumn(__stmt);
            __catSetUp.currentTable = "documents_topics";
            __catSetUp.SetUpColumn(__stmt);
            __catSetUp.currentTable = "events";
            __catSetUp.SetUpColumn(__stmt);
            __catSetUp.currentTable = "page_views_sample";
            __catSetUp.SetUpColumn(__stmt);
            __catSetUp.currentTable = "promoted_content";
            __catSetUp.SetUpColumn(__stmt);
        }
        private static Utility factoryGenSetUp() throws SQLException{
            Utility _catSetUp;
            _catSetUp = new Utility();
            _catSetUp.currentTableColumn = "document_id";
            _catSetUp.currentTableNewColumn = "document";
            _catSetUp.newTableColumn = "docNum";
            _catSetUp.newTable = "documents";
            return _catSetUp;

        }

        //creates users table
        private static SQLCon.SQLRET createDocuments(Statement __stmt) {
            Boolean _res;

            String query = "CREATE TABLE `kaggle`.`documents` (\n" +
                    "  `idDocuments` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                    "  `docNum` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`idDocuments`), \n" +
                    "  INDEX `INDEX1` (`docNum` ASC));";

            try{
                _res = __stmt.execute(query);
                return SQLCon.SQLRET.SUCCESS;
            }
            catch(java.sql.SQLException ex) {
                System.out.println("Could not create table: documents");
                return SQLCon.SQLRET.FAIL;
            }

        }

        //insert single NULL User in Users table
        private  static void insNULLDoc(Statement __stmt) throws  SQLException{
            Boolean _res;
            String query = "INSERT INTO `kaggle`.`documents` (`docNum`) VALUES ('0');";
            _res = __stmt.execute(query);

        }
        //fills the users table
        private  static void fillDocs(Statement __stmt) throws  SQLException{
            Boolean _res;
            String query = "insert into documents (docNum) \n" +
                    "\n" +
                    "SELECT document_id  FROM kaggle.documents_categories\n" +
                    "union\n" +
                    "SELECT document_id  FROM kaggle.documents_entities\n" +
                    "union\n" +
                    "SELECT document_id  FROM kaggle.documents_meta\n" +
                    "union\n" +
                    "SELECT document_id  FROM kaggle.documents_topics\n" +
                    ";";
            _res = __stmt.execute(query);

        }


        //sets confidence levels to double
        private static void ChangeConfType(Statement __stmt) throws SQLException{
            Boolean _res;
            String query = "ALTER TABLE `kaggle`.`documents_categories` \n" +
                    "CHANGE COLUMN `confidence_level` `confidence_level` DOUBLE NOT NULL ;\n";
            _res = __stmt.execute(query);

            query = "ALTER TABLE `kaggle`.`documents_entities` \n" +
                    "CHANGE COLUMN `confidence_level` `confidence_level` DOUBLE NOT NULL ;\n";
            _res = __stmt.execute(query);

            query = "ALTER TABLE `kaggle`.`documents_topics` \n" +
                    "CHANGE COLUMN `confidence_level` `confidence_level` DOUBLE NOT NULL ;\n";
            _res = __stmt.execute(query);
        }


}
