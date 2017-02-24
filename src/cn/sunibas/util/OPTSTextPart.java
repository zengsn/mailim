package cn.sunibas.util;

import cn.sunibas.entity.TSTextPart;

/**
 * Created by Administrator on 2017/2/20.
 */
public class OPTSTextPart {
    private static int enMinLen = 32;
    public static String encipher(String tsuuid,int partIndex){
        return partIndex + tsuuid;
    }

    public static TSTextPart decrypt(String enStr,TSTextPart tsTextPart) throws Exception {
        int enStrLen = enStr.length();
        if (enStrLen < enMinLen) {
            throw new Exception("something wrong : enStr length is less than enMinLen");
        } else {
            tsTextPart.setTSindex(Integer.valueOf(enStr.substring(0,enStrLen - enMinLen)));
            tsTextPart.setTSuuid(enStr.substring(enStrLen - enMinLen));
        }
        return tsTextPart;
    }
}
