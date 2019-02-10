package kr.pe.paran.myeyes.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.database.UnitPriceSQLiteOpenHelper;
import kr.pe.paran.myeyes.model.UnitPrice;

public class UnitActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BottomUnitDialog.BottomUnitListener {

    private final String    TAG     = getClass().getSimpleName();

    private ListView        mListView;
    private UnitAdapter     mUnitAdapter;

    private UnitPriceSQLiteOpenHelper   mOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("제품단가표");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog(null);
            }
        });

        mOpenHelper = new UnitPriceSQLiteOpenHelper(this);

        mListView       = findViewById(R.id.listview_unit);
        mUnitAdapter    = new UnitAdapter(this, mOpenHelper.getUnitPrices(), true);
        mListView.setAdapter(mUnitAdapter);

        mListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Position>" + position);
        Cursor  cursor  = (Cursor) mUnitAdapter.getItem(position);
        UnitPrice unitPrice = mUnitAdapter.getUnitPrice(cursor);
        Log.i(TAG, unitPrice.toString());
        showBottomDialog(unitPrice);
    }

    private void showBottomDialog(UnitPrice unitPrice) {
        BottomUnitDialog bottomUnitDialog   = new BottomUnitDialog(unitPrice);
        bottomUnitDialog.show(getSupportFragmentManager(), "BottomUnitDialog");
    }

    @Override
    public void onAddUnitPrice(UnitPrice unitPrice) {
        Log.i(TAG, "UnitPrice._ID>" + unitPrice._id);
        if (unitPrice._id > 0) {
            mOpenHelper.updateUnitPrice(unitPrice);
        }
        else {
            mOpenHelper.insertUnitPrice(unitPrice);
        }
        mUnitAdapter    = new UnitAdapter(this, mOpenHelper.getUnitPrices(), true);
        mListView.setAdapter(mUnitAdapter);
    }

    @Override
    public void onRemoveUnitPrice(int id) {
        mOpenHelper.deleteUnitPrice(id);
        mUnitAdapter    = new UnitAdapter(this, mOpenHelper.getUnitPrices(), true);
        mListView.setAdapter(mUnitAdapter);
    }
}
