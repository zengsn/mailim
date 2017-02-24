package cn.sunibas.action;

import cn.sunibas.action.retObject.PackageTSTextPart;
import cn.sunibas.entity.TSTextPart;
import cn.sunibas.service.ITSTextPartService;
import cn.sunibas.util.Object2JSON;
import cn.sunibas.util.ObjectType;
import org.apache.struts2.ServletActionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IBAS on 2017/2/17.
 */
public class HomeAction {
    private int from = -1;
    private int count = -1;
    private Object object = new Object();
    private ITSTextPartService itsTextPartService;

    public void setItsTextPartService(ITSTextPartService itsTextPartService) {
        this.itsTextPartService = itsTextPartService;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void getChip(){
        if (count < 0 || from < 0) {
            Object2JSON.JSONString(ServletActionContext.getResponse(),object,ObjectType.Object);
        } else {
            List<PackageTSTextPart> packageTSTextPartList = new ArrayList<PackageTSTextPart>();
            for (TSTextPart tsTextPart : itsTextPartService.getList(from,count)) {
                packageTSTextPartList.add(
                        new PackageTSTextPart(tsTextPart)
                );
            }
            Object2JSON.JSONString(ServletActionContext.getResponse(),packageTSTextPartList, ObjectType.Array);
        }
    }
}
