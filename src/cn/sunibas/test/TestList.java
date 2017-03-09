package cn.sunibas.test;

import cn.sunibas.entity.TSMark;
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

    @Test
    public void testContain() {
        List<TSMark> list = new ArrayList<TSMark>();
        TSMark tsMark = new TSMark();
        tsMark.setTSSuuid("123");
        tsMark.setTSKuuid("123");
        tsMark.setMark(123);
        list.add(tsMark);
        TSMark tsMark1 = new TSMark();
        tsMark1.setTSSuuid("123");
        tsMark1.setTSKuuid("123");
        tsMark1.setMark(123);
        if (list.contains(tsMark1)) {
            System.out.print("have");
        } else {
            System.out.print("have not");
        }
    }
}
