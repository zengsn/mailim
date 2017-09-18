package mailim.mailim.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mailim.mailim.MyApplication;
import mailim.mailim.R;
import mailim.mailim.util.EmailSender;
import mailim.mailim.util.EmailUtil;

public class SendEmailActivity extends Activity {
    private MyApplication app;
    private EditText to;
    private EditText title;
    private EditText body;
    private Button send;
    private EmailSender emailSender;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(app.getApplicationContext(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        inti();
    }

    private void inti(){
        app = (MyApplication)getApplication();
        emailSender = new EmailSender();

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
        String toAddr = intent.getStringExtra("to");
        if(!"".equals(toAddr)){
            to.setText(toAddr);
            title.setFocusable(true);
            title.requestFocus();
        }
    }

    private void send(){
        final String to = this.to.getText().toString();
        if(!EmailUtil.isEmail(to)){
            Toast.makeText(app.getApplicationContext(),"邮箱地址格式有误！",Toast.LENGTH_SHORT).show();
            return;
        }
        final String title = this.title.getText().toString();
        final String body = this.body.getText().toString();
        final String form = app.getEmail();
        final String pwd = app.getEmailpwd();
        final String server = app.getSmtpServer();
        new Thread(){
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                try {
                    msg.obj = new String("已发送");
                    handler.sendMessage(msg);
                    emailSender.sendMail(to,form,server,"zhangzhanhong218",pwd,title,body,null);
                    finish();
                } catch (Exception e) {
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
