package mailim.mailim.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import mailim.mailim.util.Constant;
import mailim.mailim.util.DESUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.R;
import mailim.mailim.util.EmailSender;
import mailim.mailim.util.EmailUtil;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.ToastUtil;

public class SendEmailActivity extends AppCompatActivity {
    private EditText to;
    private EditText title;
    private EditText body;
    private Button send;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(MainActivity.app,(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        setupActionBar();
        inti();
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

    private void inti(){
        to = (EditText)findViewById(R.id.send_email_to);
        title = (EditText)findViewById(R.id.send_email_title);
        body = (EditText)findViewById(R.id.send_email_body);
        send = (Button)findViewById(R.id.send_email_btn_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String toAddr = intent.getStringExtra("to");
        String text = intent.getStringExtra("text");
        if(!"".equals(name) && name != null){
            getEmail(name);
        }
        else if(!"".equals(toAddr) && toAddr != null){
            to.setText(toAddr);
            title.setFocusable(true);
            title.requestFocus();
        }
        if(!"".equals(text) && text != null){
            title.setText(text);
            body.setText(text);
            body.setFocusable(true);
            body.requestFocus();
        }
    }

    private void getEmail(String name){
        final MyApplication app = (MyApplication)getApplication();
        final String username = app.getMyUser().getUsername();
        final String password = app.getMyUser().getPassword();
        String type = "getEmail";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("username", username);
        jsonObject.put("password",password);
        jsonObject.put("name",name);
        String json = null;
        try {
            json = DESUtil.ENCRYPTMethod(jsonObject.toString(), Constant.KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("json",json);
        MyHttp.post("index.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if(!"false".equals(str) && !"".equals(str)) {
                    to.setText(str);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                ToastUtil.show(getApplicationContext(),str);
            }
        });
    }

    private void send(){
        final String to = this.to.getText().toString();
        if(!EmailUtil.isEmail(to)){
            ToastUtil.show(MainActivity.app,"邮箱地址格式有误！");
            return;
        }
        final String title = this.title.getText().toString();
        final String body = this.body.getText().toString();
        EmailSender.sendMail(to,title,body,null);
        //ToastUtil.show(MainActivity.app,"已发送");
        finish();
    }
}
