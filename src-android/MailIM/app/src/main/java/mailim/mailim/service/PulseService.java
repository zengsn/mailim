package mailim.mailim.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.activity.MyInfoActivity;
import mailim.mailim.activity.UserInfoActivity;
import mailim.mailim.entity.Chat;
import mailim.mailim.entity.Friend;
import mailim.mailim.entity.User;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.MyHttp;
import mailim.mailim.R;
import mailim.mailim.util.Constant;
import mailim.mailim.util.DESUtil;
import mailim.mailim.util.ToastUtil;

public class PulseService extends Service {
    private boolean run = true;

    public PulseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MainActivity.app = (MyApplication)getApplication();
        startThread();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        run = false;
        //ToastUtil.show(MainActivity.app,"服务结束");
        super.onDestroy();
    }

    private void startThread(){
        final AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if(MainActivity.mContext != null){
                    TextView textView = (TextView)MainActivity.mContext.findViewById(R.id.tip_net);
                    textView.setVisibility(View.GONE);
                }
                String str = new String(bytes);
//                ToastUtil.show(MainActivity.app,str);
                if("true".equals(str)) {
                }
                else if("false".equals(str)){
                    ToastUtil.show(MainActivity.app,"获取失败！");
                }
                else if("[]".equals(str)){
                }
                else if("null".equals(str)){
                }
                else if("getChat".equals(str)){
                    ToastUtil.show(MainActivity.app,"无法获取新消息！");
                }
                else {
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if(res.getBoolean("json")) {
                            JSONObject object = JSONObject.parseObject(res.getString("data"));
                            List<JSONObject> list = new ArrayList<JSONObject>();
                            for (Map.Entry<String, Object> entry : object.entrySet()) {
                                list.add(JSON.parseObject(entry.getValue().toString()));
                            }
                            Chat chat = null;
                            Intent intent = null;
                            for (JSONObject obj : list) {
                                chat = new Chat(false,obj.getString("text"));
                                chat.setTime(obj.getString("time"));
                                chat.setType(obj.getString("type"));
//                                ToastUtil.show(MainActivity.app,chat.getTime());
                                intent = new Intent(Constant.MYACTION);
                                intent.putExtra("type","chat");
                                intent.putExtra("username",obj.getString("friendname"));
                                intent.putExtra("chat",chat);
                                sendBroadcast(intent);
                            }
                        }
                    }catch (NullPointerException e){
                        Log.e("空指针",e.getMessage());
                    }catch (Exception e){
                        Log.e("未知异常",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if(MainActivity.mContext != null) {
                    TextView textView = (TextView) MainActivity.mContext.findViewById(R.id.tip_net);
                    textView.setVisibility(View.VISIBLE);
                }
                MainActivity.app.setLogin(false);
            }
        };
        final AsyncHttpResponseHandler asyncHttpResponseHandler1 = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if(MainActivity.mContext != null){
                    TextView textView = (TextView)MainActivity.mContext.findViewById(R.id.tip_net);
                    textView.setVisibility(View.GONE);
                }
                String str = new String(bytes);
//                ToastUtil.show(MainActivity.app,str);
                if("true".equals(str)) {
                }
                else if("false".equals(str)){
                    ToastUtil.show(MainActivity.app,"获取失败！");
                }
                else if("[]".equals(str)){
                }
                else if("null".equals(str)){
                }
                else if("newFriend".equals(str)){
                    ToastUtil.show(MainActivity.app,"无法获取新消息！");
                }
                else {
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if(res.getBoolean("json")) {
                            JSONObject object = JSONObject.parseObject(res.getString("data"));
                            List<JSONObject> list = new ArrayList<JSONObject>();
                            for (Map.Entry<String, Object> entry : object.entrySet()) {
                                list.add(JSON.parseObject(entry.getValue().toString()));
                            }
                            Intent intent = null;
                            for (JSONObject obj : list) {
                                intent = new Intent(Constant.MYACTION);
                                intent.putExtra("type","friend");
                                intent.putExtra("username",obj.getString("username"));
                                sendBroadcast(intent);
                            }
                        }
                    }catch (NullPointerException e){
                        Log.e("空指针",e.getMessage());
                    }catch (Exception e){
                        Log.e("未知异常",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if(MainActivity.mContext != null) {
                    TextView textView = (TextView) MainActivity.mContext.findViewById(R.id.tip_net);
                    textView.setVisibility(View.VISIBLE);
                }
                MainActivity.app.setLogin(false);
            }
        };
        new Thread(){
            @Override
            public void run() {
                while (run) {
                    String type = "getChat";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type",type);
                    MyHttp.post(jsonObject,asyncHttpResponseHandler);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    type = "newFriend";
                    jsonObject = new JSONObject();
                    jsonObject.put("type",type);
                    MyHttp.post(jsonObject,asyncHttpResponseHandler1);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
