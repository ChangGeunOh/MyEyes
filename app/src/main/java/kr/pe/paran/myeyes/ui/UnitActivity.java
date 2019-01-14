package kr.pe.paran.myeyes.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.database.UnitPriceSQLiteOpenHelper;

public class UnitActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

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

        setTitle("제품단가표");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mOpenHelper = new UnitPriceSQLiteOpenHelper(this);

        mListView       = findViewById(R.id.listview_unit);
        mUnitAdapter    = new UnitAdapter(this, mOpenHelper.getUnitPrices(), true);
        mListView.setAdapter(mUnitAdapter);

        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
