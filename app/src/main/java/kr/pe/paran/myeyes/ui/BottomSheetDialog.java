package kr.pe.paran.myeyes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import java.util.ArrayList;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.database.UnitPriceSQLiteOpenHelper;
import kr.pe.paran.myeyes.model.ProductPrice;
import kr.pe.paran.myeyes.model.UnitPrice;

@SuppressLint("ValidFragment")
public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = getClass().getSimpleName();

    private Spinner mSpinnerCategorry = null;
    private Spinner mSpinnerProduct = null;


    private UnitPriceSQLiteOpenHelper mOpenHelper;
    private ProductPrice mProductPrice;

    private BottomSheetListener mListener = null;

    private View            mView;
    private boolean         isEditMode;

    @SuppressLint("ValidFragment")
    public BottomSheetDialog(UnitPriceSQLiteOpenHelper unitPriceSQLiteOpenHelper, ProductPrice productPrice) {

        this.mOpenHelper = unitPriceSQLiteOpenHelper;

        if (productPrice == null) {
            isEditMode = false;
            this.mProductPrice = new ProductPrice();
        }
        else {
            isEditMode = true;
            this.mProductPrice = productPrice;
        }
    }

    public interface BottomSheetListener {
        void onAddProductPrice(ProductPrice productPrice);
        void onRemoveProductPrice(int id);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = LinearLayout.inflate(getContext(), R.layout.dialog_bottom, null);

        ArrayAdapter adapterCategory = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.category));
        mSpinnerCategorry = mView.findViewById(R.id.spinner_dialog_category);
        mSpinnerCategorry.setAdapter(adapterCategory);
        mSpinnerCategorry.setOnItemSelectedListener(this);

        mSpinnerProduct = mView.findViewById(R.id.spinner_dialog_product);
        mSpinnerProduct.setOnItemSelectedListener(this);

        mView.findViewById(R.id.btn_remove_number).setOnClickListener(this);
        mView.findViewById(R.id.btn_add_number).setOnClickListener(this);

        Button btnAdd       = mView.findViewById(R.id.btn_dialog_add);
        Button btnCancel    = mView.findViewById(R.id.btn_dialog_canel);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (isEditMode) {
            mSpinnerCategorry.setSelection(adapterCategory.getPosition(mProductPrice.category), true);
            btnAdd.setText("수정하기");
            btnCancel.setText("삭제하기");
            btnCancel.setBackgroundTintList(ColorStateList.valueOf(0xffd81b60));
            btnCancel.setTextColor(Color.WHITE);
            refreshView();
        }

        return mView;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_add_number) {
            mProductPrice.count++;
            refreshView();
        } else if (view.getId() == R.id.btn_remove_number) {
            mProductPrice.count--;
            if (mProductPrice.count < 1) {
                mProductPrice.count = 1;
            }
            refreshView();
        } else if (view.getId() == R.id.btn_dialog_add) {
            mListener.onAddProductPrice(mProductPrice);
            dismiss();
        } else {
            if (isEditMode) {
                mListener.onRemoveProductPrice(mProductPrice._id);
            }
            dismiss();
        }
    }

    private void refreshView() {
        mProductPrice.sum = mProductPrice.count * mProductPrice.price;
        ((TextView) mView.findViewById(R.id.tv_prouct_count)).setText(Utility.getFormated(mProductPrice.count));
        ((TextView) mView.findViewById(R.id.tv_dialog_price)).setText(Utility.getFormated(mProductPrice.sum));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Log.i(TAG, "Selected Item>" + adapterView.getSelectedItem());

        if (adapterView == mSpinnerCategorry) {
            String category = (String) adapterView.getSelectedItem();
            ArrayList<UnitPrice> productPrices = mOpenHelper.getProductPrices(category);
            ProductAdapter productAdapter = new ProductAdapter(getContext(), productPrices);
            mSpinnerProduct.setAdapter(productAdapter);
            if (isEditMode) {
                mSpinnerProduct.setSelection(productAdapter.getPosition(mProductPrice), true);
            }
        } else {
            if (!isEditMode) {
                UnitPrice unitPrice = (UnitPrice) adapterView.getSelectedItem();
                mProductPrice.category  = unitPrice.category;
                mProductPrice.product   = unitPrice.product;
                mProductPrice.standard  = unitPrice.standard;
                mProductPrice.unit      = unitPrice.unit;
                mProductPrice.price     = unitPrice.price;
                mProductPrice.count     = 1;
                mProductPrice.sum       = mProductPrice.price;
                refreshView();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement BottomSheetListener");
        }
    }
}
