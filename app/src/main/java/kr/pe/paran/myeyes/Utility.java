package kr.pe.paran.myeyes;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {


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
}
