package cn.sunibas.redis;

import org.springframework.data.redis.core.*;

import java.util.*;

/**
 * Created by sunbing on 17-3-3.
 */
public class RedisCacheUtil {

    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     * @return  缓存的对象
     */
    public <T> ValueOperations<String,T> setCacheObject(String key,T value)
    {

        ValueOperations<String,T> operation = redisTemplate.opsForValue();
        operation.set(key,value);
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     * @param key  缓存键值
     * @param operation
     * @return   缓存键值对应的数据
     */
    public <T> T getCacheObject(String key/*,ValueOperations<String,T> operation*/)
    {
        ValueOperations<String,T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 缓存List数据
     * @param key  缓存的键值
     * @param dataList 待缓存的List数据
     * @return   缓存的对象
     */
    public <T> ListOperations<String, T> setCacheList(String key,List<T> dataList)
    {
        ListOperations listOperation = redisTemplate.opsForList();
        if(null != dataList)
        {
            int size = dataList.size();
            for(int i = 0; i < size ; i ++)
            {

                listOperation.rightPush(key,dataList.get(i));
            }
        }

        return listOperation;
    }
    /**
     * 一次插入一个对象，可以多次插入，不用封装为list对象
     * */
    public<T> ListOperations<String,T> insertIntoList(String key,T data) {
        ListOperations listOperation = redisTemplate.opsForList();
        listOperation.rightPush(key,data);
        return listOperation;
    }

    /**
     * 获得缓存的list对象
     * @param key 缓存的键值
     * @return  缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key)
    {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String,T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for(int i = 0 ; i < size ; i ++)
        {
            dataList.add((T) listOperation.leftPop(key));
        }

        return dataList;
    }
    /**
     * 可以直接获取整个list而不是通过pop将内容打出
     * 没有对象时返回null
     * */
    public<T> List<T> getFromList(String key) {
        ListOperations<String,T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);
        if (size == 0) {
            return listOperation.range(key,0,size);
        } else {
            return null;
        }
    }

    /**
     * 缓存Set
     * @param key  缓存键值
     * @param dataSet 缓存的数据
     * @return   缓存数据的对象
     */
    public <T> BoundSetOperations<String,T> setCacheSet(String key,Set<T> dataSet)
    {
        BoundSetOperations<String,T> setOperation = redisTemplate.boundSetOps(key);
  /*T[] t = (T[]) dataSet.toArray();
    setOperation.add(t);*/


        Iterator<T> it = dataSet.iterator();
        while(it.hasNext())
        {
            setOperation.add(it.next());
        }

        return setOperation;
    }

    /**
     * 获得缓存的set
     * @param key
     * @param operation
     * @return
     */
    public<T> Set<T> getCacheSet(String key/*,BoundSetOperations<String,T> operation*/)
    {
        Set<T> dataSet = new HashSet<T>();
        BoundSetOperations<String,T> operation = redisTemplate.boundSetOps(key);

        Long size = operation.size();
        for(int i = 0 ; i < size ; i++)
        {
            dataSet.add(operation.pop());
        }
        return dataSet;
    }

    /**
     * 缓存Map
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String,String,T> setCacheMap(String key,Map<String,T> dataMap)
    {

        HashOperations hashOperations = redisTemplate.opsForHash();
        if(null != dataMap)
        {

            for (Map.Entry<String, T> entry : dataMap.entrySet()) {

    /*System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); */
                hashOperations.put(key,entry.getKey(),entry.getValue());
            }

        }

        return hashOperations;
    }
    public<T> void insertIntoMap(String key,String mkey,T data) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key,mkey,data);
    }

    /**
     * 获得缓存的Map
     * @param key
     * @param hashOperation
     * @return
     */
    public <T> Map<String,T> getCacheMap(String key/*,HashOperations<String,String,T> hashOperation*/)
    {
        Map<String, T> map = redisTemplate.opsForHash().entries(key);
  /*Map<String, T> map = hashOperation.entries(key);*/
        return map;
    }

    /**
     * 缓存Map
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String,Integer,T> setCacheIntegerMap(String key,Map<Integer,T> dataMap)
    {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if(null != dataMap)
        {

            for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {

    /*System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); */
                hashOperations.put(key,entry.getKey(),entry.getValue());
            }

        }

        return hashOperations;
    }

    /**
     * 获得缓存的Map
     * @param key
     * @param hashOperation
     * @return
     */
    public <T> Map<Integer,T> getCacheIntegerMap(String key/*,HashOperations<String,String,T> hashOperation*/)
    {
        Map<Integer, T> map = redisTemplate.opsForHash().entries(key);
  /*Map<String, T> map = hashOperation.entries(key);*/
        return map;
    }

}
