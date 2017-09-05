package mailim.mailim;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.net.Socket;

import mailim.mailim.activity.ChatActivity;
import mailim.mailim.entity.Message;
import mailim.mailim.util.Constant;
import mailim.mailim.util.DESUtil;

/**
 * Created by zzh on 2017/8/20.
 */
public class MySocket extends Socket {
    private String serverAddr = Constant.Server;
    private int serverPort = 80;
    private Object data;
    private String path;
    private ChatActivity chatActivity;

    public MySocket(ChatActivity chatActivity, String text){
        this.chatActivity = chatActivity;
        try {
            text = DESUtil.ENCRYPTMethod(text,"zzhzzh");
        } catch (Exception e) {
            e.printStackTrace();
        }
        path = "https://"+Constant.Baseurl+"?user="+text;
        Toast.makeText(chatActivity,path,Toast.LENGTH_SHORT).show();
    }

    public boolean send(){
        final AsyncHttpClient ckient = new AsyncHttpClient();
        ckient.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                //Toast.makeText(context,new String(bytes),Toast.LENGTH_SHORT).show();
                chatActivity.addMessage(new Message(false,new String(bytes)));
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(chatActivity,new String(bytes),Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }
}
