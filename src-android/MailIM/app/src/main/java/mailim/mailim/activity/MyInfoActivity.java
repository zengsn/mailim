package mailim.mailim.activity;

import android.annotation.SuppressLint;
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
import android.util.Log;
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
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.ToastUtil;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_head;
    private EditText et_nickname;
    private EditText et_age;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private EditText et_city;
    private EditText et_motto;
    private Button btn_save;
    private User myself;

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
        String jsonString = JSONObject.toJSONString(MyApplication.getInstance().getMyUser());
        myself = JSONObject.parseObject(jsonString,User.class);
        rb_male.setChecked(myself.isSex());
        rb_female.setChecked(!myself.isSex());
        et_nickname.setText(myself.getNickname());
        et_age.setText(String.valueOf(myself.getAge()));
        et_city.setText(myself.getCity());
        et_motto.setText(myself.getMotto());
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
        et_nickname = (EditText)findViewById(R.id.myinfo_nickname);
        et_age = (EditText)findViewById(R.id.myinfo_age);
        et_city = (EditText)findViewById(R.id.myinfo_city);
        et_motto = (EditText)findViewById(R.id.myinfo_motto);
        rg_sex = (RadioGroup)findViewById(R.id.myinfo_sex);
        rb_male = (RadioButton)findViewById(R.id.myinfo_male);
        rb_female = (RadioButton)findViewById(R.id.myinfo_female);
        btn_save = (Button)findViewById(R.id.myinfo_btn_save);

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
                .load(MyApplication.getInstance().getHeadFile())
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
        myself.setNickname(et_nickname.getText().toString());
        myself.setSex(rb_male.isChecked());
        myself.setAge(Integer.valueOf(et_age.getText().toString()));
        myself.setCity(et_city.getText().toString());
        myself.setMotto(et_motto.getText().toString());
        updataUser();
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
                            .load(Constant.HEAD_URL+MyApplication.getInstance().getMyUser().getEmail()+"?time="+ System.currentTimeMillis())
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
        jsonObject.put("nickname",myself.getNickname());
        jsonObject.put("sex",myself.isSex());
        jsonObject.put("age",myself.getAge());
        jsonObject.put("city",myself.getCity());
        jsonObject.put("motto",myself.getMotto());
        Log.e("debug", "updataUser: post");
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                Log.e("debug", "onSuccess: "+str);
                Message msg = new Message();
                msg.what = 1;
                if("true".equals(str)) {
                    MyApplication.getInstance().setMyUser(myself);
                    intiData();
                    msg.obj = "更新成功";
                    handler.sendMessage(msg);
                    finish();
                }else{
                    Log.e("debug","更新失败"+str);
                    msg.obj = "更新失败"+str;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e("debug", "onFailure: "+throwable.getMessage());
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    ToastUtil.show(msg.obj.toString());
                    Log.e("test","toast");
                    break;
                default:
                    break;
            }
        }
    };
}
