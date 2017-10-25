package mailim.mailim.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.MessageVanishedEvent;
import com.sun.mail.imap.ResyncData;
import com.sun.mail.util.MimeUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
import javax.mail.event.MailEvent;
import javax.mail.event.MessageChangedEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SubjectTerm;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Email;
import mailim.mailim.fragment.EmailFragment;

/**
 * Created by zzh on 2017/7/14.
 */
public class EmaiRecever{
    public static void downlaodMailimFile(final String username, final String pwd){
        new Thread(){
            @Override
            public void run() {
                Message[] messages;
                String server = null;
                String user = EmailUtil.getUsername(username);
                if(user.contains("qq.com")){
                    server = EmailUtil.getImapAddr(username);
                }
                else {
                    server = EmailUtil.getPopAddr(username);
                }
                try {
                    Store store = EmailUtil.login(server,user,pwd);
                    if(store == null){
//                        ToastUtil.show(MainActivity.app,"连接邮箱服务器失败！");
                    }
                    else {
                        Folder defaultfolder = store.getDefaultFolder();
                        Folder folders[] = defaultfolder.list();
                        Folder folder = null;
                        if(server != null && server.contains("imap")){
                            folder = folders[0].getFolder("mailim");
                            if(!folder.exists())folder.create(Folder.HOLDS_MESSAGES);
                            folder.open(Folder.READ_ONLY);
                            messages = folder.search(new SubjectTerm(MailMessageUtil.SUBJECT_MAILIM));
                            int count = messages.length;
                            int max = 10;
                            int i = count > max ? count - max : 0;
                            for (;i < count ; i ++){
                                getAllMultipart(messages[i],true);
                            }
                        }
                        folder = store.getFolder("INBOX");
                        if (folder != null) {
                            folder.open(Folder.READ_ONLY);
                            messages = folder.search(new SubjectTerm(MailMessageUtil.SUBJECT_MAILIM));
                            int count = messages.length;
                            int max = 10;
                            int i = count > max ? count - max : 0;
                            for (;i < count ; i ++){
                                getAllMultipart(messages[i],true);
                            }
                            folder.close(false);
                        }
                        store.close();
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                android.os.Message msg = new android.os.Message();
                msg.what = 2;
                MainActivity.app.handler.sendMessage(msg);
                super.run();
            }
        }.start();
    }

    public static void recevie(final String username, final String pwd){
        Message[] messages;
        List<Email> indexEmail = MainActivity.app.getInboxEmail();
        List<Email> mailinEmail = MainActivity.app.getMailimEmail();
        indexEmail.clear();
        mailinEmail.clear();
        String server = null;
        String user = EmailUtil.getUsername(username);
        if(user.contains("qq.com")){
            server = EmailUtil.getImapAddr(username);
        }
        else {
            server = EmailUtil.getPopAddr(username);
        }
        try {
            Store store = EmailUtil.login(server,user,pwd);
            if(store == null){
                ToastUtil.show(MainActivity.app,"连接邮箱服务器失败！");
            }
            else {
                Folder defaultfolder = store.getDefaultFolder();
                Folder folders[] = defaultfolder.list();
                Folder folder = null;
                if(server != null && server.contains("imap")){
                    folder = folders[0].getFolder("mailim");
                    if(!folder.exists())folder.create(Folder.HOLDS_MESSAGES);
                    folder.open(Folder.READ_ONLY);
                    messages = folder.getMessages();
                    int count = messages.length;
                    int max = 10;
                    int i = count > max ? count - max : 0;
                    for (;i < count ; i ++){
                        Email email = new Email();
                        email.setSubject(messages[i].getSubject());
                        InternetAddress address =(InternetAddress) messages[i].getFrom()[0];
                        String name = address.getPersonal();
                        if(null != name)email.setName(MimeUtility.decodeText(name));
                        else email.setName("");
                        email.setEmailAddr(address.getAddress());
                        email.setSendDate(messages[i].getSentDate());
                        email.setMultipart(messages[i].isMimeType("multipart/*"));
                        boolean isDownload = false;
//                        if(email.getSubject().contains("[mailim]"))isDownload = true;
                        email.setContent(getAllMultipart(messages[i],isDownload));
                        email.setEmpty(false);
                        mailinEmail.add(0,email);
                    }
                }
                folder = store.getFolder("INBOX");
                if (folder != null) {
                    ToastUtil.show(MainActivity.app,String.valueOf(folder.getMessageCount()));
                    folder.open(Folder.READ_ONLY);
                    messages = folder.getMessages();
                    int count = messages.length;
                    int max = 10;
                    int i = count > max ? count - max : 0;
                    for (;i < count ; i ++){
                        Email email = new Email();
                        email.setSubject(messages[i].getSubject());
                        InternetAddress address =(InternetAddress) messages[i].getFrom()[0];
                        String name = address.getPersonal();
                        if(null != name)email.setName(MimeUtility.decodeText(name));
                        else email.setName("");
                        email.setEmailAddr(address.getAddress());
                        email.setSendDate(messages[i].getSentDate());
                        email.setMultipart(messages[i].isMimeType("multipart/*"));
                        boolean isDownload = false;
//                        if(email.getSubject().contains("[mailim]"))isDownload = true;
                        email.setContent(getAllMultipart(messages[i],isDownload));
                        email.setEmpty(false);
                        indexEmail.add(0,email);
                    }
                    folder.close(false);
                }
                store.close();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析综合数据
     * @param part
     * @throws Exception
     */
    public static String getAllMultipart(Part part,boolean isDownload) throws Exception{
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
                text = getAllMultipart(multipart.getBodyPart(i),isDownload);
                //附件可能是截图或上传的(图片或其他数据)
                if (isDownload && multipart.getBodyPart(i).getDisposition() != null) {
                    //附件为截图
                    if (multipart.getBodyPart(i).isMimeType("image/*")) {
                        InputStream is = multipart.getBodyPart(i)
                                .getInputStream();
                        String name = multipart.getBodyPart(i).getFileName();
                        String fileName;
                        //截图图片
                        if(name.startsWith("=?")){
                            fileName = name.substring(name.lastIndexOf(".") - 1,name.lastIndexOf("?="));
                        }else{
                            //上传图片
                            fileName = name;
                        }

                        FileOutputStream fos = new FileOutputStream(MainActivity.app.getDownlaodFile(fileName));
                        int len = 0;
                        byte[] bys = new byte[1024];
                        while ((len = is.read(bys)) != -1) {
                            fos.write(bys,0,len);
                        }
                        fos.close();
                    } else {
                        //其他附件
                        InputStream is = multipart.getBodyPart(i)
                                .getInputStream();
                        String name = multipart.getBodyPart(i).getFileName();
                        FileOutputStream fos = new FileOutputStream(MainActivity.app.getDownlaodFile(name));
                        int len = 0;
                        byte[] bys = new byte[1024];
                        while ((len = is.read(bys)) != -1) {
                            fos.write(bys,0,len);
                        }
                        fos.close();
                    }
                }
            }
        }else if (part.isMimeType("message/rfc822")) {
            text = getAllMultipart((Part) part.getContent(),isDownload);
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
