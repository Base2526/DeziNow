package com.android.dezi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dezi.R;
import com.android.dezi.beans.DriverReward.RewardChildModel;
import com.android.dezi.beans.DriverReward.RewardParentModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/5/16.
 */
public class RefferalHistroyComplete extends BaseExpandableListAdapter {
    List<RewardParentModel> mList;
    Context context;
    ExpandableListView mExpndbleListRefferal;

    public RefferalHistroyComplete(Context context, List<RewardParentModel> mList, ExpandableListView mExpndbleListRefferal) {
        this.context = context;
        this.mList = mList;
        this.mExpndbleListRefferal = mExpndbleListRefferal;
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
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return mList.get(groupPosition).getChildList().get(childPosititon);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosititon) {
        return childPosititon;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentHolder parentHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group, parent, false);
            parentHolder = new ParentHolder();
            parentHolder.heading = (TextView) convertView.findViewById(R.id.lblListHeader);
            parentHolder.icon = (ImageView) convertView.findViewById(R.id.imgDropDown);

            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ParentHolder) convertView.getTag();
        }

        RewardParentModel model = mList.get(groupPosition);

        parentHolder.heading.setText(model.getHeading());

        if (model.getChildList().size() > 0) {
            if (isExpanded) {
                parentHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_up));
            } else {
                parentHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_down));
            }
        } else {
            parentHolder.icon.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ParentHolder {
        TextView heading;
        ImageView icon;
    }

    class ChildHolder {
        TextView name, status;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder parentHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            parentHolder = new ChildHolder();
            parentHolder.name = (TextView) convertView.findViewById(R.id.lblListItem);
            parentHolder.status = (TextView) convertView.findViewById(R.id.listItemValue);

            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ChildHolder) convertView.getTag();
        }

        RewardChildModel model = mList.get(groupPosition).getChildList().get(childPosition);
        parentHolder.name.setText(model.getName());
        parentHolder.status.setText(model.getStatus());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
