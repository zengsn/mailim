package mailim.mailim.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mailim.mailim.entity.Friend;
import mailim.mailim.fragment.MessageFragment;
import mailim.mailim.util.EmailSender;
import mailim.mailim.util.InputUtil;
import mailim.mailim.util.MailMessageUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.R;
import mailim.mailim.entity.Chat;
import mailim.mailim.util.Constant;
import mailim.mailim.util.DESUtil;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.OutputUtil;
import mailim.mailim.util.ToastUtil;

/**
 *聊天界面
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    private Friend friend;
    private List<Chat> chatList = new ArrayList<Chat>();
    private List<Chat> newchatList = new ArrayList<Chat>();
    private ListView myLV;
    private MyAdapter adapter;
    private boolean newChat = false;
    public static ChatActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        setupActionBar();
        inti();
    }

    @Override
    protected void onResume() {
        mContext = this;
        cannelNotification(0);
        super.onResume();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *初始化
     */
    private void inti(){
        String email = getIntent().getStringExtra("email");
        friend = MainActivity.app.getFriend(email);
        setTitle(friend.getUsername());
        MessageFragment.clearRaw(friend.getEmail());
        if("".equals(friend.getUsername()))finish();
        Button btnSend = (Button)findViewById(R.id.btn_right);
        btnSend.setOnClickListener(this);
        Button btnimgae = (Button)findViewById(R.id.btn_left);
        btnimgae.setOnClickListener(this);
        myLV = (ListView)findViewById(R.id.lv_chat);
//        chatList = MainActivity.app.getChatOnMail(friend.getEmail());
        chatList = MailMessageUtil.getChatList(MainActivity.app.getInboxEmail(),email);
        intiNewChat();
        intiData();
        chatList.removeAll(newchatList);
        chatList.addAll(newchatList);
        Collections.sort(chatList);
        adapter = new MyAdapter(this);
        myLV.setAdapter(adapter);
        myLV.setSelection(adapter.getCount()-1);
    }

    private void cannelNotification(int id){
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(id);
    }

    private void intiData(){
        InputUtil<Chat> inputUtil = new InputUtil<Chat>();
        List<Chat> localChat = inputUtil.readListFromSdCard(MainActivity.app.getLocalPath()+friend.getEmail());
        if(localChat != null){
            chatList.removeAll(localChat);
            chatList.addAll(localChat);
        }
    }

    private void saveData(){
        OutputUtil<Chat> outputUtil = new OutputUtil<Chat>();
        outputUtil.writeListIntoSDcard(MainActivity.app.getLocalPath()+friend.getEmail(),chatList);
        if(newChat){
            InputUtil<String> inputUtil = new InputUtil<String>();
            List<String> list = inputUtil.readListFromSdCard(MainActivity.app.getLocalPath()+"newChatList");
            if(null !=list){
                if(!list.contains(friend.getUsername()))list.add(friend.getUsername());
            }
            else {
                list = new ArrayList<String>();
                list.add(friend.getUsername());
            }
            OutputUtil<String> stringOutputUtil = new OutputUtil<String>();
            stringOutputUtil.writeListIntoSDcard(MainActivity.app.getLocalPath()+"newChatList",list);
        }
    }

    private void intiNewChat(){
        InputUtil<Chat> inputUtil = new InputUtil<Chat>();
        List<Chat> list = inputUtil.readListFromSdCard(MainActivity.app.getLocalPath()+friend.getEmail()+"_new");
        if(null != list)newchatList = list;
    }

    private void saveNewChat(){
        if(newchatList.size()<1)return;
        OutputUtil<Chat> outputUtil = new OutputUtil<Chat>();
        outputUtil.writeListIntoSDcard(MainActivity.app.getLocalPath()+friend.getEmail()+"_new",newchatList);
        MainActivity.app.saveChatToEmail(friend.getEmail());
    }

    /**
     *添加消息
     */
    public void addChat(Chat chat){
        newChat = true;
        chatList.add(chat);
        newchatList.add(chat);
        adapter.notifyDataSetChanged();
        myLV.setSelection(adapter.getCount()-1);
    }
    @Override
    public void onClick(View v) {
        EditText etMeg = (EditText)findViewById(R.id.et_meg);
        String text = etMeg.getText().toString();
        switch (v.getId()){
            case R.id.btn_left:
                onImageActivity();
                break;
            case R.id.btn_right:
                if("".equals(text)){
                    ToastUtil.show(this,"请输入内容！");
                    return;
                }
                Chat rm = new Chat(true,text);
                send(rm);
                break;
            default:
        }
    }

    private void send(final Chat chat){
        final MyApplication app = (MyApplication)getApplication();
        String type = "online";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("name",this.friend.getUsername());
        jsonObject.put("email",this.friend.getEmail());
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                addChat(chat);
                EditText etMeg = (EditText)findViewById(R.id.et_meg);
                etMeg.setText("");
                if("true".equals(str)) {
                    sendChat(chat);
                }
                else {
                    chat.setStatus(Chat.STATUS_EMAIL);
                    String body = new String(MailMessageUtil.getChatStringBuilder(chat,friend.getEmail()));
                    EmailSender.sendMail(friend.getEmail(),MailMessageUtil.SUBJECT_MAIL_CHAT,body,null);

                    //saveData();
//                    MainActivity.app.emailChat(friend.getEmail());
//                    AlertDialog.Builder normalDialog =
//                            new AlertDialog.Builder(ChatActivity.this);
////                    normalDialog.setIcon(R.drawable);
//                    normalDialog.setTitle("提示").setMessage("对方不在线，您要?");
//                    normalDialog.setPositiveButton("继续发送",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    sendChat(chat);
//                                }
//                            });
//                    normalDialog.setNeutralButton("邮件发送",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(ChatActivity.this,SendEmailActivity.class);
//                                    intent.putExtra("text",chat.getText());
//                                    intent.putExtra("name",username);
//                                    startActivity(intent);
//                                }
//                            });
//                    normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // ...To-do
//                        }
//                    });
//                    // 创建实例并显示
//                    normalDialog.show();
//                    ToastUtil.show(getApplicationContext(),str);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                ToastUtil.show(getApplicationContext(),str);
            }
        });
    }

    private void sendChat(Chat chat){
        chat.setStatus(Chat.STATUS_IM);
        String type = "chat";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("to",this.friend.getEmail());
        jsonObject.put("text",chat.getText());
        jsonObject.put("time",chat.getTime());
        jsonObject.put("chatType",chat.getType());
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)) {
//                    ToastUtil.show(getApplicationContext(),"发送成功");
                }
                else {
                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(ChatActivity.this);
                    dialog.setTitle("title");
                    dialog.setMessage(str);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                ToastUtil.show(getApplicationContext(),str);
            }
        });
    }

    @Override
    protected void onPause() {
        mContext = null;
        saveData();
        saveNewChat();
        if(chatList.size()>0) {
            MessageFragment.addMessage(friend.getEmail(), chatList.get(chatList.size() - 1).getText(), newChat);
            MessageFragment.clearRaw(friend.getEmail());
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mContext = null;
        super.onDestroy();
    }

    /**
     * 消息适配器
     */
    class MyAdapter extends BaseAdapter{
        private Context context;

        public MyAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return chatList.size();
        }

        @Override
        public Object getItem(int position) {
            return chatList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //获取控件
            convertView = View.inflate(this.context,R.layout.list_item_chat,null);
            RelativeLayout left = (RelativeLayout)convertView.findViewById(R.id.left);
            RelativeLayout right = (RelativeLayout)convertView.findViewById(R.id.right);
            TextView textLeft = (TextView)convertView.findViewById(R.id.text_left);
            TextView textRight = (TextView)convertView.findViewById(R.id.text_right);
            ImageView imgLeft = (ImageView)convertView.findViewById(R.id.img_chat_left);
            ImageView imgRight = (ImageView)convertView.findViewById(R.id.img_chat_right);

            //获取消息数据
            Chat m = chatList.get(position);
            if(m.isMyself()){
                //发送的消息（在右边显示）
                if(m.getType().equals(Chat.TYPE_TEXT)){
                    textRight.setText(m.getText());
                }
                else if(m.getType().equals(Chat.TYPE_IMAGE)){
                    ImageView imageRight = (ImageView)convertView.findViewById(R.id.image_right);
                    Picasso.with(getParent())
                            .load(Constant.IMAGE_URL+m.getTime())
                            .into(imageRight, new Callback() {
                                @Override
                                public void onSuccess() {
                                    adapter.notifyDataSetChanged();
                                    myLV.setSelection(adapter.getCount()-1);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                    textRight.setVisibility(View.GONE);
                    imageRight.setVisibility(View.VISIBLE);
                }
                left.setVisibility(View.GONE);
                right.setVisibility(View.VISIBLE);
                //设置头像
                Picasso.with(getParent())
                        .load(MainActivity.app.getHeadFile())
                        .into(imgRight);
                if(m.getStatus()==Chat.STATUS_EMAIL){
                    convertView.findViewById(R.id.img_tip).setVisibility(View.VISIBLE);
                }else convertView.findViewById(R.id.img_tip).setVisibility(View.GONE);
            }else{
                //接收的消息（在左边显示）
                if(m.getType().equals(Chat.TYPE_TEXT)){
                    textLeft.setText(m.getText());
                }
                else if(m.getType().equals(Chat.TYPE_IMAGE)){
                    ImageView imageLeft = (ImageView)convertView.findViewById(R.id.image_left);
                    Picasso.with(getParent())
                            .load(Constant.IMAGE_URL+m.getTime())
                            .into(imageLeft, new Callback() {
                                @Override
                                public void onSuccess() {
                                    adapter.notifyDataSetChanged();
                                    myLV.setSelection(adapter.getCount()-1);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                    textLeft.setVisibility(View.GONE);
                    imageLeft.setVisibility(View.VISIBLE);
                }
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.GONE);
                //设置头像
                Picasso.with(getParent())
                        .load(MainActivity.app.getHeadFile(friend.getEmail()))
                        .into(imgLeft);
            }
            return convertView;
        }
    }

    private void onImageActivity(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(data != null) {
                    sendImage(data.getData());
                }
                break;
        }
    }

    private void sendImage(final Uri uri){
        Bitmap bm = null;
        String path = null;
        ContentResolver resolver = getContentResolver();
        try {
            bm = MediaStore.Images.Media.getBitmap(resolver,uri);
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(column_index);
                cursor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ToastUtil.show(path);
        Chat chat = new Chat(true,"[图片]");
        chat.setType(Chat.TYPE_IMAGE);
        addChat(chat);
        sendChat(chat);
        File file = new File(path);
        EmailSender.sendMail(friend.getEmail(),MailMessageUtil.SUBJECT_MAIL_IMAGE
                ,new String(MailMessageUtil.getChatStringBuilder(chat,friend.getEmail()))
                ,file);
        if(MainActivity.app.debugable)Log.e(file.getAbsolutePath(),"发送图片");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","image");
        jsonObject.put("id",chat.getTime());
        MyHttp.post(jsonObject,file,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)) {
                    ToastUtil.show("发送成功！");
                }
                else{
                    ToastUtil.show("发送失败！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                ToastUtil.show(str);
            }
        });
    }


}
