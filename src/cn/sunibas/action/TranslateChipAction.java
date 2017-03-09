package cn.sunibas.action;

import cn.sunibas.action.retObject.TranslateChipReturnObject;
import cn.sunibas.action.retObject.abstractClass.ReturnStatus;
import cn.sunibas.entity.TSkid;
import cn.sunibas.entity.TSstatus;
import cn.sunibas.redis.RedisCacheUtil;
import cn.sunibas.redis.RedisFroTSmark;
import cn.sunibas.service.ITSstatusService;
import cn.sunibas.util.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by ibas on 2/25/17.
 */
public class TranslateChipAction extends ActionSupport {
    private ITSstatusService itSstatusService;
    private RedisFroTSmark redisFroTSmark;

    public void setItSstatusService(ITSstatusService itSstatusService) {
        this.itSstatusService = itSstatusService;
    }

    public void setRedisFroTSmark(RedisFroTSmark redisFroTSmark) {
        this.redisFroTSmark = redisFroTSmark;
    }

    /**
     * 这里强调一点，就是碎片翻译的内容是比较少的，一定程度上可以说不可能多于一个文本的大小。
     */
    private File translateContent;
    //private String translateContentFileName;
    private String tagUrl;
    private String tssuuid;
    private String chipContent;

    public File getTranslateContent() {
        return translateContent;
    }

    public void setTranslateContent(File translateContent) {
        this.translateContent = translateContent;
    }

//    public String getTranslateContentFileName() {
//        return translateContentFileName;
//    }
//
//    public void setTranslateContentFileName(String translateContentFileName) {
//        this.translateContentFileName = translateContentFileName;
//    }

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
    }

    public String getTssuuid() {
        return tssuuid;
    }

    public void setTssuuid(String tssuuid) {
        this.tssuuid = tssuuid;
    }

    public String getChipContent() {
        return chipContent;
    }

    public void setChipContent(String chipContent) {
        this.chipContent = chipContent;
    }

    /**
     *
     */
    public void translate() {
        TranslateChipReturnObject translateChipReturnObject = new TranslateChipReturnObject();
        TSstatus tSstatus = new TSstatus();
        if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
            TSkid tSkid = CheckLoginStatus.getKid(ActionContext.getContext());
            if (tssuuid.length() == 32 || redisFroTSmark.check(tagUrl,tSkid.getId())) {
                translateChipReturnObject.setStatus(201);
                translateChipReturnObject.setRetStr("暂时不允许修改内容");
                Object2JSON.JSONString(
                        ServletActionContext.getResponse(),
                        translateChipReturnObject,
                        ObjectType.Object
                );
                return;
            }
            try {
                OPTSTextPart.decrypt(tagUrl, tSstatus);
            } catch (Exception e) {
                translateChipReturnObject.setStatusOnly(300);
                Object2JSON.JSONString(
                        ServletActionContext.getResponse(),
                        translateChipReturnObject,
                        ObjectType.Object
                );
                e.printStackTrace();
                return;
            }
            tSstatus.setUuid(MyUUID.getMyUUID());
            tSstatus.setCreateTime(new Date());
            tSstatus.setTSkidid(tSkid.getId());
            try {
                if (translateContent != null) {
                    SaveTranslatedChip.saveFile(translateContent,tSstatus);
                    itSstatusService.createStatus(tSstatus);
                } else if (chipContent != null) {
                    //保存chipContent
                    SaveTranslatedChip.saveFile(chipContent,tSstatus);
                } else {
                    throw new Exception("内容都为空");
                }
            } catch (Exception e) {
                e.printStackTrace();
                translateChipReturnObject.setStatusOnly(500);
                Object2JSON.JSONString(
                        ServletActionContext.getResponse(),
                        translateChipReturnObject,
                        ObjectType.Object
                );
                return;
            }
            translateChipReturnObject.setStatusOnly(200);
            redisFroTSmark.newStatusInRedis(tagUrl, tSstatus.getUuid(), tSstatus.getMark());
            redisFroTSmark.setOneTsstatus(tagUrl,tSstatus.getUuid(),tSstatus.getMark(),true);
            redisFroTSmark.saveTssuuidAndKididInCache(tSstatus.getUuid(), tSkid.getId());
            ///////加入redis缓存//////
            redisFroTSmark.newStatusInRedis(tagUrl,tSstatus.getTSuuid(),tSstatus.getMark());
            translateChipReturnObject.setRetStr(tSstatus.getUuid());
            /*
            if (redisFroTSmark.newStatusInRedis(tagUrl,tSkid.getId(),tSstatus.getMark()) == null) {
                translateChipReturnObject.setStatusOnly(ReturnStatus.OccureError);
            }
              */
            ////////////////
            Object2JSON.JSONString(
                    ServletActionContext.getResponse(),
                    translateChipReturnObject,
                    ObjectType.Object
            );
            return;
        } else { //else for if (login)
            translateChipReturnObject.setStatusOnly(500);
            Object2JSON.JSONString(
                    ServletActionContext.getResponse(),
                    translateChipReturnObject,
                    ObjectType.Object
            );
            return;
        }
    }

}
