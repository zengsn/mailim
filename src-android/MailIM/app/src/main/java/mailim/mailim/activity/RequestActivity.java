package mailim.mailim.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mailim.mailim.R;
import mailim.mailim.entity.Friend;
import mailim.mailim.entity.User;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.ToastUtil;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_username;
    private TextView tv_sex;
    private TextView tv_email;
    private TextView tv_qianming;
    private Button btn_agree;
    private Button btn_disagree;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        setupActionBar();
        intiView();
        getUser(getIntent().getStringExtra("username"));
    }

    private void intiView() {
        tv_username = (TextView) findViewById(R.id.request_username);
        tv_sex = (TextView) findViewById(R.id.request_sex);
        tv_email = (TextView) findViewById(R.id.request_email);
        tv_qianming = (TextView) findViewById(R.id.request_qianming);
        btn_agree = (Button) findViewById(R.id.request_btn_agree);
        btn_disagree = (Button) findViewById(R.id.request_btn_disagree);

        btn_agree.setOnClickListener(this);
        btn_disagree.setOnClickListener(this);
    }

    private void setData(){
//        tv_username.setText(user.getUsername());
        if(user.isSex()) {
            tv_sex.setText("男");
        }
        else {
            tv_sex.setText("女");
        }
        tv_email.setText(user.getEmail());
//        tv_qianming.setText(user.getQianming());
    }

    public void getUser(final String username) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "getUser");
        jsonObject.put("name", username);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if ("true".equals(str)) {
                } else if ("false".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "获取失败！");
                } else if ("[]".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "用户不存在！");
                } else if ("getUser".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "无法查看用户！");
                } else {
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if ("user".equals(res.getString("type"))) {
                            user = new User();
//                            user.setUsername(res.getString("username"));
                            user.setSex(res.getBoolean("sex"));
                            user.setEmail(res.getString("email"));
//                            user.setQianming(res.getString("qianming"));
                            setData();
                        }
                    } catch (RuntimeException e) {
                        Log.e("Runtiem", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.show(RequestActivity.this, "failure");
            }
        });
    }

    public void agree(final String username) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "agree");
        jsonObject.put("friendname", username);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if ("true".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "已同意");
                    MyApplication.getInstance().getNewFriends().remove(username);
                    MainActivity.updataNum();
                    finish();
                } else if ("false".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "获取失败！");
                } else if ("[]".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "用户不存在！");
                } else if ("agree".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "无法查看用户！");
                } else {
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.show(RequestActivity.this, "failure");
            }
        });
    }

    public void disagree(final String username) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "disagree");
        jsonObject.put("friendname", username);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if ("true".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "已拒绝");
                    MyApplication.getInstance().getNewFriends().remove(username);
                    MainActivity.updataNum();
                    finish();
                } else if ("false".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "获取失败！");
                } else if ("[]".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "用户不存在！");
                } else if ("agree".equals(str)) {
                    ToastUtil.show(RequestActivity.this, "无法查看用户！");
                } else {
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.show(RequestActivity.this, "failure");
            }
        });
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request_btn_agree:
//                agree(user.getUsername());
                break;
            case R.id.request_btn_disagree:
//                disagree(user.getUsername());
                break;
        }
    }
}
