package kr.pe.paran.myeyes.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import kr.pe.paran.myeyes.R;

public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final String TAG  = getClass().getSimpleName();

    private Spinner mSpinnerCategorry   = null;
    private Spinner mSpinnerProduct     = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LinearLayout.inflate(getContext(), R.layout.dialog_bottom, null);

        mSpinnerCategorry = view.findViewById(R.id.spinner_dialog_category);
        ArrayAdapter adapterCategory = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.category));
        mSpinnerProduct.setAdapter(adapterCategory);
        mSpinnerCategorry.setOnItemClickListener(this);
        mSpinnerCategorry.setAdapter(adapterCategory);

        mSpinnerProduct  = view.findViewById(R.id.spinner_dialog_product);
        mSpinnerProduct.setOnItemClickListener(this);

        Button  btnAdd       = view.findViewById(R.id.btn_add_number);
        Button  btnRemove    = view.findViewById(R.id.btn_remove_number);
        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "Category Spinner is " + (view == mSpinnerCategorry));
        Log.i(TAG, "Product  Spinner is " + (view == mSpinnerProduct));
    }
}
