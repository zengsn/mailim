package mailim.mailim.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zzh on 2017/11/15.
 */
public class DateUtil {
    public static String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_HHMM = "HH:mm";
    public static String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * 获取日期字符串（yyyy-MM-dd）
     * @param date 日期
     * @return 字符串
     */
    public static String DateToString(Date date){
        String time = null;
        if(date != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE);
            time = dateFormat.format(date);
        }
        return time;
    }

    /**
     * 将字符串转换为日期
     * @param dateString 日期字符串
     * @return date
     */
    public static Date StringToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DEFAULT);
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将时间戳转换为字符串
     * @param time 时间戳
     * @return string
     */
    public static String LongToString(Long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DEFAULT);
        dateFormat.setLenient(false);
        return dateFormat.format(time);
    }

    /**
     * 将时间戳转换为字符串
     * @param time 时间戳
     * @return string
     */
    public static Date LongToDate(Long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DEFAULT);
        dateFormat.setLenient(false);
        String str = dateFormat.format(time);
        try {
            return dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取时间（HH:mm）
     * @param date 日期
     * @return 字符串
     */
    public static String getTime(Date date){
        String time = null;
        if(date != null){
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_HHMM);
            time = dateFormat.format(date);
        }
        return time;
    }

    /**
     * 获取时间（时:分）
     * @param dateString 日期字符串
     * @return 字符串
     */
    public static String getTime(String dateString){
        String time;
        Date date = StringToDate(dateString);
        time = getTime(date);
        return time;
    }

    /**
     * 获取时间（时:分）
     * @param timeStamp 时间戳
     * @return 字符串
     */
    public static String getTime(Long timeStamp){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_HHMM);
        dateFormat.setLenient(false);
        return dateFormat.format(timeStamp);
    }

    /**
     *  获取日期字符串
     * @param timeStamp 时间戳
     * @return 字符串
     */
    public static String getDateString(Long timeStamp){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE);
        return dateFormat.format(timeStamp);
    }

    /**
     * 是否是日期字符串
     * @param str 字符串
     * @return 是或否
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess=true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess=false;
        }
        return convertSuccess;
    }



}
