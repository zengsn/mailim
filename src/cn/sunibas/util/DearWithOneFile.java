package cn.sunibas.util;

import cn.sunibas.entity.TSNewText;
import cn.sunibas.entity.TSTextPart;
import cn.sunibas.service.ITSTextPartService;

import java.io.*;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/2/16.
 */
public class DearWithOneFile {

    private PackageCharsetDetector packageCharsetDetector;
    private ITSTextPartService tsTextPartService;

    public void setPackageCharsetDetector(PackageCharsetDetector packageCharsetDetector) {
        this.packageCharsetDetector = packageCharsetDetector;
    }

    public void setTsTextPartService(ITSTextPartService tsTextPartService) {
        this.tsTextPartService = tsTextPartService;
    }

    /**
     * file : 文件
     * index ： 索引值，用于TSTextPart 的 index
     * */
    public int dearWithOneFile(String parentPath,String filePath,TSNewText tsNewText,int index) {
        try {
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(parentPath + "\\" + filePath),
                                    packageCharsetDetector.GetFileCharset(parentPath + "\\" + filePath)
                            )
                    );
            BufferedWriter writer;/* =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(dir + "\\1.txt"),
                                    ManuscrripitDefaultSetting.defaultEncoding
                            )
                    );*/
            //这里仅仅模拟，假设一个文件一一行分割
            Iterator<String> lines = reader.lines().iterator();
            (new File(parentPath + ManuscrripitDefaultSetting.childFolderRelativeLocation)).mkdir();
            while (lines.hasNext()) {
                String line = lines.next();
                if (line.equals("")) {
                    continue;
                }
                writer = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(
                                        parentPath +
                                        ManuscrripitDefaultSetting.childFolderRelativeLocation + "\\" +
                                        index + ".txt"
                                ),
                                ManuscrripitDefaultSetting.defaultEncoding
                        )
                );
                writer.write(line);
                writer.flush();
                writer.close();

                //数据库操作部分（请勿修改，但可以移动）
                TSTextPart tsTextPart = new TSTextPart();
                tsTextPart.setTSindex(index);
                tsTextPart.setTSuuid(tsNewText.getUuid());
                tsTextPart.setScore(tsNewText.getScore());
                tsTextPart.setMoney(tsNewText.getMoney());
                tsTextPart.setOverTimes(0);
                tsTextPart.setToLanguage(tsNewText.getToLanguage());
                tsTextPart.setFromLanguage(tsNewText.getFromLanguage());
                tsTextPartService.save(tsTextPart);

                index++;
            }
            reader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }
}
