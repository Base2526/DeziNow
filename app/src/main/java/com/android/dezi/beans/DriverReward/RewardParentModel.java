package com.android.dezi.beans.DriverReward;

import java.util.List;

/**
 * Created by anuj.sharma on 5/13/2016.
 */
public class RewardParentModel {
    private String heading="";
    private List<RewardChildModel> childList;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<RewardChildModel> getChildList() {
        return childList;
    }

    public void setChildList(List<RewardChildModel> childList) {
        this.childList = childList;
    }


}
