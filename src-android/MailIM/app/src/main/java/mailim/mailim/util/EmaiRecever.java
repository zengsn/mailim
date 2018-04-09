package mailim.mailim.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SubjectTerm;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Email;

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
                server = EmailUtil.getDefaultAddr(username);
                try {
                    Store store = EmailUtil.login(server,user,pwd);
                    if(store == null){
//                        ToastUtil.show(MyApplication.getInstance(),"连接邮箱服务器失败！");
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
                                getAllMultipart(0,messages[i],true);
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
                                getAllMultipart(0,messages[i],true);
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
                MyApplication.getInstance().handler.sendMessage(msg);
                super.run();
            }
        }.start();
    }

    public static void recevie(final String username, final String pwd){
        Message[] messages;
        List<Email> indexEmail = MyApplication.getInstance().getInboxEmail();
        List<Email> mailinEmail = MyApplication.getInstance().getMailimEmail();
        indexEmail.clear();
        mailinEmail.clear();
        String server = null;
        String user = EmailUtil.getUsername(username);
        server = EmailUtil.getDefaultAddr(username);
        try {
            Store store = EmailUtil.login(server,user,pwd);
            if(store == null){
                ToastUtil.show(MyApplication.getInstance(),"连接邮箱服务器失败！");
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
                        email.setContent(getAllMultipart(0,messages[i],isDownload));
                        email.setEmpty(false);
                        mailinEmail.add(0,email);
                    }
                }
                folder = store.getFolder("INBOX");
                if (folder != null) {
                    folder.open(Folder.READ_WRITE);
                    System.out.println(String.valueOf(folder.getDeletedMessageCount()));
                    System.out.println(String.valueOf(folder.getMessageCount()));
                    messages = folder.getMessages();
                    int count = messages.length;
                    int max = 100;
                    int i = count > max ? count - max : 0;
                    for (;i < count ; i ++){
                        Email email = new Email();
                        email.setSubject(messages[i].getSubject());
                        InternetAddress address =(InternetAddress) messages[i].getFrom()[0];
                        String name = address.getPersonal();
                        if(messages[i].getFlags().toString().equals("javax.mail.Flags@10")){
                            name = "新";
                        }
//                        Flags flags = messages[i].getFlags();
//                        System.out.println(flags.toString());
//                        if (flags.contains(Flags.Flag.SEEN))
//                            System.out.println("这是一封已读邮件");
//                        else {
//                            System.out.println("未读邮件");
//                            messages[i].setFlag(Flags.Flag.SEEN, true);
//                            messages[i].saveChanges();
//                        }
                        if(null != name)email.setName(MimeUtility.decodeText(name));
                        else email.setName("");
                        email.setEmailAddr(address.getAddress());
                        email.setSendDate(messages[i].getSentDate());
                        email.setMultipart(messages[i].isMimeType("multipart/*"));
                        boolean isDownload = email.isMultipart();
                        Log.e("邮件类型：",address.getAddress()+messages[i].getSentDate()+"multipart/*："+String.valueOf(messages[i].isMimeType("multipart/*")));
//                        if(email.getSubject().contains("[mailim]"))isDownload = true;
                        email.setContent(getAllMultipart(0,messages[i],isDownload));
                        Log.e("邮件内容：",email.getContent());
                        String text = email.getContent();
                        email.setEmpty(false);
                        indexEmail.add(0,email);
                    }
                    folder.close(true);
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
    public static String getAllMultipart(int j,Part part,boolean isDownload) throws Exception{
        String contentType = part.getContentType();
        Log.e("消息类型：",part.getContentType());
        int index = contentType.indexOf("name");
        boolean conName = false;
        if(index!=-1){
            conName=true;
            Log.e("name","true");
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
                Log.e("消息数:",String.valueOf(i));
                text += getAllMultipart(j+1,multipart.getBodyPart(i),isDownload);
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
                        String time = MailMessageUtil.findValue(text,"时间");
                        FileOutputStream fos = new FileOutputStream(MyApplication.getInstance().getDownlaodFile(time));
                        int len = 0;
                        byte[] bys = new byte[1024];
                        while ((len = is.read(bys)) != -1) {
                            fos.write(bys,0,len);
                        }
                        fos.close();
                        if(isCidImgAndReplace(text)){
                            String cid = getCid(multipart.getBodyPart(i));
                            if(cid != null)Log.e("cid：",cid);
                            File file = MyApplication.getInstance().getDownlaodFile(time);
                            fileName = file.getAbsolutePath();
                            Log.e("filename:",fileName);
                            text = replaceLocalPathByImgCid(text,cid,fileName);
                            Log.e("替换后内容：",text);
                        }
                    } else {
                        //其他附件
                        InputStream is = multipart.getBodyPart(i)
                                .getInputStream();
                        String name = multipart.getBodyPart(i).getFileName();
                        FileOutputStream fos = new FileOutputStream(MyApplication.getInstance().getDownlaodFile(name));
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
            text = getAllMultipart(j+1,(Part) part.getContent(),isDownload);
        }else{
            //text = getAllMultipart((Part) part.getContent(),isDownload);
        }
//        Log.e("text内容：",text);
        return text;
    }

    public static String getCid(Part p) throws MessagingException {
        String content, cid;
        String[] headers = p.getHeader("Content-Id");
//        if(headers != null)Log.e("headers:", Arrays.toString(headers)+
//                p.getContentType());
        if (headers != null && headers.length > 0) {
            content = headers[0];
        } else {
            return null;
        }
        if (content.startsWith("<") && content.endsWith(">")) {
            cid = "cid:" + content.substring(1, content.length() - 1);
        } else {
            cid = "cid:" + content;
        }
//        Log.e("cid:",cid);
        return cid;
    }

    public static boolean isCidImgAndReplace(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        return text.contains("<img src=\"cid:");
    }

    public static String replaceLocalPathByImgCid(String content,String fileName,String filePath) {
        return content.replace("<img src=" + "\"" + fileName + "\"", "<img src=\"file://" + filePath + "\"");
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
