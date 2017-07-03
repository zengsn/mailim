package cc.zhangzhanhong.www.mailim;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView topBar;
    private TextView tabMessage;
    private TextView tabMatey;
    private TextView tabEmail;
    private TextView tabHome;

    private FrameLayout ly_content;

    private MessageFragment f1,f2,f3,f4;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bindView();

    }

    //UI组件初始化与事件绑定
    private void bindView() {
        topBar = (TextView)this.findViewById(R.id.txt_top);
        tabMessage = (TextView)this.findViewById(R.id.txt_message);
        tabMatey = (TextView)this.findViewById(R.id.txt_matey);
        tabEmail = (TextView)this.findViewById(R.id.txt_email);
        tabHome = (TextView)this.findViewById(R.id.txt_home);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        tabMessage.setOnClickListener(this);
        tabMatey.setOnClickListener(this);
        tabEmail.setOnClickListener(this);
        tabHome.setOnClickListener(this);

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
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.txt_message:
                selected();
                tabMessage.setSelected(true);
                if(f1==null){
                    f1 = new MessageFragment("第一个Fragment");
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.txt_matey:
                selected();
                tabMatey.setSelected(true);
                if(f2==null){
                    f2 = new MessageFragment("第二个Fragment");
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.txt_email:
                selected();
                tabEmail.setSelected(true);
                if(f3==null){
                    f3 = new MessageFragment("第三个Fragment");
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                if(f4==null){
                    f4 = new MessageFragment("第四个Fragment");
                    transaction.add(R.id.fragment_container,f4);
                }else{
                    transaction.show(f4);
                }
                break;
        }

        transaction.commit();
    }}
