package cn.sunibas.util;

import cn.sunibas.entity.TSTextPart;
import cn.sunibas.entity.TSstatus;

/**
 * Created by IBAS on 2017/2/20.
 */
public class OPTSTextPart {
    private static int enMinLen = 32;
    public static String encipher(String tsuuid,int partIndex){
        return partIndex + tsuuid;
    }
    public static String encipher(TSTextPart tsTextPart) {
        return encipher(tsTextPart.getTSuuid(),tsTextPart.getTSindex());
    }
    public static String encipher(TSstatus tSstatus) {
        return encipher(tSstatus.getTSuuid(),tSstatus.getTSindex());
    }

    public static TSstatus decrypt(String enStr,TSstatus tSstatus) throws Exception {
        int enStrLen = enStr.length();
        if (enStrLen < enMinLen) {
            throw new Exception("something wrong : enStr length is less than enMinLen");
        } else {
            tSstatus.setTSindex(Integer.valueOf(enStr.substring(0,enStrLen - enMinLen)));
            tSstatus.setTSuuid(enStr.substring(enStrLen - enMinLen));
        }
        return tSstatus;
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

    public static String getTChipFileFullName(String tssuuid,int tsindex,String tskidid) {
        TSstatus tSstatus = new TSstatus();
        tSstatus.setTSuuid(tssuuid);
        tSstatus.setTSkidid(tskidid);
        tSstatus.setTSindex(tsindex);
        return SaveTranslatedChip.getFileDir(tSstatus,0);
    }

    public static String getTChipFileFullName(TSstatus tSstatus) {
        return SaveTranslatedChip.getFileDir(tSstatus,0);
    }
}
