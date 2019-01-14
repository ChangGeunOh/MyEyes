package kr.pe.paran.myeyes.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.database.UnitPriceEntry;
import kr.pe.paran.myeyes.model.UnitPrice;

public class UnitAdapter extends CursorAdapter {

    private final String    TAG = getClass().getSimpleName();

    private String          mBeforeCategory = "";

    public UnitAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LinearLayout.inflate(context, R.layout.item_unit, null);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvSection    = view.findViewById(R.id.tv_unit_section);
        viewHolder.tvPrice      = view.findViewById(R.id.tv_unit_price);
        viewHolder.tvProduct    = view.findViewById(R.id.tv_unit_prodct);
        viewHolder.tvStandard   = view.findViewById(R.id.tv_unit_standard);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        UnitPrice   unitPrice = getUnitPrice(cursor);
        Log.i(TAG, unitPrice.toString());

        viewHolder.tvSection.setVisibility(View.VISIBLE);
        if (!cursor.isFirst()) {
            cursor.moveToPrevious();
            UnitPrice beforeUnitprice = getUnitPrice(cursor);
            if (unitPrice.category.equals(beforeUnitprice.category)) {
                viewHolder.tvSection.setVisibility(View.GONE);
            }
            cursor.moveToNext();
        }

        viewHolder.tvSection.setText(unitPrice.category);
        viewHolder.tvProduct.setText(unitPrice.product);
        viewHolder.tvStandard.setText(unitPrice.standard);
        viewHolder.tvPrice.setText(Utility.getFormated(unitPrice.price) + " 원");
        mBeforeCategory = unitPrice.category;
    }

    public UnitPrice getUnitPrice(Cursor cursor) {

        UnitPrice unitPrice = new UnitPrice();
        unitPrice._id       = cursor.getInt(cursor.getColumnIndex(UnitPriceEntry._ID));
        unitPrice.category  = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.CATEGORY_COLUMN));
        unitPrice.product   = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.PRODUCT_COLUMN));
        unitPrice.standard  = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.STANDARD_COLUMN));
        unitPrice.unit      = cursor.getString(cursor.getColumnIndex(UnitPriceEntry.UNIT_COLUMN));
        unitPrice.price     = cursor.getInt(cursor.getColumnIndex(UnitPriceEntry.PRICE_COLUMN));

        return unitPrice;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    private static class ViewHolder {
        public TextView    tvSection;
        public TextView    tvProduct;
        public TextView    tvStandard;
        public TextView    tvPrice;
    }
}
