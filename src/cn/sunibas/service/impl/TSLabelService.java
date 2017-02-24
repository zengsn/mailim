package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSLabelDao;
import cn.sunibas.entity.TSLabel;
import cn.sunibas.service.ITSLabelService;

import java.util.List;

/**
 * Created by IBAS on 2017/2/20.
 */
public class TSLabelService implements ITSLabelService{

    private ITSLabelDao itsLabelDao;

    public void setItsLabelDao(ITSLabelDao itsLabelDao) {
        this.itsLabelDao = itsLabelDao;
    }

    @Override
    public void save(TSLabel tsLabel) {
        itsLabelDao.save(tsLabel);
    }

    @Override
    public List<TSLabel> getByUuidLimit(String uuid,int from, int count) {
        return itsLabelDao.getByUuidLimit(uuid,from,count);
    }

    @Override
    public List<TSLabel> getByLabelLimit(String label, int from, int count) {
        return itsLabelDao.getByLabelLimit(label,from,count);
    }

    @Override
    public List<TSLabel> getByLabel(String label) {
        return itsLabelDao.getByLabel(label);
    }

    @Override
    public List<TSLabel> getTopLabelList(int count) {
        return itsLabelDao.getTopLabelList(count);
    }
}
