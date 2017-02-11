package cn.sunibas.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/6.
 */
public class TSkid {
    private String id;
    private String name;
    private String pwd;
    private Date createTime;
    public int Checked = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getChecked() {
        return Checked;
    }

    public void setChecked(int checked) {
        Checked = checked;
    }

    @Override
    public String toString(){
        return "id = " + getId() + "\t" +
                "name = " + getName() + "\t" +
                "pwd = " + getPwd() + "\t" +
                "createTime = " + getCreateTime() + "\t" +
                "checked = " + getChecked();

    }
}
