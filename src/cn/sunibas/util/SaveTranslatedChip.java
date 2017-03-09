package cn.sunibas.util;

import cn.sunibas.entity.TSstatus;
import org.aspectj.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ibas on 2/26/17.
 */
public class SaveTranslatedChip {
    public static String getTFileName(TSstatus tSstatus) {
        return ManuscrripitDefaultSetting.folderLocation +
                tSstatus.getTSuuid() + ManuscrripitDefaultSetting.defaultSplitDirChar +
                ManuscrripitDefaultSetting.childFolderRelativeLocation +
                tSstatus.getTSindex() + 't' + ManuscrripitDefaultSetting.defaultSplitDirChar +
                tSstatus.getTSkidid();
    }

    /**
     * 这里是获取到文件夹中的最后一个文件并加上指定的偏移，默认为1或0
     * 加一用在新建新的文件时，加零用在查询文件下最后一个文件时
     * */
    public static int lastFileIndex(String path,int add) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            List<File> files = Arrays.asList(file.listFiles());
            if (file.length() == 0) {
                return 0;
            }
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });
            if (files.size() == 0) {
                return 0;
            } else {
                return Integer.valueOf(files.get(files.size() - 1).getName()) + add;
            }
        } else {
            return -1;
        }
    }

    public static String getFileDir(TSstatus tSstatus,int add) {
        //构建文件夹
        String dir = getTFileName(tSstatus);
        File file = new File(dir);
        int index = 0;
        //确认文件加是否存在
        if (file.exists()) {
            //
            index = lastFileIndex(dir,add);
        } else {
            mkdir(dir);
        }
        return dir + ManuscrripitDefaultSetting.defaultSplitDirChar + NumberExt.Int2String(index);
    }

    /**
     * 经实验发现
     * 创建文件夹必须是层层创建，不能跨越层次
     * */
    public static void mkdir(String dir) {
        String[] str = dir.split("/");
        StringBuilder sb = new StringBuilder();
        sb.append(str[0]);
        for (int i = 1;i < str.length;i++) {
            //System.out.println(str[i]);
            sb.append("/" + str[i]);
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    //未进行编码处理
    public static void saveFile(File chipFile,TSstatus tSstatus) throws IOException {
        String dir = getFileDir(tSstatus,1);
        FileUtil.copyFile(chipFile, new File(dir));
    }

    //保存字符串到文件中
    public static void saveFile(String str,TSstatus tSstatus) {
        String dir = getFileDir(tSstatus,1);
        try {
            SaveToFile.saveInFile(dir,str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
