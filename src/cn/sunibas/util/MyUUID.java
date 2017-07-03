package cn.sunibas.util;

import java.util.Date;
import java.util.Random;

/**
 * Created by IBAS on 2017/2/6.
 * 这里自定义了两种uuid，都是一时间戳作为uuid的一部分
 * 其中一种使用随机数，使同一时间内的uuid编码尽可能不相同，理论上是一个时间戳内的uuid编码有 36^(len - 13)个
 * 另一种使用增长的数值，理论上一个时间戳内的uuid编码有10^(nextUUIDLen - 13)个
 */
public class MyUUID {

    //字符集，项目运营时可适当修改
    private static char[] charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    //字符集的大小
    private static int len = charset.length;
    //随机对象
    private static Random random = new Random();
    //默认uuid长度
    private static int defaultLen = 32;
    //默认最大的uuid长度
    private static int maxLen = 64;
    //用于顺序uuid中的数值增长记录
    private static long recordA = 0,    //一个是18位
                        recordB = 0,
                        recordC = 0,
                        longLen = 1000000000000000000L,
                        tarMod = 0;
    //顺序uuid中的不规则部分的长度
    private  static int tarLen = 0;
    //顺序uuid的长度
    private static int nextUUIDLen = 0;

    //设置顺序uuid的长度，并且只能设置一次
    public static void setNextUUIDLen(int len) {
        if (nextUUIDLen != 0) {
            try {
                throw new Exception("长度只能设置一次，保证运行过程中的uuid编码长度一直便于计算下一个uuid编码。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (len < 13 || len > 64) {
            tarLen = 10;
            nextUUIDLen = 32;
        } else {
            nextUUIDLen = len;
            tarMod = (long)Math.pow(10,(double)((len - 13) % 18));
            if (tarMod == 0) {
                tarMod = tarMod;
            }
            tarLen = len - 13;
        }
    }

    //默认方法，返回32为的uuid
    public static String getMyUUID() {
        return getMyUUID(32);
    }

    //通过参数len要求返回的uuid位数
    public static String getMyUUID(int len){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (len <= 13) {
            len = defaultLen;
        }
        if (len > 64) {
            len = 64;
        }
        getTimeUUID(sb);
        for (i = 13;i < len;i++) {
            sb.append(charset[random.nextInt(len)]);
        }
        return sb.toString();
    }

    //获取一个下一个顺序uuid
    public static String getNexUUID() {
        if (nextUUIDLen == 0) {
            setNextUUIDLen(-1);
        }
        StringBuilder sb = new StringBuilder();
        getTimeUUID(sb);
        getNextLong(nextUUIDLen - 13, sb);
        return sb.toString();
    }

    //获取一个下一个数字
    private static void getNextLong(int len,StringBuilder sb) {
        if (len < 18) {
            recordA++;
            recordA %= tarMod;
            sb.append(NumberExt.Long2String(recordA, tarLen));
        } else if (len < 360) {
            recordA++;
            if (recordA > longLen) {
                recordB++;
                recordA %= longLen;
                if (recordB > tarMod) {
                    recordB %= tarMod;
                }
            }
            sb.append(NumberExt.Long2String(recordB, tarLen));
            sb.append(NumberExt.Long2String(recordA, 18));
        } else {
            recordA++;
            if (recordA > longLen) {
                recordB++;
                recordA %= longLen;
                if (recordB > longLen) {
                    recordC++;
                    recordB %= longLen;
                    if (recordC > tarMod) {
                        recordC %= tarMod;
                    }
                }
            }
            sb.append(NumberExt.Long2String(recordC, tarLen));
            sb.append(NumberExt.Long2String(recordB, 18));
            sb.append(NumberExt.Long2String(recordA, 18));
        }
    }

    private static void getTimeUUID(StringBuilder sb) {
        int i;
        long curTime = (new Date()).getTime();
        for (i = 0;i < 13;i++) {
            sb.append(charset[(int)Math.floorMod(curTime,10)]);
            curTime /= 10;
        }
    }
}
