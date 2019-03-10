package kr.pe.paran.myeyes.ui;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.database.UnitPriceSQLiteOpenHelper;
import kr.pe.paran.myeyes.model.OptionItem;
import kr.pe.paran.myeyes.model.UnitPrice;

public class OptionActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener, BottomUnitDialog.BottomUnitListener {

    private final String    TAG     = getClass().getSimpleName();

    private ListView mListView;
    private UnitAdapter     mUnitAdapter;

    private UnitPriceSQLiteOpenHelper mOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("옵션설정");

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog(null);
            }
        });
*/

        mOpenHelper = new UnitPriceSQLiteOpenHelper(this);

        mListView       = findViewById(R.id.listview_unit);

        ArrayList<OptionItem> optionItems = Utility.loadOptions(this);
        for (OptionItem optionItem : optionItems) {
            Log.i(TAG, optionItem.toString());
        }
        OptionAdapter optionAdapter= new OptionAdapter(this, optionItems);

        mListView.setAdapter(optionAdapter);

        mListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_option_add) {
            OptionDialog optionDialog = new OptionDialog();
            optionDialog.show(getSupportFragmentManager(), "add");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Position>" + position);
        OptionDialog optionDialog = new OptionDialog();
        optionDialog.setOptionItem(position, (OptionItem) mListView.getAdapter().getItem(position));
        optionDialog.show(getSupportFragmentManager(), "edit");
    }

    private void showBottomDialog(UnitPrice unitPrice) {
        BottomUnitDialog bottomUnitDialog   = new BottomUnitDialog(unitPrice);
        bottomUnitDialog.show(getSupportFragmentManager(), "BottomUnitDialog");
    }

    @Override
    public void onAddUnitPrice(UnitPrice unitPrice) {
/*
        Log.i(TAG, "UnitPrice._ID>" + unitPrice._id);
        if (unitPrice._id > 0) {
            mOpenHelper.updateUnitPrice(unitPrice);
        }
        else {
            mOpenHelper.insertUnitPrice(unitPrice);
        }
        mUnitAdapter    = new UnitAdapter(this, mOpenHelper.getUnitPrices(), true);
        mListView.setAdapter(mUnitAdapter);
*/
    }

    @Override
    public void onRemoveUnitPrice(int id) {
/*
        mOpenHelper.deleteUnitPrice(id);
        mUnitAdapter    = new UnitAdapter(this, mOpenHelper.getUnitPrices(), true);
        mListView.setAdapter(mUnitAdapter);
*/
    }
}
