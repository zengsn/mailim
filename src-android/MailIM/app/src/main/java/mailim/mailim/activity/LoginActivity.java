package mailim.mailim.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import mailim.mailim.entity.User;
import mailim.mailim.util.EmailUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.MyHttp;
import mailim.mailim.R;
import mailim.mailim.service.PulseService;
import mailim.mailim.util.ToastUtil;

/**
 * Created by zzh on 2017/8/20.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_psw;
    private Button btn_login;
    private TextView tv_register;
    private CheckBox cb_save_pwd;
    private CheckBox cb_autoLogin;

    private ProgressDialog waitingDialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    ToastUtil.show(msg.obj.toString());
                    break;
                case 2:
                    loginToServer();
                    waitingDialog.dismiss();
                    ToastUtil.show("登录成功！");
                    Intent intent1 = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent1);
                    MyApplication.getInstance().login();
                    LoginActivity.this.finish();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    ToastUtil.show("登录失败！");
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiView();
        if(getIntent().getBooleanExtra("auto",true))checkAutoLogin();
    }

    private void intiView(){
        et_username = (EditText)findViewById(R.id.login_et_username);
        et_psw = (EditText)findViewById(R.id.login_et_userpsw);
        btn_login = (Button)findViewById(R.id.login_btn_login);
        cb_save_pwd = (CheckBox)findViewById(R.id.login_save_pwd);
        cb_autoLogin = (CheckBox)findViewById(R.id.login_auto_login);

        btn_login.setOnClickListener(this);

        cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_save_pwd.setChecked(true);
                }
            }
        });
        cb_save_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    cb_autoLogin.setChecked(false);
                }
            }
        });

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                }
                else{
                    switch (v.getId()) {
                        case R.id.login_et_username:
                            if (et_username.length() == 0) {
                                et_username.setError("请输入邮箱！");
                            }
                            break;
                        case R.id.login_et_userpsw:
                            if (et_psw.length() == 0) {
                                et_psw.setError("请输入密码！");
                            }
                            break;
                    }
                }
            }
        };
        et_username.setOnFocusChangeListener(focusChangeListener);
        et_psw.setOnFocusChangeListener(focusChangeListener);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        et_username.setText(sp.getString("email",""));
        et_psw.setText(sp.getString("password",""));
        cb_save_pwd.setChecked(sp.getBoolean("savePassword",true));
        cb_autoLogin.setChecked(sp.getBoolean("autoLogin",true));

        waitingDialog = new ProgressDialog(this);
    }

    private void save(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email",et_username.getText().toString());
        editor.putBoolean("autoLogin",cb_autoLogin.isChecked());
        editor.putBoolean("savePassword",cb_save_pwd.isChecked());
        if(cb_save_pwd.isChecked())editor.putString("password",et_psw.getText().toString());
        else editor.remove("password");
        editor.apply();
    }

    private void checkAutoLogin(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean("autoLogin",false)){
            String email = sp.getString("email","");
            String password = sp.getString("password","");
            login(email,password);
        }
    }

    private void loginToServer(){
        String type = "login";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                ToastUtil.show(getApplication(), str);
                if("false".equals(str)) {
                    ToastUtil.show(getApplication(),"登录失败！1"+str);
                } else if("login".equals(str)) {
                    ToastUtil.show(getApplication(),"登录失败！"+str);
                } else if("[]".equals(str)) {
                    ToastUtil.show(getApplication(),"登录失败！"+str);
                } else {
                    MyApplication.getInstance().setLogin(true);
                    Log.e("debug","登录成功："+str);
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if ("user".equals(res.getString("type"))) {
                            User user = MyApplication.getInstance().getMyUser();
                            user.setNickname(res.getString("nickname"));
                            user.setSex(res.getBoolean("sex"));
                            user.setAge(res.getInteger("age"));
                            user.setCity(res.getString("city"));
                            user.setMotto(res.getString("motto"));
                        }
                    } catch (RuntimeException e) {
                        Log.e("Runtiem", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.show("失败"+R.string.tip_login_fail+":"+throwable.getMessage());
                Log.e("test", throwable.getMessage());
                MyApplication.getInstance().setLogin(false);
            }
        });
    }

    public void login( final String email,final String password){
        MyApplication.getInstance().getMyUser().setEmail(email);
        MyApplication.getInstance().getMyUser().setPassword(password);
        waitingDialog.setTitle("正在登录");
        waitingDialog.setMessage("登录中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(true);
        waitingDialog.show();
        new Thread(){
            @Override
            public void run() {
                Log.e("debug","login()_run()_start");
                Looper.prepare();
                android.os.Message msg = new android.os.Message();
                if(EmailUtil.checkEmail(email,password)) {
                    msg.what = 2;
                    Log.e("debug","checkEmail_OK");
                } else{
                    msg.what = 3;
                    Log.e("debug","checkEmail_error");
                }
                handler.sendMessage(msg);
                Log.e("debug","login()_run()_end");
                super.run();
            }
        }.start();
        Log.e("debug","login()_end");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                String email = et_username.getText().toString();
                String password = et_psw.getText().toString();
                save();
                login(email,password);
                break;
//            case R.id.login_register:
//                Intent intent = new Intent(this,RegisterActivity.class);
//                startActivity(intent);
            default:
        }
    }
}