package mailim.mailim.entity;

import java.util.Date;

/**
 * Created by zzh on 2017/7/14.
 */
public class Email {
    protected String from_address;
    protected String pet_name;
    protected String subject;
    protected String content;
    protected Date sendDate;
    protected Date recevieDate;
    protected boolean empty;

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Date getRecevieDate() {
        return recevieDate;
    }

    public void setRecevieDate(Date recevieDate) {
        this.recevieDate = recevieDate;
    }
}
