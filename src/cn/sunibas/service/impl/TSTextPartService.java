package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSTextPartDao;
import cn.sunibas.entity.TSTextPart;
import cn.sunibas.service.ITSTextPartService;

import java.util.List;

/**
 * Created by IBAS on 2017/2/16.
 */
public class TSTextPartService implements ITSTextPartService {

    private ITSTextPartDao itsTextPartDao;

    public void setItsTextPartDao(ITSTextPartDao itsTextPartDao) {
        this.itsTextPartDao = itsTextPartDao;
    }

    @Override
    public void save(TSTextPart tsTextPart) {
        itsTextPartDao.save(tsTextPart);
    }

    @Override
    public TSTextPart getByUuid(String uuid) {
        return itsTextPartDao.getByUuid(uuid);
    }

    @Override
    public TSTextPart getByUuidAndPart(String uuid, int partIndex) {
        return itsTextPartDao.getByUuidAndPart(uuid,partIndex);
    }

    @Override
    public List<TSTextPart> getByUuidList(List<String> list) {
        return itsTextPartDao.getByUuidList(list);
    }

    @Override
    public List<TSTextPart> getByUuidList(String list) {
        return itsTextPartDao.getByUuidList(list);
    }

    @Override
    public List<TSTextPart> getList(int index, int count) {
        return itsTextPartDao.getTop(index,count);
    }

    @Override
    public void update(TSTextPart tsTextPart) {

    }
}
