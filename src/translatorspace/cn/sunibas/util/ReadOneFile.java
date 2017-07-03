package cn.sunibas.util;

import java.io.*;

/**
 * Created by Administrator on 2017/2/20.
 */
public class ReadOneFile {
    public static BufferedReader readByBufferReader(String fileName,String encoding) throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName),
                        encoding
                )
        );
    }

    public static StringBuilder readFileAsStringBuilder(String fileName,String encoding){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = readByBufferReader(fileName,encoding);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {}
        return stringBuilder;
    }
}
