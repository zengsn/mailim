package mailim.mailim.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import mailim.mailim.MyApplication;
import mailim.mailim.MySocket;
import mailim.mailim.R;
import mailim.mailim.service.PulseService;
import mailim.mailim.util.Constant;
import mailim.mailim.util.DESUtil;
import mailim.mailim.util.ToastUtil;

/**
 * Created by zzh on 2017/8/20.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_psw;
    private Button btn_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        MyApplication app = (MyApplication)getApplication();
//        Toast.makeText(app.getApplicationContext(),"开始",Toast.LENGTH_SHORT).show();
        intiView();
    }

    private void intiView(){
        et_username = (EditText)findViewById(R.id.login_et_username);
        et_psw = (EditText)findViewById(R.id.login_et_userpsw);
        btn_login = (Button)findViewById(R.id.login_btn_login);

        btn_login.setOnClickListener(this);
    }

    private void save(){
    }

    public boolean checkEmail(){
        return false;
    }

    public void login(){
        final MyApplication app = (MyApplication)getApplication();
//        Toast.makeText(app.getApplicationContext(),"login",Toast.LENGTH_LONG).show();
        String username = et_username.getText().toString();
        String password = et_psw.getText().toString();
        if("".equals(username)) {
            Toast.makeText(app.getApplicationContext(), R.string.tip_null_username, Toast.LENGTH_LONG).show();
            return;
        }
        if("".equals(password)) {
            Toast.makeText(app.getApplicationContext(), R.string.tip_null_password, Toast.LENGTH_LONG).show();
            return;
        }
        String type = "login";
        app.setUsername(username);
        app.setPassword(password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("username", username);
        jsonObject.put("password",password);
        String json = null;
        try {
            json = DESUtil.ENCRYPTMethod(jsonObject.toString(),Constant.KEY);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(app.getContext(),e.getMessage());
        }
        RequestParams params = new RequestParams();
        params.put("json",json);
        MySocket.post("index.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                app.setLogin(true);
                String str = new String(bytes);
                ToastUtil.show(app.getApplicationContext(),str);
                Intent intent = new Intent(getApplication(), PulseService.class);
                startService(intent);
                //LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(app.getApplicationContext(),R.string.tip_login_fail+":"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                app.setLogin(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
//                MyApplication app = (MyApplication)getApplication();
//                Toast.makeText(app.getApplicationContext(),"点击",Toast.LENGTH_SHORT).show();
                login();
                break;
            default:
        }
    }
}