package kr.pe.paran.myeyes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
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

import kr.pe.paran.myeyes.MainActivity;
import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.database.UnitPriceSQLiteOpenHelper;
import kr.pe.paran.myeyes.model.ProductPrice;
import kr.pe.paran.myeyes.model.UnitPrice;

@SuppressLint("ValidFragment")
public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DoubleClick.DoubleClickListener {

    private final String TAG = getClass().getSimpleName();

    public static String TAG_DIALOG_NUMBER_ONE = "dialog_number1";
    public static String TAG_DIALOG_NUMBER_TWO = "dialog_number2";

    private Spinner mSpinnerCategorry = null;
    private Spinner mSpinnerProduct = null;


    private UnitPriceSQLiteOpenHelper mOpenHelper;
    private ProductPrice mProductPrice;

    private BottomSheetListener mListener = null;

    private View mView;
    private boolean isEditMode;
    private TextView mTextViewNumber;

    private int beforeCnt = 0;
    private boolean isPeriodDiscount = false;


    @SuppressLint("ValidFragment")
    public BottomSheetDialog(UnitPriceSQLiteOpenHelper unitPriceSQLiteOpenHelper, ProductPrice productPrice, boolean isDiscount) {

        this.isPeriodDiscount = isDiscount;
        this.mOpenHelper = unitPriceSQLiteOpenHelper;

        if (productPrice == null) {
            isEditMode = false;
            this.mProductPrice = new ProductPrice();
        } else {
            isEditMode = true;
            this.mProductPrice = productPrice;
        }
    }

    @Override
    public void onSingleClick(View view) {
        if (view.getId() == R.id.btn_add_number) {
            mProductPrice.count++;
        } else if (view.getId() == R.id.btn_remove_number) {
            mProductPrice.count--;
            if (mProductPrice.count < 1) {
                mProductPrice.count = 1;
            }
        } else if (view.getId() == R.id.btn_add_number2) {
            mProductPrice.subCount++;
        } else {
            mProductPrice.subCount--;
            if (mProductPrice.subCount < 1) {
                mProductPrice.subCount = 1;
            }
        }
        reCalculateUnitPrice();
    }

    @Override
    public void onDoubleClick(View view) {
        if (view.getId() == R.id.btn_add_number) {
            mProductPrice.count += 10;
        } else if (view.getId() == R.id.btn_remove_number) {
            mProductPrice.count -= 10;
            if (mProductPrice.count < 1) {
                mProductPrice.count = 1;
            }
        } else if (view.getId() == R.id.btn_add_number2) {
            mProductPrice.subCount += 10;
        } else {
            mProductPrice.subCount -= 10;
            if (mProductPrice.subCount < 1) {
                mProductPrice.subCount = 1;
            }
        }
        reCalculateUnitPrice();
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

        mView.findViewById(R.id.btn_remove_number).setOnClickListener(new DoubleClick(this));
        mView.findViewById(R.id.btn_add_number).setOnClickListener(new DoubleClick(this));

        mView.findViewById(R.id.btn_remove_number2).setOnClickListener(new DoubleClick(this));
        mView.findViewById(R.id.btn_add_number2).setOnClickListener(new DoubleClick(this));

        mTextViewNumber = mView.findViewById(R.id.tv_prouct_count);
        mTextViewNumber.findViewById(R.id.tv_prouct_count).setOnClickListener(this);
        mView.findViewById(R.id.tv_prouct_count2).setOnClickListener(this);

        Button btnAdd = mView.findViewById(R.id.btn_dialog_add);
        Button btnCancel = mView.findViewById(R.id.btn_dialog_canel);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        if (isEditMode) {
            mSpinnerCategorry.setSelection(adapterCategory.getPosition(mProductPrice.category), true);
            btnAdd.setText("수정하기");
            btnCancel.setText("삭제하기");
            btnCancel.setBackgroundTintList(ColorStateList.valueOf(0xffd81b60));
            btnCancel.setTextColor(Color.WHITE);
            reCalculateUnitPrice();
        }

        return mView;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_add_number) {
            mProductPrice.count++;
            reCalculateUnitPrice();
        } else if (view.getId() == R.id.btn_remove_number) {
            mProductPrice.count--;
            if (mProductPrice.count < 1) {
                mProductPrice.count = 1;
            }
            reCalculateUnitPrice();
        } else if (view.getId() == R.id.btn_dialog_add) {
            mListener.onAddProductPrice(mProductPrice);
            dismiss();
        } else if (view.getId() == R.id.tv_prouct_count) {
            showNumberDialog(view);
        } else if (view.getId() == R.id.tv_prouct_count2) {
            Log.i(TAG, "Product Count... 2");
            showNumberDialog(view);
        } else {
            if (isEditMode) {
                mListener.onRemoveProductPrice(mProductPrice._id);
            }
            dismiss();
        }
    }

    private void refreshView() {

        mProductPrice.sum = mProductPrice.count * mProductPrice.price * (mProductPrice.subCount < 1 ? 1 : mProductPrice.subCount);
        ((TextView) mView.findViewById(R.id.tv_prouct_count)).setText(Utility.getFormated(mProductPrice.count));
        ((TextView) mView.findViewById(R.id.tv_prouct_count2)).setText(Utility.getFormated(mProductPrice.subCount));
        ((TextView) mView.findViewById(R.id.tv_dialog_price)).setText(Utility.getFormated(mProductPrice.sum));

        if (mProductPrice.product == null) return;

        String strPrice;
        if (mProductPrice.product.equals("인건비")) {
            strPrice = "단가 : " + Utility.getFormated(mProductPrice.price) +
                    "원, 수량 : " + Utility.getFormated(mProductPrice.count) +
                    "명x" + Utility.getFormated(mProductPrice.subCount) +
                    "일";
        } else {
            strPrice = "단가 : " + Utility.getFormated(mProductPrice.price) + "원, 수량 : " + Utility.getFormated(mProductPrice.count);
        }
        ((TextView) mView.findViewById(R.id.tv_product_price)).setText(strPrice);

//        reCalculateUnitPrice();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Log.i(TAG, "Selected Item>" + adapterView.getSelectedItem());
        reCalculateUnitPrice();

        if (adapterView == mSpinnerCategorry) {
            String category = (String) adapterView.getSelectedItem();
            ArrayList<UnitPrice> productPrices = mOpenHelper.getProductPrices(category);
            ProductAdapter productAdapter = new ProductAdapter(getContext(), productPrices);
            mSpinnerProduct.setAdapter(productAdapter);
            if (isEditMode) {
                mSpinnerProduct.setSelection(productAdapter.getPosition(mProductPrice), true);
            }
        } else {
            UnitPrice unitPrice = (UnitPrice) adapterView.getSelectedItem();
            if (!isEditMode) {
                mProductPrice.count = 1;
                mProductPrice.sum = mProductPrice.price;
            }
            mProductPrice.category = unitPrice.category;
            mProductPrice.product = unitPrice.product;
            mProductPrice.standard = unitPrice.standard;
            mProductPrice.unit = unitPrice.unit;
            mProductPrice.price = unitPrice.price;

            if (unitPrice.product.equals("인건비")) {
                showSubCount(true);
            } else {
                showSubCount(false);
            }
        }
        reCalculateUnitPrice();
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

    private void showNumberDialog(View view) {
        NumberDialog numberDialog = new NumberDialog();
        numberDialog.show(getFragmentManager(), view.getId() == R.id.tv_prouct_count ? TAG_DIALOG_NUMBER_ONE : TAG_DIALOG_NUMBER_TWO);
    }

    public void setCount(int count) {
        mProductPrice.count = count;
        reCalculateUnitPrice();
    }

    public void setSubCount(int count) {
        mProductPrice.subCount = count;
        reCalculateUnitPrice();
    }


    private void reCalculateUnitPrice() {

        String category = (String) mSpinnerCategorry.getSelectedItem();
        UnitPrice product = (UnitPrice) mSpinnerProduct.getSelectedItem();

        if (product == null) return;

        if (product.product.startsWith("CCTV")) {
            Log.i(TAG, "category>" + category + ", " + product.toString());
            Log.i(TAG, "CCTV Count>" + MainActivity.mCntCCTV);
            Log.i(TAG, "CCTV Count>" + Integer.parseInt(mTextViewNumber.getText().toString()));

            int totalCCTV = MainActivity.mCntCCTV + mProductPrice.count;

            Log.i(TAG, "Total>" + totalCCTV + ", Before>" + beforeCnt);
            if (totalCCTV > 100) {
                mProductPrice.price = 8500 - (isPeriodDiscount ? 200 : 0);
                if (beforeCnt <= 100) {
                    // 101대 이상 1,000원, 약정기간 할인 200원 적용 (단가 : 8,300원/월)
                    String message = "100대 이상 1,000원";
                    message += isPeriodDiscount ? ", 약정기간 200원 할인" : " 할인";
                    Utility.showMessage(getContext(), message);
                }
            } else if (totalCCTV > 50) {
                mProductPrice.price = 9000 - (isPeriodDiscount ? 200 : 0);
                if (beforeCnt <= 50 || beforeCnt > 100) {
                    // 50~100대 500원 할인, 약정기간 할인 200원 적용 (단가 : 8,800원/월)
                    String message = "50~100대 500원";
                    message += isPeriodDiscount ? ", 약정기간 200원 할인" : " 할인";
                    Utility.showMessage(getContext(), message);
                }
            } else {
                mProductPrice.price = 9500 - (isPeriodDiscount ? 200 : 0);
                if (beforeCnt > 49 && isPeriodDiscount) {
                    // 약정기간 할인 200원 적용 (단가 : 9,300원/월)
                    Utility.showMessage(getContext(), "약정기간 200원 할인");
                }
            }
            beforeCnt = totalCCTV;
        }

        refreshView();
    }

    private void showSubCount(boolean isView) {
        mView.findViewById(R.id.tv_count_label).setVisibility(isView ? View.VISIBLE : View.GONE);
        mView.findViewById(R.id.tv_count_label2).setVisibility(isView ? View.VISIBLE : View.GONE);
        mView.findViewById(R.id.btn_add_number2).setVisibility(isView ? View.VISIBLE : View.GONE);
        mView.findViewById(R.id.btn_remove_number2).setVisibility(isView ? View.VISIBLE : View.GONE);
        mView.findViewById(R.id.tv_prouct_count2).setVisibility(isView ? View.VISIBLE : View.GONE);
    }
}
