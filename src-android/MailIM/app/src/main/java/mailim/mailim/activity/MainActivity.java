package mailim.mailim.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Target;

import org.apache.http.Header;

import mailim.mailim.entity.User;
import mailim.mailim.service.PulseService;
import mailim.mailim.util.EmailSender;
import mailim.mailim.util.MyApplication;
import mailim.mailim.R;
import mailim.mailim.fragment.EmailFragment;
import mailim.mailim.fragment.HomeFragment;
import mailim.mailim.fragment.MateyFragment;
import mailim.mailim.fragment.MessageFragment;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.ToastUtil;

public class MainActivity extends Activity implements View.OnClickListener{
    private static TextView tabMessage;
    private static TextView tabMatey;
    private static TextView tabEmail;
    private static TextView tabHome;
    public static TextView mTextNum1,mTextNum2,mTextNum3,mTextNum4;
    public static int num1,num2,num3,num4;

    public static MessageFragment f1;
    public static MateyFragment f2;
    public static EmailFragment f3;
    public static HomeFragment f4;

    public static MyApplication app;
    public static MainActivity mContext;
//    private static Button btn_add;
    private Target target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;

        bindView();
//        intiMyAApplication();
        app.loadHead(app.getMyUser().getUsername());
        Intent intent = new Intent(getApplication(), PulseService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void intiMyAApplication(){
        app = (MyApplication)getApplication();
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        app.getMyUser().setUsername(sp.getString("username",""));
//        app.getMyUser().setPassword(sp.getString("password",""));
//        app.getMyUser().setEmail(sp.getString("email",""));
//        app.getMyUser().setEmailpwd(sp.getString("email_pwd",""));
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        tabMessage = (TextView)this.findViewById(R.id.tab_message);
        tabMatey = (TextView)this.findViewById(R.id.tab_matey);
        tabEmail = (TextView)this.findViewById(R.id.tab_email);
        tabHome = (TextView)this.findViewById(R.id.tab_home);

        mTextNum1 = (TextView) this.findViewById(R.id.tab_message_num);
        mTextNum2 = (TextView) this.findViewById(R.id.tab_matey_num);
        mTextNum3 = (TextView) this.findViewById(R.id.tab_email_num);
        mTextNum4 = (TextView) this.findViewById(R.id.tab_home_num);

//        btn_add = (Button)findViewById(R.id.title_add);
//        btn_add.setOnClickListener(this);

        num1 = num2 = num3 = num4 = 0;

        tabMessage.setOnClickListener(this);
        tabMatey.setOnClickListener(this);
        tabEmail.setOnClickListener(this);
        tabHome.setOnClickListener(this);

        tabMessage.setSelected(true);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        intiFragment();
//        btn_add.setText("备份记录");
//        btn_add.setVisibility(View.VISIBLE);
        transaction.show(f1);
        transaction.commit();
        updataNum();
    }

    public void intiFragment(){
        f1 = new MessageFragment();
        f2 = new MateyFragment();
        f3 = new EmailFragment();
        f4 = new HomeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,f1);
        transaction.add(R.id.fragment_container,f2);
        transaction.add(R.id.fragment_container,f3);
        transaction.add(R.id.fragment_container,f4);
        hideAllFragment(transaction);
        transaction.commit();
    }

    //重置所有文本的选中状态
    public static void selected(){
        tabMessage.setSelected(false);
        tabMatey.setSelected(false);
        tabEmail.setSelected(false);
        tabHome.setSelected(false);
//        btn_add.setVisibility(View.GONE);
    }

