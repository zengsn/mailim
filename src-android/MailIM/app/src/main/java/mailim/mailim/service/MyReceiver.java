package mailim.mailim.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import mailim.mailim.R;
import mailim.mailim.activity.ChatActivity;
import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Chat;
import mailim.mailim.fragment.MessageFragment;
import mailim.mailim.util.InputUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.OutputUtil;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String type = intent.getStringExtra("type");
        if("chat".equals(type)){
            Chat chat = (Chat) intent.getSerializableExtra("chat");
            String username = intent.getStringExtra("username");
            String time = chat.getTime();
            List<Chat> list;
            if(ChatActivity.mContext == null) {
                InputUtil<Chat> inputUtil = new InputUtil<>();
                list = inputUtil.readListFromSdCard(MyApplication.getInstance().getLocalPath() + username);
                if (list == null) list = new ArrayList<>();
                list.add(chat);
                OutputUtil<Chat> outputUtil = new OutputUtil<>();
                outputUtil.writeListIntoSDcard(MyApplication.getInstance().getLocalPath() + username , list);
                MessageFragment.addMessage(time,username,chat.getText(), true);
                MainActivity.updataNum();
                notification(context, username, chat.getText());
            }
            else {
                ChatActivity.mContext.addChat(chat);
            }
        }
        else if("friend".equals(type)) {
            String username = intent.getStringExtra("username");
            if (MyApplication.getInstance().getNewFriends().indexOf(username) < 0) {
                MyApplication.getInstance().getNewFriends().add(username);
                MainActivity.updataNum();
                if(MyApplication.getInstance().getNewFriends().size()>0){
                    if(MainActivity.f2 != null){
                        LinearLayout ll = (LinearLayout)MainActivity.mContext.findViewById(R.id.matey_new_friend_lv);
                        ll.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    if(MainActivity.f2 != null){
                        LinearLayout ll = (LinearLayout)MainActivity.mContext.findViewById(R.id.matey_new_friend_lv);
                        ll.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void notification(Context context,String username,String text){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!preferences.getBoolean("notifications_new_message",true))return;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        long[] vibrateTime = {0,0,1000};
        if(preferences.getBoolean("notifications_new_message_vibrate",false))
            vibrateTime[1] = 200;
        String ringtone = preferences.getString("notifications_new_message_ringtone",null);
        if(null == ringtone)ringtone = "content://settings/system/notification_sound";
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username",username);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notify = new Notification.Builder(context)
                .setAutoCancel(true)
                .setTicker("新消息")
                .setSmallIcon(R.mipmap.ic)
                .setContentTitle("来自" + username + "的消息")
                .setContentText(text)
                .setVibrate(vibrateTime)
                .setSound(android.net.Uri.parse(ringtone))
                .setColor(Color.BLUE)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi).build();
        if (manager != null) {
            manager.notify(0, notify);
        }
    }
}
