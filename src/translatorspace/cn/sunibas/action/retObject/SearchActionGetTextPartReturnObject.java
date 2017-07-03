package cn.sunibas.action.retObject;

import cn.sunibas.action.retObject.abstractClass.CommonProto;
import cn.sunibas.entity.TSTextPart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IBAS on 2017/2/22.
 */
public class SearchActionGetTextPartReturnObject extends CommonProto{
    /**
     * 200 成功
     * 300 查询结束没有更多内容（这里是当前有查找到内容）
     * 400 失败或者错误或者没有内容
     */
    public SearchActionGetTextPartReturnObject() {
        //setStatusRetStrMap(200,"成功");
        setStatusRetStrMap(300,"查询结束没有更多内容");
        setStatusRetStrMap(400,"失败或者错误或者没有内容");
    }

    private List<PackageTSTextPart> data = new ArrayList<PackageTSTextPart>();

    public List<PackageTSTextPart> getData() {
        return data;
    }

    public void addData(List<TSTextPart> tsTextParts) {
        Iterator<TSTextPart> iterable = tsTextParts.iterator();
        while (iterable.hasNext()) {
            data.add(new PackageTSTextPart(iterable.next()));
        }
    }
}
