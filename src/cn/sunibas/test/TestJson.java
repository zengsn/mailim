package cn.sunibas.test;

import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/17.
 */
public class TestJson {

    @Test
    public void testOne(){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        System.out.println(JSONObject.fromObject(list));
    }
}
