package com.example.root.contactsdashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.contactsdashboard.R;
import com.example.root.contactsdashboard.util.CallLogDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 25/10/16.
 */
public class ContactAdapter extends BaseAdapter {

    List<CallLogDetail> callLogDetailList;
    LayoutInflater inflater;
    Context context;


    public ContactAdapter(Context context, List<CallLogDetail> callLogDetailList) {
        this.callLogDetailList = callLogDetailList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return callLogDetailList.size();
    }

    @Override
    public CallLogDetail getItem(int position) {
        return callLogDetailList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item_layout, parent, false);

            mViewHolder = new ContactViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ContactViewHolder) convertView.getTag();
        }

        CallLogDetail callLogDetail = callLogDetailList.get(position);
        if(callLogDetail.getContactName()!=null)
             mViewHolder.tvName.setText(callLogDetail.getContactName());
        if(callLogDetail.getContactPhoneNumber()!=null)
            mViewHolder.tvPhone.setText(callLogDetail.getContactPhoneNumber());
        if(callLogDetail.getContactEmail()!=null)
              mViewHolder.tvEmail.setText(callLogDetail.getContactEmail());

        //Last Call Time
        mViewHolder.tvLastContactTime.setText("LCT : "+String.valueOf(callLogDetail.getContactLastContactTime()).replace("I","\nI"));

        //Calculating Hours,Mintues fromm Seeconds

        int sec = (callLogDetail.getTotalCallDuration() % 60);
        int min = (callLogDetail.getTotalCallDuration() / 60)%60;
        int hours = (callLogDetail.getTotalCallDuration()/60)/60;

        String time=null;
        if(hours>0)
            time=String.valueOf(hours)+"  hr ";
        if(time !=null && min >0 )
            time +=String.valueOf(min)+" Mins ";
        else if(min >0)
            time =String.valueOf(min)+" Mins";

        if(time !=null && sec >0 )
            time +=String.valueOf(sec)+" Sec ";

        else if(sec >=0)
            time =String.valueOf(sec)+ " Sec ";

        //Total Call Duration
        mViewHolder.tvTotalContactTime.setText("TCD : "+time);


        //Picasso For Image Loading Library for Image View
        if((callLogDetail.getContactImage()!=null))
            Picasso.with(context).load(callLogDetail.getContactImage()).into(mViewHolder.ivPhoto);
        else
            Picasso.with(context).load(R.drawable.ic_person).into(mViewHolder.ivPhoto);

        return convertView;

    }


    //View Holder Pattern Initializing Views
    private class ContactViewHolder {
        TextView tvName, tvPhone, tvEmail, tvLastContactTime, tvTotalContactTime;
            CircleImageView ivPhoto;

        public ContactViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.tv_name);
            tvPhone = (TextView)item.findViewById(R.id.tv_mobile);
            tvEmail = (TextView)item.findViewById(R.id.tv_email);
            tvLastContactTime = (TextView)item.findViewById(R.id.tv_last_cont_time);
            tvTotalContactTime = (TextView)item.findViewById(R.id.tv_total_cont_time);
            ivPhoto = (CircleImageView)item.findViewById(R.id.profile_image);

        }
   }
}