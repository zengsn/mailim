package mailim.mailim.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Chat;
import mailim.mailim.util.MyHttp;
import mailim.mailim.R;
import mailim.mailim.util.Constant;
import mailim.mailim.util.ToastUtil;

import static mailim.mailim.util.MyApplication.*;

public class PulseService extends Service {
    private boolean run = true;

    public PulseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        IBinder result = null;
        if ( null == result ) result = new MyBinder() ;
        ToastUtil.show("onBind");
        startThread();
        return result;
    }

    public class MyBinder extends Binder {
        //此方法是为了可以在Acitity中获得服务的实例
        public PulseService getService() {
            return PulseService.this;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startThread();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        run = false;
        ToastUtil.show(getInstance(),"服务结束");
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
                ToastUtil.show(getInstance(),str);
                if("true".equals(str)) {
                }
                else if("false".equals(str)){
                    ToastUtil.show(getInstance(),"获取失败！");
                }
                else if("[]".equals(str)){
                }
                else if("null".equals(str)){
                }
                else if("getChat".equals(str)){
                    ToastUtil.show(getInstance(),"无法获取新消息！");
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
//                                ToastUtil.show(MyApplication.getInstance(),chat.getTime());
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
                getInstance().setLogin(false);
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
//                ToastUtil.show(MyApplication.getInstance(),str);
                if("true".equals(str)) {
                }
                else if("false".equals(str)){
                    ToastUtil.show(getInstance(),"获取失败！");
                }
                else if("[]".equals(str)){
                }
                else if("null".equals(str)){
                }
                else if("newFriend".equals(str)){
                    ToastUtil.show(getInstance(),"无法获取新消息！");
                }
                else {
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if(res.getBoolean("json")) {
                            JSONObject object = JSONObject.parseObject(res.getString("data"));
                            List<JSONObject> list = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : object.entrySet()) {
                                list.add(JSON.parseObject(entry.getValue().toString()));
                            }
                            Intent intent;
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
                getInstance().setLogin(false);
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
//                    type = "newFriend";
//                    jsonObject = new JSONObject();
//                    jsonObject.put("type",type);
//                    MyHttp.post(jsonObject,asyncHttpResponseHandler1);
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }.start();
    }
}
