package mailim.mailim.util;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import mailim.mailim.activity.MainActivity;

/**
 * Created by zzh on 2017/9/14.
 */
public class ToastUtil {
    public static void show(Context context,String text){
        if(!MainActivity.app.debugable)Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
    public static void show(String text){
        if(!MainActivity.app.debugable)Toast.makeText(MainActivity.app,text,Toast.LENGTH_SHORT).show();
    }
}
