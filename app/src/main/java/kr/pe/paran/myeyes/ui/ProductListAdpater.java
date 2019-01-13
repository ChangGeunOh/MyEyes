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
import kr.pe.paran.myeyes.model.ProductPrice;

public class ProductListAdpater extends BaseAdapter {

    private final String        TAG = getClass().getSimpleName();

    private Context                     mContext;
    private ArrayList<ProductPrice>     mProductPrices;

    public ProductListAdpater(Context context) {
        this.mContext = context;
        this.mProductPrices = new ArrayList<>();
    }

    public void setProductPrices(ArrayList<ProductPrice> productPrices) {
        this.mProductPrices = productPrices;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mProductPrices.size();
    }

    @Override
    public ProductPrice getItem(int i) {
        return mProductPrices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LinearLayout.inflate(mContext, R.layout.item_product_list, null);

            ViewHolder  viewHolder  = new ViewHolder();
            viewHolder.tvProduct    = view.findViewById(R.id.tv_product);
            viewHolder.tvStandard   = view.findViewById(R.id.tv_standard);
            viewHolder.tvCount      = view.findViewById(R.id.tv_count);
            viewHolder.tvSum        = view.findViewById(R.id.tv_sum);

            view.setTag(viewHolder);
        }

        ProductPrice productPrice = getItem(i);
        ViewHolder  viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvProduct.setText(productPrice.product);
        viewHolder.tvStandard.setText(productPrice.standard);
        viewHolder.tvCount.setText("수량 : " + Utility.getFormated((productPrice).count));
        viewHolder.tvSum.setText(Utility.getFormated(productPrice.sum) + "원");

        return view;
    }


    private static class ViewHolder {
        TextView    tvProduct;
        TextView    tvStandard;
        TextView    tvCount;
        TextView    tvSum;
    }
}
