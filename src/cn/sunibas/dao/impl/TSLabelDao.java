package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSLabelDao;
import cn.sunibas.entity.TSLabel;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by IBAS on 2017/2/20.
 */
public class TSLabelDao implements ITSLabelDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TSLabel tsLabel) {
        sessionFactory.getCurrentSession().save(tsLabel);
    }

    @Override
    public List<TSLabel> getByUuidLimit(String uuid,int from,int count) {
        return sessionFactory.getCurrentSession()
                .createQuery("from TSLabel where tsUuid=?")
                .setString(0,uuid)
                .setFirstResult(from)
                .setMaxResults(count)
                .list();
    }

    @Override
    public List<TSLabel> getByLabelLimit(String label, int from, int count) {
        return sessionFactory.getCurrentSession()
                .createQuery("from TSLabel where tsLabel=?")
                .setString(0,label)
                .setFirstResult(from)
                .setMaxResults(count)
                .list();
    }

    @Override
    public List<TSLabel> getByLabel(String label) {
        return sessionFactory.getCurrentSession()
                .createQuery("from TSLabel where tsLabel=?")
                .setString(0,label)
                .list();
    }

    @Override
    public List<TSLabel> getTopLabelList(int count) {
        return sessionFactory.getCurrentSession()
                .createSQLQuery(
                        "SELECT TSlabel,TSuuid from (" +
                        "   select TSlabel, TSuuid, count(TSuuid)c" +
                        "   from T_LABEL" +
                        "   GROUP BY TSlabel" +
                        "   ORDER BY c desc" +
                        "   limit " + count +
                        ") t;"
                )
                .addEntity(cn.sunibas.entity.TSLabel.class)
                .list();
    }

//    @Override
//    public List<TSLabel> getAll() {
//        return sessionFactory.getCurrentSession()
//                .createSQLQuery("select distinct tslabel from t_label")
//                .addEntity(cn.sunibas.entity.TSLabel.class)
//                .list();
//    }
}
