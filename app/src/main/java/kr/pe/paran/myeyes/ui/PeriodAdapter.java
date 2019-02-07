package kr.pe.paran.myeyes.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import kr.pe.paran.myeyes.R;

public class PeriodAdapter extends BaseAdapter {

    private Context     mContext;
    private String[]    mPeriods;


    public PeriodAdapter(Context context) {
        this.mContext = context;
        mPeriods    = context.getResources().getStringArray(R.array.period);
    }

    @Override
    public int getCount() {
        return mPeriods.length;
    }

    @Override
    public String getItem(int position) {
        return mPeriods[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_period, null);
            ViewHoler viewHoler = new ViewHoler(convertView);
            convertView.setTag(viewHoler);
        }
        ViewHoler viewHoler = (ViewHoler) convertView.getTag();
        viewHoler.tvPeoriod.setText(getItem(position));

        return convertView;
    }

    private static class ViewHoler {

        public TextView tvPeoriod;
        public ViewHoler(View view) {
            this.tvPeoriod = view.findViewById(R.id.tv_period);
        }
    }
}
