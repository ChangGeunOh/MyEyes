package kr.pe.paran.myeyes.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import kr.pe.paran.myeyes.R;

public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private final String TAG  = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LinearLayout.inflate(getContext(), R.layout.dialog_bottom, null);

        Spinner spinnerCategory = view.findViewById(R.id.spinner_dialog_category);
        Spinner spinnerProduct  = view.findViewById(R.id.spinner_dialog_product);
        Button  buttonAdd       = view.findViewById(R.id.btn_add_number);
        Button  buttonRemove    = view.findViewById(R.id.btn_remove_number);
        buttonAdd.setOnClickListener(this);
        buttonRemove.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
