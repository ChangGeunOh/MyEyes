package kr.pe.paran.myeyes.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberFormat implements TextWatcher {


    private EditText    mEditText;
    private String      mUnit;

    private boolean     isEditing;


    public NumberFormat(EditText editText) {

        this.mEditText  = editText;
        this.mUnit      = "";

    }

    public NumberFormat(EditText editText, String unit) {
        this.mEditText = editText;
        this.mUnit = " " + unit;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (isEditing) return;
        isEditing = true;

        String str = s.toString().replaceAll("[^\\d]", "");
        if (str.length() > 0) {
            Long value = Long.valueOf(str);
            mEditText.setText(String.format("%,d", value) + mUnit);
            mEditText.setSelection(mEditText.getText().toString().length() - mUnit.length());
        }

        isEditing = false;
    }

    public int getNumber() {
        return Integer.parseInt(mEditText.getText().toString().replaceAll("[^\\d]", ""));
    }
}
