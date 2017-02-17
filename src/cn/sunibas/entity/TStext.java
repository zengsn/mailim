package cn.sunibas.entity;

import cn.sunibas.util.ForDebug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IBAS on 2017/2/6.
 */

public class TStext implements Serializable {
    private String uuid;
    private String title;
    private String label;
    private Date createTime;
    private int status;
    private String TSkidid;
    private int score;
    private float money;
    private int part;
    private int fromLanguage;
    private int toLanguage;
    private List<String> labelList = new ArrayList<String>();

    public TStext(){
        fromLanguage = 0;
        toLanguage = 0;
        score = 0;
        money = 0;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        //将标签写入list中
        label.split(",");
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTSkidid() {
        return TSkidid;
    }

    public void setTSkidid(String TSkidid) {
        this.TSkidid = TSkidid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(int fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public int getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(int toLanguage) {
        this.toLanguage = toLanguage;
    }

    @Override
    public String toString(){
        return ForDebug.Object2String(this);
    }
}
