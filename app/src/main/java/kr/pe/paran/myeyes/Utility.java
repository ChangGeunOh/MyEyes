package kr.pe.paran.myeyes;

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
}
