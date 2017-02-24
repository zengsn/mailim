package cn.sunibas.dao;

import cn.sunibas.entity.TSTextPart;

import java.util.List;

/**
 * Created by IBAS on 2017/2/16.
 * 在将文稿分隔完后进行记录-------------增
 * 在前端将分割好的文稿碎片进行显示------查
 *      1.直接查按翻译量返回一定数量的碎片
 *      2.按id返回一个碎片
 *      3.按id列表返回一部分碎片
 * 在有碎片被翻译之后将碎片翻译量加一----改
 * 这里查询结果为排序结果
 */
public interface ITSTextPartDao {

    void save(TSTextPart tsTextPart);

    TSTextPart getByUuid(String uuid);

    TSTextPart getByUuidAndPart(String uuid,int partIndex);

    List<TSTextPart> getTop(int first,int count);

    List<TSTextPart> getByUuidList(List<String> list);

    List<TSTextPart> getByUuidList(String list);

    void update(TSTextPart tsTextPart);

}
