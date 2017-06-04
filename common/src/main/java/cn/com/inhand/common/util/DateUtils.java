package cn.com.inhand.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期格式化类
 *
 * @author franklin.li
 */
public class DateUtils {

    /**
     * 获取utc零时区的时间戳（10位 second）
     *
     * @return
     */
    public static long getUTC() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT");
        calendar.setTimeZone(tz);
        return calendar.getTimeInMillis() / 1000;
    }

    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone zone = TimeZone.getDefault();
        df.setTimeZone(zone);
        String date = df.format(new Date());
        return date;
    }

    /**
     * @param "GMT+8"
     * @return
     */
    public static String dateFormat(long time, String ID) {
        if (time / 1000000000000l == 0) {
            time *= 1000;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone zone = TimeZone.getTimeZone(ID);
        format.setTimeZone(zone);
        return format.format(time);
    }

    /**
     * 本地默认时区下的时间格式化
     *
     * @return
     */
    public static String dateFormat(long time) {
        if (time / 1000000000000l == 0) {
            time *= 1000;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone zone = TimeZone.getDefault();
        format.setTimeZone(zone);
        return format.format(time);
    }

    /**
     * @param time 标准utc时间（10位）s
     * @return
     */
    public static String dateFormat(long time, String ID, String format) {
        if (time / 1000000000000l == 0) {
            time *= 1000;
        }
        DateFormat df = new SimpleDateFormat(format);
        TimeZone zone = TimeZone.getTimeZone(ID);
        df.setTimeZone(zone);
        return df.format(time);
    }

    public static int getDateByUTC(long utc) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT");
        calendar.setTimeZone(tz);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

	public static Date firstDateOfWeek(Date date) {
		Date date1 = org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
		Calendar calendar = org.apache.commons.lang3.time.DateUtils.toCalendar(date1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return org.apache.commons.lang3.time.DateUtils.addDays(date1, 1 - dayOfWeek);
	}
	
	public static Long getTimeStampByDate(Date date){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long ts=Timestamp.valueOf(sim.format(date)).getTime();
		return ts;
	}
}
