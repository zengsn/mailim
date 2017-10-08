package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/8/28.
 */
public class Chat implements Serializable {
    private boolean isMyself;
    private int type;
    private String time;
    private String text;

    public Chat(boolean isMyself, String text){
        setMyself(isMyself);
        setText(text);
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
