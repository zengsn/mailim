package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/8/28.
 */
public class Chat implements Serializable,Comparable<Chat> {
    private boolean isMyself;
    private String type;
    private String time;
    private String text;
    private int status;

    public Chat(boolean isMyself, String text){
        setMyself(isMyself);
        setText(text);
        setType("text");
        setTime(String.valueOf(System.currentTimeMillis()/1000));
        setStatus(0);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tiem:  ").append(time);
        stringBuilder.append("type:  ").append(type);
        stringBuilder.append("status:").append(String.valueOf(status));
        if(isMyself) stringBuilder.append("我:  ").append(text);
        else stringBuilder.append("友:  ").append(text);
        return stringBuilder.toString();
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int compareTo(Chat o) {
        if(Integer.valueOf(time) < Integer.valueOf(o.getTime()))return -1;
        else if(Integer.valueOf(time) > Integer.valueOf(o.getTime()))return 1;
        else return 0;
    }
}
