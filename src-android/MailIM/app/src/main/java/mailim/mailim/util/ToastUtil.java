package mailim.mailim.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zzh on 2017/9/14.
 */
public class ToastUtil {
    public static void show(Context context,String text){
        if(!MyApplication.getInstance().debugable)Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
    public static void show(String text){
        if(!MyApplication.getInstance().debugable)Toast.makeText(MyApplication.getInstance(),text,Toast.LENGTH_SHORT).show();
    }
}
