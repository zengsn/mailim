package mailim.mailim.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import mailim.mailim.R;
import mailim.mailim.entity.User;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.ToastUtil;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_username;
    private boolean isFriend = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setupActionBar();

        intiView();
    }

    private void intiView(){
        tv_username = (TextView)findViewById(R.id.userinfo_username);
        TextView tv_sex = (TextView) findViewById(R.id.userinfo_sex);
        TextView tv_email = (TextView) findViewById(R.id.userinfo_email);
        TextView tv_qianming = (TextView) findViewById(R.id.userinfo_qianming);
        Button btn_chat = (Button) findViewById(R.id.userinfo_btn_chat);

        btn_chat.setOnClickListener(this);

        User user = new User();

        Intent intent = getIntent();
        if(intent.getBooleanExtra("user",false)){
            user = (User)intent.getSerializableExtra("userdata");
        }
        else {
//            user.setUsername(intent.getStringExtra("username"));
            user.setSex(intent.getBooleanExtra("sex",true));
            user.setEmail(intent.getStringExtra("email"));
//            user.setQianming(intent.getStringExtra("qianming"));
        }

//        tv_username.setText(user.getUsername());
//        if(!MyApplication.getInstance().isFriend(user.getUsername())){
//            isFriend = false;
//            btn_chat.setText("添加好友");
//        }
//        if(user.isSex()) {
//            tv_sex.setText("男");
//        }
//        else {
//            tv_sex.setText("女");
//        }
        tv_email.setText(user.getEmail());
//        tv_qianming.setText(user.getQianming());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinfo_btn_chat:
                if (isFriend) onChat();
                else onAddFriend();
                break;
        }
    }

    private void onAddFriend(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","addFriend");
        jsonObject.put("name",tv_username.getText());
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)) {
                    ToastUtil.show(UserInfoActivity.this,"已发送请求");
                    finish();
                }
                else if("false".equals(str)){
                    ToastUtil.show(UserInfoActivity.this,"失败！");
                }
                else if("[]".equals(str)){
                    ToastUtil.show(UserInfoActivity.this,"空");
                }
                else if("addFriend".equals(str)){
                    ToastUtil.show(UserInfoActivity.this,"服务器无法接收");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.show(UserInfoActivity.this,"failure");
            }
        });
    }

    private void onChat(){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("username",tv_username.getText());
        startActivity(intent);
        finish();
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

}
