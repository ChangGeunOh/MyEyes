package kr.pe.paran.myeyes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ListView;

import kr.pe.paran.myeyes.database.EstimateSQLiteOpenHelper;
import kr.pe.paran.myeyes.database.UnitPriceSQLiteOpenHelper;
import kr.pe.paran.myeyes.model.Customer;
import kr.pe.paran.myeyes.model.Estimate;
import kr.pe.paran.myeyes.model.ProductPrice;
import kr.pe.paran.myeyes.model.UnitPrice;
import kr.pe.paran.myeyes.ui.BottomSheetDialog;
import kr.pe.paran.myeyes.ui.CustomerAdapter;
import kr.pe.paran.myeyes.ui.CustomerDialog;
import kr.pe.paran.myeyes.ui.ProductListAdpater;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomSheetDialog.BottomSheetListener, CustomerDialog.CustomerDialogListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private final String    TAG         = getClass().getSimpleName();

    private Estimate        mEstimate   = null;


    private UnitPriceSQLiteOpenHelper   mUnitPriceDbHelper;
    private EstimateSQLiteOpenHelper    mEstimateDbHelper;

    private ListView                    mProductListView;
    private ProductListAdpater          mProductListAdpater;

    private ListView                    mCustomListView;
    private CustomerAdapter             mCustomerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog(null);
            }
        });

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProductListView   = findViewById(R.id.listview);
        mProductListAdpater = new ProductListAdpater(this);
        mProductListView.setAdapter(mProductListAdpater);
        mProductListView.setOnItemLongClickListener(this);

        mUnitPriceDbHelper  = new UnitPriceSQLiteOpenHelper(this);
        mEstimateDbHelper   = new EstimateSQLiteOpenHelper(this);

        mCustomListView = findViewById(R.id.listview_menu);
        mCustomerAdapter = new CustomerAdapter(this, mEstimateDbHelper.getCustomers());
        mCustomListView.setAdapter(mCustomerAdapter);
        mCustomListView.setOnItemClickListener(this);
        mCustomListView.setOnItemLongClickListener(this);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAddProductPrice(ProductPrice productPrice) {

        Log.i(TAG, "onAddProductPrice>" + (mEstimate == null));

        if (mEstimate != null) {
            if (productPrice._id > 1) {
                // EsitamteUpdate...
                mEstimateDbHelper.updateEstimate(productPrice._id, productPrice);
                refresh(mEstimate.custmer, mEstimate.reg_date);
            }
            else {
                Log.i(TAG, productPrice.toString());
                mEstimate.addProduct(productPrice);
                mEstimateDbHelper.insertProduct(mEstimate.custmer, productPrice, mEstimate.reg_date);
            }
            mProductListAdpater.setProductPrices(mEstimate.productPrices);
        }
    }

    @Override
    public void onRemoveProductPrice(int id) {
        mEstimateDbHelper.deleteProdcutPrice(id);
        refresh(mEstimate.custmer, mEstimate.reg_date);
    }

    @Override
    public void onCompleteCustomer(String name) {
        refresh(name, Utility.getRegDate());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView == mProductListView) {
            ProductPrice productPrice = mProductListAdpater.getItem(i);
            showBottomDialog(productPrice);
        }
        else {
            //
            Customer customer = mCustomerAdapter.getItem(i);
            deleteEstimate(customer);
        }

        return true;
    }

    private void deleteEstimate(Customer customer) {
        mEstimateDbHelper.deleteCustomer(customer);
        mCustomerAdapter.setCusmters(mEstimateDbHelper.getCustomers());
    }

    private void showBottomDialog(ProductPrice productPrice) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mUnitPriceDbHelper, productPrice);
        bottomSheetDialog.show(getSupportFragmentManager(), "BottomSheetDialog");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Customer customer = mCustomerAdapter.getItem(i);
        refresh(customer.customer, customer.reg_date);
    }

    private void refresh(String customer, String reg_date) {

        setTitle(customer);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        mEstimate           = mEstimateDbHelper.getEsitmate(reg_date);
        mProductListAdpater.setProductPrices(mEstimate.productPrices);
    }
}
