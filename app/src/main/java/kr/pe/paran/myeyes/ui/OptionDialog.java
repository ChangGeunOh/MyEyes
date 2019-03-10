package kr.pe.paran.myeyes.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.model.OptionItem;

public class OptionDialog extends BottomSheetDialogFragment implements View.OnClickListener {


    private int         mPosition;
    private OptionItem  mOptionItem     = null;
    private EditText    mEditTextName;
    private EditText    mEditTextPrice;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_option, null);

        mEditTextName   = view.findViewById(R.id.et_option_standard);
        mEditTextPrice  = view.findViewById(R.id.et_option_price);

        AppCompatButton btn_add     = view.findViewById(R.id.btn_option_add);
        AppCompatButton btn_cancel  = view.findViewById(R.id.btn_option_remove);
        btn_add.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        if (getTag().equals("edit")) {

            btn_add.setText("수정하기");
            btn_cancel.setText("삭제하기");
            btn_cancel.setBackgroundTintList(ColorStateList.valueOf(0xffd81b60));
            btn_cancel.setTextColor(Color.WHITE);
        }
        if (mOptionItem != null) {
            mEditTextName.setText(mOptionItem.summary);
            mEditTextPrice.setText(Utility.getFormated(mOptionItem.price) + " 원");
        }


        return view;
    }

    @Override
    public void onClick(View v) {

    }


    public void setOptionItem(int position, OptionItem optionItem) {
        this.mPosition      = position;
        this.mOptionItem    = optionItem;
    }
}
