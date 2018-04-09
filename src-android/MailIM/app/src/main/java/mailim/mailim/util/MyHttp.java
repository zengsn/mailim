package mailim.mailim.util;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by zzh on 2017/8/20.
 */
public class MyHttp{
    private static String BASE_URL = Constant.BASEURL + "/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static MyApplication app = MyApplication.getInstance();

    public MyHttp(){
    }

    public static void post(JSONObject jsonObject,AsyncHttpResponseHandler responseHandler){
        jsonObject.put("myUsername", app.getMyUser().getUsername());
        jsonObject.put("myPassword",app.getMyUser().getPassword());
        jsonObject.put("myEmail",app.getMyUser().getEmail());
        String json = null;
        try {
            json = DESUtil.ENCRYPTMethod(jsonObject.toString(),Constant.KEY);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(app,e.getMessage());
        }
        RequestParams params = new RequestParams();
        params.put("json",json);
        client.post(getAbsoluteUrl("index.php"),params,responseHandler);
    }

    public static void post(JSONObject jsonObject, File file,AsyncHttpResponseHandler responseHandler){
        jsonObject.put("myUsername", app.getMyUser().getUsername());
        jsonObject.put("myPassword",app.getMyUser().getPassword());
        jsonObject.put("myEmail",app.getMyUser().getEmail());
        String json = null;
        try {
            json = DESUtil.ENCRYPTMethod(jsonObject.toString(),Constant.KEY);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(app,e.getMessage());
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
