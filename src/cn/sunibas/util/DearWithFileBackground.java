package cn.sunibas.util;

import cn.sunibas.entity.TSNewText;
import cn.sunibas.service.ITSNewTextService;

import java.io.File;
import java.util.List;

/**
 * Created by IBAS on 2017/2/16.
 * 这里讲求一个原则，当文稿上传后启动这里的一个线程，线程一直执行到没有文件可以处理时结束。
 * 这里只启动一个线程进行处理。
 */
public class DearWithFileBackground {
    private ITSNewTextService itsNewTextService;
    private DearWithOneFile dearWithOneFile;
    private boolean threadRun = false;

    public void setItsNewTextService(ITSNewTextService itsNewTextService) {
        this.itsNewTextService = itsNewTextService;
    }

    public void setDearWithOneFile(DearWithOneFile dearWithOneFile) {
        this.dearWithOneFile = dearWithOneFile;
    }

    /**
     * 这里的逻辑第一步是文件上传时触发
     * 然后循环执行到没有文件可以处理
     *      这里分为两步
     *          第一步，循环处理已知的文件
     *          第二步，循环查询是否有新的文件
     * (这里给前端加一个催进度的按钮，避免程序异常没有启动文件处理)
     * */
    public void start() {
        if (threadRun) {
            return ;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadRun = true;
                while (threadRun) {
                    List<TSNewText> tsNewTexts = itsNewTextService.getAll();
                    if (tsNewTexts.isEmpty()) {
                        break;
                    } else {
                        for (TSNewText tsNewText : tsNewTexts) {
                            //修改状态
                            tsNewText.setStatus(1);
                            itsNewTextService.update(tsNewText);
                            //处理文件
                            /*
                            try {
                                System.out.println("开始处理文件" + tsNewText.getUuid() + "\t预计需要30秒");
                                Thread.sleep(30 * 1000);
                                System.out.println("文件" + tsNewText.getUuid() + "处理完成");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            */
                            File file = new File(
                                    ManuscrripitDefaultSetting.folderLocation +
                                            tsNewText.getUuid());
                            int index = 0;
                            if (file.isDirectory()) {
                                String[] fileName = file.list();
                                for (int i = 0;i < fileName.length;i++) {
                                    index = dearWithOneFile.dearWithOneFile(
                                            file.getPath(),
                                            fileName[i],
                                            tsNewText,
                                            index
                                    );
                                }
                            }
                            //删除记录
                            itsNewTextService.delete(tsNewText);
                        }
                    }
                }
                System.out.println("run over");
                threadRun = false;
            }
        }).start();
    }
}
