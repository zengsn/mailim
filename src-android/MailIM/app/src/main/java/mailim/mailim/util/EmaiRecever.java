package mailim.mailim.util;

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.InputStream;
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
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import mailim.mailim.entity.Email;
import mailim.mailim.fragment.EmailFragment;

/**
 * Created by zzh on 2017/7/14.
 */
public class EmaiRecever extends AsyncTask<Void,String,List<Email>> {
    // 连接pop3服务器的主机名、协议、用户名、密码
    protected String pop3Server = "pop.163.com";
    protected String protocol = "pop3";
    protected String user = "zhangzhanhong218";
    protected String pwd = "1234567zzh";

    public EmailFragment fragment;

    public EmaiRecever(EmailFragment fragment){
        this.fragment = fragment;
    }

    @Override
    protected void onPostExecute(List<Email> emails){
        fragment.updataEmail(emails);
    }
    @Override
    protected void onProgressUpdate(String... value) {

    }
    @Override
    protected List<Email> doInBackground(Void... params) {
        List<Email> emails = new ArrayList<Email>();
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
                email.setSubject( messages[i].getSubject());
                String str[] = messages[i].getFrom()[0].toString().split(" ");
                if(str.length == 1){
                    email.setFrom_address(str[0]);
                }
                else {
                    email.setFrom_address(str[1].substring(1, str[1].length() - 1));
                }
                email.setSendDate( messages[i].getSentDate());
                email.setMultipart(messages[i].isMimeType("multipart/*"));
                email.setContent( getAllMultipart(messages[i]));
                email.setEmpty(false);
                emails.add(email);
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
        return emails;
    }

    /**
     * 解析综合数据
     * @param part
     * @throws Exception
     */
    private static String getAllMultipart(Part part) throws Exception{
        String contentType = part.getContentType();
        int index = contentType.indexOf("name");
        boolean conName = false;
        if(index!=-1){
            conName=true;
        }
        String text = "";
        //判断part类型
        if(part.isMimeType("text/plain") && ! conName) {
            text = (String) part.getContent();
        }else if (part.isMimeType("text/html") && ! conName) {
            text = (String) part.getContent();
        }else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                //递归获取数据
                text = getAllMultipart(multipart.getBodyPart(i));
//                //附件可能是截图或上传的(图片或其他数据)
//                if (multipart.getBodyPart(i).getDisposition() != null) {
//                    //附件为截图
//                    if (multipart.getBodyPart(i).isMimeType("image/*")) {
//                        InputStream is = multipart.getBodyPart(i)
//                                .getInputStream();
//                        String name = multipart.getBodyPart(i).getFileName();
//                        String fileName;
//                        //截图图片
//                        if(name.startsWith("=?")){
//                            fileName = name.substring(name.lastIndexOf(".") - 1,name.lastIndexOf("?="));
//                        }else{
//                            //上传图片
//                            fileName = name;
//                        }
//
//                        FileOutputStream fos = new FileOutputStream("D:\\"
//                                + fileName);
//                        int len = 0;
//                        byte[] bys = new byte[1024];
//                        while ((len = is.read(bys)) != -1) {
//                            fos.write(bys,0,len);
//                        }
//                        fos.close();
//                    } else {
//                        //其他附件
//                        InputStream is = multipart.getBodyPart(i)
//                                .getInputStream();
//                        String name = multipart.getBodyPart(i).getFileName();
//                        FileOutputStream fos = new FileOutputStream("D:\\"
//                                + name);
//                        int len = 0;
//                        byte[] bys = new byte[1024];
//                        while ((len = is.read(bys)) != -1) {
//                            fos.write(bys,0,len);
//                        }
//                        fos.close();
//                    }
//                }
            }
        }else if (part.isMimeType("message/rfc822")) {
            text = getAllMultipart((Part) part.getContent());
        }
        return text;
    }

    /**
     * 解析附件内容
     * @param part
     * @throws Exception
     */
    private static void getAttachmentMultipart(Part part) throws Exception{
        if(part.isMimeType("multipart/*")){
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if(bodyPart.getDisposition()!=null){
                    InputStream is = bodyPart.getInputStream();
                    FileOutputStream fos=new FileOutputStream("路径+文件名");
                    int len=0;
                    byte[] bys=new byte[1024];
                    while((len=is.read(bys))!=-1){
                        fos.write(bys, 0, len);
                    }
                    fos.close();
                }
            }
        }

    }
    /**
     * 解析图片内容
     * @param part
     * @throws Exception
     */
    private static void getPicMultipart(Part part) throws Exception{
        if(part.isMimeType("multipart/*")){
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if(bodyPart.isMimeType("image/*")){
                    InputStream is = bodyPart.getInputStream();
                    FileOutputStream fos=new FileOutputStream("路径+文件名");
                    int len=0;
                    byte[] bys=new byte[1024];
                    while((len=is.read(bys))!=-1){
                        fos.write(bys, 0, len);
                    }
                    fos.close();
                }
            }
        }
    }
    /**
     * 解析文本内容
     * @param part
     * @throws Exception
     */
    private static String getTextMultipart(Part part) throws Exception{
        String text = "";
        if(part.isMimeType("text/html")){
            text = (String) part.getContent();
        }else if(part.isMimeType("text/plain")){
            text = (String) part.getContent();
        }
        return text;
    }
}
