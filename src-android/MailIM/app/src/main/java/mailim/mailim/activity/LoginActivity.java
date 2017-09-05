package mailim.mailim.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import mailim.mailim.R;

/**
 * Created by zzh on 2017/8/20.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_psw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiView();
    }

    private void intiView(){
        et_username = (EditText)findViewById(R.id.login_et_username);
        et_psw = (EditText)findViewById(R.id.login_et_userpsw);
    }

    private void save(){

    }

    public boolean checkEmail(){
        return false;
    }

    public boolean login(){
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                break;
            default:
        }
    }
}