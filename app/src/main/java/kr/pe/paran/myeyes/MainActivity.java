package kr.pe.paran.myeyes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import kr.pe.paran.myeyes.database.EstimateSQLiteOpenHelper;
import kr.pe.paran.myeyes.database.UnitPriceSQLiteOpenHelper;
import kr.pe.paran.myeyes.model.Customer;
import kr.pe.paran.myeyes.model.Estimate;
import kr.pe.paran.myeyes.model.ProductPrice;
import kr.pe.paran.myeyes.ui.BottomSheetDialog;
import kr.pe.paran.myeyes.ui.CustomerAdapter;
import kr.pe.paran.myeyes.ui.CustomerDialog;
import kr.pe.paran.myeyes.ui.InfoActivity;
import kr.pe.paran.myeyes.ui.NumberDialog;
import kr.pe.paran.myeyes.ui.ProductListAdpater;
import kr.pe.paran.myeyes.ui.ReportActivity;
import kr.pe.paran.myeyes.ui.SettingsActivity;
import kr.pe.paran.myeyes.ui.UnitActivity;

public class MainActivity extends AppCompatActivity
        implements BottomSheetDialog.BottomSheetListener, CustomerDialog.CustomerDialogListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, NumberDialog.OnNumberInputListener {

    private final String TAG = getClass().getSimpleName();

    private Estimate mEstimate = null;


    private UnitPriceSQLiteOpenHelper mUnitPriceDbHelper;
    private EstimateSQLiteOpenHelper mEstimateDbHelper;

    private ListView mProductListView;
    private ProductListAdpater mProductListAdpater;

    private ListView mCustomListView;
    private CustomerAdapter mCustomerAdapter;
    private FloatingActionButton mFab_add;
    private BottomSheetDialog       mBottomSheetDialog;

    public static int mCntCCTV = 0;
//    public static Customer mCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        mFab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog(null);
            }
        });
        mFab_add.setVisibility(View.GONE);

        FloatingActionButton fab_new = findViewById(R.id.fab_new);
        fab_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerDialog customerDialog = new CustomerDialog();
                customerDialog.show(getSupportFragmentManager(), "CustomDialog");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mProductListView = findViewById(R.id.listview);
        mProductListAdpater = new ProductListAdpater(this);
        mProductListView.setAdapter(mProductListAdpater);
        mProductListView.setOnItemLongClickListener(this);

        mUnitPriceDbHelper = new UnitPriceSQLiteOpenHelper(this);
        mEstimateDbHelper = new EstimateSQLiteOpenHelper(this);

        mCustomListView = findViewById(R.id.listview_menu);
        mCustomerAdapter = new CustomerAdapter(this, mEstimateDbHelper.getCustomers());
        mCustomListView.setAdapter(mCustomerAdapter);

        mCustomListView.setOnItemClickListener(this);
        mCustomListView.setOnItemLongClickListener(this);

        Log.i(TAG, ">>>>>" + Utility.getWaterMark(this));
    }

    private void test() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        for (String key : preferences.getAll().keySet()) {
            Log.i(TAG, "Key>" + key);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_price) {
            Intent intent = new Intent(this, UnitActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_info) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_report) {
            showReportActivity();
        } else if (id == R.id.action_settings) {
            showSettingsActivity();
        }


        return super.onOptionsItemSelected(item);
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAddProductPrice(ProductPrice productPrice) {

        if (mEstimate != null) {
            if (productPrice._id > -1) {
                // EsitamteUpdate...
                Log.i(TAG, "Update......>" + productPrice.toString());
                mEstimateDbHelper.updateEstimate(productPrice._id, productPrice);
            } else {
                Log.i(TAG, productPrice.toString());
                mEstimate.addProduct(productPrice);
                mEstimateDbHelper.insertProduct(new Customer(mEstimate.custmer, mEstimate.period, mEstimate.reg_date), productPrice);
            }
            refresh(new Customer(mEstimate.custmer, mEstimate.period, mEstimate.reg_date));
            mProductListAdpater.setProductPrices(mEstimate.productPrices);
        }
    }

    @Override
    public void onRemoveProductPrice(int id) {
        mEstimateDbHelper.deleteProdcutPrice(id);
        refresh(new Customer(mEstimate.custmer, mEstimate.period, mEstimate.reg_date));
    }

    @Override
    public void onCompleteCustomer(String name, String period) {
        refresh(new Customer(name, period, Utility.getRegDate()));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView == mProductListView) {
            ProductPrice productPrice = mProductListAdpater.getItem(i);
            showBottomDialog(productPrice);
        } else {
            //
            final Customer customer = mCustomerAdapter.getItem(i);
            new AlertDialog.Builder(this)
                    .setMessage(customer.customer + " 고객님의 견적서를 삭제 하시겠습니까?")
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteEstimate(customer);
                        }
                    })
                    .create().show();
        }
        return true;
    }

    private void deleteEstimate(Customer customer) {
        mEstimateDbHelper.deleteCustomer(customer);
        mCustomerAdapter.setCusmters(mEstimateDbHelper.getCustomers());
    }

    private void showBottomDialog(ProductPrice productPrice) {
        mBottomSheetDialog = new BottomSheetDialog(mUnitPriceDbHelper, productPrice, Utility.isDiscount(mEstimate.period));
        mBottomSheetDialog.show(getSupportFragmentManager(), "BottomSheetDialog");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Customer customer = mCustomerAdapter.getItem(i);
        refresh(customer);
    }

    private void refresh(Customer customer) {

        setTitle(customer.customer);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        mEstimateDbHelper.reCalculate(customer.reg_date, customer.period);

        mEstimate = mEstimateDbHelper.getEsitmate(customer.reg_date);
        mProductListAdpater.setProductPrices(mEstimate.productPrices);
        mEstimate.custmer   = customer.customer;
        mEstimate.period    = customer.period;
        mEstimate.reg_date  = customer.reg_date;

        long total = 0; int cctvCnt = 0;
        for (ProductPrice productPrice : mEstimate.productPrices) {
            total += productPrice.sum;
            Log.i(TAG, productPrice + " > " + total + ", CCTV cnt>" + cctvCnt);
            if (productPrice.product.equals("CCTV")) {
                cctvCnt += productPrice.count;
            }
        }
        mCntCCTV = cctvCnt;

        ((TextView) findViewById(R.id.tv_sum_price)).setText(Utility.getFormated(total) + "원");

        mCustomerAdapter = new CustomerAdapter(this, mEstimateDbHelper.getCustomers());
        mCustomListView.setAdapter(mCustomerAdapter);

        mFab_add.setVisibility(View.VISIBLE);
    }

    private void showReportActivity() {
        if (mEstimate != null) {
            if (mEstimate.productPrices.size() > 0) {
                startActivity(ReportActivity.getIntent(this, new Customer(mEstimate.custmer, mEstimate.period, mEstimate.reg_date)));
            } else {
                Utility.showMessage(this, "견적서 작성을 위해 자재등록 후 실행하세요");
            }
        } else {
            Utility.showMessage(this, "고객등록과 자재등록 후 실행하세요.");
        }
    }

    @Override
    public void onInputNumber(int count) {
        Log.i(TAG, "onInputNumber>" + count);
        mBottomSheetDialog.setCount(count);
    }

}
