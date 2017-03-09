package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSstatusDao;
import cn.sunibas.entity.TSstatus;
import org.hibernate.SessionFactory;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TSstatusDao implements ITSstatusDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TSstatus tSstatus) {
        sessionFactory
                .getCurrentSession()
                .save(tSstatus);
    }

    @Override
    public TSstatus findByUuid(String uuid) {
        return (TSstatus)sessionFactory
                .getCurrentSession()
                .createQuery("from TSstatus where uuid=?")
                .setString(0,uuid)
                .uniqueResult();
    }

    @Override
    public void delete(TSstatus tSstatus) {
        sessionFactory
                .getCurrentSession()
                .delete(tSstatus);
    }
}
