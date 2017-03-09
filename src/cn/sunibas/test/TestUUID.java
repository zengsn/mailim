package cn.sunibas.test;

import cn.sunibas.util.MyUUID;
import org.junit.Test;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2017/2/6.
 */
public class TestUUID {
    @Test
    public void testOne(){
        UUID uuid = new UUID(32,16);
        System.out.println(uuid.getLeastSignificantBits());
    }

    @Test
    public void testRandom() {
        Random random = new Random();
        for (int i = 0;i < 10;i++)
            System.out.println(random.nextInt(2));
    }

    @Test
    public void testDate(){
        Date date = new Date();
        System.out.println(date.getTime());
        System.out.println(new Date().getTime());
    }

    @Test
    public void testLongMod() {
        long t = (new Date()).getTime();
        System.out.println(t);
        while (t > 0) {
            System.out.print(Math.floorMod(t,10));
            t /= 10;
        }
        System.out.println("\r\nOver");
    }

    @Test
    public void testMyUUID() {
        System.out.println(MyUUID.getMyUUID(32));
        System.out.println(MyUUID.getMyUUID(32));
        System.out.println(MyUUID.getMyUUID(32));
        System.out.println(MyUUID.getMyUUID(32));
        System.out.println(MyUUID.getMyUUID(32));
        System.out.println(MyUUID.getMyUUID(32));
        System.out.println(MyUUID.getMyUUID(32));
    }

    @Test
    public void testMaxLong() {
        System.out.println(Long.MAX_VALUE);
        //9223 37203 68547 75807
    }

    @Test
    public void Long2String() {
        long a = 123;
        System.out.println(Long.toString(a,10));
    }

    @Test
    public void testNextUUID() {
        MyUUID.setNextUUIDLen(15);
        for (int i = 0;i < 150;i++) {
            System.out.println(MyUUID.getNexUUID());
        }
    }

    @Test
    public void testGetMyUUID() {
        System.out.println(MyUUID.getMyUUID());
    }
}
