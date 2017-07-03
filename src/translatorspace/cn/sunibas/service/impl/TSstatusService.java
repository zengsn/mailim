package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSstatusDao;
import cn.sunibas.entity.TSstatus;
import cn.sunibas.redis.RedisCacheUtil;
import cn.sunibas.service.ITSstatusService;
import cn.sunibas.util.OPTSTextPart;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TSstatusService implements ITSstatusService {

    //注入dao
    private ITSstatusDao tsStatusDao;
    public void setTsStatusDao(ITSstatusDao itSstatusDao) {
        this.tsStatusDao = itSstatusDao;
    }

    //注入redisCacheUtil
    private RedisCacheUtil redisCacheUtil;
    public void setRedisCacheUtil(RedisCacheUtil redisCacheUtil) {
        this.redisCacheUtil = redisCacheUtil;
    }

    @Override
    public void createStatus(TSstatus tSstatus) {
        redisCacheUtil.setCacheObject(OPTSTextPart.encipher(tSstatus),tSstatus.getUuid());
        tsStatusDao.save(tSstatus);
    }

    @Override
    public void deleteStatus(TSstatus tSstatus) {
        tsStatusDao.delete(tSstatus);
    }

    @Override
    public TSstatus getStatus(String uuid) {
        TSstatus tSstatus = tsStatusDao.findByUuid(uuid);
        redisCacheUtil.setCacheObject(tSstatus.getUuid(),tSstatus.getTSkidid());
        return tSstatus;
    }

    @Override
    public List<TSstatus> getStatusByTsuuidAndIndex(String tsuuid, int index) {
        return tsStatusDao.findByTSuuidAndIndex(tsuuid, index);
    }

    @Override
    public TSstatus getFirstByTSuuidAndIndex(String tsuuid, int index) {
        return tsStatusDao.getFirstByTSuuidAndIndex(tsuuid,index);
    }

    @Override
    public int updateMarkByTssuuid(int mark, String tssuuid) {
        return tsStatusDao.updateMarkByTSsuuid(mark,tssuuid);
    }
}
