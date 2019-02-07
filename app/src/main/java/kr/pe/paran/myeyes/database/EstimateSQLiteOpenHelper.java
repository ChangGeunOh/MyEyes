package kr.pe.paran.myeyes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import kr.pe.paran.myeyes.model.Customer;
import kr.pe.paran.myeyes.model.Estimate;
import kr.pe.paran.myeyes.model.ProductPrice;

public class EstimateSQLiteOpenHelper extends SQLiteOpenHelper {

    private final String            TAG     = getClass().getSimpleName();

    private static final String     DB_NAME     = "Estimate.db";
    private static final int        DB_VERSION  = 3;

    private Context mContext;


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EstimateEntry.TABLE_NAME + " (" +
                    EstimateEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    EstimateEntry.CUSTOMER_COLUMN   + " TEXT," +
                    EstimateEntry.PERIOD_COLUMN     + " TEXT," +
                    EstimateEntry.CATEGORY_COLUMN   + " TEXT," +
                    EstimateEntry.PRODUCT_COLUMN    + " TEXT," +
                    EstimateEntry.STANDARD_COLUMN   + " TEXT," +
                    EstimateEntry.UNIT_COLUMN       + " TEXT," +
                    EstimateEntry.PRICE_COLUMN      + " INTEGER," +
                    EstimateEntry.COUNT_COLUMN      + " INTEGER," +
                    EstimateEntry.SUBCOUNT_COLUMN   + " INTEGER," +
                    EstimateEntry.SUM_COULUMN       + " INTEGER," +
                    EstimateEntry.REG_DATE_COLUMN   + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EstimateEntry.TABLE_NAME;

    public EstimateSQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
//        onTest();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public Estimate getEsitmate(String reg_date) {

        Cursor cursor = getWritableDatabase().query(EstimateEntry.TABLE_NAME,
                null,
                EstimateEntry.REG_DATE_COLUMN + " = ?",
                new String[]{reg_date},
                null,
                null,
                EstimateEntry.CATEGORY_COLUMN + " ASC",
                null);

        Log.i(TAG, "ColumnCount>" + cursor.getColumnIndex(EstimateEntry.CUSTOMER_COLUMN));
        Log.i(TAG, "ColumnCount>" + cursor.getColumnName(1));

        Estimate estimate = new Estimate();

        while(cursor.moveToNext()) {

            estimate.custmer    = cursor.getString(cursor.getColumnIndex(EstimateEntry.CUSTOMER_COLUMN));
            estimate.period     = cursor.getString(cursor.getColumnIndex(EstimateEntry.PERIOD_COLUMN));
            estimate.reg_date   = cursor.getString(cursor.getColumnIndex(EstimateEntry.REG_DATE_COLUMN));

            ProductPrice productPrice = new ProductPrice();
            productPrice._id        = cursor.getInt(cursor.getColumnIndex(EstimateEntry._ID));
            productPrice.category   = cursor.getString(cursor.getColumnIndex(EstimateEntry.CATEGORY_COLUMN));
            productPrice.product    = cursor.getString(cursor.getColumnIndex(EstimateEntry.PRODUCT_COLUMN));
            productPrice.standard   = cursor.getString(cursor.getColumnIndex(EstimateEntry.STANDARD_COLUMN));
            productPrice.unit       = cursor.getString(cursor.getColumnIndex(EstimateEntry.UNIT_COLUMN));
            productPrice.price      = cursor.getInt(cursor.getColumnIndex(EstimateEntry.PRICE_COLUMN));
            productPrice.count      = cursor.getInt(cursor.getColumnIndex(EstimateEntry.COUNT_COLUMN));
            productPrice.subCount   = cursor.getInt(cursor.getColumnIndex(EstimateEntry.SUBCOUNT_COLUMN));
            productPrice.sum        = cursor.getLong(cursor.getColumnIndex(EstimateEntry.SUM_COULUMN));
            estimate.addProduct(productPrice);
        }

        return estimate;
    }
    
    public long insertProduct(Customer customer, ProductPrice productPrice) {

        if (customer == null) return  -1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(EstimateEntry.CUSTOMER_COLUMN, customer.customer);
        contentValues.put(EstimateEntry.PERIOD_COLUMN, customer.period);
        contentValues.put(EstimateEntry.CATEGORY_COLUMN, productPrice.category);
        contentValues.put(EstimateEntry.PRODUCT_COLUMN, productPrice.product);
        contentValues.put(EstimateEntry.STANDARD_COLUMN, productPrice.standard);
        contentValues.put(EstimateEntry.UNIT_COLUMN, productPrice.unit);
        contentValues.put(EstimateEntry.PRICE_COLUMN, productPrice.price);
        contentValues.put(EstimateEntry.COUNT_COLUMN, productPrice.count);
        contentValues.put(EstimateEntry.SUBCOUNT_COLUMN, productPrice.subCount);
        contentValues.put(EstimateEntry.SUM_COULUMN, productPrice.sum);
        contentValues.put(EstimateEntry.REG_DATE_COLUMN, customer.reg_date);

        return getWritableDatabase().insert(EstimateEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Customer> getCustomers() {

        ArrayList<Customer> customers = new ArrayList<>();

        Cursor cursor   = getWritableDatabase().query(true,
                EstimateEntry.TABLE_NAME,
                new String[]{EstimateEntry.CUSTOMER_COLUMN, EstimateEntry.PERIOD_COLUMN, EstimateEntry.REG_DATE_COLUMN},
                null,
                null,
                null,
                null,
                EstimateEntry.REG_DATE_COLUMN + " DESC",
                null);
        while (cursor.moveToNext()) {
            Customer customer = new Customer(cursor.getString(cursor.getColumnIndex(EstimateEntry.CUSTOMER_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(EstimateEntry.PERIOD_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(EstimateEntry.REG_DATE_COLUMN)));
            if (customer.customer != null) customers.add(customer);
        }

        return customers;
    }

    public long updateEstimate(int id, ProductPrice productPrice) {
        return getWritableDatabase().update(EstimateEntry.TABLE_NAME, getContentValue(productPrice), EstimateEntry._ID + " = " + id, null);
    }

    private ContentValues getContentValue(ProductPrice productPrice) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(EstimateEntry.CATEGORY_COLUMN, productPrice.category);
        contentValues.put(EstimateEntry.PRODUCT_COLUMN, productPrice.product);
        contentValues.put(EstimateEntry.STANDARD_COLUMN, productPrice.standard);
        contentValues.put(EstimateEntry.UNIT_COLUMN, productPrice.unit);
        contentValues.put(EstimateEntry.PRICE_COLUMN, productPrice.price);
        contentValues.put(EstimateEntry.COUNT_COLUMN, productPrice.count);
        contentValues.put(EstimateEntry.SUBCOUNT_COLUMN, productPrice.subCount);
        contentValues.put(EstimateEntry.SUM_COULUMN, productPrice.sum);

        return contentValues;
    }

    public long deleteProdcutPrice(int id) {
        return getWritableDatabase().delete(EstimateEntry.TABLE_NAME, EstimateEntry._ID + " = " + id, null);
    }

    public long deleteCustomer(Customer customer) {
        return getWritableDatabase().delete(EstimateEntry.TABLE_NAME, EstimateEntry.REG_DATE_COLUMN +  " =  ?", new String[]{customer.reg_date});
    }

    public void onTest() {
        Cursor cursor = getWritableDatabase().query(EstimateEntry.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext())
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(EstimateEntry.CUSTOMER_COLUMN)));
    }


}
