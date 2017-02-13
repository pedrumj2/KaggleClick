import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pedrum on 12/23/2016.
 */


    /**
     * Created by Pedrum on 12/23/2016.
     */
    public class TrainPrevViews {


        public static void Run(Statement __stmt, int _start, int _end) throws SQLException {
            int _row = _start-1;
            RowKey _rowKey;
            int _prevDisplay_id =0;
            User _user = null;
            _rowKey = GetRowKeys(__stmt, _row);

            while ((_rowKey != null) && (_row < _end -1)) {
                _rowKey = GetRowKeys(__stmt, _row);
                //normally there are multiple ads for each display id. We don't
                //want to loop seperaitly for each of these rows.
                if (_rowKey.display_id != _prevDisplay_id) {
                    _user = new User(_rowKey.user, __stmt, _rowKey.document, _rowKey.timeStamp);
                    _prevDisplay_id = _rowKey.display_id;
                }
                _row++;
                if (_row == 164){
                    _row =164;
                }

                System.out.println(_row);
                _user.InsertSimilarites(__stmt,_rowKey.tempCalcID );



            }

        }


        private static RowKey GetRowKeys(Statement __stmt, int __offset) throws SQLException {
            ResultSet _res;
            RowKey _rowKey;
            _rowKey = new RowKey();
            int _documentID = 0;
            String query = "SELECT * FROM kaggle.tempCalc1 order by idtempCalc1 limit " + __offset + ", 1 ";
            _res = __stmt.executeQuery(query);
            if (_res.next()) {
                _rowKey.document = _res.getInt("document");
                _rowKey.user = _res.getInt("user");
                _rowKey.display_id = _res.getInt("display_id");
                _rowKey.tempCalcID = _res.getInt("idtempCalc1");
                _rowKey.timeStamp = _res.getInt("timestamp");
                return _rowKey;
            } else {
                return null;
            }
        }
    }

