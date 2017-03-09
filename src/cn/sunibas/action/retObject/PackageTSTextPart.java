package cn.sunibas.action.retObject;

import cn.sunibas.entity.TSTextPart;
import cn.sunibas.util.OPTSTextPart;

/**
 * Created by IBAS on 2017/2/20.
 */
public class PackageTSTextPart {
    //为访问文稿时的tag属性
    private int score;
    private int money;
    private int overTimes;
    private int fromLanguage;
    private int toLanguage;
    private String partText;
    private String urlTag;

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

    public String getPartText() {
        return partText;
    }

    public void setPartText(String partText) {
        this.partText = partText;
    }

    public String getUrlTag() {
        return urlTag;
    }

    public void setUrlTag(String urlTag) {
        this.urlTag = urlTag;
    }

    public PackageTSTextPart(){}
    public PackageTSTextPart(TSTextPart tsTextPart) {
        this.overTimes = tsTextPart.getOverTimes();
        this.partText = tsTextPart.getPartText();
        this.money = tsTextPart.getMoney();
        this.score = tsTextPart.getScore();
        this.toLanguage = tsTextPart.getToLanguage();
        this.fromLanguage = tsTextPart.getFromLanguage();
        this.urlTag = OPTSTextPart.encipher(tsTextPart);
    }
}
