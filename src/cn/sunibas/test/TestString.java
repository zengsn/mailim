package cn.sunibas.test;

import cn.sunibas.util.MyStringExt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
public class TestString {
    @Test
    public void testSplit() {
        String srcStr = "feaa,";
        String[] s = srcStr.split(",");
        List<String> list = new ArrayList<String>();
        for (int i = 0;i < s.length;i++) {
            list.add(s[i]);
        }
        System.out.println("items = " + s.length);
        System.out.println(list);
    }

    @Test
    public void testSubStr(){
        String str = "ibasbing";
        System.out.println(MyStringExt.SbuString(str,8));
    }

    private int len = 12;
    @Test
    public void testSplit1(){
        String testStr = "123ibasibasibas";
        int strlen = testStr.length();
        int n = Integer.valueOf(testStr.substring(0,strlen - len));
        System.out.println("n = " + n + "\tstr = " + testStr.substring(strlen - len));
    }
}
