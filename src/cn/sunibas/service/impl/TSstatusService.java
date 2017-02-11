package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSstatusDao;
import cn.sunibas.entity.TSstatus;
import cn.sunibas.service.ITSstatusService;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TSstatusService implements ITSstatusService {

    //注入dao
    private ITSstatusDao tsStatusDao;
    public void setTsStatusDao(ITSstatusDao itSstatusDao) {
        this.tsStatusDao = itSstatusDao;
    }

    @Override
    public void createStatus(TSstatus tSstatus) {
        tsStatusDao.save(tSstatus);
    }

    @Override
    public void deleteStatus(TSstatus tSstatus) {
        tsStatusDao.delete(tSstatus);
    }

    @Override
    public TSstatus getStatus(String uuid) {
        return tsStatusDao.findByUuid(uuid);
    }
}
