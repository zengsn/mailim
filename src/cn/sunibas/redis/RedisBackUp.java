package cn.sunibas.redis;

/**
 * Created by sunbing on 17-3-4.
 */

import cn.sunibas.entity.TSMark;
import cn.sunibas.redis.staticData.KeepDownChange;
import cn.sunibas.redis.staticData.TSuuidType;
import cn.sunibas.service.ITSMarkService;
import cn.sunibas.service.ITSstatusService;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis 定时写回数据库
 * 这里使用子线程定时完成操作
 * 不知道会有什么不妥
 * 但是暂时使用的是hibernate框架
 * 不知道如何配置
 */
public class RedisBackUp {
    private static RedisCacheUtil redisCacheUtil;
    private static ITSstatusService itSstatusService;
    private static ITSMarkService itsMarkService;
    public static Thread bakThread;
    private static boolean isRunning = false;

    public void setRedisCacheUtil(RedisCacheUtil redisCacheUtil) {
        this.redisCacheUtil = redisCacheUtil;

    }

    public void setItSstatusService(ITSstatusService itSstatusService) {
        this.itSstatusService = itSstatusService;
    }

    public void setItsMarkService(ITSMarkService itsMarkService) {
        this.itsMarkService = itsMarkService;
    }

    //这里通过一个线程来完成，但是现在不写
    public void init() {
        if (isRunning) {
            System.out.println("isRunning is true");
            return ;
        }
        System.out.println("isRunning is false");
        isRunning = true;
        bakThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> mark_list = redisCacheUtil.getCacheList(KeepDownChange.list_markChange);
                List<String> status_list = redisCacheUtil.getCacheList(KeepDownChange.list_statusChange);
                if (mark_list != null) {
                    Iterator<String> mark_ite = mark_list.iterator();
                    while (mark_ite.hasNext()) {
                        //HashOperations<String tssuuid,String kidid,T mark>
                        String tssuuid = mark_ite.next();
                        Map<String,String> map = null;
                        try {
                            map = redisCacheUtil.getCacheMap(TSuuidType.mark + tssuuid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Set<String> keys = map.keySet();
                        Iterator<String> key = keys.iterator();
                        while (key.hasNext()) {
                            String k = key.next();
                            int mark = Integer.valueOf(map.get(k));
                            TSMark tsMark = new TSMark();
                            tsMark.setTSSuuid(tssuuid);
                            tsMark.setTSKuuid(k);
                            tsMark.setMark(mark);
                            itsMarkService.insert(tsMark);
                        }
                    }
                }
                if (status_list != null) {
                    Iterator<String> status_ite= status_list.iterator();
                    while (status_ite.hasNext()) {
                        //HashOperations<String tagUrl,String tssuuid,T mark>
                        String tagUrl = status_ite.next();
                        Map<String,String> map = redisCacheUtil.getCacheMap(TSuuidType.status + tagUrl);
                        Set<String> keys = map.keySet();
                        Iterator<String> key = keys.iterator();
                        while (key.hasNext()) {
                            String tssuuid = key.next();
                            int mark = Integer.valueOf(map.get(tssuuid));
                            itSstatusService.updateMarkByTssuuid(mark,tssuuid);
                        }
                    }
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("bak once");
                    bakThread.run();
                    try {
                        //Thread.sleep(24 * 3600 * 1000);
                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
