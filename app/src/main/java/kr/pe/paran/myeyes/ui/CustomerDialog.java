package kr.pe.paran.myeyes.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import kr.pe.paran.myeyes.R;

public class CustomerDialog extends DialogFragment {

    private final String        TAG = getClass().getSimpleName();

    private CustomerDialogListener  mListener;
    private EditText                mCustomer;

    public interface CustomerDialogListener {
        void onCompleteCustomer(String name);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view   = getActivity().getLayoutInflater().inflate(R.layout.dialog_customer, null);
        mCustomer   = view.findViewById(R.id.dialog_customer);

        builder.setView(view)
                .setPositiveButton("완료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCompleteCustomer(mCustomer.getText().toString());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomerDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
          mListener = (CustomerDialogListener) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement CustomDialogListener");
        }
    }
}
