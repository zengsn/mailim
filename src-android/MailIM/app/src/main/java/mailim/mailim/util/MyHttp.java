package mailim.mailim.util;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.Map;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.util.Constant;

/**
 * Created by zzh on 2017/8/20.
 */
public class MyHttp{
    private static String BASE_URL = Constant.BASEURL + "/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public MyHttp(){
    }

    public static void post(JSONObject jsonObject,AsyncHttpResponseHandler responseHandler){
        final String username = MainActivity.app.getMyUser().getUsername();
        final String password = MainActivity.app.getMyUser().getPassword();
        jsonObject.put("username", username);
        jsonObject.put("password",password);
        String json = null;
        try {
            json = DESUtil.ENCRYPTMethod(jsonObject.toString(),Constant.KEY);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(MainActivity.app,e.getMessage());
        }
        RequestParams params = new RequestParams();
        params.put("json",json);
        client.post(getAbsoluteUrl("index.php"),params,responseHandler);
    }

    public static void post(JSONObject jsonObject, File file,AsyncHttpResponseHandler responseHandler){
        final String username = MainActivity.app.getMyUser().getUsername();
        final String password = MainActivity.app.getMyUser().getPassword();
        jsonObject.put("username", username);
        jsonObject.put("password",password);
        String json = null;
        try {
            json = DESUtil.ENCRYPTMethod(jsonObject.toString(),Constant.KEY);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(MainActivity.app,e.getMessage());
        }
        RequestParams params = new RequestParams();
        params.put("json",json);
        try {
            params.put("uploadfile",file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(getAbsoluteUrl("index.php"),params,responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
