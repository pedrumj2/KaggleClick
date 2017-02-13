import SQLConPackage.SQLCon;
import Timer.Timer;

public class Main {
    public static SQLCon sqlCon;
    public static void main(String[] args) {
        try{
            sqlCon = new SQLCon();
         // SetUpPackage.Main.RunSetUp(sqlCon.stmt);
Timer.start();
          //TrainEvent.CreateTraining(sqlCon.stmt);
         TrainPrevViews.Run(sqlCon.stmt, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            sqlCon.close();
            Timer.end("run from " + args[0] + " to " + args[1] + ": ");
        }
        catch(java.sql.SQLException ex) {

            System.out.println(ex);

        }



    }

    /**
     * Created by Pedrum on 12/23/2016.
     */
    public static class TrainPreviousViews {
    }
}
