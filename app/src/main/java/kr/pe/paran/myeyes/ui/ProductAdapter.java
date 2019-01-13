package kr.pe.paran.myeyes.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.model.ProductPrice;
import kr.pe.paran.myeyes.model.UnitPrice;

public class ProductAdapter extends BaseAdapter {

    private final String        TAG = getClass().getSimpleName();

    private Context                  mContext;
    private ArrayList<UnitPrice>     mProductPrices;

    public ProductAdapter(Context context, ArrayList<UnitPrice> productPrices) {
        this.mContext       = context;
        this.mProductPrices = productPrices;
    }

    @Override
    public int getCount() {
        return mProductPrices.size();
    }

    @Override
    public UnitPrice getItem(int i) {
        return mProductPrices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LinearLayout.inflate(mContext, R.layout.item_product, null);
            ViewHolder viewHolder   = new ViewHolder();
            viewHolder.tvProduct    = view.findViewById(R.id.item_product);
            viewHolder.tvStandard   = view.findViewById(R.id.item_standard);
            view.setTag(viewHolder);
        }
        UnitPrice productPrice = getItem(i);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvProduct.setText(productPrice.product);
        viewHolder.tvStandard.setText(productPrice.standard);

        return view;
    }

    public int getPosition(ProductPrice productPrice) {

        int index = 0;
        for (int i = 0; i < mProductPrices.size(); i++) {
            UnitPrice unitPrice = mProductPrices.get(i);
            if (unitPrice.product.equals(productPrice.product) && unitPrice.standard.equals(productPrice.standard)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private static class ViewHolder {
        TextView    tvProduct;
        TextView    tvStandard;
    }
}
