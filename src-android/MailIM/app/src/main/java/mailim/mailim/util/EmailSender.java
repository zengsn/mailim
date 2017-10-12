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
        new Thread(){
            @Override
            public void run() {
                String from = MainActivity.app.getMyUser().getEmail();
                String server = EmailUtil.getSmtpAddr(from);
                String username = EmailUtil.getUsername(from);
                String password = MainActivity.app.getMyUser().getEmailpwd();
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

        Properties props = System.getProperties();// Get system properties

        props.put("mail.smtp.host", server);// Setup mail server

        props.put("mail.smtp.auth", "true");

        EmailUtil.MyAuthenticator myauth =new EmailUtil.MyAuthenticator(username, password);// Get

        Session session = Session.getInstance(props, myauth);

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
    }
}
