import javax.print.Doc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Pedrum on 12/23/2016.
 */
public class User {
    public List<Document> documents;
    public List<Double> catSimilarity;
    public List<Double> topicSimilarity;
    public List<Double> entitySimilarity;
    public Document currentDocument;
    public double averageCatSimilarity =0 ;
    public double averageTopicSimilarity=0 ;
    public double averageEntitySimilarity=0 ;
    public double averageSourceSimiliarity=0 ;
    public double averagePublisherSimiliarity=0 ;
    public double averageTrafficSourceSimiliarity=0;
    public double averagePlatformSimiliarity=0;
    public double averageT1Similiarity=0 ;
    public double averageT2Similiarity=0 ;
    public double averageT3Similiarity=0 ;

    private int userID;

    public User(int __userID, Statement __stmt, int __currentDocument, int __timeStamp) throws SQLException{
        documents = new ArrayList<Document>();
        userID =__userID;
        currentDocument = new Document(__currentDocument, __stmt, __timeStamp);

        GetDocuments(__stmt);

        catSimilarity  = new ArrayList<Double>();
        topicSimilarity  = new ArrayList<Double>();
        entitySimilarity  = new ArrayList<Double>();

        for (int i = 0; i < documents.size(); i++){
            CalculateSimilarity(i);

        }
        CalculateAverageSimilarity();
    }

    public void InsertSimilarites(Statement __stmt, int __idTempCalc)throws SQLException{
       // for (int i = 0; i < documents.size(); i++){

            InsertAverageSimilarity(__stmt,  __idTempCalc);
        //}
    }
    private void CalculateAverageSimilarity(){
        int _size;
       for (int i = 0; i < documents.size(); i++){
           averageCatSimilarity += catSimilarity.get(i);
           averageTopicSimilarity += topicSimilarity.get(i);
           averageEntitySimilarity += entitySimilarity.get(i);
           if (documents.get(i).trafficSource == currentDocument.trafficSource){
               averageTrafficSourceSimiliarity+=1;
           }
           if (documents.get(i).publisher == currentDocument.publisher){
               averagePublisherSimiliarity+=1;
           }
           if (documents.get(i).platform == currentDocument.platform){
               averagePlatformSimiliarity+=1;
           }
           if (documents.get(i).source == currentDocument.source){
               averageSourceSimiliarity+=1;
           }

       }
        _size = documents.size();
        if (_size == 0){
            _size = 1;
        }
        averageCatSimilarity = averageCatSimilarity/_size;
        averageTopicSimilarity = averageTopicSimilarity/_size;
        averageEntitySimilarity = averageEntitySimilarity/_size;
        averageTrafficSourceSimiliarity = averageTrafficSourceSimiliarity/_size;
        averagePublisherSimiliarity = averagePublisherSimiliarity/_size;
        averagePlatformSimiliarity = averagePlatformSimiliarity/_size;
        averageSourceSimiliarity = averageSourceSimiliarity/_size;
    }



    private void CalculateSimilarity(int i){
        double _catSimilarity;
        double _topicSimilarity;
        double _entitySimilarity;
        _catSimilarity = CalculateListSimilarity(documents.get(i).categories, currentDocument.categories,
                documents.get(i).categoryConf, currentDocument.categoryConf);
        _topicSimilarity = CalculateListSimilarity(documents.get(i).topics, currentDocument.topics,
                documents.get(i).topicConf, currentDocument.topicConf);
        _entitySimilarity = CalculateListSimilarity(documents.get(i).entities, currentDocument.entities,
                documents.get(i).entityConf, currentDocument.entityConf);
        catSimilarity.add(_catSimilarity);
        topicSimilarity.add(_topicSimilarity);
        entitySimilarity.add(_entitySimilarity);

    }
    private double CalculateListSimilarity(List<Integer> _list1, List<Integer> _list2,
                                           List<Double> _conf1, List<Double> _conf2){
        int _index;
        double _output = 0;
        for (int i = 0; i < _list1.size(); i++){
            _index = _list2.indexOf(_list1.get(i));
            if (_index != -1){
                _output += _conf1.get(i)*_conf2.get(_index);
            }

        }
        return _output;
    }

    private void InsertAverageSimilarity(Statement __stmt, int __tempCalcID) throws SQLException{
        Boolean _res;
      //  int _pageViewIndex = i +1;
        String query = "insert into pastViews" + 1 + " (idTempCalc, catSimilarity, source, " +
                "publisher,  topicSimilarity, entitySimilarity, trafficSource, platform)\n" +
                "Values (" + __tempCalcID + ", " + averageCatSimilarity + ", " + averageSourceSimiliarity +
                ", " + averagePublisherSimiliarity +", " +
                averageTopicSimilarity + ", " + averageEntitySimilarity + ", " + averageTrafficSourceSimiliarity +
                ", " +averagePlatformSimiliarity + ");";

        __stmt.execute(query);

    }

    private void InserttDocumentSimilarity(Statement __stmt, int i, int __tempCalcID) throws SQLException{
        Boolean _res;
        int _pageViewIndex = i +1;
        String query = "insert into pastViews" + _pageViewIndex + " (idTempCalc, catSimilarity, source, " +
                "publisher, timestamp, topicSimilarity, entitySimilarity, trafficSource, platform, T1, T2, T3)\n" +
                "Values (" + __tempCalcID + ", " + catSimilarity.get(i) + ", " + documents.get(i).source +
                ", " + documents.get(i).publisher + ", " + documents.get(i).timestamp + ", " +
                topicSimilarity.get(i) + ", " + entitySimilarity.get(i) + ", " + documents.get(i).trafficSource +
                ", " + documents.get(i).platform + ", " + documents.get(i).Loc1 +    ", " + documents.get(i).Loc2 +
                ", " + documents.get(i).Loc3 + ");";

        __stmt.execute(query);

    }
    private void GetDocuments(Statement __stmt) throws SQLException {
        ResultSet _res;
        Document _document;
        String query = "SELECT * FROM kaggle.page_views_sample\n" +
                "inner join locations\n" +
                "on locations.idlocations = page_views_sample.location \n" +
                "inner join documents_meta\n" +
                "on documents_meta.document = page_views_sample.document\n" +
                "where page_views_sample.user = " + userID + " and (page_views_sample.document <> " +
                currentDocument.documentID + " or page_views_sample.timestamp <> " +  currentDocument.timestamp + ");";
        _res = __stmt.executeQuery(query);
        while (_res.next()){
            _document = GetDocument(_res, __stmt);


            documents.add(_document);
        }
    }

    private Document GetDocument(ResultSet __res,Statement __stmt ) throws SQLException{
        int _documentID;
        Document _document;
        _documentID =  __res.getInt("document");
        _document = new Document(_documentID);
        GetOtherDocInfo(__res, _document);
        _document.SetData(__stmt);

        return _document;
    }

    private void GetOtherDocInfo(ResultSet __res, Document __document) throws SQLException{

        __document.timestamp =  __res.getInt("timestamp");
        __document.platform =  __res.getInt("platform2");
        __document.trafficSource =  __res.getInt("trafficSource");
        __document.Loc1 =  __res.getInt("T1");
        __document.Loc2 =  __res.getInt("T2");
        __document.Loc3 =  __res.getInt("T3");
        __document.source =  __res.getInt("source");
        __document.publisher =  __res.getInt("publisher");
    }




}
