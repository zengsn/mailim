package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSLabelCountDao;
import cn.sunibas.entity.TSLabelCount;
import cn.sunibas.service.ITSLabelCountService;

import java.util.List;

/**
 * Created by IBAS on 2017/2/21.
 */
public class TSLabelCountService implements ITSLabelCountService {
    private ITSLabelCountDao itsLabelCountDao;

    public void setItsLabelCountDao(ITSLabelCountDao itsLabelCountDao) {
        this.itsLabelCountDao = itsLabelCountDao;
    }

    @Override
    public List<TSLabelCount> getTop() {
        return itsLabelCountDao.getTop();
    }
}
