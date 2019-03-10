package kr.pe.paran.myeyes.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.model.OptionItem;
import kr.pe.paran.myeyes.model.UnitPrice;

public class OptionAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<OptionItem> mOptionItems;

    public OptionAdapter(Context context, ArrayList<OptionItem> optionItems) {

        this.mContext = context;
        this.mOptionItems = optionItems;
    }

    @Override
    public int getCount() {
        return mOptionItems.size();
    }

    @Override
    public OptionItem getItem(int position) {
        return mOptionItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.item_unit, null);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        OptionItem optionItem = getItem(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvSection.setVisibility(View.VISIBLE);
        if (position > 0 && position < mOptionItems.size()) {
            OptionItem beforeItem = getItem(position - 1);
            if (beforeItem.category.equals(optionItem.category)) {
                viewHolder.tvSection.setVisibility(View.GONE);
            }
        }
        viewHolder.tvSection.setText(optionItem.category);
        viewHolder.tvProduct.setText(optionItem.product);
        viewHolder.tvStandard.setText(optionItem.summary);
        viewHolder.tvPrice.setText(Utility.getFormated(optionItem.price) + " 원");

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvSection;
        public TextView tvProduct;
        public TextView tvStandard;
        public TextView tvPrice;

        public ViewHolder(View view) {
            tvSection = view.findViewById(R.id.tv_unit_section);
            tvProduct = view.findViewById(R.id.tv_unit_prodct);
            tvStandard= view.findViewById(R.id.tv_unit_standard);
            tvPrice     = view.findViewById(R.id.tv_unit_price);
        }
    }
}
