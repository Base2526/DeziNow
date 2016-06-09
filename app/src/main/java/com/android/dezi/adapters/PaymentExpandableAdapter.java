package com.android.dezi.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dezi.R;
import com.android.dezi.beans.passenger_payment.PaymentChildModel;
import com.android.dezi.beans.passenger_payment.PaymentParentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuj.sharma on 5/13/2016.
 */
public class PaymentExpandableAdapter extends BaseExpandableListAdapter {
    Context mContext;
    List<PaymentParentModel> mList = new ArrayList<>();

    public PaymentExpandableAdapter(Context context, List<PaymentParentModel> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getChildList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mList.get(groupPosition).getChildList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentHolder parentHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_group_credit,parent,false);
            parentHolder = new ParentHolder();
            parentHolder.paymentType = (TextView)convertView.findViewById(R.id.payment_type);
            parentHolder.icon = (ImageView)convertView.findViewById(R.id.imgDropDown);
            convertView.setTag(parentHolder);
        }
        else{
            parentHolder = (ParentHolder)convertView.getTag();
        }
        PaymentParentModel obj = mList.get(groupPosition);
        parentHolder.paymentType.setText(obj.getPaymentType());
        if(mList.size()>0){
            if (isExpanded) {
                parentHolder.icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_up));
            } else {
                parentHolder.icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_down));
            }
        }
        else{
            parentHolder.icon.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_credit,parent,false);
            childHolder = new ChildHolder();
            childHolder.cardNumber = (TextView)convertView.findViewById(R.id.card_number);
            childHolder.mOkaybtn = (ImageView)convertView.findViewById(R.id.check_img);
            childHolder.mCrossBtn = (ImageView)convertView.findViewById(R.id.cross_img);
            convertView.setTag(childHolder);
        }
        else{
            childHolder = (ChildHolder)convertView.getTag();
        }
        PaymentChildModel obj = mList.get(groupPosition).getChildList().get(childPosition);
        childHolder.cardNumber.setText(obj.getCardnumber());

        return convertView;
    }

    class ParentHolder {
        TextView paymentType;
        ImageView icon;
    }

    class ChildHolder {
        TextView cardNumber;
        ImageView mOkaybtn, mCrossBtn;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
