package kr.pe.paran.myeyes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import kr.pe.paran.myeyes.model.ProductPrice;

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
        readLineFile("UnitPrice.txt");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


    public void readLineFile(String filename) {

        BufferedReader bufferedReader = null;
        try (
            InputStreamReader inputStreamReader = new InputStreamReader(mContext.getAssets().open(filename), "UTF-8")) {
            bufferedReader = new BufferedReader(inputStreamReader);
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                String[] price = strLine.split("\\|");          // Length Size 4, 5
                insertProductPrice(new ProductPrice(price[0], price[1], price[2], price[3], (price.length == 4 ? Integer.valueOf(price[4]) : 0)));
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

    private long insertProductPrice(ProductPrice productPrice) {
        return getWritableDatabase().insert(UnitPriceEntry.TABLE_NAME, null, getContentValues(productPrice));
    }

    private ContentValues getContentValues(ProductPrice productPrice) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UnitPriceEntry.CATEGORY_COLUMN, productPrice.category);
        contentValues.put(UnitPriceEntry.PRODUCT_COLUMN, productPrice.product);
        contentValues.put(UnitPriceEntry.STANDARD_COLUMN, productPrice.standard);
        contentValues.put(UnitPriceEntry.UNIT_COLUMN, productPrice.unit);
        contentValues.put(UnitPriceEntry.PRICE_COLUMN, productPrice.price);

        return contentValues;
    }

    public Cursor getProductPrices() {
        return getWritableDatabase().query(UnitPriceEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    public ProductPrice getProductPrice(Cursor cursor) {

        ProductPrice productPrice = new ProductPrice();
        productPrice.category   = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.CATEGORY_COLUMN));
        productPrice.product    = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.PRODUCT_COLUMN));
        productPrice.standard   = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.STANDARD_COLUMN));
        productPrice.unit       = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.UNIT_COLUMN));
        productPrice.price      = cursor.getInt(cursor.getColumnIndex(UnitPriceEntry.PRICE_COLUMN));

        return productPrice;
    }
}
