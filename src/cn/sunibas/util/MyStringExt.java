package cn.sunibas.util;

/**
 * Created by IBAS on 2017/2/17.
 */
public class MyStringExt {

    /**
     * @param str
     * @param to    一定是从0到某一个长度
     * @return
     */
    public static String SbuString(String str,int to) {
        if (str.length() <= to) {
            return str;
        } else {
            return str.substring(0,to) + "...";
        }
    }
}
