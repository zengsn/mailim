package cn.sunibas.redis;

/**
 * Created by sunbing on 17-3-4.
 */

import cn.sunibas.action.retObject.abstractClass.ReturnStatus;
import cn.sunibas.entity.TSMark;
import cn.sunibas.entity.TSstatus;
import cn.sunibas.redis.staticData.KeepDownChange;
import cn.sunibas.redis.staticData.TSuuidType;
import cn.sunibas.service.ITSMarkService;
import cn.sunibas.service.ITSstatusService;
import cn.sunibas.util.CreateNewMark;
import cn.sunibas.util.OPTSTextPart;
import cn.sunibas.util.PackageCharsetDetector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 大致流程为，查询redis中是否有缓存，如果有则返回，没有则进行数据库查询后放入缓存并返回
* */
public class RedisFroTSmark {
    private RedisCacheUtil redisCacheUtil;
    private ITSMarkService tsMarkService;
    private ITSstatusService itSstatusService;

    public void setRedisCacheUtil(RedisCacheUtil redisCacheUtil) {
        this.redisCacheUtil = redisCacheUtil;
    }

    public void setTsMarkService(ITSMarkService tsMarkService) {
        this.tsMarkService = tsMarkService;
    }

    public void setItSstatusService(ITSstatusService itSstatusService) {
        this.itSstatusService = itSstatusService;
    }

    ///////////////////////////以下为mark op////////////////////////////////////////////////////

    /**
     * 获取评分信息(一个小译对一个文稿碎片的评分信息)
     * (理论上不应该调用到该函数)
     * */
    public String getOneMark(String tssuuid,String kidid) {
        Map<String,String> map = getMark(tssuuid);
        if (map.containsKey(kidid)) {
            return map.get(kidid);
        } else {
            return null;
        }
    }

    /**
     * 这里在程序中只要运行一次就存入redis缓存中了
     * */
     public Map<String,String> getMark(String tssuuid) {
        Map<String,String> map = redisCacheUtil.getCacheMap(TSuuidType.mark + tssuuid);
        if (map.size() == 0) {
            //从数据库中查找并放入缓存
            List<TSMark> list = tsMarkService.getList(tssuuid);
            int size = list.size();
            map = new HashMap<String, String>();
            for (int i = 0;i < size;i++) {
                TSMark tsMark = list.get(i);
                map.put(tsMark.getTSKuuid(),tsMark.getMark() + "");
            }
            redisCacheUtil.setCacheMap(TSuuidType.mark + tssuuid,map);
        }
        return map;
    }

    /**
     * 处理一个新增的评分信息（一个小译对一个文稿碎片的评分信息）
     * 存入内容同TSMark对象中的内容
     *
     * 返回值
     *  0   插入成功
     *  1   插入失败（对象已经存在）
     *  2   插入出错
     * */
    public int newMark(String tagUrl,String tssuuid,String kidid,int mark) {
        int status = insertMark(tssuuid,kidid,mark);
        if (status == ReturnStatus.Success) {
            //插入成功将文稿碎片的评分进行修改
            //这里需要将修改添加到日志中，以便写回数据库
            redisCacheUtil.insertIntoList(KeepDownChange.list_markChange,tssuuid);
            if (setOneTsstatus(tagUrl,tssuuid,mark,false) == null) {
                //这里发生将有点难以处理
                return ReturnStatus.OccureError;
            }
            redisCacheUtil.insertIntoList(KeepDownChange.list_statusChange,tagUrl);
        }
        return status;
    }

    /**
     * 插入一个小译对一个文稿碎片的评分信息，插入后要更新评分
     * 返回值
     *  0   插入成功
     *  1   插入失败（对象已经存在）
     *
     * */
    private int insertMark(String tssuuid,String kidid,int mark) {
        //获取该tssuuid下的所有评分情况，查看是否已经完成评分，如果完成则返回错误
        //Map<String tkidid,TSMark mark>
        String markStr = mark + "";
        Map<String,String> map = redisCacheUtil.getCacheMap(TSuuidType.mark + tssuuid);
        if (map.size() == 0) {
                map = getMark(tssuuid);
        }
        if (map.size() == 0) {
            redisCacheUtil.insertIntoMap(TSuuidType.mark + tssuuid, kidid, markStr);
            return ReturnStatus.Success;
        }
        if (map.containsKey(kidid)) {
            return ReturnStatus.Fail;
        } else {
            redisCacheUtil.insertIntoMap(TSuuidType.mark + tssuuid,kidid, markStr);
            return ReturnStatus.Success;
        }
    }

    ///////////////////////////以下为status op////////////////////////////////////////////////////

