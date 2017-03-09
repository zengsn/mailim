package cn.sunibas.action.testRedis;

import cn.sunibas.entity.TSNewText;
import cn.sunibas.entity.TSkid;
import cn.sunibas.redis.RedisCacheUtil;
import cn.sunibas.util.MyUUID;
import cn.sunibas.util.Object2JSON;
import cn.sunibas.util.ObjectType;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.data.redis.core.ListOperations;

import java.util.*;

/**
 * Created by sunbing on 17-3-3.
 */
public class TestRedis extends ActionSupport {
    private RedisCacheUtil redisCacheUtil;

    public void setRedisCacheUtil(RedisCacheUtil redisCacheUtil) {
        this.redisCacheUtil = redisCacheUtil;
    }

    private String key;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void testGetAndSet() {
        redisCacheUtil.setCacheObject(key,value);
        System.out.println(redisCacheUtil.getCacheObject(key));
    }

    public void testSetCacheList() {
        String akey = "setCacheList";
        List<String> list = new ArrayList<String>();
        list.add("ibas");
        list.add("bing");
        list.add("haha");
        ListOperations<String,String> listOperations = redisCacheUtil.setCacheList(akey, list);
        System.out.println(1);
    }

    public void testGetCacheList() {
        String akey = "setCacheList";
        List<String> list = redisCacheUtil.getCacheList(akey);
        System.out.println(list);
    }

    public void testSetObj() {
        TSkid tSkid = new TSkid();
        tSkid.setChecked(1);
        tSkid.setCreateTime(new Date());
        tSkid.setId(MyUUID.getMyUUID());
        tSkid.setName("ibas");
        tSkid.setPwd("ihfae");
        tSkid.setRole(1);
        redisCacheUtil.setCacheObject("kid",tSkid);
        TSkid getKid = redisCacheUtil.getCacheObject("kid");
        System.out.println(getKid);
    }

    public void testSomeInsert() {
        redisCacheUtil.insertIntoList(key,value);
    }

    public void testGetList() {
        List<String> list = redisCacheUtil.getCacheList(key);
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                list,
                ObjectType.Array
        );
    }

    public void testOjbectInsert() {
        TSNewText tsNewText = new TSNewText();
        tsNewText.setUuid(value);
        redisCacheUtil.insertIntoList(key,tsNewText);
    }

    public void testGetListButNotPop() {
        List<String> list = redisCacheUtil.getFromList(key);
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                list,
                ObjectType.Array
        );
    }

    public void testSetInMap() {
        Random random = new Random();
        redisCacheUtil.insertIntoMap(key,value,random.nextInt() + "");
    }

    public void testGetInMap() {
        Map<String,Integer> map = redisCacheUtil.getCacheMap(key);
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                map,
                ObjectType.Object
        );
    }
}
