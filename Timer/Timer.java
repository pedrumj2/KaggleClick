package Timer;


/**
 * Created by Pedrum on 12/24/2016.
 */
import java.util.Date;

public class Timer {
    private static Date date;
    private static long startTime;
    private static long endTime;
    public static void start(){
        date = new Date();
        startTime = date.getTime();
    }

    public static void end(String __print){
        double _timeDiff;
        date = new Date();
        endTime = date.getTime();
        _timeDiff = endTime - startTime;
        _timeDiff = _timeDiff /1000;
        System.out.println(__print  + _timeDiff);
    }
}

