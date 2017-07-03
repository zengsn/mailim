package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSkidDao;
import cn.sunibas.entity.TSkid;
import cn.sunibas.service.ITSkidService;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TSkidService implements ITSkidService {

    //注入dao
    private ITSkidDao tskidDao;
    public void setTskidDao(ITSkidDao itSkidDao) {
        this.tskidDao = itSkidDao;
    }

    @Override
    public void register(TSkid tSkid) {
        //注册审查通过后到达这里
        tskidDao.save(tSkid);
    }

    @Override
    public TSkid login(TSkid tSkid) {
        return tskidDao.findByNameAndPassword(tSkid.getName(),tSkid.getPwd());
    }
}
