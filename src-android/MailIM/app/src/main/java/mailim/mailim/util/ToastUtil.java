package mailim.mailim.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zzh on 2017/9/14.
 */
public class ToastUtil {
    public static void show(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
