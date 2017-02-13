package SetUpPackage;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pedrum on 12/23/2016.
 */
public class General {

    //adjusts the data type of some columns.ALso adds indexes to some columns
    public static void dataTypeAdjust(Statement __stmt) throws SQLException {

        Utility.changeColumnType(__stmt, "clicks_train", "display_id", "INT");
        Utility.changeColumnType(__stmt, "clicks_train", "ad_id", "INT");
        Utility.changeColumnType(__stmt, "clicks_train", "clicked", "INT");
        Utility.changeColumnType(__stmt, "events", "display_id", "INT");
        Utility.changeColumnType(__stmt, "events", "timestamp", "INT");
        Utility.changeColumnType(__stmt, "events", "platform", "INT");
        Utility.changeColumnType(__stmt, "promoted_content", "ad_id", "INT");
     //   Utility.changeColumnType(__stmt, "page_views_sample", "timestamp", "INT");
    }

    //adjusts the data type of some columns.ALso adds indexes to some columns
    public static void addIndices(Statement __stmt) throws SQLException {
        Utility.addIndex(__stmt, "promoted_content", "INDEX1", "ad_id");
        Utility.addIndex(__stmt, "clicks_train", "INDEX1", "ad_id");
        Utility.addIndex(__stmt, "events", "INDEX1", "display_id");
        Utility.addIndex(__stmt, "clicks_train", "INDEX2", "display_id");
        Utility.addIndex(__stmt, "events", "INDEX2", "location");
        Utility.addIndex(__stmt, "events", "INDEX3", "document");
    }

    public static void SetUp(Statement __stmt) throws  SQLException{
        /*SetUpPublisher(__stmt);
        SetUpPlatform(__stmt);
        SetUpTrafficSource(__stmt);
        SetUpAdvertiser(__stmt);
        SetUpCampaign(__stmt);
        SetUpCat(__stmt);
        SetUpEnt(__stmt);
        SetUpMeta(__stmt);
        SetUpTopic(__stmt);*/
        SetUpPlatformEvents(__stmt);

    }


    private static void SetUpPublisher(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "publisher_id";
        catSetUp.currentTable = "documents_meta";
        catSetUp.currentTableNewColumn = "publisher";
        catSetUp.newTableColumn = "publisherNum";
        catSetUp.newTable = "publishers";
        catSetUp.SetUp(__stmt, "INT");
    }
    private static void SetUpPlatformEvents(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "platform";
        catSetUp.currentTable = "events";
        catSetUp.currentTableNewColumn = "platform2";
        catSetUp.newTableColumn = "platformNum";
        catSetUp.newTable = "platforms";
        catSetUp.SetUpColumn(__stmt);
    }
    private static void SetUpPlatform(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "platform";
        catSetUp.currentTable = "page_views_sample";
        catSetUp.currentTableNewColumn = "platform2";
        catSetUp.newTableColumn = "platformNum";
        catSetUp.newTable = "platforms";
        catSetUp.SetUp(__stmt, "INT");
    }

    private static void SetUpTrafficSource(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "traffic_source";
        catSetUp.currentTable = "page_views_sample";
        catSetUp.currentTableNewColumn = "trafficSource";
        catSetUp.newTableColumn = "trafficSourceNum";
        catSetUp.newTable = "trafficSource";
        catSetUp.SetUp(__stmt, "INT");
    }

    private static void SetUpAdvertiser(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "advertiser_id";
        catSetUp.currentTable = "promoted_content";
        catSetUp.currentTableNewColumn = "advertiser";
        catSetUp.newTableColumn = "advertiserNum";
        catSetUp.newTable = "advertisers";
        catSetUp.SetUp(__stmt, "INT");
    }

    private static void SetUpCampaign(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "campaign_id";
        catSetUp.currentTable = "promoted_content";
        catSetUp.currentTableNewColumn = "campaign";
        catSetUp.newTableColumn = "campaignNum";
        catSetUp.newTable = "campaigns";
        catSetUp.SetUp(__stmt, "INT");
    }


    private static void SetUpCat(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "category_id";
        catSetUp.currentTable = "documents_categories";
        catSetUp.currentTableNewColumn = "category";
        catSetUp.newTableColumn = "catNum";
        catSetUp.newTable = "categories";
        catSetUp.SetUp(__stmt, "INT");
    }


    private static void SetUpEnt(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "entity_id";
        catSetUp.currentTable = "documents_entities";
        catSetUp.currentTableNewColumn = "entity";
        catSetUp.newTableColumn = "entNum";
        catSetUp.newTable = "entities";
        catSetUp.SetUp(__stmt, "CHAR(30)");
    }

    private static void SetUpMeta(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "source_id";
        catSetUp.currentTable = "documents_meta";
        catSetUp.currentTableNewColumn = "source";
        catSetUp.newTableColumn = "sourceNum";
        catSetUp.newTable = "sources";
        catSetUp.SetUp(__stmt, "INT");
    }


    private static void SetUpTopic(Statement __stmt) throws SQLException{
        Utility catSetUp;
        catSetUp = new Utility();
        catSetUp.currentTableColumn = "topic_id";
        catSetUp.currentTable = "documents_topics";
        catSetUp.currentTableNewColumn = "topic";
        catSetUp.newTableColumn = "topicNum";
        catSetUp.newTable = "topics";
        catSetUp.SetUp(__stmt, "INT");
    }

}
