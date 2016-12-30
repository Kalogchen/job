package com.intermediary.job.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.intermediary.job.R;
import com.intermediary.job.model.JobMessage;

import java.util.List;

/**
 * Created by kalogchen on 2016/12/24.
 */

public class JobAdapter extends ArrayAdapter<JobMessage> {

    private int resourceId;

    public JobAdapter(Context context, int textViewResourceId, List<JobMessage> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前项的jobMessage实例
        JobMessage jobMessage = getItem(position);

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.position = (TextView) view.findViewById(R.id.tv_job);
            viewHolder.price = (TextView) view.findViewById(R.id.tv_pay);
            viewHolder.companyName = (TextView) view.findViewById(R.id.tv_companyName);
            viewHolder.address = (TextView) view.findViewById(R.id.tv_address);
            viewHolder.inviteInfoID = (TextView) view.findViewById(R.id.tv_inviteInfoID);
            //将viewholder存储在view中
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
            viewHolder.position.setText("职位：" + jobMessage.getPosition());
            viewHolder.price.setText("工资：" + jobMessage.getPrice() + "/小时");
            viewHolder.companyName.setText("企业名称：" + jobMessage.getCompanyName());
            viewHolder.address.setText("地址：" + jobMessage.getAddress());
            viewHolder.inviteInfoID.setText(jobMessage.getInviteInfoID());
        return view;
        }

    class ViewHolder {
        TextView position;
        TextView price;
        TextView companyName;
        TextView address;
        TextView inviteInfoID;
    }

    }
