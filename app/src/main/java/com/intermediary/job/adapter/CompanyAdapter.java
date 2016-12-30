package com.intermediary.job.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.intermediary.job.R;
import com.intermediary.job.model.CompanyMessage;

import java.util.List;

/**
 * Created by kalogchen on 2016/12/25.
 */

public class CompanyAdapter extends ArrayAdapter<CompanyMessage> {

    private int resourceId;

    public CompanyAdapter(Context context, int textViewResourceId, List<CompanyMessage> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前项的CompanyMessage实例
        CompanyMessage companyMessage = getItem(position);

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvCompanyName = (TextView) view.findViewById(R.id.tv_companyName);
            viewHolder.tvCompanyType = (TextView) view.findViewById(R.id.tv_companyType);
            viewHolder.tvBussiness = (TextView) view.findViewById(R.id.tv_bussiness);
            viewHolder.tvDescript = (TextView) view.findViewById(R.id.tv_bussiness);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvCompanyName.setText("企业名称：" + companyMessage.getCompanyName());
        viewHolder.tvCompanyType.setText("企业类型：" + companyMessage.getCompanyType());
        viewHolder.tvBussiness.setText("企业主营：" + companyMessage.getBussiness());
        viewHolder.tvDescript.setText("企业名称：" + companyMessage.getCompanyType());

        return view;
    }

    class ViewHolder {
        TextView tvCompanyType;
        TextView tvCompanyName;
        TextView tvBussiness;
        TextView tvDescript;
    }
}
