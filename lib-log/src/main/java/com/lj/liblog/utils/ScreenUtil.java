package com.lj.liblog.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.WindowManager;

/**
 * 作者：liujian on 2018/9/5 10:02
 * 邮箱：15313727484@163.com
 */
public class ScreenUtil {
    /**
     * 获取实际屏幕的宽高
     */
    public static Rect getScreenSize(Context context) {
        Rect r = new Rect();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRectSize(r);
        return r;
    }
}
