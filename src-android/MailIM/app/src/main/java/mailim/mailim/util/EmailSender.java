package mailim.mailim.util;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import mailim.mailim.activity.MainActivity;

/**
 * Created by zzh on 2017/9/12.
 */
public class EmailSender {

    public EmailSender(){

    }

    public static void sendMail(final String to, final String sub, final String text,
                                final File file){
        final String from = MainActivity.app.getMyUser().getEmail();
        final String server = EmailUtil.getSmtpAddr(from);
        final String username = EmailUtil.getUsername(from);
        final String password = MainActivity.app.getMyUser().getEmailpwd();
//        final String password = "zzh74849264";
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

//        props.put("defaultEncoding","UTF-8");


        EmailUtil.MyAuthenticator myauth =new EmailUtil.MyAuthenticator(fromMail, password);// Get

        Session session = Session.getInstance(props, myauth);

//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.smtp.socketFactory.fallback", "true");
//        props.setProperty("mail.smtp.starttls.enable", "true");
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.socketFactory.port", "465");

        MimeMessage message = new MimeMessage(session); // Define message

        message.setFrom(new InternetAddress(fromMail)); // Set the from address

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                toMail));// Set
        // address
        message.setSubject(title);// Set the subject

        // message.setText(MimeUtility.encodeWord(body));// Set the content

        MimeMultipart allMultipart = new MimeMultipart("mixed");

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(body);
        allMultipart.addBodyPart(textBodyPart);

        if(attachment != null) {
            MimeBodyPart attachPart = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(attachment);
            attachPart.setDataHandler(new DataHandler(fds));
            attachPart.setFileName(MimeUtility.encodeWord(fds.getName()));
            allMultipart.addBodyPart(attachPart);
        }

        message.setContent(allMultipart);
        message.saveChanges();
        Transport.send(message);
        android.os.Message msg = new android.os.Message();
        msg.what = 1;
        msg.obj = new String("邮件已发送！");
        MainActivity.app.handler.sendMessage(msg);
    }
}