    //隐藏所有Fragment
    public static void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }
        if(f4!=null){
            transaction.hide(f4);
        }
    }

    public static void updataNum(){
        num1 = MessageFragment.getMsgCount();
        if(num1 > 0){
            mTextNum1.setText(String.valueOf(num1));
            mTextNum1.setVisibility(View.VISIBLE);
        }
        else {
            mTextNum1.setText("");
            mTextNum1.setVisibility(View.INVISIBLE);
        }
        num2 = app.getNewFriends().size();
        if(num2 > 0){
            mTextNum2.setText(String.valueOf(num2));
            mTextNum2.setVisibility(View.VISIBLE);
        }
        else mTextNum2.setVisibility(View.INVISIBLE);
        if(num3 > 0){
            mTextNum3.setText(String.valueOf(num3));
            mTextNum3.setVisibility(View.VISIBLE);
        }
        else mTextNum3.setVisibility(View.INVISIBLE);
        if(num4 > 0){
            mTextNum4.setText(String.valueOf(num4));
            mTextNum4.setVisibility(View.VISIBLE);
        }
        else mTextNum4.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch(v.getId()){
            case R.id.tab_message:
                hideAllFragment(transaction);
                selected();
                tabMessage.setSelected(true);
                //btn_add.setText("备份记录");
                //btn_add.setVisibility(View.VISIBLE);
                transaction.show(f1);
                f1.update();
                break;

            case R.id.tab_matey:
                hideAllFragment(transaction);
                selected();
                tabMatey.setSelected(true);
                //btn_add.setText("备份好友");
                //btn_add.setVisibility(View.VISIBLE);
                transaction.show(f2);
                f2.updateFriend();
                break;

            case R.id.tab_email:

                boolean isHidden = false;
                if(f3.isHidden())isHidden = true;
                hideAllFragment(transaction);
                selected();
                tabEmail.setSelected(true);
                //btn_add.setText("筛选");
                //btn_add.setVisibility(View.VISIBLE);
                transaction.show(f3);
                if(!isHidden || f3.emails.size() == 0)f3.recevieEmail();
                break;

            case R.id.tab_home:
                hideAllFragment(transaction);
                selected();
                tabHome.setSelected(true);
                transaction.show(f4);
                f4.loadHead();
                break;
            case R.id.option_setting:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.title_add:
                if(!f1.isHidden()){
                }
                if(!f2.isHidden()){
                    app.updateFriendToEmail();
                }
                if(!f3.isHidden()){
                    f3.filter();
                }
//                inputUsername();
                break;
            case R.id.home_my_info:
                Intent intent2 = new Intent(this,MyInfoActivity.class);
                startActivity(intent2);
                break;
            case R.id.matey_new_friend:
                Intent intent3 = new Intent(this,RequestActivity.class);
                intent3.putExtra("username",app.getNewFriends().get(0));
                startActivity(intent3);
                break;
        }
        transaction.commit();
    }

    private void inputUsername(){
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("请输入用户名或邮箱").setView(editText);
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!"".equals(editText.getText().toString()))getUser(editText.getText().toString());
                        else ToastUtil.show(MainActivity.this,"请输入内容！");
                    }
                }).show();
    }

    public void getUser(final String username){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","getUser");
        jsonObject.put("name",username);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)) {
                }
                else if("false".equals(str)){
                    ToastUtil.show(MainActivity.this,"获取失败！");
                }
                else if("[]".equals(str)){
                    ToastUtil.show(MainActivity.this,"用户不存在！");
                }
                else if("getUser".equals(str)){
                    ToastUtil.show(MainActivity.this,"无法查看用户！");
                }
                else {
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if ("user".equals(res.getString("type"))) {
                            User user = new User();
                            user.setUsername(res.getString("username"));
                            user.setSex(res.getBoolean("sex"));
                            user.setEmail(res.getString("email"));
                            user.setQianming(res.getString("qianming"));
                            Intent intent = null;
                            if(username.equals(MainActivity.app.getMyUser().getUsername())){
                                intent = new Intent(MainActivity.this, MyInfoActivity.class);
                            }
                            else {
                                intent = new Intent(MainActivity.this, UserInfoActivity.class);
                            }
                            intent.putExtra("userdata", user);
                            intent.putExtra("user", true);
                            startActivity(intent);
                        }
                    } catch (RuntimeException e){
                        Log.e("Runtiem",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.show(MainActivity.this,"failure");
            }
        });
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                super.run();
            }
        }.start();
    }

    public static void setTabMessage(FragmentTransaction transaction){
        hideAllFragment(transaction);
        selected();
        tabMessage.setSelected(true);
        if(f1==null){
            f1 = new MessageFragment();
            transaction.add(R.id.fragment_container,f1);
        }else{
            transaction.show(f1);
        }
        transaction.commit();
    }

    @Override
    protected void onRestart() {
        updataNum();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        updataNum();
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("确认退出？")
                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("后台运行", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(false);
                        }
                    }).create();
            alertDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        savePreferences();
        f1 = null;
        f2 = null;
        f3 = null;
        f4 = null;
        mContext = null;
        unbindService(serviceConnection);
        super.onDestroy();
    }

    public void savePreferences(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("username",app.getMyUser().getUsername());
        editor.putString("password",app.getMyUser().getPassword());
        editor.apply();
    }

    public void clearPreferences(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("email","");
        editor.putString("email_pwd","");
        editor.apply();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ToastUtil.show("绑定服务成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ToastUtil.show("绑定服务失败");
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    ToastUtil.show(MainActivity.this,msg.obj.toString());
                    break;
                case 2:
                    Bundle data = msg.getData();
                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle(data.getString("title"));
                    dialog.setMessage(data.getString("message"));
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                    break;
            }
        }
    };
}
