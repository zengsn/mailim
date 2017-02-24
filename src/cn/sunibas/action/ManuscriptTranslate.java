package cn.sunibas.action;

import cn.sunibas.action.retObject.ManuscriptTranslateReturnObject;
import cn.sunibas.action.retObject.PackageTSTextPart;
import cn.sunibas.entity.TSTextPart;
import cn.sunibas.service.ITSTextPartService;
import cn.sunibas.util.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.File;

/**
 * Created by IBAS on 2017/2/20.
 */
public class ManuscriptTranslate extends ActionSupport {

    private ITSTextPartService itsTextPartService;

    public void setItsTextPartService(ITSTextPartService itsTextPartService) {
        this.itsTextPartService = itsTextPartService;
    }

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 这里发现没有登陆应该提示去登录然后引导回到这里
     * 这里要记录下访问的链接即tag，在访问失败的时候
     * @return
     */
    public void translate(){
        ManuscriptTranslateReturnObject manuscriptTranslateReturnObject
                = new ManuscriptTranslateReturnObject();
        if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
            //解开tag对应的内容
            ActionContext.getContext().getSession().put("textPart", null);
            TSTextPart tsTextPart = new TSTextPart();
            PackageTSTextPart packageTSTextPart;
            try {
                OPTSTextPart.decrypt(tag, tsTextPart);
            } catch (Exception e) {
                manuscriptTranslateReturnObject.oneSet(400);
                Object2JSON.JSONString(
                        ServletActionContext.getResponse(),
                        manuscriptTranslateReturnObject,
                        ObjectType.Object
                );
                e.printStackTrace();
                return;
            }
            String fileName = ManuscrripitDefaultSetting.folderLocation +
                    tsTextPart.getTSuuid() +
                    ManuscrripitDefaultSetting.childFolderRelativeLocation +
                    tsTextPart.getTSindex();
            if (!(new File(fileName).exists())) {
                manuscriptTranslateReturnObject.oneSet(300);
                Object2JSON.JSONString(
                        ServletActionContext.getResponse(),
                        manuscriptTranslateReturnObject,
                        ObjectType.Object
                );
                return;
            }
            tsTextPart = itsTextPartService.getByUuidAndPart(tsTextPart.getTSuuid(),tsTextPart.getTSindex());
            packageTSTextPart = new PackageTSTextPart(tsTextPart);
            packageTSTextPart.setPartText(
                    ReadOneFile.readFileAsStringBuilder(
                            fileName,
                            ManuscrripitDefaultSetting.defaultEncoding
                    ).toString());
            ActionContext.getContext().getSession().put("textPart", packageTSTextPart);
            manuscriptTranslateReturnObject.oneSet(200);
            manuscriptTranslateReturnObject.setPackageTSTextPart(packageTSTextPart);
            Object2JSON.JSONString(
                    ServletActionContext.getResponse(),
                    manuscriptTranslateReturnObject,
                    ObjectType.Object
            );
            return;
        }
        manuscriptTranslateReturnObject.oneSet(500);
        Object2JSON.JSONString(
                ServletActionContext.getResponse(),
                manuscriptTranslateReturnObject,
                ObjectType.Object
        );
    }
}
