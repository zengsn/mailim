package mailim.mailim.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiApp();
        intiView();
        if(getIntent().getBooleanExtra("auto",true))checkAutoLogin();
    }

    public void intiApp(){
        MainActivity.app = (MyApplication)getApplication();
    }

    private void checkAutoLogin(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sp.getString("username","");
        String password = sp.getString("password","");
        if(sp.getBoolean("autoLogin",false)){
            MainActivity.app.getMyUser().setUsername(username);
            MainActivity.app.getMyUser().setPassword(password);
            login(username,password,true);
        }
    }

    private void intiView(){
        et_username = (EditText)findViewById(R.id.login_et_username);
        et_psw = (EditText)findViewById(R.id.login_et_userpsw);
        btn_login = (Button)findViewById(R.id.login_btn_login);
        tv_register = (TextView)findViewById(R.id.login_register);
        cb_save_pwd = (CheckBox)findViewById(R.id.login_save_pwd);
        cb_autoLogin = (CheckBox)findViewById(R.id.login_auto_login);

        btn_login.setOnClickListener(this);
        tv_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_register.setOnClickListener(this);

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
                                et_username.setError("请输入用户名！");
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
        et_username.setText(sp.getString("username",""));
        et_psw.setText(sp.getString("password",""));
        cb_save_pwd.setChecked(sp.getBoolean("savePassword",true));
        cb_autoLogin.setChecked(sp.getBoolean("autoLogin",true));
    }

    private void save(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username",et_username.getText().toString());
        editor.putBoolean("autoLogin",cb_autoLogin.isChecked());
        editor.putBoolean("savePassword",cb_save_pwd.isChecked());
        if(cb_save_pwd.isChecked())editor.putString("password",et_psw.getText().toString());
        else editor.remove("password");
        editor.apply();
    }

    public void login(String username, String password, final boolean auto){
        String type = "login";
        MainActivity.app.getMyUser().setUsername(username);
        MainActivity.app.getMyUser().setPassword(password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("false".equals(str)) {
                    ToastUtil.show(getApplication(),"登录失败！"+str);
                } else if("login".equals(str)) {
                    ToastUtil.show(getApplication(),"登录失败！"+str);
                } else if("[]".equals(str)) {
                    ToastUtil.show(getApplication(),"登录失败！"+str);
                } else {
                    MainActivity.app.setLogin(true);
//                    ToastUtil.show(getApplicationContext(), str);
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if ("user".equals(res.getString("type"))) {
                            User user = MainActivity.app.getMyUser();
                            user.setUsername(res.getString("username"));
                            user.setSex(res.getBoolean("sex"));
                            user.setEmail(res.getString("email"));
                            user.setEmailpwd(res.getString("emailpwd"));
                            user.setQianming(res.getString("qianming"));
                            if(!auto)save();
                            Intent intent1 = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent1);
                            MainActivity.app.login();
                            Intent intent2 = new Intent(getApplication(), PulseService.class);
                            startService(intent2);
                            LoginActivity.this.finish();
                        }
                    } catch (RuntimeException e) {
                        Log.e("Runtiem", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getApplicationContext(),R.string.tip_login_fail+":"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                MainActivity.app.setLogin(false);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                String username = et_username.getText().toString();
                String password = et_psw.getText().toString();
                login(username,password,false);
                break;
            case R.id.login_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
            default:
        }
    }
}