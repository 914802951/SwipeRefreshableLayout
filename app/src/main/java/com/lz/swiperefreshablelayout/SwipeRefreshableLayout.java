package com.lz.swiperefreshablelayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by lz on 2017/12/25.
 */

public class SwipeRefreshableLayout extends FrameLayout {

    public interface SwipeRefreshableListener{
        void onRefreshing();
    }

    private static final String TAG = LogHelper.makeLogTag(SwipeRefreshableLayout.class.getSimpleName());

    private static final int DOWN_OR_RIGHT = 1;
    private static final int UP_OR_LEFT = -1;

    private static final int HEADER_HEIGHT_IN_DP = 60;
    private static final int FOOTER_HEIGHT_IN_DP = 60;

    private int mHeaderMaxHeight;
    private int mFooterMaxHeight;

    private SwipeView mHeader;
    private View mContent;
    private SwipeView mFooter;

    private float mMotionDownY;

    private SwipeRefreshableListener mListener;
    private boolean mIsRefreshing = false;

    public SwipeRefreshableLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshableLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mHeaderMaxHeight = PxUtils.dpToPx(HEADER_HEIGHT_IN_DP, getContext());
        mFooterMaxHeight = PxUtils.dpToPx(FOOTER_HEIGHT_IN_DP, getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 1 || getChildCount() <= 0){
            throw new RuntimeException("");
        }

        mContent = getChildAt(0);
        addHeader();
        addFooter();
    }

    private void addHeader(){
        mHeader = new HeaderView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
        mHeader.setLayoutParams(layoutParams);
        addView(mHeader, 0);
    }

    private void addFooter(){
        mFooter = new FooterView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        mFooter.setLayoutParams(layoutParams);
        addView(mFooter, -1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogHelper.v(TAG, LogHelper._FUNC_(), ev);
//        LogHelper.v(TAG, ev.getAction(), contentCanScrollVertically(1) + "    " + contentCanScrollVertically(-1));

        if(mIsRefreshing){
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMotionDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int distance = (int) (ev.getY() - mMotionDownY);
                if(!contentCanScrollVertically(DOWN_OR_RIGHT) && distance < 0){
                    return true;
                }else if(!contentCanScrollVertically(UP_OR_LEFT) && distance > 0){
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LogHelper.v(TAG, LogHelper._FUNC_(), event);

        int distance = (int) (event.getY() - mMotionDownY) / 3;
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(!contentCanScrollVertically(UP_OR_LEFT) && distance > 0){
                    distance = Math.min(distance, mHeaderMaxHeight);
                    mHeader.getLayoutParams().height = distance;
                    mContent.setTranslationY(distance);
                    requestLayout();
                    mHeader.setRefreshable(distance == mHeaderMaxHeight);
                } else if(!contentCanScrollVertically(DOWN_OR_RIGHT) && distance < 0){
                    distance = Math.max(distance, -mFooterMaxHeight);
                    mFooter.getLayoutParams().height = -distance;
                    mContent.setTranslationY(distance);
                    requestLayout();
                    mFooter.setRefreshable(distance == -mHeaderMaxHeight);
                }else{
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(distance > 0){
                    distance = Math.min(distance, mHeaderMaxHeight);
                    if(distance != mHeaderMaxHeight){
                        refreshCancelled();
                    }else{
                        mHeader.onRefreshing();
                        onRefreshing();
                    }
                }else if(distance < 0){
                    distance = Math.max(distance, -mFooterMaxHeight);
                    if(distance != -mHeaderMaxHeight){
                        refreshCancelled();
                    }else{
                        mFooter.onRefreshing();
                        onRefreshing();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void refreshCompleted(){
        mIsRefreshing = false;
        mHeader.refreshCompleted();
        mFooter.refreshCompleted();
        refreshCancelled();
    }

    private void refreshCancelled(){
        mHeader.getLayoutParams().height = 0;
        mFooter.getLayoutParams().height = 0;
        mContent.setTranslationY(0);
        requestLayout();
    }

    private void onRefreshing(){
        mIsRefreshing = true;
        if(mListener != null){
            mListener.onRefreshing();
        }
    }

    private boolean contentCanScrollVertically(int direction){
        if(mContent == null){
            return false;
        }else{
            return mContent.canScrollVertically(direction);
        }
    }

    public void setSwipeRefreshableListener(SwipeRefreshableListener listener){
        mListener = listener;
    }
}
