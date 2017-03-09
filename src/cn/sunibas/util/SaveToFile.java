package cn.sunibas.util;

import java.io.*;

/**
 * Created by sunbing on 17-3-9.
 */
public class SaveToFile {

    public static void saveInFile(String dir,String content) throws IOException {
        File file = new File(dir);
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file),
                        ManuscrripitDefaultSetting.defaultEncoding
                )
        );
        bufferedWriter.write(content);
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
