package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSMarkDao;
import cn.sunibas.entity.TSMark;
import cn.sunibas.service.ITSMarkService;

import java.util.List;

/**
 * Created by sunbing on 17-3-4.
 */
public class TSMarkService implements ITSMarkService {
    private ITSMarkDao itsMarkDao;

    public void setItsMarkDao(ITSMarkDao itsMarkDao) {
        this.itsMarkDao = itsMarkDao;
    }

    @Override
    public void insert(TSMark tsMark) {
        itsMarkDao.insert(tsMark);
    }

    @Override
    public List<TSMark> getList(String TSSuuid) {
        return itsMarkDao.getList(TSSuuid);
    }
}
