package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITStextDao;
import cn.sunibas.entity.TStext;
import org.hibernate.SessionFactory;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TStextDao implements ITStextDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TStext tStext) {
        sessionFactory
                .getCurrentSession()
                .save(tStext);
    }

    @Override
    public TStext findByUuid(String uuid) {
        return (TStext)sessionFactory
                .getCurrentSession()
                .createQuery("from TStext where uuid=?")
                .setString(0,uuid)
                .uniqueResult();
    }

    @Override
    public void update(TStext tStext) {
        sessionFactory
                .getCurrentSession()
                .update(tStext);
    }

    @Override
    public void delete(TStext tStext) {
        sessionFactory
                .getCurrentSession()
                .delete(tStext);
    }

    @Override
    public void deleteByUuid(String uuid) {
        delete(findByUuid(uuid));
    }
}
