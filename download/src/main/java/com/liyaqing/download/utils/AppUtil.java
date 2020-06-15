package com.liyaqing.download.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.File;

public class AppUtil {
    public static void openApk(Context context, File file) {
        install(context, file.getAbsolutePath());
    }
    public static void install(Context context, String path) {
    File apkFile = new File(path);
    if (apkFile.exists()) {
        Intent intent = new Intent("android.intent.action.VIEW");
        String authority = context.getPackageName() + ".provider";
        Uri uri =getUriForFile(context, authority, apkFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
} public static Uri getUriForFile(@NonNull Context context, @NonNull String authority, @NonNull File file) {
    if (Build.VERSION.SDK_INT <= 23) {
        try {
            Runtime.getRuntime().exec("chmod 777 " + file.getCanonicalPath());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return Uri.fromFile(file);
    } else {
        return getUriForFile(context, authority, file);
    }
}
}
