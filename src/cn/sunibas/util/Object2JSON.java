package cn.sunibas.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/2/13.
 */
public class Object2JSON {
    /**
     * @param httpServletResponsesr : action 中传入 ServletActionContext.getResponse()
     * @param o                     : 任意object 对象
     * @throws IOException
     */
    public static void JSONString(HttpServletResponse httpServletResponsesr,Object o,ObjectType objectType) {
        httpServletResponsesr.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpServletResponsesr.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (objectType == ObjectType.Object) {
            out.print(Object2Json(o));
        } else if (objectType == ObjectType.Array) {
            out.print(Array2Json(o));
        } else if (objectType == ObjectType.NullArray) {
            out.print("[]");
        }else if (objectType == ObjectType.NullObject) {
            out.print("{}");
        }
        out.flush();
    }

    public static String Object2Json(Object o){
        return JSONObject.fromObject(o).toString();
    }
    public static String Array2Json(Object o){
        return JSONArray.fromObject(o).toString();
    }
}