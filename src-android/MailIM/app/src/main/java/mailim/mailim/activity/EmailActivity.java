package mailim.mailim.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import mailim.mailim.util.DateUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.R;
import mailim.mailim.entity.Email;

public class EmailActivity extends AppCompatActivity {
    private TextView fromaddr;
    private TextView time;
    private TextView subject;
    private WebView text;
    private Button send;

    private Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        setupActionBar();

        intiVeiw();
        setViewContex();
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

    @Override
    protected void onPause() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.onPause();
    }

    private void intiVeiw(){
        email = (Email)getIntent().getSerializableExtra("email");

        fromaddr = (TextView)findViewById(R.id.email_fromaddr);
        time = (TextView)findViewById(R.id.email_time);
        subject = (TextView)findViewById(R.id.email_subject);
        text = (WebView)findViewById(R.id.email_text);
        send = (Button)findViewById(R.id.email_btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailActivity.this,SendEmailActivity.class);
                intent.putExtra("to",email.getEmailAddr());
                startActivity(intent);
                finish();
            }
        });
    }

    private void setViewContex(){
        if(!email.isEmpty()){
//            Toast.makeText(app.getApplicationContext(),"not null",Toast.LENGTH_SHORT).show();
            fromaddr.setText(email.getEmailAddr());
            //time.setText(email.getSendDate().toString());
            time.setText(DateUtil.DateToString(email.getSendDate()));
            subject.setText(email.getSubject());
            WebSettings settings = text.getSettings();
//支持javascript
            settings.setJavaScriptEnabled(true);
// 设置可以支持缩放
            settings.setSupportZoom(true);
// 设置出现缩放工具
            settings.setBuiltInZoomControls(true);
//扩大比例的缩放
            settings.setUseWideViewPort(true);
//自适应屏幕
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setLoadWithOverviewMode(true);
            if(email.isMultipart()) {
                settings.setSupportZoom(true);
                settings.setTextZoom(400);
            }
            text.loadDataWithBaseURL(null,email.getContent().replace("\n","<br>"),"text/html","UTF-8",null);
        }
    }
}
