package kr.pe.paran.myeyes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.model.UnitPrice;

@SuppressLint("ValidFragment")
public class BottomUnitDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private BottomUnitListener mListener;

    private Spinner mSpinnerCategory;
    private EditText mEditTextProduct;
    private EditText mEditTextStandard;
    private EditText mEditTextUnit;
    private EditText mEditTextPrice;

    private UnitPrice mUnitPrice;
    private boolean isEditMode = false;

    public BottomUnitDialog(UnitPrice unitPrice) {

        this.mUnitPrice = unitPrice;
        this.isEditMode = unitPrice == null ? false : true;
//        Log.i(TAG, isEditMode + ">" + mUnitPrice.toString());
    }


    public interface BottomUnitListener {
        void onAddUnitPrice(UnitPrice unitPrice);

        void onRemoveUnitPrice(int id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LinearLayout.inflate(getContext(), R.layout.dialog_unit, null);

        mSpinnerCategory = view.findViewById(R.id.spinner_unit_category);
        mEditTextProduct = view.findViewById(R.id.et_unit_product);
        mEditTextStandard = view.findViewById(R.id.et_unit_standard);
        mEditTextUnit = view.findViewById(R.id.et_unit_unit);
        mEditTextPrice = view.findViewById(R.id.et_unit_price);

        mEditTextPrice.addTextChangedListener(new TextWatcher() {

            boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (isEditing) return;
                isEditing = true;

                String str = editable.toString().replaceAll("[^\\d]", "");
                if (str.length() > 0) {
                    Long value = Long.valueOf(str);
                    mEditTextPrice.setText(String.format("%,d 원", value));
                    mEditTextPrice.setSelection(mEditTextPrice.getText().toString().length() - 2);
                }

                isEditing = false;
            }
        });


        AppCompatButton btnAdd = view.findViewById(R.id.btn_unit_add);
        AppCompatButton btnRemove = view.findViewById(R.id.btn_unit_remove);
        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

        if (isEditMode) {
            mSpinnerCategory.setSelection(getPosition(mUnitPrice.category), true);
            btnAdd.setText("수정하기");
            btnRemove.setText("삭제하기");
            btnRemove.setBackgroundTintList(ColorStateList.valueOf(0xffd81b60));
            btnRemove.setTextColor(Color.WHITE);

            mEditTextProduct.setText(mUnitPrice.product);
            mEditTextStandard.setText(mUnitPrice.standard);
            mEditTextUnit.setText(mUnitPrice.unit);
            mEditTextPrice.setText(Utility.getFormated(mUnitPrice.price) + "원");
        }

        return view;
    }

    private int getPosition(String value) {
        String[] category = getResources().getStringArray(R.array.category);
        int position = -1;
        for (int i = 0; i < category.length; i++) {
            if (value.equals(category[i])) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public void onClick(View view) {

        Log.i(TAG, "Button Clicked......");
        if (view.getId() == R.id.btn_unit_add) {
            if (!isEditMode) {
                mUnitPrice = new UnitPrice();
            }

            mUnitPrice.category = (String) mSpinnerCategory.getSelectedItem();
            mUnitPrice.product = mEditTextProduct.getText().toString();
            mUnitPrice.standard = mEditTextStandard.getText().toString();
            mUnitPrice.unit = mEditTextUnit.getText().toString();

            String value = mEditTextPrice.getText().toString();
            value = value.replaceAll("[^\\d]", "");
            mUnitPrice.price = Integer.parseInt(value);
            Log.i(TAG, "----------->" + mUnitPrice._id);
            mListener.onAddUnitPrice(mUnitPrice);
        } else {
            if (isEditMode) {
                mListener.onRemoveUnitPrice(mUnitPrice._id);
            }
        }
        dismiss();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomUnitListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement BottomUnitListener");
        }
    }
}