    /**
     * 这里是通过tagUrl来查找到大量的文稿碎片
     * 所以这里的流程不同于上面的流程
     * 1）这里第一步是检查tagUrl是否记录在redis缓存中
     * 2-1）如果在缓存中则直接将内容返回
     * 2-2）如果不在缓存中则查询数据库后记入缓存
     *
     * 这里缓存内容数据结构应该是
     * tagUrl,Map<Tssuuid,TsstatusTMP>
     * */
    public Map<String,String> getTSstatusFromRedis(String tagUrl) {
        //Map<String tssuuid,TSstatusTMP>
        Map<String,String> map = redisCacheUtil.getCacheMap(TSuuidType.status + tagUrl);
        if (map.size() == 0) {
            TSstatus tSstatus = new TSstatus();
            try {
                tSstatus = OPTSTextPart.decrypt(tagUrl,tSstatus);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            List<TSstatus> list = itSstatusService.getStatusByTsuuidAndIndex(tSstatus.getTSuuid(),tSstatus.getTSindex());
            int size = list.size();
            map = new HashMap<String, String>();
            for (int i = 0;i < size;i++) {
                tSstatus = list.get(i);
                map.put(tSstatus.getUuid(), tSstatus.getMark() + "");
                redisCacheUtil.setCacheObject(tSstatus.getUuid(), tSstatus.getTSkidid());
            }
            redisCacheUtil.setCacheMap(TSuuidType.status + tagUrl,map);
        }
        return map;
    }

    /**
     * 单独在redis缓存中查询一个文稿碎片的记录
     * */
    public String getOneStatusFromRedis(String tagUrl,String tssuuid) {
        Map<String,String> map = getTSstatusFromRedis(tagUrl);
        if (map.size() == 0) {
            return null;
        } else {
            if (map.containsKey(tssuuid)) {
                return map.get(tssuuid);
            } else {
                return null;
            }
        }
    }

    /**
     * 单独在redis缓存中修改一个文稿碎片的记录
     * */
    public String setOneTsstatus(String tagUrl,String tssuuid,int mark,boolean isAdd) {
        Map<String,String> map = getTSstatusFromRedis(tagUrl);
        if (map.size() == 0) {
            redisCacheUtil.insertIntoMap(TSuuidType.status + tagUrl,tssuuid,mark + "");
            return null;
        } else {
            if (map.containsKey(tssuuid)) {
                if (isAdd) {
                    return null;
                }
                String markStr = map.get(tssuuid);
                mark = CreateNewMark.newMark(Integer.valueOf(markStr),mark);
                redisCacheUtil.insertIntoMap(TSuuidType.status + tagUrl,tssuuid,mark + "");
                return markStr;
            } else {
                redisCacheUtil.insertIntoMap(TSuuidType.status + tagUrl,tssuuid,mark + "");
                return "";
            }
        }
    }

    /**
     * 为TranslateChipAction
     * 默认是不能返回nul的
     * */
    public String newStatusInRedis(String tagUrl,String tssuuid,int mark) {
        Map<String,String> map = getTSstatusFromRedis(tagUrl);
        if (map.size() == 0) {
            redisCacheUtil.insertIntoMap(TSuuidType.mark + tagUrl,tssuuid,mark + "");
            return mark + "";
        } else {
            if (map.containsKey(tssuuid)) {
                return null;
            } else {
                redisCacheUtil.insertIntoMap(TSuuidType.mark + tagUrl,tssuuid,mark + "");
                return mark + "";
            }
        }
    }

    public String getKididFromTssuuid(String tssuuid) {
        return redisCacheUtil.getCacheObject(tssuuid);
    }

    public void addToRemove(String tssuuid) {
        redisCacheUtil.insertIntoList(KeepDownChange.list_statusRemove,tssuuid);
    }

    public void inserNewStatue(TSstatus tSstatus) {

    }

    public void saveTssuuidAndKididInCache(String tssuuid,String kidid) {
        redisCacheUtil.setCacheObject(tssuuid, kidid);
    }

    /**
     * 检查用户是否已经上传过文稿碎片的翻译
     * */
    public boolean check(String tagUrl,String kidid) {
        Iterator<String> iterator = getTSstatusFromRedis(tagUrl).keySet().iterator();
        while (iterator.hasNext()) {
            String tssuuid = iterator.next();
            String tkidid = getKididFromTssuuid(tssuuid);
            if (tkidid != null) {
                if (tkidid.equals(kidid)) {
                    return true;
                }
            }
        }
        return false;
    }
  }

/**
 * 这里再一次详述一次这里涉及到的两种数据结构的存储方式
 *
 * 1)评分数据结构（这里强调的一点时评分只能进行一次，即一个文稿里一个小译只能有一个评分结果）
 * （这里代码容易混乱，在文稿翻译状态表中，tssuuid是唯一的，但是在评分表中tssuuid和kidid是同时作为主键的，即tssuuid是不唯一的属性）
 * （这里假定的情景是对T_MARK表进行操作，因此存在一点混乱，也因此这里的缓存结构是这个样子）
 *      HashOperations<String tssuuid,String kidid,T mark>
 *      这里记录的是，多个小译对一个文稿翻译结果的评分
 *      设计理由是，当访问到一个文稿碎片时，同时会将翻译的文稿的信息展示出来
 *      通过tssuuid批量获取文稿翻译件的评分信息
 *      这里通过kidid来作为第二主键，区分评分的来源
 *
 * 2)文稿评分数据结构
 *      HashOperations<String tagUrl,String tssuuid,T mark>*
 *      这里的第一主键是tagUrl，原因是在tsstatus表中tagUrl为非主键，但是他们规定了指定文稿碎片
 *      以kidid为第二主键是因为kidid能将tagUrl获取到的翻译结果进行分类，
 *      因为规定了一个小译对一个文稿只能进行一次翻译
 * */

/**
 * 这里要特别区分命名规则，
 * 例如 TSuuidType.status + tagUrl
 *
 * 另外这里出现修改或者添加应该加入记录，以简化批量写回数据库过程
 * */

/**
 * 另外，超级抱歉这里的命名十分混乱
 *
 * 考虑到redis的hmap是不能存储 <String,String,Object>，这里使用<String,String,int>
 * */


/**
 * For debug
 del mark_56140609688411hg6asu2ev3d9p60fs9
 del status_056140609688411hg6asu2ev3d9p60fs9
 del list_markChange
 del list_statusChange




 * */