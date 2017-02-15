package cn.sunibas.action;

import cn.sunibas.action.retObject.ManuscriptActionReturnObject;
import cn.sunibas.entity.TSkid;
import cn.sunibas.entity.TStext;
import cn.sunibas.service.ITStextService;
import cn.sunibas.util.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/11.
 */
public class ManuscriptAction extends ActionSupport {

    private StaticObject staticObject;

    public void setStaticObject(StaticObject staticObject) {
        this.staticObject = staticObject;
    }

    private File[] manuscript;
    private String[] manuscriptFileName; //文件名称
    private TStext tstext;

    public File[] getManuscript() {
        return manuscript;
    }

    public void setManuscript(File[] manuscript) {
        this.manuscript = manuscript;
    }

    public String[] getManuscriptFileName() {
        return manuscriptFileName;
    }

    public void setManuscriptFileName(String[] manuscriptFileName) {
        this.manuscriptFileName = manuscriptFileName;
    }

    public TStext getTstext() {
        return tstext;
    }

    public void setTstext(TStext tstext) {
        this.tstext = tstext;
    }
//    private String title;
//    private String label = "null";
//    private int score = 0;
//    private float money = 0;
//    private int fromLanguage = 0;
//    private int toLanguage = 0;
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }
//
//    public float getMoney() {
//        return money;
//    }
//
//    public void setMoney(float money) {
//        this.money = money;
//    }
//
//    public int getFromLanguage() {
//        return fromLanguage;
//    }
//
//    public void setFromLanguage(int fromLanguage) {
//        this.fromLanguage = fromLanguage;
//    }
//
//    public int getToLanguage() {
//        return toLanguage;
//    }
//
//    public void setToLanguage(int toLanguage) {
//        this.toLanguage = toLanguage;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    //ioc
    private ITStextService itStextService;

    public void setItStextService(ITStextService itStextService) {
        this.itStextService = itStextService;
    }

    /**
     * 获取上传的文稿文件(本质上是文件上传)
     * */
    public void upload() throws IOException {
        ManuscriptActionReturnObject manuscriptActionReturnObject = new ManuscriptActionReturnObject();
        //检查语言是否设置
        if (staticObject.getLanguage().containsKey(tstext.getFromLanguage()) && staticObject.getLanguage().containsKey(tstext.getToLanguage())) {
            if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
                if (manuscript != null) {
                    //有文件上传，这里暂行接受，但随即进行文件处理
                    //检查文件名是否和文件数量一致
                    if (manuscript.length == manuscriptFileName.length) {
                        //uuid在一次上传时只需要创建一次
                        String uuid = MyUUID.getMyUUID();
                        TSkid tSkid = CheckLoginStatus.getKid(ActionContext.getContext());
                        {   //仅仅需要保存一次
                            //TStext tstext = new TStext();
                            //填充文稿信息并保存
                            tstext.setUuid(uuid);
                            //前端警告文件名不能超过一定长度（len<50）
                            //tstext.setTitle(title);
                            //这里label有默认值 null
                            //tstext.setLabel(label);
                            tstext.setCreateTime(new Date());
                            tstext.setStatus(0);
                            //tstext.setScore(score);
                            //tstext.setMoney(money);
                            //part在后期进行设置
                            tstext.setPart(0);
                            tstext.setTSkidid(tSkid.getId());
                            //tstext.setFromLanguage(fromLanguage);
                            //tstext.setToLanguage(toLanguage);
                            itStextService.createText(tstext);
                        }
                        for (int i = 0;i < manuscript.length;i++) {
                            File file = new File(ManuscrripitFolderLocation.folderLocation + uuid, i + "");
                            FileUtil.copyFile(manuscript[i],file);
                            /* debug */
                            //System.out.println(i + "" + tStext);
                        }
                        manuscriptActionReturnObject.setStatus(200);
                        manuscriptActionReturnObject.setRetStr("成功");
                    } else {
                        manuscriptActionReturnObject.setStatus(300);
                        manuscriptActionReturnObject.setRetStr("上传失败");
                    }
                } else {
                    manuscriptActionReturnObject.setStatus(400);
                    manuscriptActionReturnObject.setRetStr("没有文件");
                }
            } else {
                manuscriptActionReturnObject.setStatus(500);
                manuscriptActionReturnObject.setRetStr("没有登陆");
            }
        } else {
            manuscriptActionReturnObject.setStatus(600);
            manuscriptActionReturnObject.setRetStr("没有设置语言");
        }
        Object2JSON.JSONString(ServletActionContext.getResponse(),manuscriptActionReturnObject);
    }


}
