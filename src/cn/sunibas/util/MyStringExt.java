package cn.sunibas.util;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> split(String str,String regx){
        String[] ret = str.split(regx);
        List<String> list = new ArrayList<String>();
        for (int i = 0;i < ret.length;i++) {
            if (!ret[i].equals("")) {
                list.add(ret[i]);
            }
        }
        return list;
    }
}
