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
import kr.pe.paran.myeyes.model.Customer;

public class CustomerAdapter extends BaseAdapter {

    private final String    TAG = getClass().getSimpleName();

    private Context             mContext;
    private ArrayList<Customer> mCustomers;

    public CustomerAdapter(Context context, ArrayList<Customer> customers) {
        this.mContext = context;
        this.mCustomers = customers;
        Log.i(TAG, "Customer Size>" + customers.size());
    }

    public void setCusmters(ArrayList<Customer> customers) {
        mCustomers = customers;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCustomers.size();
    }

    @Override
    public Customer getItem(int i) {
        return mCustomers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LinearLayout.inflate(mContext, R.layout.item_customer, null);

            ViewHolder viewHolder   = new ViewHolder();
            viewHolder.tvCustomer   = view.findViewById(R.id.tv_customer);
            viewHolder.tvRegData    = view.findViewById(R.id.tv_regdate);
            view.setTag(viewHolder);
        }

        Customer customer = getItem(i);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvCustomer.setText(customer.customer);
        viewHolder.tvRegData.setText(customer.reg_date.substring(0, 16));

        return view;
    }

    private static class ViewHolder {
        TextView        tvCustomer;
        TextView        tvRegData;
    }
}
