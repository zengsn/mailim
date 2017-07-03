package cn.sunibas.redis.staticData;

/**
 * Created by sunbing on 17-3-4.
 */
public class KeepDownChange {
    //这里使用list将改变过的对象的大类记录下来，
    //例如status的redis数据结构是
    //HashOperations<String tagUrl,String kidid,T mark>
    //这里记录的是 tagUrl
    public static String list_statusChange = "list_statusChange";
    //HashOperations<String tssuuid,String kidid,T mark>
    //这里记录的是 tssuuid
    public static String list_markChange = "list_markChange";

    public static String list_statusRemove = "list_statusRemove";

    public static String getStatus(String ls_statue) {
        return ls_statue.substring(list_statusChange.length());
    }

    public static String getMark(String ls_mark) {
        return ls_mark.substring(list_markChange.length());
    }
}
