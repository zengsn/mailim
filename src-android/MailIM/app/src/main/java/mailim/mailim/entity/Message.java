package mailim.mailim.entity;

/**
 * Created by zzh on 2017/8/28.
 */
public class Message {
    private boolean isMyself;
    private int type;
    private int index;
    private String text;

    public Message(boolean isMyself,String text){
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
