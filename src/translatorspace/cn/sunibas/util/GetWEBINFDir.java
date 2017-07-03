package cn.sunibas.util;

/**
 * Created by Administrator on 2017/2/13.
 */
public class GetWEBINFDir {
    public static String getDir() {
        //file:/D:/JavaWeb/.metadata/.me_tcat/webapps/TestBeanUtils/WEB-INF/classes/
        String path=Thread.currentThread().getContextClassLoader().getResource("").toString();
        path=path.replace('/', '\\'); // 将/换成\
        path=path.replace("file:", ""); //去掉file:
        path=path.replace("classes\\", ""); //去掉class\
        path=path.substring(1); //去掉第一个\,如 \D:\JavaWeb...
        //System.out.println(path);
        return path;
    }
}
