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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.pe.paran.myeyes.model.OptionItem;

import static android.content.Context.MODE_PRIVATE;

public class Utility {

    private static final String TAG = Utility.class.getSimpleName();

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

    public static ArrayList<OptionItem> loadOptions(Context context) {
        String[] options = context.getResources().getStringArray(R.array.option);
        ArrayList<OptionItem> optionItems = new ArrayList<>();
        for (int i = 0; i < options.length; i++) {
            Log.i(TAG, options[i]);
            ArrayList<OptionItem> optionsList = getOptions(context, options[i]);
            Log.i(TAG, "OptionList size>" + optionsList.size());
            for(OptionItem item : optionsList) {
                Log.i(TAG, options[i] + " : " + item);
                optionItems.add(item);
            }
        }

        return optionItems;
    }


    private static ArrayList<OptionItem> getOptions(Context context, String key) {

        ArrayList<OptionItem> optionItems = null;
        String[] options = context.getResources().getStringArray(R.array.option);

        SharedPreferences sharedPreferences = context.getSharedPreferences("Option", MODE_PRIVATE);
        Gson gson   = new Gson();
        String gsonValue = sharedPreferences.getString(key, "");

        Log.i(TAG, "GsonValue>" + gsonValue);
        if (!gsonValue.equals("")) {
            Type type = new TypeToken< ArrayList < OptionItem >>() {}.getType();
            optionItems = gson.fromJson(gsonValue, type);
        }
        else {
            optionItems = getDefaultOptions(key);
        }
        Log.i(TAG, "OpteionItems>" + optionItems);

        return optionItems;
    }

    public static void saveOptions(Context context, HashMap<String, ArrayList<OptionItem>> optiosHashMap) {
        String[] options = context.getResources().getStringArray(R.array.option);

        Gson gson = new Gson();
        SharedPreferences.Editor editor = context.getSharedPreferences("Option", MODE_PRIVATE).edit();
        for (int i = 0; i < options.length; i++) {
            ArrayList<OptionItem> optionItems = optiosHashMap.get(options[i]);
            String connectionsJSONString = gson.toJson(optionItems);
            editor.putString(options[i], connectionsJSONString);
        }
        editor.commit();

    }

    public static ArrayList<OptionItem> getDefaultOptions(String key) {
        Log.i(TAG, "getDefaultOptions>" + key);
        ArrayList<OptionItem> items = new ArrayList<>();
        if (key.equals("약정할인")) {
            items.add(new OptionItem("약정할인","CCTV", "1년 약정", 0));
            items.add(new OptionItem("약정할인","CCTV", "2년 약정", 0));
            items.add(new OptionItem("약정할인","CCTV", "3년 약정", 0));
            items.add(new OptionItem("약정할인","CCTV", "4년 약정", -200));
            items.add(new OptionItem("약정할인","CCTV", "5년 약정", -400));
        }
        else if(key.equals("선택사항")) {
            items.add(new OptionItem("저장공간","CCTV", "저장공간 15일 추가", 1500));
            items.add(new OptionItem("저장공간","CCTV", "저장공산 15일 추가 (아파트 프로모션)", 500));
        }

        return items;
    }

}
