package mailim.mailim.util;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Created by zzh on 2017/10/12.
 */
public class MailMessageUtil {
    public static String SUBJECT_MAILIM = "[mailim]";
    public static String SUBJECT_FRIEND = "[friend]";
    public static String SUBJECT_STAR = "[star]";
    public static String SUBJECT_CHAT = "[chat]";
    public static String SUBJECT_MAIL_FRI = "[mailim][friend]";
    public static String SUBJECT_MAIL_FRI_STAR = "[mailim][friend][star]";

    public static List<Message> getMailimMessages(List<Message> messageList) throws MessagingException {
        List<Message> list = new ArrayList<Message>();
        for (Message message : messageList){
            if(message.getSubject().indexOf(SUBJECT_MAILIM)>=0)list.add(message);
        }
        return list;
    }

    public static List<Message> getFriendMessages(List<Message> messageList) throws MessagingException {
        List<Message> list = new ArrayList<Message>();
        for (Message message : messageList){
            if(message.getSubject().indexOf(SUBJECT_MAIL_FRI)>=0)list.add(message);
        }
        return list;
    }

    public static List<Message> getStarFriendMessages(List<Message> messageList) throws MessagingException {
        List<Message> list = new ArrayList<Message>();
        for (Message message : messageList){
            if(message.getSubject().indexOf(SUBJECT_MAIL_FRI_STAR)>=0)list.add(message);
        }
        return list;
    }
}
