package cn.sunibas.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public class TestList {

    @Test
    public void testToString(){
        List<String> list = new ArrayList<String>();
        for (int i = 0;i < 10;i++) {
            list.add(i + "");
        }
        System.out.println(list.toString());
    }
}
