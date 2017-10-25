package mailim.mailim.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.Header;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.mail.Message;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Chat;
import mailim.mailim.entity.Email;
import mailim.mailim.entity.Friend;
import mailim.mailim.entity.User;

/**
 * Created by zzh on 2017/8/30.
 */
public class MyApplication extends Application {
    private User myUser;
    private List<Friend> friendList = new ArrayList<Friend>();
    private List<String> newFriends = new ArrayList<String>();
    private boolean isLogin;
    private Target target;
    private List<Email> inboxEmail = new ArrayList<>();
    private List<Email> mailimEmail = new ArrayList<>();
    private boolean isUpdateFriend = false;
    public boolean debugable = true;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtil.show(getApplicationContext(), msg.obj.toString());
                    break;
                case 2:
                    intiFriend();
                    break;
                case 3:
                    friendList.get(msg.arg1).setStatus(msg.arg2);
                    break;
            }
        }
    };

    public void clear(){
        myUser.clear();
        friendList.clear();
        newFriends.clear();
        isLogin = false;
        inboxEmail.clear();
        mailimEmail.clear();
    }

    public void recevieMailimEmail(){
        EmaiRecever.downlaodMailimFile(myUser.getEmail(),myUser.getEmailpwd());
    }

    private void intiFriend(){
        InputUtil<Friend> inputUtil = new InputUtil<Friend>();
        List<Friend> list = inputUtil.readListFromSdCard(getDownloadPath()+"friend");
        if(null != list){
            try {
                JSONObject.parseArray(JSONObject.toJSONString(list),Friend.class);
                friendList = list;
                checkOnline();
            }catch (JSONException e){
                if(debugable){
                    ToastUtil.show(this,e.getMessage());
                }
                e.printStackTrace();
            }
        }
        else{
            ToastUtil.show(getApplicationContext(),"好友为空");
        }
    }

    public void checkOnline(){
        for (Friend obj:friendList){
            checkOnline(friendList.indexOf(obj));
        }
    }

    private void checkOnline(final int index){
        String type = "online";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("email",friendList.get(index).getEmail());
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)){
                    android.os.Message msg = new android.os.Message();
                    msg.what = 3;
                    msg.arg1 = index;
                    msg.arg2 = 1;
                    handler.sendMessage(msg);
                }
                else {
                    android.os.Message msg = new android.os.Message();
                    msg.what = 3;
                    msg.arg1 = index;
                    msg.arg2 = 0;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    public void login(){

//        recevieMailimEmail();
    }

    public void intiFriendList(){
        friendList = MailMessageUtil.getFriendList(inboxEmail);
    }

    public void unLogin(){
        saveFriendToFile();
        if(isUpdateFriend)updateFriendToEmail();
        clear();
    }

    public void updateChat(String email,List<Chat> chats){
        Friend friend = getFriend(email);
        saveFriendToFile();
    }

    public void updateChatToEmail() {
        InputUtil<String> inputUtil = new InputUtil<String>();
        List<String> list = inputUtil.readListFromSdCard(MainActivity.app.getLocalPath()+"newChatList");
        if(null != list){
            for(String obj:list){
                updateChatToEmail(obj+".txt");
            }
            File file = getLocalFile("newChatList.txt");
            if(!file.delete()){
                ToastUtil.show(this,"无法删除文件！");
            }
        }
    }

    private void updateChatToEmail(String name){
        String to = myUser.getEmail();
        String subject = MailMessageUtil.SUBJECT_MAIL_CHAT;
        String body = "chat";
        if(!EmailUtil.isEmail(to)){
            ToastUtil.show(MainActivity.app,"邮箱地址格式有误！");
            return;
        }
        EmailSender.sendMail(to,subject,body,getLocalFile(name));
    }

    public void updateFriendToEmail(){
        saveFriendToFile();
        String to = myUser.getEmail();
        String subject = MailMessageUtil.SUBJECT_MAIL_FRI;
        String body = MailMessageUtil.getFriendlistString(friendList);
        if(debugable)ToastUtil.show(this,body);
        if(!EmailUtil.isEmail(to)){
            ToastUtil.show(MainActivity.app,"邮箱地址格式有误！");
            return;
        }
        EmailSender.sendMail(to,subject,body,null);
//        EmailSender.sendMail(to,subject,body,getLocalFile("friend.txt"));
    }

    public boolean saveFriendToFile(){
        OutputUtil<mailim.mailim.entity.Friend> outputUtil = new OutputUtil<mailim.mailim.entity.Friend>();
        return outputUtil.writeListIntoSDcard(MainActivity.app.getLocalPath()+"friend",friendList);
    }

    public Friend getFriend(String email){
        for(Friend obj:friendList){
            if(obj.getEmail().equals(email))return obj;
        }
        return new Friend(email);
    }

    public String getFriendUsername(String email){
        for(Friend obj:friendList){
            if(obj.getEmail().equals(email))return obj.getUsername();
        }
        return "未命名";
    }

    public void addFriend(String email,String name){
        final Friend friend = new Friend();
        friend.setUsername(name);
        friend.setEmail(email);
        friendList.add(0,friend);

        String type = "checkUserByEmail";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("email",email);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if ("true".equals(str)) {
                    friend.setStar(Friend.STAR_USER);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
        saveFriendToFile();
    }

    public void checkUserByEmail(String email){
        String type = "checkUserByEmail";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("email",email);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if ("true".equals(str)) {
                }
                else {

                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    public List<Email> getInboxEmail() {
        return inboxEmail;
    }

    public void setInboxEmail(List<Email> inboxEmail) {
        this.inboxEmail = inboxEmail;
    }

    public List<Email> getMailimEmail() {
        return mailimEmail;
    }

    public void setMailimEmail(List<Email> mailimEmail) {
        this.mailimEmail = mailimEmail;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        inti();
    }

    private void inti(){
        myUser = new User();
        friendList = new ArrayList<Friend>();
        setLogin(false);
    }

    public void downlaodMailimData(){

    }

    public List<String> getHistoryUsers(){
        List<String> users = new ArrayList<String>();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  //检测sd卡是否存在
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir,"mailim");
            File file[] = sdFile.listFiles();
            for (File obj:file){
                if(obj.isDirectory()){
                    users.add(obj.getName());
                }
            }
        }
        return users;
    }

    public File getDownlaodFile(String name){
        File file = new File(Environment.getExternalStorageDirectory()
                +"/mailim/"+myUser.getUsername()+"/download/",name);
        if(!file.getParentFile().exists())file.getParentFile().mkdirs();
        return file;
    }

    public File getLocalFile(String name){
        File file = new File(Environment.getExternalStorageDirectory()
                +"/mailim/"+myUser.getUsername()+"/local/",name);
        if(!file.getParentFile().exists())file.getParentFile().mkdirs();
        return file;
    }

    public File getUpdateFile(String name){
        File file = new File(Environment.getExternalStorageDirectory()
                +"/mailim/"+myUser.getUsername()+"/update/",name);
        if(!file.getParentFile().exists())file.getParentFile().mkdirs();
        return file;
    }

    public String getUpdatePath(){
        return "mailim/"+myUser.getUsername()+"/update/";
    }

    public String getLocalPath(){
        return "mailim/"+myUser.getUsername()+"/local/";
    }

    public String getDownloadPath(){
        return "mailim/"+myUser.getUsername()+"/download/";
    }

    public File getHeadFile(String fileName){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取sd卡目录
            File path = new File(sdCardDir+"/mailim/"+myUser.getUsername()+"/head/");
            if(!path.exists())path.mkdirs();
            File sdFile = new File(path,fileName);
            return sdFile;
        }
        return null;
    }

    public List<Chat> getChatOnMail(String email){
        List<Chat> chatList = new ArrayList<Chat>();
        String text = null;
        for(Email obj:inboxEmail){
            if((MailMessageUtil.SUBJECT_MAIL_CHAT+email).equals(obj.getSubject())){
                text = obj.getContent();
                List<Chat> list = JSONArray.parseArray(text,Chat.class);
                chatList.addAll(list);
            }
        }
        return chatList;
    }

    public void emailChat(String email){
        Friend friend = getFriend(email);
        InputUtil<Chat> inputUtil = new InputUtil<Chat>();
        List<Chat> localChat = inputUtil.readListFromSdCard(getLocalPath()+friend.getEmail());
        List<Chat> mailChat = inputUtil.readListFromSdCard(getDownloadPath()+friend.getEmail());
        List<Chat> chatList = new ArrayList<Chat>();
        if(localChat != null && mailChat != null){
            chatList.clear();
            chatList.addAll(mailChat);
            chatList.removeAll(localChat);
            chatList.addAll(localChat);
        }
        else if(localChat != null)chatList = localChat;
        else if(mailChat != null) chatList = mailChat;
        Iterator<Chat> iterator = chatList.iterator();
        while (iterator.hasNext()){
            Chat chat = iterator.next();
            try {
                if (Integer.valueOf(chat.getTime()) < Integer.valueOf(friend.getEmailLastTime())) {
                    iterator.remove();
                }
            }catch (NumberFormatException e){
                if(debugable)ToastUtil.show(e.getMessage());
                e.printStackTrace();
            }
        }
//        if(debugable)ToastUtil.show(this,String.valueOf(chatList.size()));
//        StringBuilder stringBuilder = new StringBuilder();
////        for (Chat obj:chatList) {
////            stringBuilder.append(JSONObject.toJSONString(obj));
////        }
//        stringBuilder.append(JSONArray.toJSONString(chatList));
        if(chatList.size()<3)return;
        String body = MailMessageUtil.getChatlistString(chatList,email);
        friend.setEmailLastTime(String.valueOf(System.currentTimeMillis()/1000));
        saveFriendToFile();
        if(debugable)ToastUtil.show(this,body);
//        EmailSender.sendMail(friend.getEmail(), MailMessageUtil.SUBJECT_MAIL_CHAT+friend.getEmail(),body.toString(),null);
        EmailSender.sendMail(myUser.getEmail(), MailMessageUtil.SUBJECT_MAIL_CHAT+friend.getEmail(),body.toString(),null);
    }

    public File getHeadFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取sd卡目录
            File path = new File(sdCardDir+"/mailim/"+myUser.getUsername()+"/head/");
            if(!path.exists())path.mkdirs();
            File sdFile = new File(path,myUser.getEmail());
            return sdFile;
        }
        return null;
    }

    public void loadHead(final String email){
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                File sdFile = getHeadFile(email);
                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(sdFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.with(this)
                .load(Constant.HEAD_URL+email+"?time="+ System.currentTimeMillis())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(target);
    }

    public boolean isFriend(String username){
        for (Friend obj:friendList){
            if(obj.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public static boolean isTopActivy(String cmdName, Context context)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        String cmpNameTemp = null;
        if (null != runningTaskInfos)
        {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
        }

        if (null == cmpNameTemp)
        {
            return false;
        }

        return cmpNameTemp.equals(cmdName);

    }

    public List<String> getNewFriends() {
        return newFriends;
    }

    public void setNewFriends(List<String> newFriends) {
        this.newFriends = newFriends;
    }

    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
