package cn.sunibas.test;

import cn.sunibas.util.ManuscrripitDefaultSetting;
import cn.sunibas.util.PackageCharsetDetector;
import org.junit.Test;

import java.io.*;
import java.util.Iterator;

/**
 * Created by IBAS on 2017/2/16.
 */
public class TestGetFileCharset {

    private static PackageCharsetDetector packageCharsetDetector = new PackageCharsetDetector();

    @Test
    public void testOne(){
        System.out
                .println(
                        packageCharsetDetector
                                .GetFileCharset(
                                        "E:\\java\\1028191127841ks7r1826n3aioufb6te\\0.txt"
                                )
                );
        System.out
                .println(
                        packageCharsetDetector
                                .GetFileCharset(
                                        "E:\\java\\1028191127841ks7r1826n3aioufb6te\\1.txt"
                                )
                );
        System.out
                .println(
                        packageCharsetDetector
                                .GetFileCharset(
                                        "E:\\java\\1028191127841ks7r1826n3aioufb6te\\2.txt"
                                )
                );
    }

    @Test
    public void testTwo() throws IOException {
        String file = "E:\\java\\1028191127841ks7r1826n3aioufb6te\\0.txt";
        int co = 0;
        int ch;
        String dir = "E:\\java\\1028191127841ks7r1826n3aioufb6te\\test";
        System.out.println("Encoding : " + packageCharsetDetector.GetFileCharset(file));
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file),
                                packageCharsetDetector.GetFileCharset(file)
                        )
                );
        OutputStreamWriter outputStreamWriter =
                new OutputStreamWriter(
                        new FileOutputStream(dir + "\\" + co + ".txt",true),
                        ManuscrripitDefaultSetting.defaultEncoding//"UTF-8"//PackageCharsetDetector.GetFileCharset(file)
                );
        while ((ch = reader.read()) != -1) {
            outputStreamWriter.write(ch);
//            if (PackageCharsetDetector.getChBreakList().contains(ch)) {
//                co++;
//                outputStreamWriter.flush();
//                outputStreamWriter.close();
//                outputStreamWriter =
//                        new OutputStreamWriter(
//                                new FileOutputStream(dir + "\\" + co + ".txt",true),
//                                PackageCharsetDetector.GetFileCharset(file)
//                        );
//            }
        }
        outputStreamWriter.close();
        reader.close();
    }

    @Test
    public void testThree() throws IOException {

        String file = "E:\\java\\1028191127841ks7r1826n3aioufb6te\\0.txt";
        int co = 0;
        int ch;
        String dir = "E:\\java\\1028191127841ks7r1826n3aioufb6te\\test";
        System.out.println("Encoding : " + packageCharsetDetector.GetFileCharset(file));
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file),
                                packageCharsetDetector.GetFileCharset(file)
                        )
                );
        BufferedWriter writer =
                new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(dir + "\\1.txt"),
                                ManuscrripitDefaultSetting.defaultEncoding
                        )
                );
        Iterator<String> stringStream = reader.lines().iterator();
        int index = 0;
        while (stringStream.hasNext()) {
            //System.out.print(stringStream.next());
            index++;
            System.out.println(" ------ " + index + " ------ ");
            writer.write(stringStream.next());
        }
        writer.close();
        reader.close();
    }
}
