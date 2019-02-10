package kr.pe.paran.myeyes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {


    private static AppCompatDialog      mAppCompatDialog;

    public static String getFormated(int value) {
        return String.format("%,d", value);
    }

    public static String getFormated(long value) {
        return String.format("%,d", value);
    }

    public static String getRegDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        return simpleDateFormat.format(new Date());
    }

    public static String getFilenName(String name) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("(yyyyMMdd) ");
        return simpleDateFormat.format(new Date()) + name + ".pdf";
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showProgress(Context context) {

        if (mAppCompatDialog == null) {
            mAppCompatDialog = new AppCompatDialog(context);
            mAppCompatDialog.setCancelable(false);
            mAppCompatDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mAppCompatDialog.setContentView(R.layout.dialog_spinner);
        }
        mAppCompatDialog.show();

        final ImageView img_loading_frame = (ImageView) mAppCompatDialog.findViewById(R.id.img_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
    }

    public static void hideProgress() {
        mAppCompatDialog.dismiss();
    }

    public static File getDownloadFile(String strFile) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), strFile);
    }

    public static boolean isDiscount(String value) {
        if (value.startsWith("4년 이상")) {
            return true;
        }
        return false;
    }

    public static int getPriceCCTV(int cntCCTV, boolean isPeriodDiscount) {

        if (cntCCTV > 100) {
            return 8500 - (isPeriodDiscount ? 200 : 0);
        }
        else if (cntCCTV > 50) {
            return 9000  - (isPeriodDiscount ? 200 : 0);
        }

        return 9500  - (isPeriodDiscount ? 200 : 0);
    }

    public static String getWaterMark(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("group", "") + " " + preferences.getString("name", "");
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        return simpleDateFormat.format(new Date());
    }


}
