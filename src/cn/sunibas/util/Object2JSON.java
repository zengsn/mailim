package cn.sunibas.util;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/2/13.
 */
public class Object2JSON {
    public static void JSONString(HttpServletResponse httpServletResponsesr,Object o) throws IOException {
        httpServletResponsesr.setContentType("text/html;charset=utf-8");
        PrintWriter out = httpServletResponsesr.getWriter();
        out.print(Object2Json(o));
    }

    public static String Object2Json(Object o){
        return JSONObject.fromObject(o).toString();
    }
}
