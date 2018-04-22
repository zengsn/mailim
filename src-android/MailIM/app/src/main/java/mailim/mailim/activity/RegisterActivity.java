package mailim.mailim.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import mailim.mailim.util.EmaiRecever;
import mailim.mailim.util.EmailUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.MyHttp;
import mailim.mailim.R;
import mailim.mailim.service.PulseService;
import mailim.mailim.util.Constant;
import mailim.mailim.util.DESUtil;
import mailim.mailim.util.ToastUtil;

/**
 * Created by zzh on 2017/8/20.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_psw;
    private EditText et_repsw;
    private EditText et_email;
    private EditText et_emailpsw;
    private Button btn_register;
    private TextView tv_login;

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
                    register();
                    waitingDialog.cancel();
                    onBackPressed();
                    finish();
                    break;
                case 3:
                    et_emailpsw.setError("邮箱或邮箱密码不正确");
                    clearFocus();
                    Log.e("test","test");
                    et_emailpsw.requestFocus();
                    waitingDialog.cancel();
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupActionBar();
        intiView();
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

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void intiView(){
        et_username = (EditText)findViewById(R.id.register_et_username);
        et_psw = (EditText)findViewById(R.id.register_et_userpsw);
        et_repsw = (EditText)findViewById(R.id.register_et_re_psw);
        et_email = (EditText)findViewById(R.id.register_et_email);
        et_emailpsw = (EditText)findViewById(R.id.register_et_emailpsw);
        btn_register = (Button) findViewById(R.id.register_btn_register);
        tv_login = (TextView)findViewById(R.id.register_login);

        tv_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);

        waitingDialog = new ProgressDialog(this);
    }

    private void save(String username, String password){
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
    }

    private void clearFocus(){
        View view = findViewById(R.id.register_null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    private boolean checkInput(){
        final String username = et_username.getText().toString();
        final String password = et_psw.getText().toString();
        final String repwd = et_repsw.getText().toString();
        final String email = et_email.getText().toString();
        final String emailpwd = et_emailpsw.getText().toString();
        if("".equals(username)){
            et_username.setError("请输入用户名");
            clearFocus();
            et_username.requestFocus();
            return false;
        }
        if("".equals(password)){
            et_psw.setError("请输入密码");
            clearFocus();
            et_psw.requestFocus();
            return false;
        }
        if(!repwd.equals(password)){
            et_repsw.setError("两次密码不一样");
            clearFocus();
            et_repsw.requestFocus();
            return false;
        }
        if("".equals(email)){
            et_email.setError("请输入邮箱");
            clearFocus();
            et_email.requestFocus();
            return false;
        }
        if(!EmailUtil.isEmail(email)){
            et_email.setError("邮箱格式错误");
            clearFocus();
            et_email.requestFocus();
            return false;
        }
        if("".equals(emailpwd)){
            et_emailpsw.setError("请输入邮箱密码");
            clearFocus();
            et_emailpsw.requestFocus();
            return false;
        }
        return true;
    }

    public void check(){
        final String email = et_email.getText().toString();
        final String emailpwd = et_emailpsw.getText().toString();
        if(!checkInput())return;
        waitingDialog.setTitle("正在注册");
        waitingDialog.setMessage("检查邮箱...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(true);
        waitingDialog.show();
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                android.os.Message msg = new android.os.Message();
                if(EmailUtil.checkEmail(email,emailpwd)) {
                    msg.what = 2;
                } else{
                    msg.what = 3;
                }
                handler.sendMessage(msg);
                super.run();
            }
        }.start();
    }

    private void register(){
        final String username = et_username.getText().toString();
        final String password = et_psw.getText().toString();
        final String email = et_email.getText().toString();
        String type = "register";
//        MyApplication.getInstance().getMyUser().setUsername(username);
        MyApplication.getInstance().getMyUser().setPassword(password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if ("true".equals(str)) {
                    MyApplication.getInstance().setLogin(true);
                    ToastUtil.show(getApplicationContext(), str);
                    save(username, password);
                    Intent intent = new Intent(getApplication(), PulseService.class);
                    startService(intent);
                } else ToastUtil.show(getApplication(), "注册失败！" + str);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.show(getApplication(), "注册失败！!");
                MyApplication.getInstance().setLogin(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn_register:
                check();
                break;
            case R.id.register_login:
                onBackPressed();
                finish();
            default:
        }
    }
}