package com.lj.ljuilibs;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lj.liblog.WindowTest;
import com.lj.ljuilibs.utils.PermissionUtil;

//import com.lj.libtoolbar.CommonToolbar;
//import static com.lj.libtoolbar.Style.TRANSPARENT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        CommonToolbar.Builder(this)
//                .setStatusBarStyle(TRANSPARENT)
//                .setBackgroundColorRes(R.color.colorAccent)
//                .addLeftIcon(1, R.mipmap.ic_launcher, 30, 30,null) // 响应左部图标的点击事件
//                .addLeftText(2, "Left", 50f,null)// 响应左部文本的点击事件
//                .addRightText(3, "Right",null) // 响应右部文本的点击事件
//                .addRightIcon(4, R.mipmap.ic_launcher_round,null) // 响应右部图标的点击事件
//                .apply();
        boolean isPermissionOk = true;
        if (Build.VERSION.SDK_INT > 23) {
            isPermissionOk = PermissionUtil.request(this, Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
        WindowTest.install(getApplicationContext());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WindowTest.install(getApplication());
    }
}
