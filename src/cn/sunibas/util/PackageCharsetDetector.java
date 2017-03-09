package cn.sunibas.util;

import info.monitorenter.cpdetector.io.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */
public class PackageCharsetDetector {

    private CodepageDetectorProxy proxy = null;

    private String defaultCode = "US-ASCII";

    private List<Integer> chBreakList = new ArrayList<Integer>();
    public List<Integer> getChBreakList(){
        return chBreakList;
    }

    private char[] chBreak = new char[]{
            '.','!','\'','\"','?',')','{',
            '。','？','!','）','！'
    };

    private char[] chMid = new char[] {
            ',',':','-',';','(','}','`',
            '’','”','，','、','；','：','（','—','…','-','·','《','》',
    };

    public void init() {
        //获取文件检查单例
        proxy = CodepageDetectorProxy.getInstance();
        //不显示检查过程，可检查字符流
        proxy.add(new ParsingDetector(false));
        proxy.add(new ByteOrderMarkDetector());
        //依次加入测定类实例
        proxy.add(JChardetFacade.getInstance());
        proxy.add(UnicodeDetector.getInstance());
        proxy.add(ASCIIDetector.getInstance());
        for (int i = 0;i < chBreak.length;i++) {
            if (chBreakList.contains(chBreak[i])) {
                continue;
            }
            chBreakList.add((int)chBreak[i]);
        }
    }

    public String GetFileCharset(String fileName){
        if (proxy == null) {
            init();
        }
        Charset charset = null;
        try {
            //FileInputStream file = new FileInputStream(f);
            charset = proxy.detectCodepage(new File(fileName).toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (charset != null) {
            return charset.name();
        }
        return defaultCode;
    }
}
