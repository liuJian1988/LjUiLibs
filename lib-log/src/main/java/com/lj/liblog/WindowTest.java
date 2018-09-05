package com.lj.liblog;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lj.liblog.utils.ScreenUtil;


public class WindowTest implements View.OnTouchListener {
    private static final String TAG = "WindowTest";
    private static WindowTest self;
    private TextView tv_showMessage;
    private static final boolean isFinish = false;
    private WindowManager wm;
    private LayoutParams lp;
    private Container container;
    private int currentX, currentY;
    private int w = 600, h = 600;
    static Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            String logStr = (String) msg.obj;
            self.upDateView(logStr);
        }
    };

    public static void install(Context context) {
        if (!isFinish) {
            if (self == null) {
                synchronized (WindowTest.class) {
                    if (self == null) {
                        self = new WindowTest(context);

                    }
                }
            }
        }
    }

    private WindowTest(Context context) {
        makeNewSystemWindow(context);

    }

    static StringBuilder sb = new StringBuilder();

    private void makeNewSystemWindow(Context context) {
        wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        container = new Container(context, null);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.setBackgroundColor(0x88000000);
        container.setOnTouchListener(this);
        tv_showMessage = new TextView(context);
        tv_showMessage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_showMessage.setTextColor(Color.RED);
        tv_showMessage.setClickable(false);
        tv_showMessage.setEnabled(false);
        container.addView(tv_showMessage);
        tv_showMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        lp = new LayoutParams();
        //设置类型
        lp.type = LayoutParams.TYPE_PHONE;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        //如果悬浮窗图片为透明图片,需要设置该参数为 PixelFormat.RGBA_8888
        lp.format = PixelFormat.RGBA_8888;
        lp.width = w;
        lp.height = h;
        //浮窗在屏幕中的位置
        currentX = ScreenUtil.getScreenSize(context).right - w;
        currentY = 0;
        lp.y = 0;
        lp.x = currentX;
        wm.addView(container, lp);

//        WindowManager wm = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        View view = LayoutInflater.from(context.getApplicationContext())
//                .inflate(R.layout.windowfloat, null);
//        tv_showMessage = (TextView) view.findViewById(R.id.tv_showMessage);
//        tv_showMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
//        LayoutParams lp = new LayoutParams();
//        lp.type = LayoutParams.TYPE_SYSTEM_OVERLAY;
//        lp.width = 600;
//        lp.height = 600;
//        lp.gravity = Gravity.RIGHT | Gravity.TOP;
//        wm.addView(view, lp);
        WindowLog.e("我把log打印到屏幕上了");
    }

    public void upDateView(String msg) {
        sb.append(msg + "\n");
        tv_showMessage.setText(sb.toString());
        int maxLine = tv_showMessage.getHeight()
                / tv_showMessage.getLineHeight();
        int y = tv_showMessage.getLineHeight()
                * (tv_showMessage.getLineCount() - maxLine);
        Log.e(TAG, "scrollTo " + y);
        if (y < 0)
            y = 0;
        tv_showMessage.scrollTo(0, y);
    }

    int x = 0, y = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        WindowLog.e(event.getRawX() + " ， " + event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int defX = (int) (x - event.getRawX());
                int defY = (int) (y - event.getRawY());
                if (Math.abs(defX) > 10 || Math.abs(defY) > 10) {//设置移动的阀值
                    currentX -= defX;
                    currentY -= defY;
                }
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:

                break;

        }
        lp.x = currentX;
        lp.y = currentY;
        wm.updateViewLayout(container, lp);
        return true;
    }

    public static class WindowLog {

        public static synchronized void e(String logStr) {
            if (isFinish)
                return;
            Message msg = handler.obtainMessage();
//            Bundle bd = new Bundle();
//            bd.putString("msg", logStr);
//            msg.setData(bd);
            msg.what = 0;
            msg.obj = logStr;
            handler.sendMessage(msg);
        }
    }

    private class Container extends LinearLayout {

        public Container(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);

        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }
}
