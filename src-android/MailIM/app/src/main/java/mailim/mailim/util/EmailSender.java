package mailim.mailim.util;

import java.io.File;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Chat;

/**
 * Created by zzh on 2017/9/12.
 */
public class EmailSender {

    public EmailSender(){

    }

    public static void sendMail(final String to, final String sub, final String text,
                                final File file){
        final String from = MyApplication.getInstance().getMyUser().getEmail();
        final String server = EmailUtil.getSmtpAddr(from);
        final String username = EmailUtil.getUsername(from);
        final String password = MyApplication.getInstance().getMyUser().getPassword();
        new Thread(){
            @Override
            public void run() {
                try {
                    sendMail(to,from,server,username,password,sub,text,file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    public static void sendMail(String toMail, String fromMail, String server,
                         String username, String password, String title, String body,
                         File attachment) throws Exception {

        Properties props = new Properties();// Get system properties

        props.put("mail.smtp.host", server);// Setup mail server

        props.put("mail.smtp.auth", "true");

        EmailUtil.MyAuthenticator myauth =new EmailUtil.MyAuthenticator(fromMail, password);// Get

        Session session = Session.getInstance(props, myauth);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        MimeMessage message = new MimeMessage(session); // Define message

        message.setFrom(new InternetAddress(fromMail)); // Set the from address

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                toMail));// Set
        // address
        message.setSubject(title);// Set the subject

        // message.setText(MimeUtility.encodeWord(body));// Set the content
        MimeMultipart allMultipart = new MimeMultipart("mixed");

        if(attachment != null && Chat.TYPE_IMAGE.equals(MailMessageUtil.findValue(body,"类型"))) {
            addImage(allMultipart, body, attachment);
        }else {

//        MimeMultipart allMultipart = new MimeMultipart("mixed");

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(body);
            allMultipart.addBodyPart(textBodyPart);

            if (attachment != null) {
                MimeBodyPart attachPart = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(attachment);
                attachPart.setDataHandler(new DataHandler(fds));
                attachPart.setFileName(MimeUtility.encodeWord(fds.getName()));
                allMultipart.addBodyPart(attachPart);
            }
        }

        message.setContent(allMultipart);
        message.saveChanges();
        Transport.send(message);
        android.os.Message msg = new android.os.Message();
        msg.what = 1;
        msg.obj = "邮件已发送！";
        MyApplication.getInstance().handler.sendMessage(msg);
    }

    /**
     * 在邮件中添加可以显示的图片
     * @param image File 图片
     */
    public static void addImage(MimeMultipart multipart,String text,File image){
        try {
            String header = UUID.randomUUID().toString();
            String img = text+"<br><img src=\"cid:" + header + "\">";
            addHtml(multipart,img);
            addAttach(multipart,image, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在邮件中添加 html 代码
     * @param html String
     */
    public static void addHtml(MimeMultipart multipart,String html) {
        try {
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(html, "text/html;charset=utf8");
            multipart.addBodyPart(bodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 在邮件内容中增加附件（邮件中添加需要在邮件中显示的图片时使用）
     * @param attach File 附件
     * @param header String Content-ID
     */
    private static void addAttach(MimeMultipart multipart,File attach, String header) {
        try {
            BodyPart bodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(attach);
            bodyPart.setDataHandler(new DataHandler(dataSource));
            bodyPart.setFileName(attach.getName());
            if(header != null){
                bodyPart.setHeader("Content-ID", "<" + header + ">");
            }
            multipart.addBodyPart(bodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
