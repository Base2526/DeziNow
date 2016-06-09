package com.android.dezi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.dezi.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by  on 12/5/16.
 */
public class RefferalHistroyInComplete extends BaseExpandableListAdapter {
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Context context;
    String headerTitle;
    String childText;
    ExpandableListView mExpndbleList;

    public RefferalHistroyInComplete(Context Context, List<String> listDataHeader, HashMap<String, List<String>> listDataChild, ExpandableListView mExpndbleList) {

        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        this.mExpndbleList = mExpndbleList;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
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
        headerTitle = (String) getGroup(groupPosition);
        Context context = parent.getContext();

        if (convertView == null) {

            LayoutInflater l1 = LayoutInflater.from(context);
            convertView = l1.inflate(R.layout.list_group, parent, false);


        }
        /*if(isExpanded){
            ViewGroup.MarginLayoutParams layoutParams= (ViewGroup.MarginLayoutParams) convertView.getLayoutParams();
            layoutParams.setMargins(0,30,0,20);
           convertView.setLayoutParams(layoutParams);
        }*/
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        childText = (String) getChild(groupPosition, childPosition);
        Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater l1 = LayoutInflater.from(context);
            convertView = l1.inflate(R.layout.list_item, parent, false);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        TextView txtListChildValue = (TextView) convertView.findViewById(R.id.listItemValue);



            txtListChildValue.setText(" NOT COMPLETED");


        txtListChild.setText(childText);
        // mExpndbleList.setDividerHeight(0);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
