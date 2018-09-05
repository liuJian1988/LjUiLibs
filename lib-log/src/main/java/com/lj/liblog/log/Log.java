package com.lj.liblog.log;

import android.os.Environment;

import com.lj.liblog.WindowTest;
import com.lj.liblog.utils.FileUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Liujian on 2018/3/6 0006.
 *
 * @link http://blog.csdn.net/liujian8654562
 */

public class Log implements ILog {
    public static ILog instance = new Log();
    private String dir = "";
    private String fileName = "";

    private Log() {

    }

    @Override
    public void writeToFile(String tag, String msg) {
        Date date = new Date();
        dir = Environment.getExternalStorageDirectory() + "/flx/log";
        fileName = date.getMonth()+ date.getDate()+"";

        boolean isOk = false;
        try {
            isOk = FileUtil.createFile(dir, fileName);
            String currentTime = date.toGMTString();
            if (isOk) {
                FileUtil.writeStrToFile(dir, fileName, currentTime + " : " + tag + " : " + msg + "\n", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WindowTest.WindowLog.e(msg);
    }
}
