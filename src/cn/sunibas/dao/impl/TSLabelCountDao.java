package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSLabelCountDao;
import cn.sunibas.entity.TSLabelCount;
import cn.sunibas.util.LabelCountDefaultSetting;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
public class TSLabelCountDao implements ITSLabelCountDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<TSLabelCount> getTop() {
        return sessionFactory.getCurrentSession()
                .createSQLQuery(
                        "select * from T_LABEL_COUNT order by TScount desc limit " +
                                LabelCountDefaultSetting.getTop()
                )
                .addEntity(cn.sunibas.entity.TSLabelCount.class)
                .list();
    }
}
