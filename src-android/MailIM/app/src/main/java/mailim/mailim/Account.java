package mailim.mailim;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import mailim.mailim.fragment.EmailFragment;

/**
 * Created by zzh on 2017/7/14.
 */
public class Account extends AsyncTask<Void,String,Boolean> {
    // 连接pop3服务器的主机名、协议、用户名、密码
    protected String pop3Server = "pop.163.com";
    protected String smtpServer = "smtp.163.com";
    protected String imapServer = "imap.163.com";
    protected String protocol = "pop3";
    protected String user = "zhangzhanhong218";
    protected String pwd = "1234567zzh";

    protected List<Email> receive_emails;
    public EmailFragment fragment;

    public Account(EmailFragment fragment){
        this.fragment = fragment;
        this.receive_emails = fragment.emails;
    }

    @Override
    protected void onPostExecute(Boolean result){
        if(!result)return;
        fragment.renovate();
    }
    @Override
    protected void onProgressUpdate(String... value) {
        fragment.show();
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = null;
        Message[] messages;
        try {
            store = session.getStore("pop3");
            store.connect(pop3Server, user, pwd);
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            messages = folder.getMessages();
            int mailCounts = messages.length;
            for(int i = 0; i < mailCounts; i++) {
                Email email = new Email();
                email.subject = messages[i].getSubject();
                email.from_address = messages[i].getFrom()[0].toString();
                email.sendDate = messages[i].getSentDate();
                email.recevieDate = messages[i].getReceivedDate();
//                email.content = messages[i].getContent().toString();
                email.setEmpty(true);
                receive_emails.add(email);
            }
            folder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean setAccount(String user,String pwd){
        if(!checkEmaile(user)){
            return false;
        }
        String str[] = user.split("@");
        this.user = str[0];
        this.pop3Server = "pop."+str[1];
        return true;
    }

    private static boolean checkEmaile(String emaile){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }

    private Session getSessionMail() throws Exception {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", pop3Server);
        properties.put("mail.smtp.auth", true);
        Session sessionMail = Session.getDefaultInstance(properties, null);
        return sessionMail;
    }

    public static Store login(String host, String user, String password) {
        // 连接服务器
        Properties props =  System.getProperties();
        Session session = Session.getDefaultInstance(props);
        Store store = null;
        try {
            /**  QQ邮箱需要建立ssl连接 */
            props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.pop3.socketFactory.fallback", "false");
            props.setProperty("mail.pop3.starttls.enable","true");
            props.setProperty("mail.pop3.port", "995");
            props.setProperty("mail.pop3.socketFactory.port", "995");
            // 创建Session实例对象
            // Session session = Session.getInstance(props);  //pop3/smtp :jwovgwaypwrebecd
            store = session.getStore("pop3");
            store.connect(host, user, password);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
        return store;
    }

    public String getString(){
        String str = null;
        // 连接服务器
        Properties props =  System.getProperties();
        Session session = Session.getDefaultInstance(props);
        Store store = null;
        try {
            /**  QQ邮箱需要建立ssl连接 */
            props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.pop3.socketFactory.fallback", "false");
            props.setProperty("mail.pop3.starttls.enable", "true");
            props.setProperty("mail.pop3.port", "995");
            props.setProperty("mail.pop3.socketFactory.port", "995");
            URLName urln = new URLName(protocol,pop3Server,110,null,user,pwd);
            store = session.getStore(urln);
            //store.connect(pop3Server, user, pwd);
            str = store.getURLName().toString();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return str;
    }

    protected void getRecevieEmailByInternet(){
        // 连接服务器
        Properties props =  System.getProperties();
        //Session session = Session.getDefaultInstance(props);
        Store store = null;
        try {
            /**  QQ邮箱需要建立ssl连接 */
            props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.pop3.socketFactory.fallback", "false");
            props.setProperty("mail.pop3.starttls.enable","true");
            props.setProperty("mail.pop3.port", "995");
            props.setProperty("mail.pop3.socketFactory.port", "995");
            // 创建Session实例对象
            Session session = Session.getInstance(props);  //pop3/smtp :jwovgwaypwrebecd
            //URLName urln = new URLName(protocol,pop3Server,110,null,user,pwd);
            store = session.getStore(protocol);
            //store.connect();
            store.connect(pop3Server, user, pwd);

            Email email1 = new Email();
            email1.subject = "bb";
            email1.from_address = "bb";
            email1.setEmpty(true);
            receive_emails.add(email1);
            if(store != null){
                Email email = new Email();
                email.subject = "bb";
                email.from_address = "bb";
                email.setEmpty(true);
                receive_emails.add(email);
            }


//            // 获得邮箱内的邮件夹Folder对象，以"只读"打开
//            Folder folder = store.getFolder("INBOX");
//            folder.open(Folder.READ_ONLY);
//
//            // 获得邮件夹Folder内的所有邮件Message对象
//            Message[] messages = folder.getMessages();
//
//            int mailCounts = messages.length;
//
//            if(mailCounts == 0){
//                Email email = new Email();
//                email.subject = "bb";
//                email.from_address = "bb";
//                email.setEmpty(true);
//                receive_emails.add(email);
//            }
//
//            for(int i = 0; i < mailCounts; i++) {
//                Email email = new Email();
//                email.subject = messages[i].getSubject();
//                email.from_address = (messages[i].getFrom()[0]).toString();
//                email.sendDate = messages[i].getSentDate();
//                email.recevieDate = messages[i].getReceivedDate();
//                email.content = messages[i].getContent().toString();
//                email.setEmpty(true);
//                receive_emails.add(email);
//            }
//            folder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Email>  getAllMail(String name) throws Exception{
        List<Email> mailList = new ArrayList<Email>();
        String number="";
        Store store =login("pop.163.com","zhangzhanhong218","1234567zzh");
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        int mailCount = folder.getMessageCount();
        if (mailCount == 0) {
            folder.close(true);
            store.close();
            return null;
        } else {
            // 取得所有的邮件
            Message[] messages = folder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                Email mail = new Email();
                MimeMessage pmm = new MimeMessage((MimeMessage)messages[i]);
                //pmm.getSubject().contains("[作业]") ||pmm.getSubject().contains("[布置作业]"
                Email email = new Email();
                email.subject = messages[i].getSubject();
                email.from_address = (messages[i].getFrom()[0]).toString();
                email.sendDate = messages[i].getSentDate();
                email.recevieDate = messages[i].getReceivedDate();
                email.content = messages[i].getContent().toString();
                email.setEmpty(true);
                mailList.add(email);
            }
            return mailList;
        }
    }

    public List<Email> getReceive_emails() {
        //getRecevieEmailByInternet();
        Email email = new Email();
        email.subject = "aa";
        email.from_address = "aa";
        email.setEmpty(true);
        receive_emails.add(email);
        return receive_emails;
    }

    public void send(String host, String user, String pwd) {

        Properties props = new Properties();

        // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");

        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);

        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        //session.setDebug(true);

        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            String from = "zhangzhanhong218@163.com";
            String to = "zhangzhanhong218@163.com";
            String subject = "sub";
            message.setFrom(new InternetAddress(from));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // 加载标题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            //contentPart.setText("aaaaa");
            //multipart.addBodyPart(contentPart);
            // 将multipart对象放到message中
            //message.setContent(multipart);
            // 保存邮件
            //message.saveChanges();
            // 发送邮件
            //Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            //transport.connect(host, user, pwd);
            // 把邮件发送出去
            //transport.sendMessage(message, message.getAllRecipients());
            //transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
