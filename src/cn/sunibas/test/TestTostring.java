package cn.sunibas.test;

import cn.sunibas.entity.TSkid;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/13.
 */
public class TestTostring {
    @Test
    public void testGetFieldName() throws ClassNotFoundException, IllegalAccessException {
        TSkid tSkid = new TSkid();
        tSkid.setCreateTime(new Date());
        tSkid.setId("1");
        tSkid.setChecked(1);
        tSkid.setName("ibas");
        tSkid.setPwd("ibas");
        tSkid.setRole(2);
        System.out.println(tSkid.toString());
        //ForDebug.Object2String(tSkid,'\n');
//        Field[] f = tSkid.getClass().getDeclaredFields();
//        for (int i = 0;i < f.length;i++) {
//            f[i].setAccessible(true);
//            System.out.println(i + "\t" + f[i].getName() + "\t" + f[i].get(tSkid));
//        }
    }
}
