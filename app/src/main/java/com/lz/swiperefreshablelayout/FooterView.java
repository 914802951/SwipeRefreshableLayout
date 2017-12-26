package com.lz.swiperefreshablelayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by lz on 2017/12/26.
 */

public class FooterView extends SwipeView {

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    public FooterView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.default_header, this, true);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
        setLayoutParams(layoutParams);

        mImageView = findViewById(R.id.header_iv);
        mProgressBar = findViewById(R.id.header_pb);
        mTextView = findViewById(R.id.header_tv);

        mTextView.setText("下拉加载");
    }

    @Override
    protected void onRefreshableChanged(boolean currentStatus, boolean newStatus) {
        if(newStatus) {
            mTextView.setText("松开刷新");
            ObjectAnimator icon_anim = ObjectAnimator.ofFloat(mImageView, "rotation", 0.0F, 180F);
            icon_anim.start();
        } else {
            mTextView.setText("下拉加载");
            ObjectAnimator icon_anim = ObjectAnimator.ofFloat(mImageView, "rotation", 180F, 0F);
            icon_anim.start();
        }
    }

    @Override
    public void onRefreshing() {
        mImageView.setRotation(0);
        mImageView.setVisibility(GONE);
        mProgressBar.setVisibility(VISIBLE);
        mTextView.setText("刷新中...");
    }

    @Override
    protected void dismissSwipe() {
        mImageView.setRotation(0);
        mImageView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mTextView.setText("下拉加载");
    }
}
