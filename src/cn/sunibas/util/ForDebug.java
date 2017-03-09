package cn.sunibas.util;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/2/13.
 */
public class ForDebug {

    public static String Object2String(Object o) {
        return Object2String(o,'\t');
    }
    /**
     * ch 为分割符号
     * */
    public static String Object2String(Object o,char ch){
        StringBuilder sb = new StringBuilder();
        Field[] fs = o.getClass().getDeclaredFields();
        for (int i = 0;i < fs.length;i++) {
            fs[i].setAccessible(true);
            try {
                sb.append(fs[i].getName() + " : " + fs[i].get(o) + ch);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }
}
