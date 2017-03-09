package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSNewTextDao;
import cn.sunibas.entity.TSNewText;
import cn.sunibas.service.ITSNewTextService;

import java.util.List;

/**
 * Created by IBAS on 2017/2/16.
 */
public class TSNewTextService implements ITSNewTextService{

    //注入dao
    private ITSNewTextDao itsNewTextDao;

    public void setItsNewTextDao(ITSNewTextDao itsNewTextDao) {
        this.itsNewTextDao = itsNewTextDao;
    }

    @Override
    public List<TSNewText> getAll() {
        return itsNewTextDao.getAll();
    }

    @Override
    public void update(TSNewText tsNewText) {
        itsNewTextDao.update(tsNewText);
    }

    @Override
    public void delete(TSNewText tsNewText) {
        itsNewTextDao.delete(tsNewText);
    }
}
