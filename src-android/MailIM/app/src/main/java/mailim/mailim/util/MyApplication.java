package mailim.mailim.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Chat;
import mailim.mailim.entity.Friend;
import mailim.mailim.entity.User;

/**
 * Created by zzh on 2017/8/30.
 */
public class MyApplication extends Application {
    private User myUser;
    private List<Friend> friendList;
    private List<String> newFriends = new ArrayList<String>();
    private boolean isLogin;
    private Target target;
    private List<Message> inboxMessages = new ArrayList<>();
    private List<Message> mailimMessages = new ArrayList<>();

    public List<Message> getInboxMessages() {
        return inboxMessages;
    }

    public void setInboxMessages(List<Message> inboxMessages) {
        this.inboxMessages = inboxMessages;
    }

    public List<Message> getMailimMessages() {
        return mailimMessages;
    }

    public void setMailimMessages(List<Message> mailimMessages) {
        this.mailimMessages = mailimMessages;
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

    public File getHeadFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取sd卡目录
            File path = new File(sdCardDir+"/mailim/"+myUser.getUsername()+"/head/");
            if(!path.exists())path.mkdirs();
            File sdFile = new File(path,myUser.getUsername());
            return sdFile;
        }
        return null;
    }

    public void loadHead(final String username){
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                File sdFile = getHeadFile(username);
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
                .load(Constant.HEAD_URL+username+"?time="+ System.currentTimeMillis())
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
