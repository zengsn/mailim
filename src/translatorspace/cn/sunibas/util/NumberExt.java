package cn.sunibas.util;

/**
 * Created by IBAS on 2/26/17.
 */
public class NumberExt {
    //用于整型转字符串时位数不足的补零
    private static String intZeroStr = "0000000000";
    private static int intMaxLenght = intZeroStr.length();
    private static int defaultChipFileNameLength = 3;
    public static int getDefaultChipFileNameLength() {
        return defaultChipFileNameLength;
    }

    public static void setDefaultChipFileNameLength(int defaultChipFileNameLength) {
        NumberExt.defaultChipFileNameLength = defaultChipFileNameLength;
    }

    //用于长整型转字符串时位数不足的补零
    private static String longZeroStr = "000000000000000000";
    private static int longMaxLength = longZeroStr.length();

    public static String Int2String(int I) {
        return Int2String(I,defaultChipFileNameLength);
    }
    public static String Int2String(int I, int bit) {
        if (bit > intMaxLenght) {
            try {
                throw new Exception("bit 大于 int 型的最大长度。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String Istr = Integer.toString(I);
        if (Istr.length() < bit) {
            return longZeroStr.substring(0,bit - Istr.length()) + Istr;
        } else {
            return Istr;
        }
    }

    //将长整型转换为指定长度的字符串，不够位数将补零(供其他位置调用)
    public static String Long2String(long L,int bit) {
        if (bit > longMaxLength) {
            try {
                throw new Exception("bit 大于 Long 型的最大长度。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String Lstr = Long.toString(L);
        if (Lstr.length() < bit) {
            return longZeroStr.substring(0,bit - Lstr.length()) + Lstr;
        } else {
            return Lstr;
        }
    }
}
