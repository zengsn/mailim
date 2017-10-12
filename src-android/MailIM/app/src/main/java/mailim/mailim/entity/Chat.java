package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/8/28.
 */
public class Chat implements Serializable {
    private boolean isMyself;
    private String type;
    private String time;
    private String text;
    private String mulId;

    public Chat(boolean isMyself, String text){
        setMyself(isMyself);
        setText(text);
        setType("text");
        setTime(String.valueOf(System.currentTimeMillis()/1000));
    }

    @Override
    public boolean equals(Object obj) {
        Chat c = (Chat)obj;
        if(time.equals(c.getTime()))return true;
        return false;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMulId() {
        return mulId;
    }

    public void setMulId(String mulId) {
        this.mulId = mulId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
