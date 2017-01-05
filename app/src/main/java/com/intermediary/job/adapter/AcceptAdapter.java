package com.intermediary.job.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.intermediary.job.R;
import com.intermediary.job.model.AcceptMessage;

import java.util.List;

/**
 * Created by kalogchen on 2016/12/24.
 */

public class AcceptAdapter extends ArrayAdapter<AcceptMessage> {

    private int resourceId;

    public AcceptAdapter(Context context, int textViewResourceId, List<AcceptMessage> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前项的jobMessage实例
        AcceptMessage acceptMessage = getItem(position);

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.position = (TextView) view.findViewById(R.id.tv_accept_job);
            viewHolder.price = (TextView) view.findViewById(R.id.tv_accept_pay);
            viewHolder.companyName = (TextView) view.findViewById(R.id.tv_accept_companyName);
            viewHolder.address = (TextView) view.findViewById(R.id.tv_accept_address);
            viewHolder.inviteInfoID = (TextView) view.findViewById(R.id.tv_accept_inviteInfoID);
            viewHolder.applyTime = (TextView) view.findViewById(R.id.tv_accept_applytime);
            //将viewholder存储在view中
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
            viewHolder.position.setText("职位：" + acceptMessage.getPosition());
            viewHolder.price.setText("工资：" + acceptMessage.getPrice() + "/小时");
            viewHolder.companyName.setText("企业名称：" + acceptMessage.getCompanyName());
            viewHolder.address.setText("地址：" + acceptMessage.getAddress());
            viewHolder.inviteInfoID.setText(acceptMessage.getInviteInfoID());
            viewHolder.applyTime.setText("申请时间：" + acceptMessage.getApplyTime());
        return view;
        }

    class ViewHolder {
        TextView position;
        TextView price;
        TextView companyName;
        TextView address;
        TextView inviteInfoID;
        TextView applyTime;
    }

    }
