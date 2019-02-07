package kr.pe.paran.myeyes.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import kr.pe.paran.myeyes.R;

public class NumberDialog extends DialogFragment {


    private OnNumberInputListener mListener;


    private EditText mEditTextNumber;

    public interface OnNumberInputListener {
        void onInputNumber(int count);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LinearLayout.inflate(getContext(), R.layout.dialog_number, null);
        mEditTextNumber = view.findViewById(R.id.et_dialog_number);

        final NumberFormat numberFormat = new NumberFormat(mEditTextNumber);
        mEditTextNumber.addTextChangedListener(numberFormat);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onInputNumber(numberFormat.getNumber());
                        dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditTextNumber, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnNumberInputListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement OnNumberInputListener");
        }
    }

}
