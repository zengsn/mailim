package cn.sunibas.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/6.
 */

public class TStext {
    private String uuid;
    private String title;
    private String label;
    private Date createTime;
    private int status;
    private String TSkidid;
    private int score;
    private float money;

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
}
