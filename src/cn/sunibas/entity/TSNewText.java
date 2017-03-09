package cn.sunibas.entity;

import cn.sunibas.util.ForDebug;

import java.io.Serializable;

/**
 * Created by IBAS on 2017/2/16.
 */
public class TSNewText implements Serializable {
    private String uuid;
    private int status;
    private int part;
    private int score;
    private int money;
    private int fromLanguage;
    private int toLanguage;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
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
