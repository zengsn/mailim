package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSkidDao;
import cn.sunibas.entity.TSkid;
import org.hibernate.SessionFactory;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TSkidDao implements ITSkidDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TSkid tSkid) {
        sessionFactory
                .getCurrentSession()
                .save(tSkid);
    }

    @Override
    public TSkid findById(String id) {
        return (TSkid)sessionFactory
                .getCurrentSession()
                .createQuery("from TSkid where id=?")
                .setString(0,id)
                .uniqueResult();
    }

    @Override
    public TSkid findByNameAndPassword(String name, String pwd) {
        return (TSkid)sessionFactory
                .getCurrentSession()
                .createQuery("from TSkid where name=? and pwd=?")
                .setString(0, name)
                .setString(1,pwd)
                .uniqueResult();
    }

    @Override
    public void update(TSkid tSkid) {
        sessionFactory.getCurrentSession().update(tSkid);
    }
}
