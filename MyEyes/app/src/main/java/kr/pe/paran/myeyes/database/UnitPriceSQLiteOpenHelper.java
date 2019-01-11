package kr.pe.paran.myeyes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class UnitPriceSQLiteOpenHelper extends SQLiteOpenHelper {

    private final String            TAG     = getClass().getSimpleName();

    private static final String     DB_NAME     = "UnitPrice.db";
    private static final int        DB_VERSION  = 1;

    private Context                 mContext;


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UnitPriceEntry.TABLE_NAME + " (" +
                    UnitPriceEntry._ID              + " INTEGER PRIMARY KEY," +
                    UnitPriceEntry.CATEGORY_COLUMN  + " TEXT," +
                    UnitPriceEntry.PRODUCT_COLUMN   + " TEXT," +
                    UnitPriceEntry.STANDARD_COLUMN  + " TEXT," +
                    UnitPriceEntry.UNIT_COLUMN      + " TEXT," +
                    UnitPriceEntry.PRICE_COLUMN     + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UnitPriceEntry.TABLE_NAME;

    public UnitPriceSQLiteOpenHelper(@Nullable Context context) {

        super(context, DB_NAME, null, DB_VERSION);

        mContext    = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


    private void readLineFile(String filename) {

        BufferedReader bufferedReader = null;
        try (
            InputStreamReader inputStreamReader = new InputStreamReader(mContext.getAssets().open(filename), "UTF-8")) {
            bufferedReader = new BufferedReader(inputStreamReader);
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                Log.i(TAG, strLine);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
