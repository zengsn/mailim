package mailim.mailim;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.net.Socket;

import mailim.mailim.util.Constant;

/**
 * Created by zzh on 2017/8/20.
 */
public class MySocket extends Socket {
    private static String BASE_URL = "http://"+Constant.BASEURL + "/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public MySocket(){
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
