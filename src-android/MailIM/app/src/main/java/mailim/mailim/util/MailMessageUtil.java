package mailim.mailim.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Chat;
import mailim.mailim.entity.Email;
import mailim.mailim.entity.Friend;

/**
 * Created by zzh on 2017/10/12.
 */
public class MailMessageUtil {
    public static String SUBJECT_MAILIM = "[mailim]";
    public static String SUBJECT_FRIEND = "[friend]";
    public static String SUBJECT_CHAT = "[chat]";
    public static String SUBJECT_MAIL_FRI = "[mailim][friend]";
    public static String SUBJECT_MAIL_CHAT = "[mailim][chat]";
    public static String FENGEXIAN = "--------------------------------------\n";
    public static String FENGEFU = ":";
    public static String LINEEND = "\n";
    private static int KEY_SPACE = 10;

    public static List<Chat> getChatList(List<Email> emailList,String friendEmail){
        List<Email> chatEmailList = getEmailBySubject(emailList,SUBJECT_MAIL_CHAT);
        if(MainActivity.app.debugable)ToastUtil.show("聊天邮件数："+String.valueOf(chatEmailList.size()));
        List<Chat> chatList = new ArrayList<Chat>();
        for (Email obj:chatEmailList){
            addChatByEmail(chatList,obj,friendEmail);
        }
        Collections.sort(chatList);
        return chatList;
    }

    private static void addChatByEmail(List<Chat> list,Email email,String friendEmail){
        String body = email.getContent();
        String[] chatString = body.split("\\-{38}");
//        String[] chatString = body.split(FENGEXIAN);
        if(MainActivity.app.debugable) {
//            ToastUtil.show("聊天数：" + String.valueOf(chatString.length));
//            for (int i =0 ;i<chatString.length;i++)ToastUtil.show(chatString[i]);
        }
        for (String obj:chatString) {
            if(obj.length()<10)continue;
            Chat chat = null;
            String time = findValue(obj,"时间");
            String status = findValue(obj,"状态");
            String type = findValue(obj,"类型");
            String from = findValue(obj,"发送");
            String to = findValue(obj,"接收");
            String text = findValue(obj,"内容");
            boolean isMyself = false;
            if(from.equals(MainActivity.app.getMyUser().getEmail()))isMyself = true;
            chat = new Chat(isMyself,text);
            chat.setTime(time);
            try {
                chat.setStatus(Integer.valueOf(status.trim()));
            }catch (NumberFormatException e){
                if(MainActivity.app.debugable)ToastUtil.show(e.getMessage());
                e.printStackTrace();
            }
            chat.setType(type);
//            list.add(chat);
            if(friendEmail.equals(from) || friendEmail.equals(to))list.add(chat);
        }
    }

    private static String findValue(String chatString,String key){
        String[] hangs = chatString.split(LINEEND);
        for(String obj:hangs){
            if(obj.contains(key)) {
                String[] string = obj.split(FENGEFU);
                if(string.length>1 && string[0].contains(key)){
                    return obj.substring(string[0].length() + FENGEFU.length()).trim();
                }
            }
        }
        return "";
    }

    public static String getChatlistString(List<Chat> chatList,String friendEmail){
        StringBuilder stringBuilder = new StringBuilder();
        for (Chat obj:chatList){
            stringBuilder.append(getChatStringBuilder(obj,friendEmail));
        }
        return new String(stringBuilder);
    }

    private static StringBuilder getChatStringBuilder(Chat chat,String friendEmail){
        StringBuilder stringBuilder = new StringBuilder(FENGEXIAN);
        String myEmail = MainActivity.app.getMyUser().getEmail();
        addKey_Value(stringBuilder,"时间",chat.getTime());
        addKey_Value(stringBuilder,"状态",String.valueOf(chat.getStatus()));
        addKey_Value(stringBuilder,"类型",chat.getType());
        addKey_Value(stringBuilder,"发送",chat.isMyself()? myEmail:friendEmail);
        addKey_Value(stringBuilder,"接收",chat.isMyself()? friendEmail:myEmail);
        addKey_Value(stringBuilder,"内容",chat.getText());
        return stringBuilder;
    }

    private static void addKey_Value(StringBuilder stringBuilder,String key,String value){
        stringBuilder.append(key);
//        for (int i = KEY_SPACE - key.length();i>0;i--)stringBuilder.append(" ");
        stringBuilder.append(FENGEFU);
        stringBuilder.append(value);
        stringBuilder.append(LINEEND);
    }

    public static List<Email> getEmailBySubject(List<Email> emailList,String cintainsSubject) {
        List<Email> list = new ArrayList<Email>();
        for (Email email : emailList){
            if(email.getSubject().contains(cintainsSubject))list.add(email);
        }
        return list;
    }

    public static String getFriendlistString(List<Friend> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (Friend obj:list){
            stringBuilder.append(getFriendStringBuilder(obj));
        }
        return new String(stringBuilder);
    }

    private static StringBuilder getFriendStringBuilder(Friend friend){
        StringBuilder stringBuilder = new StringBuilder(FENGEXIAN);
        addKey_Value(stringBuilder,"邮箱",friend.getEmail());
        addKey_Value(stringBuilder,"备注",friend.getUsername());
        addKey_Value(stringBuilder,"最近",friend.getEmailLastTime());
        addKey_Value(stringBuilder,"星级",String.valueOf(friend.getStar()));
        return stringBuilder;
    }

    public static List<Friend> getFriendList(List<Email> emailList){
        List<Email> chatEmailList = getEmailBySubject(emailList,SUBJECT_MAIL_FRI);
        List<Friend> friendListist = new ArrayList<Friend>();
        for (Email obj:chatEmailList){
            addFriendByEmail(friendListist,obj);
        }
        Collections.reverse(friendListist);
        return friendListist;
    }

    private static void addFriendByEmail(List<Friend> list,Email email){
        String body = email.getContent();
        String[] friendString = body.split(FENGEXIAN);
//        if(MainActivity.app.debugable)ToastUtil.show("聊天数："+String.valueOf(chatString.length));
        for (String obj:friendString) {
            Friend friend = null;
            String friendEmail = findValue(obj,"邮箱");
            String username = findValue(obj,"备注");
            String lastTime = findValue(obj,"最近");
            String star = findValue(obj,"星级");
            friend = new Friend(friendEmail);
            friend.setUsername(username);
            friend.setEmailLastTime(lastTime);
            try {
                friend.setStatus(Integer.valueOf(star.trim()));
            }catch (NumberFormatException e){
                if(MainActivity.app.debugable)ToastUtil.show(e.getMessage());
                e.printStackTrace();
            }
            list.add(friend);
        }
    }
}
