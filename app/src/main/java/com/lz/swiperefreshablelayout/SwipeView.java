package com.lz.swiperefreshablelayout;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by lz on 2017/12/26.
 */

public abstract class SwipeView extends LinearLayout{

    protected Context mContext;
    protected boolean mRefreshable = false;

    public SwipeView(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setRefreshable(boolean refreshable){
        boolean currentStatus = mRefreshable;
        mRefreshable = refreshable;
        if(currentStatus != refreshable){
            onRefreshableChanged(currentStatus, refreshable);
        }
    }

    public void refreshCompleted(){
        mRefreshable = false;
        dismissSwipe();
    }

    protected abstract void onRefreshableChanged(boolean currentStatus, boolean newStatus);

    public abstract void onRefreshing();

    protected abstract void dismissSwipe();
}
