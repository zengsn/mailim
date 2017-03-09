package cn.sunibas.service.impl;

import cn.sunibas.dao.ITStextDao;
import cn.sunibas.entity.TStext;
import cn.sunibas.service.ITStextService;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TStextService implements ITStextService {

    //注入dao
    private ITStextDao tstextDao;
    public void setTstextDao(ITStextDao itStextDao) {
        this.tstextDao = itStextDao;
    }

    @Override
    public void createText(TStext tStext) {
        tstextDao.save(tStext);
    }

    @Override
    public void deleteText(TStext tStext) {
        tstextDao.delete(tStext);
    }

    @Override
    public TStext getText(String uuid) {
        return tstextDao.findByUuid(uuid);
    }

    @Override
    public void updateText(TStext tStext) {
        tstextDao.update(tStext);
    }
}
