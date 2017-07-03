package cn.sunibas.action;

import cn.sunibas.action.retObject.OPChipMarkActionGetChipReturnObject;
import cn.sunibas.action.retObject.OPChipMarkActionGetChipStatusReturnObject;
import cn.sunibas.action.retObject.abstractClass.CommonProto;
import cn.sunibas.action.retObject.abstractClass.ReturnStatus;
import cn.sunibas.entity.TSkid;
import cn.sunibas.entity.TSstatus;
import cn.sunibas.redis.RedisFroTSmark;
import cn.sunibas.util.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by sunbing on 17-3-4.
 */
public class OPChipMarkAction extends ActionSupport {
    private RedisFroTSmark redisFroTSmark;

    public void setRedisFroTSmark(RedisFroTSmark redisFroTSmark) {
        this.redisFroTSmark = redisFroTSmark;
    }

    private String tagUrl;
    private int mark;
    private String tssuuid;
    private String kidid;

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getTssuuid()
    {
        return tssuuid;
    }

    public void setTssuuid(String tssuuid) {
        this.tssuuid = tssuuid;
    }

    public String getKidid() {
        return kidid;
    }

    public void setKidid(String kidid) {
        this.kidid = kidid;
    }

    /**
     * 这里获取到的是当前小译对当前碎片的评分
     * (为什么要这么一个函数？？？)（确实用到了）
     * 所需参数 tssuuid
     * */
    public void getMarks() {
        TSkid tSkid;
        CommonProto commonProto = new CommonProto() {};
        if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
            tSkid = CheckLoginStatus.getKid(ActionContext.getContext());
        } else {
            commonProto.setStatusOnly(ReturnStatus.unLogin);
            Object2JSON.JSONString(
                    ServletActionContext.getResponse(),
                    commonProto,
                    ObjectType.Object
            );
            return;
        }
        if (tssuuid == null) {
            redisFroTSmark.addToRemove(tssuuid);
        }
        if (tssuuid.equals("")){
            Object2JSON.RetNullObject(ServletActionContext.getResponse());
            commonProto.setStatusOnly(ReturnStatus.SettingError);
            Object2JSON.JSONString(
                    ServletActionContext.getResponse(),
                    commonProto,
                    ObjectType.Object
            );
            return;
        } else {
            Map<String,String> map = redisFroTSmark.getMark(tssuuid);
            String markStr = null;
            if (map.containsKey(tSkid.getId())) {
                markStr = map.get(tSkid.getId());
            }
            commonProto.setStatusOnly(ReturnStatus.Success);
            commonProto.setRetStr(markStr);
            Object2JSON.JSONString(
                    ServletActionContext.getResponse(),
                    commonProto,
                    ObjectType.Object
            );
            return;
        }
    }

    /**
     * 设置一个翻译好文稿的评分，这里由一个小译自己决定评分
     * 所需参数 tagUrl tssuuid mark
     * */
    public void setMark() {
        CommonProto commonProto = new CommonProto() {};
        int status;
        if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
            TSkid tSkid = CheckLoginStatus.getKid(ActionContext.getContext());
            commonProto.setStatusOnly(redisFroTSmark.newMark(tagUrl,tssuuid, tSkid.getId(), mark));
        } else {
            commonProto.setStatusOnly(ReturnStatus.unLogin);
        }
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                commonProto,
                ObjectType.Object
        );
    }

    /**
     * 这里获取到的是当前文稿的总评分结果
     * List<TSstatus> 对象
     * */
    public void getChipsStatus() {
        OPChipMarkActionGetChipStatusReturnObject opChipMarkActionGetChipStatusReturnObject
                = new OPChipMarkActionGetChipStatusReturnObject();
        //检验tarUrl的正确性
        if (tagUrl.equals("")) {
            opChipMarkActionGetChipStatusReturnObject.setStatusOnly(ReturnStatus.SettingError);
            return ;
        } else {
            opChipMarkActionGetChipStatusReturnObject.setStatus(ReturnStatus.Success);
            opChipMarkActionGetChipStatusReturnObject.setMap(redisFroTSmark.getTSstatusFromRedis(tagUrl));
            if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
                opChipMarkActionGetChipStatusReturnObject.check(redisFroTSmark,CheckLoginStatus.getKid(ActionContext.getContext()).getId());
            }
        }
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                opChipMarkActionGetChipStatusReturnObject,
                ObjectType.Object
        );
    }

    /**
     * 获取一个翻译好的文稿内容
     * 所需参数 tagUrl,tssuuid
     * */
    public void getTChip() {
        //
        OPChipMarkActionGetChipReturnObject opChipMarkActionGetChipReturnObject
                = new OPChipMarkActionGetChipReturnObject();
        TSstatus tSstatus = new TSstatus();
        try {
            tSstatus = OPTSTextPart.decrypt(tagUrl,tSstatus);
        } catch (Exception e) {
            e.printStackTrace();
            Object2JSON.JSONString(
                    ServletActionContext.getResponse(),
                    null,
                    ObjectType.NullObject
            );
            return;
        }
        tSstatus.setTSkidid(redisFroTSmark.getKididFromTssuuid(tssuuid));
        opChipMarkActionGetChipReturnObject.setContent(
                ReadOneFile.readFileAsStringBuilder(
                        OPTSTextPart.getTChipFileFullName(tSstatus)
                        , ManuscrripitDefaultSetting.defaultEncoding).toString()
        );
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                opChipMarkActionGetChipReturnObject,
                ObjectType.Object
        );
        return;
    }

}
