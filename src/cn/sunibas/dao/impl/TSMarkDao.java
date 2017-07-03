package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSMarkDao;
import cn.sunibas.dao.ITSkidDao;
import cn.sunibas.entity.TSMark;
import cn.sunibas.entity.TSkid;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TSMarkDao implements ITSMarkDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void insert(TSMark tsMark) {
        sessionFactory.getCurrentSession()
                .save(tsMark);
    }

    @Override
    public List<TSMark> getList(String TSSuuid) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from TSMark where TSSuuid=?")
                .setString(0,TSSuuid)
                .list();
    }
}
