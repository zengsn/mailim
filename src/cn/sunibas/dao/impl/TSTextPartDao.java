package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSTextPartDao;
import cn.sunibas.entity.TSTextPart;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IBAS on 2017/2/16.
 */
public class TSTextPartDao implements ITSTextPartDao{
    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TSTextPart tsTextPart) {
        sessionFactory.getCurrentSession().save(tsTextPart);
    }

    @Override
    public TSTextPart getByUuid(String uuid) {
        return (TSTextPart)sessionFactory.getCurrentSession()
                .createQuery("from TSTextPart where uuid=?")
                .setString(0,uuid)
                .uniqueResult();
    }

    @Override
    public List<TSTextPart> getTop(int first, int count) {
        return sessionFactory.getCurrentSession()
                .createQuery("from TSTextPart")
                .setFirstResult(first)
                .setMaxResults(count)
                .list();
    }

    @Override
    public List<TSTextPart> getByUuidList(List<String> list) {
        List<TSTextPart> tsTextPartList = new ArrayList<TSTextPart>();
        for (String str : list) {
            tsTextPartList.add(getByUuid(str));
        }
        return tsTextPartList;
    }

    @Override
    public void update(TSTextPart tsTextPart) {
        sessionFactory.getCurrentSession().update(tsTextPart);
    }
}
