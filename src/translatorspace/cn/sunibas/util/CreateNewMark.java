package cn.sunibas.util;

/**
 * Created by sunbing on 17-3-4.
 */
public class CreateNewMark {
    /**
     * 评分机制
     * 通过旧评分和打分将评分进行修改
     * */
    public static int newMark(int oldMark,int mark) {
        return oldMark + mark;
    }
}
