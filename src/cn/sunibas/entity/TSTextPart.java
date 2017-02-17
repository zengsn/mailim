package cn.sunibas.entity;

import java.io.Serializable;

/**
 * Created by IBAS on 2017/2/16.
 */
public class TSTextPart implements Serializable{
    private String TSuuid;
    private int TSindex;
    private int score;
    private int money;
    private int overTimes;
    private int fromLanguage;
    private int toLanguage;

    public String getTSuuid() {
        return TSuuid;
    }

    public void setTSuuid(String TSuuid) {
        this.TSuuid = TSuuid;
    }

    public int getTSindex() {
        return TSindex;
    }

    public void setTSindex(int TSindex) {
        this.TSindex = TSindex;
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

    public int getOverTimes() {
        return overTimes;
    }

    public void setOverTimes(int overTimes) {
        this.overTimes = overTimes;
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
}
