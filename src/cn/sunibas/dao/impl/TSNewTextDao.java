package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSNewTextDao;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by IBAS on 2017/2/16.
 */
public class TSNewTextDao implements ITSNewTextDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<cn.sunibas.entity.TSNewText> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from TSNewText")
                .list();
    }

    @Override
    public void update(cn.sunibas.entity.TSNewText tsNewText) {
        sessionFactory.getCurrentSession()
                .update(tsNewText);
    }

    @Override
    public void delete(cn.sunibas.entity.TSNewText tsNewText) {
        sessionFactory.getCurrentSession().delete(tsNewText);
    }
}
