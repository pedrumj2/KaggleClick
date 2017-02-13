import SQLConPackage.SQLCon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by Pedrum on 12/23/2016.
 */
public class Document {
    public List<Integer> categories;
    public List<Double> categoryConf;
    public List<Integer> topics;
    public List<Double> topicConf;
    public List<Integer> entities;
    public List<Double> entityConf;
    public int documentID;
    public int publisher;
    public int source;
    public int timestamp;
    public int Loc1;
    public int Loc2;
    public int Loc3;
    public int platform;
    public int trafficSource;

    public Document(int __documentID) throws SQLException{
        categories = new ArrayList<Integer>();
        categoryConf = new ArrayList<Double>();
        topics = new ArrayList<Integer>();
        topicConf = new ArrayList<Double>();
        entities = new ArrayList<Integer>();
        entityConf = new ArrayList<Double>();
        documentID = __documentID;


    }

    //this is called for current document
    public Document(int __documentID, Statement __stmt, int __timeStamp) throws SQLException{
        categories = new ArrayList<Integer>();
        categoryConf = new ArrayList<Double>();
        topics = new ArrayList<Integer>();
        topicConf = new ArrayList<Double>();
        entities = new ArrayList<Integer>();
        entityConf = new ArrayList<Double>();
        documentID = __documentID;
        SetData(__stmt);
        timestamp = __timeStamp;


    }



    public void SetData(Statement __stmt) throws SQLException{
        SQLCon sqlCon;
        sqlCon = new SQLCon();


        GetCategories(sqlCon.stmt);
        GetEntities(sqlCon.stmt);
        GetTopics(sqlCon.stmt);
        sqlCon.close();

    }


    public  void GetCategories(Statement __stmt) throws SQLException {
        ResultSet _res;
        String query = "SELECT * FROM kaggle.documents_categories\n" +
                "where document = " + documentID + ";";
        _res = __stmt.executeQuery(query);
        while (_res.next()){
            categories.add(_res.getInt("category"));
            categoryConf.add(_res.getDouble("confidence_level"));
        }

    }

    public  void GetEntities(Statement __stmt) throws SQLException {
        ResultSet _res;
        String query = "SELECT * FROM kaggle.documents_entities\n" +
                "where document = " + documentID + ";";
        _res = __stmt.executeQuery(query);
        while (_res.next()){
            entities.add(_res.getInt("entity"));
            entityConf.add(_res.getDouble("confidence_level"));
        }

    }


    public  void GetTopics(Statement __stmt) throws SQLException {
        ResultSet _res;
        String query = "SELECT * FROM kaggle.documents_topics\n" +
                "where document = " + documentID + ";";
        _res = __stmt.executeQuery(query);
        while (_res.next()){
            topics.add(_res.getInt("topic"));
            topicConf.add(_res.getDouble("confidence_level"));
        }

    }
}
