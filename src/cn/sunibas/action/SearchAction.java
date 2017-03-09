package cn.sunibas.action;

import cn.sunibas.action.retObject.SearchActionGetTextLabelReturnObject;
import cn.sunibas.action.retObject.SearchActionGetTextPartReturnObject;
import cn.sunibas.action.sessionObject.LabelInfo;
import cn.sunibas.service.ITSLabelService;
import cn.sunibas.service.ITSTextPartService;
import cn.sunibas.util.LabelCountDefaultSetting;
import cn.sunibas.util.Object2JSON;
import cn.sunibas.util.ObjectType;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;

import java.util.Map;

/**
 * Created by IBAS on 2017/2/21.
 */
public class SearchAction {
    private String label;
    private ITSTextPartService itsTextPartService;
    private ITSLabelService itsLabelService;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setItsTextPartService(ITSTextPartService itsTextPartService) {
        this.itsTextPartService = itsTextPartService;
    }

    public void setItsLabelService(ITSLabelService itsLabelService) {
        this.itsLabelService = itsLabelService;
    }

    public void getTextPart() {
        Map<String,Object> session = ActionContext.getContext().getSession();
        if (label.equals("")) {
            session.put("labelInfo",null);
            session.put("tagLabel",null);
            Object2JSON
                    .JSONString(
                            ServletActionContext.getResponse(),
                            null,
                            ObjectType.NullObject
                    );
            return ;
        }
        LabelInfo labelInfo;
        SearchActionGetTextPartReturnObject searchActionReturnObject = new SearchActionGetTextPartReturnObject();
        //判断session中是有记录
        //用于判断是第一次还是第n次开始搜索
        if (session.get("tagLabel") != null) {
            if (!session.get("tagLabel").equals(label)) {
                session.put("labelInfo",null);
            }
        } else {
            session.put("labelInfo",null);
        }
        session.put("tagLabel",label);
        //获取相关label的文稿uuid
        if (session.get("labelInfo") != null) {
            labelInfo = (LabelInfo)session.get("labelInfo");
        } else {
            labelInfo = new LabelInfo();
            labelInfo.setTsLabelList(itsLabelService.getByLabel(label));
            session.put("labelInfo",labelInfo);
        }
        //开始找文稿并返回
        if (labelInfo.getStatus()) {
            searchActionReturnObject.setStatus(400);
            searchActionReturnObject.setRetStr("没有查找到内容");
        } else {
            searchActionReturnObject.setStatus(200);
            searchActionReturnObject.setRetStr("查找成功");
            searchActionReturnObject.addData(
                    itsTextPartService
                            .getByUuidList(labelInfo.getTarget())
            );
            session.put("labelInfo",labelInfo);
        }
        Object2JSON
                .JSONString(
                        ServletActionContext.getResponse(),
                        searchActionReturnObject,
                        ObjectType.Object
                );
    }

    public void getTextLabel(){
        //检查？
        SearchActionGetTextLabelReturnObject searchActionGetTextLabelReturnObject
                = new SearchActionGetTextLabelReturnObject(
                itsLabelService.getTopLabelList(
                        LabelCountDefaultSetting.getTop()
                )
        );
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                searchActionGetTextLabelReturnObject,
                ObjectType.Object
        );
    }
}
