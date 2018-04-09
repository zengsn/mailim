package mailim.mailim.util;

import android.util.Log;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by zzh on 2017/9/13.
 */
public class EmailUtil {
    private static String protocol = "pop";

    public static String getProtocol() {
        return protocol;
    }

    public static void setProtocol(String protocol) {
        EmailUtil.protocol = protocol;
    }

    static public class MyAuthenticator extends javax.mail.Authenticator {
        private String strUser;
        private String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }

    public static boolean checkEmail(String email, String pwd) {
        String host = getDefaultAddr(email);
        String user = getUsername(email);
        Store store = login(host, user, pwd);
        return store != null;
    }

    public static Store login(String host, String user, String password) {
        Store store = null;
        // 连接服务器
        Properties props = System.getProperties();
        EmailUtil.MyAuthenticator myauth = new EmailUtil.MyAuthenticator(user, password);// Get
        Session session = Session.getInstance(props, myauth);
        try {
            /**  QQ邮箱需要建立ssl连接 */
            if (host.contains("pop")) {
                props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.pop3.socketFactory.fallback", "false");
                props.setProperty("mail.pop3.starttls.enable", "true");
                props.setProperty("mail.pop3.port", "995");
                props.setProperty("mail.pop3.socketFactory.port", "995");
                store = session.getStore("pop3");
            } else {
                props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.imap.socketFactory.fallback", "false");
                props.setProperty("mail.imap.starttls.enable", "true");
                props.setProperty("mail.imap.port", "993");
                props.setProperty("mail.imap.socketFactory.port", "993");
                store = session.getStore("imaps");
            }
            // 创建Session实例对象
            // Session session = Session.getInstance(props);  //pop3/smtp :jwovgwaypwrebecd
            store.connect(host, user, password);
        } catch (MessagingException e) {
            Log.e("mail", e.toString());
            e.printStackTrace();
            return null;
        }
        return store;
    }


    public static String getUsername(String email) {
        if (null == email || "".equals(email)) return "";
        return email.split("@")[0];
    }

    public static String getDefaultAddr(String email) {

        if (null == email || "".equals(email)) return "";
        String str[] = email.split("@");
        if (str.length > 1) return protocol+"." + str[1];
        return null;
    }

    public static String getPopAddr(String email){
        if(null == email || "".equals(email))return "";
        String str[] = email.split("@");
        if(str.length>1) return "pop."+str[1];
        return null;
    }

    public static String getSmtpAddr(String email){
        if(null == email || "".equals(email))return "";
        //if(email.contains("qq.com"))return "smtp.exmail.qq.com";
        String str[] = email.split("@");
        if(str.length>1) return "smtp."+str[1];
        return null;
    }

    public static String getImapAddr(String email){
        if(null == email || "".equals(email))return "";
        String str[] = email.split("@");
        if(str.length>1) return "imap."+str[1];
        return null;
    }

    public static boolean isEmail(String email){
        //正则表达式
  /*
    String regex = "^[A-Za-z]{1,40}@[A-Za-z0-9]{1,40}\\.[A-Za-z]{2,3}$";
    return email.matches(regex);
   */

        //不适用正则
        if(email==null||"".equals(email)) return false ;
        if(!containsOneWord('@',email)||!containsOneWord('.',email)) return false;
        String prefix = email.substring(0,email.indexOf("@"));
        String middle = email.substring(email.indexOf("@")+1,email.indexOf("."));
        String subfix = email.substring(email.indexOf(".")+1);
        System.out.println("prefix="+prefix +"  middle="+middle+"  subfix="+subfix);

        if(prefix==null||prefix.length()>40||prefix.length()==0) return false ;
        if(!isAllWordsAndNo(prefix)) return false ;
        if(middle==null||middle.length()>40||middle.length()==0) return false ;
        if(!isAllWordsAndNo(middle)) return false ;
        if(subfix==null||subfix.length()>3||subfix.length()<2) return false ;
        if(!isAllWords(subfix)) return false ;
        return true ;
    }
    //判断字符串只包含指定的一个字符c
    private static boolean containsOneWord(char c , String word){
        char[] array = word.toCharArray();
        int count = 0 ;
        for(Character ch : array){
            if(c == ch) {
                count++;
            }
        }
        return count==1 ;
    }
    //检查一个字符串是否全部是字母
    private static boolean isAllWords(String prefix){
        char[] array = prefix.toCharArray();
        for(Character ch : array){
            if(ch<'A' || ch>'z' || (ch<'a' && ch>'Z')) return false ;
        }
        return true;
    }
    //检查一个字符串是否包含字母和数字
    private static boolean isAllWordsAndNo(String middle){
        char[] array = middle.toCharArray();
        for(Character ch : array){
            if(ch<'0' || ch > 'z') return false ;
            else if(ch >'9' && ch <'A') return false ;
            else if(ch >'Z' && ch <'a') return false ;
        }
        return true ;
    }
}
