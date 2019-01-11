package kr.pe.paran.myeyes.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;

import kr.pe.paran.myeyes.R;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private final String TAG  = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LinearLayout.inflate(getContext(), R.layout.dialog_bottom, null);

        Spinner spinnerCategory = view.findViewById(R.id.spinner_dialog_category);
        Spinner spinnerProduct  = view.findViewById(R.id.spinner_dialog_product);
        /* test. */



        return view;
    }
}
