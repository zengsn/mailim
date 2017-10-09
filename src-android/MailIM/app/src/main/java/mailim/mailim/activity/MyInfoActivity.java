package mailim.mailim.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

import mailim.mailim.R;
import mailim.mailim.entity.User;
import mailim.mailim.util.Constant;
import mailim.mailim.util.EmailUtil;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.ToastUtil;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_head;
    private EditText et_username;
    private EditText et_password;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private EditText et_email;
    private EditText et_emailpwd;
    private EditText et_qianming;
    private Button btn_save;
    private User myself;
    private CheckBox cb_no_updataemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        setupActionBar();
        intiView();
        intiData();
        clearFocus();
    }

    private void intiData(){
        String jsonString = JSONObject.toJSONString(MainActivity.app.getMyUser());
        myself = JSONObject.parseObject(jsonString,User.class);
        et_username.setText(myself.getUsername());
        et_password.setText(myself.getPassword());
        et_email.setText(myself.getEmail());
        et_emailpwd.setText(myself.getEmailpwd());
        et_qianming.setText(myself.getQianming());
        rb_male.setChecked(myself.isSex());
        rb_female.setChecked(!myself.isSex());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    public void intiView(){
        iv_head = (ImageView)findViewById(R.id.myinfo_iv_head);
        et_username = (EditText)findViewById(R.id.myinfo_username);
        et_password = (EditText)findViewById(R.id.myinfo_password);
        et_email = (EditText)findViewById(R.id.myinfo_email);
        et_emailpwd = (EditText)findViewById(R.id.myinfo_emailpwd);
        et_qianming = (EditText)findViewById(R.id.myinfo_qianming);
        rg_sex = (RadioGroup)findViewById(R.id.myinfo_sex);
        rb_male = (RadioButton)findViewById(R.id.myinfo_male);
        rb_female = (RadioButton)findViewById(R.id.myinfo_female);
        btn_save = (Button)findViewById(R.id.myinfo_btn_save);
        cb_no_updataemail = (CheckBox)findViewById(R.id.myinfo_updata_email);

        btn_save.setOnClickListener(this);
        iv_head.setOnClickListener(this);

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.myinfo_male:
                        myself.setSex(true);
                        break;
                    case R.id.myinfo_female:
                        myself.setSex(false);
                        break;
                }
            }
        });

        Picasso.with(this)
                .load(MainActivity.app.getHeadFile())
                .into(iv_head);
    }

    private void clearFocus(){
        View view = findViewById(R.id.myinfo_null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myinfo_btn_save:
                onBtn_save();
                break;
            case R.id.myinfo_iv_head:
                onHeadActivity();
                break;
        }
    }

    private void onBtn_save(){
        myself.setUsername(et_username.getText().toString());
        myself.setPassword(et_password.getText().toString());
        myself.setSex(rb_male.isChecked());
        myself.setQianming(et_qianming.getText().toString());
        if(!cb_no_updataemail.isChecked()) {
            myself.setEmail(et_email.getText().toString());
            myself.setEmailpwd(et_emailpwd.getText().toString());
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    if (EmailUtil.checkEmail(myself.getEmail(), myself.getEmailpwd())) {
                        Message msg = new Message();
                        msg.what = 4;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "错误");
                        bundle.putString("message", "邮箱或邮箱密码错误！");
                        msg.setData(bundle);
                        msg.obj = new String("邮箱或邮箱密码错误！");
                        handler.sendMessage(msg);
                    }
                    super.run();
                }
            }.start();
        }
        else updataUser();
    }

    private void onHeadActivity(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(data != null) {
                    setHead(data.getData());
                }
                break;
        }
    }

    private void setHead(final Uri uri){
        Bitmap bm = null;
        String path = null;
        ContentResolver resolver = getContentResolver();
        try {
            bm = MediaStore.Images.Media.getBitmap(resolver,uri);
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(column_index);
                cursor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(path);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","head");
        MyHttp.post(jsonObject,file,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)) {
                    Picasso.with(MyInfoActivity.this)
                            .load(Constant.HEAD_URL+MainActivity.app.getMyUser().getUsername()+"?time="+ System.currentTimeMillis())
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(iv_head);
                }
                else{
                    ToastUtil.show(MyInfoActivity.this,"上传失败！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                ToastUtil.show(MyInfoActivity.this,str);
            }
        });
    }

    private void updataUser(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","updateUser");
        jsonObject.put("newUsername",myself.getUsername());
        jsonObject.put("newPassword",myself.getPassword());
        jsonObject.put("sex",myself.isSex());
        jsonObject.put("email",myself.getEmail());
        jsonObject.put("emailpwd",myself.getEmailpwd());
        jsonObject.put("qianming",myself.getQianming());
        jsonObject.put("updateemail",!cb_no_updataemail.isChecked());
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                Message msg = new Message();
                msg.what = 1;
                if("true".equals(str)) {
                    MainActivity.app.setMyUser(myself);
                    intiData();
                    msg.obj = new String("更新成功");
                    handler.sendMessage(msg);
                    finish();
                }else if("false".equals(str)){
                    msg.obj = new String("更新失败");
                    handler.sendMessage(msg);
                }
                else {
                    msg.obj = new String(str);
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    ToastUtil.show(MyInfoActivity.this,msg.obj.toString());
                    break;
                case 2:
                    Bundle data = msg.getData();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MyInfoActivity.this);
                    dialog.setTitle(data.getString("title"));
                    dialog.setMessage(data.getString("message"));
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearFocus();
                            et_emailpwd.requestFocus();
                        }
                    });
                    dialog.show();
                    break;
                case 3:
                    et_emailpwd.setError("密码错误！");
                    clearFocus();
                    et_emailpwd.requestFocus();
                    break;
                case 4:
                    updataUser();
                    break;
            }
        }
    };
}
