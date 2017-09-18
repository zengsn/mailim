package mailim.mailim.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import mailim.mailim.MyApplication;
import mailim.mailim.R;
import mailim.mailim.entity.Email;
import mailim.mailim.fragment.EmailFragment;
import mailim.mailim.fragment.HomeFragment;
import mailim.mailim.fragment.MateyFragment;
import mailim.mailim.fragment.MessageFragment;
import mailim.mailim.util.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tabMessage;
    private TextView tabMatey;
    private TextView tabEmail;
    private TextView tabHome;
    private TextView mTextNum1,mTextNum2,mTextNum3,mTextNum4;

    private FrameLayout ly_content;

    private MessageFragment f1;
    private MateyFragment f2;
    private EmailFragment f3;
    private HomeFragment f4;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        bindView();
//        if(!app.isLogin()) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        tabMessage = (TextView)this.findViewById(R.id.tab_message);
        tabMatey = (TextView)this.findViewById(R.id.tab_matey);
        tabEmail = (TextView)this.findViewById(R.id.tab_email);
        tabHome = (TextView)this.findViewById(R.id.tab_home);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        mTextNum1 = (TextView) this.findViewById(R.id.tab_message_num);
        mTextNum2 = (TextView) this.findViewById(R.id.tab_matey_num);
        mTextNum3 = (TextView) this.findViewById(R.id.tab_email_num);
        mTextNum4 = (TextView) this.findViewById(R.id.tab_home_num);

        tabMessage.setOnClickListener(this);
        tabMatey.setOnClickListener(this);
        tabEmail.setOnClickListener(this);
        tabHome.setOnClickListener(this);

        tabMessage.setSelected(true);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(f1==null){
            f1 = new MessageFragment();
            transaction.add(R.id.fragment_container,f1);
        }else{
            transaction.show(f1);
        }
        transaction.commit();
    }

    //重置所有文本的选中状态
    public void selected(){
        tabMessage.setSelected(false);
        tabMatey.setSelected(false);
        tabEmail.setSelected(false);
        tabHome.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
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

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch(v.getId()){
            case R.id.tab_message:
                hideAllFragment(transaction);
                selected();
                tabMessage.setSelected(true);
                if(f1==null){
                    f1 = new MessageFragment();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                mTextNum1.setText("");
                mTextNum1.setVisibility(View.VISIBLE);
                break;

            case R.id.tab_matey:
                hideAllFragment(transaction);
                selected();
                tabMatey.setSelected(true);
                if(f2==null){
                    f2 = new MateyFragment("第二个Fragment");
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                mTextNum2.setText("1");
                mTextNum2.setVisibility(View.VISIBLE);
                break;

            case R.id.tab_email:
                hideAllFragment(transaction);
                selected();
                tabEmail.setSelected(true);
                if(f3==null){
                    f3 = new EmailFragment(mContext);
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                mTextNum3.setText("11");
                mTextNum3.setVisibility(View.VISIBLE);
                break;

            case R.id.tab_home:
                hideAllFragment(transaction);
                selected();
                tabHome.setSelected(true);
                if(f4==null){
                    f4 = new HomeFragment("第四个Fragment");
                    transaction.add(R.id.fragment_container,f4);
                }else{
                    transaction.show(f4);
                }
                mTextNum4.setText("");
                mTextNum4.setVisibility(View.VISIBLE);
                break;
            case R.id.option_setting:
                //Toast.makeText(MainActivity.this, "点击设置", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
        }

        transaction.commit();
    }

    public void onTest(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

//        Email email = new Email();
//        email.setFrom_address("fafafa");
//        email.setSubject("dsfsa");
//
//        ToastUtil.show(this,JSON.toJSON(email).toString());
    }
}
