package cn.com.dj.util;

import java.util.Date;

/**
 * Created by dujiang02 on 17/7/23.
 */
public class DateUtils {

    /**
     * 倒数第几秒
     * @param second
     * @return
     */
    public static Date bottomSecond(int second){
        long time = System.currentTimeMillis() - second * 1000;
        return new Date(time);
    }
}
