package mailim.mailim.fragment;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

import mailim.mailim.activity.LoginActivity;
import mailim.mailim.activity.MainActivity;
import mailim.mailim.R;
import mailim.mailim.service.PulseService;
import mailim.mailim.util.Constant;
import mailim.mailim.util.MyHttp;
import mailim.mailim.util.ToastUtil;
import mailim.mailim.view.CircleImageView;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private CircleImageView headView;
    private TextView tv_username;
    private Button bnt_unLogin;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        headView = (CircleImageView)view.findViewById(R.id.hmoe_personal_image);
        tv_username = (TextView)view.findViewById(R.id.home_username);
        bnt_unLogin = (Button)view.findViewById(R.id.home_unlogin);

        headView.setOnClickListener(this);
        bnt_unLogin.setOnClickListener(this);
        intiData();
        return view;
    }

    private void intiData(){
        if(MainActivity.app.isLogin()){
            loadHead();
            tv_username.setText(MainActivity.app.getMyUser().getUsername());
        }
    }

    public void loadHead(){
        MainActivity.app.loadHead(MainActivity.app.getMyUser().getUsername());
        Picasso.with(MainActivity.app)
                .load(Constant.HEAD_URL+MainActivity.app.getMyUser().getUsername()+"?time="+ System.currentTimeMillis())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(headView);
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
        ContentResolver resolver = getActivity().getContentResolver();
        try {
            bm = MediaStore.Images.Media.getBitmap(resolver,uri);
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
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
                    MainActivity.app.loadHead(MainActivity.app.getMyUser().getUsername());
                    loadHead();
                }
                else{
                    ToastUtil.show(getActivity(),"上传失败！");
                    Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                ToastUtil.show(getActivity(),str);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hmoe_personal_image:
                onHeadActivity();
                break;
            case R.id.home_unlogin:
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("auto",false);
                startActivity(intent);
                Intent intent1 = new Intent(getActivity(),PulseService.class);
                getActivity().stopService(intent1);
                MainActivity.mContext.clearPreferences();
                MainActivity.mContext.finish();
                break;
        }
    }
}
