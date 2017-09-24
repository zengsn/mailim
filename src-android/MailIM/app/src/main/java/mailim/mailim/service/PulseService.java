package mailim.mailim.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URI;

import mailim.mailim.MyApplication;
import mailim.mailim.MySocket;
import mailim.mailim.R;
import mailim.mailim.util.Constant;
import mailim.mailim.util.DESUtil;
import mailim.mailim.util.ToastUtil;

public class PulseService extends Service {
    private MyApplication app;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    ToastUtil.show(PulseService.this,(String)msg.obj);
                    break;
                default:
            }
        }
    };

    public PulseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        app = (MyApplication)getApplication();
        startThread();
        return super.onStartCommand(intent,flags,startId);
    }

    private void startThread(){
        final boolean run = app.isLogin();
        final AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                app.setLogin(true);
                String str = new String(bytes);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = str;
                handler.sendMessage(msg);
                //LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(app.getApplicationContext(), R.string.tip_login_fail+":"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                app.setLogin(false);
            }
        };
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                while (run) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    String type = "pulse";
                    String username = app.getUsername();
                    String password = app.getPassword();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type",type);
                    jsonObject.put("username", username);
                    jsonObject.put("password",password);
                    String json = null;
                    try {
                        json = DESUtil.ENCRYPTMethod(jsonObject.toString(), Constant.KEY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RequestParams params = new RequestParams();
                    params.put("json",json);
                    SyncHttpClient client = new SyncHttpClient();
                    client.post("index.php", params, new ResponseHandlerInterface() {
                        @Override
                        public void sendResponseMessage(HttpResponse httpResponse) throws IOException {

                        }

                        @Override
                        public void sendStartMessage() {

                        }

                        @Override
                        public void sendFinishMessage() {

                        }

                        @Override
                        public void sendProgressMessage(long l, long l1) {

                        }

                        @Override
                        public void sendCancelMessage() {

                        }

                        @Override
                        public void sendSuccessMessage(int i, Header[] headers, byte[] bytes) {
                            String str = new String(bytes);
                            ToastUtil.show(getApplication(),str);
                        }

                        @Override
                        public void sendFailureMessage(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                        }

                        @Override
                        public void sendRetryMessage(int i) {

                        }

                        @Override
                        public URI getRequestURI() {
                            return null;
                        }

                        @Override
                        public Header[] getRequestHeaders() {
                            return new Header[0];
                        }

                        @Override
                        public void setRequestURI(URI uri) {

                        }

                        @Override
                        public void setRequestHeaders(Header[] headers) {

                        }

                        @Override
                        public void setUseSynchronousMode(boolean b) {

                        }

                        @Override
                        public boolean getUseSynchronousMode() {
                            return false;
                        }

                        @Override
                        public void setUsePoolThread(boolean b) {

                        }

                        @Override
                        public boolean getUsePoolThread() {
                            return false;
                        }

                        @Override
                        public void onPreProcessResponse(ResponseHandlerInterface responseHandlerInterface, HttpResponse httpResponse) {

                        }

                        @Override
                        public void onPostProcessResponse(ResponseHandlerInterface responseHandlerInterface, HttpResponse httpResponse) {

                        }

                        @Override
                        public void setTag(Object o) {

                        }

                        @Override
                        public Object getTag() {
                            return null;
                        }
                    });
                    //MySocket.post("index.php", params,asyncHttpResponseHandler);
                    //handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
