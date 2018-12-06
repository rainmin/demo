package com.rainmin.demo.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

public class RefreshLayout extends ViewGroup {

    private final static int DEFAULT_TRIGGER_DISTANCE = 200;
    private View headerView;
    private View contentView;
    private float mLastY;
    private int mTriggerDistance = DEFAULT_TRIGGER_DISTANCE;
    private boolean mRefreshing = false;
    private Scroller mScroller;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        addHeader(context);
    }

    private void addHeader(Context context) {
        if (headerView != null) {
            removeView(headerView);
        }
        headerView = new TextView(context);
        ((TextView)headerView).setText("pull to refresh");
        headerView.setPadding(0, 20, 0, 20);
        ((TextView) headerView).setGravity(Gravity.CENTER_HORIZONTAL);
        addView(headerView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (headerView != null) {
            headerView.layout(0, 0 - headerView.getMeasuredHeight(), getWidth(), 0);
        }
        if (contentView != null) {
            contentView.layout(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() == 2) {
            contentView = getChildAt(1);
        }
        if (getChildCount() > 2) {
            throw new IllegalArgumentException("there are two children at most");
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 2) {
            throw new IllegalArgumentException("there are two children at most");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int mark = (int) (mCurY - mLastY);
                if (contentView.canScrollVertically(-mark)) {
                    return false;
                }
                if (mark >= 50) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mCurY = ev.getY();
                int yOffset = (int) (mCurY - mLastY);
                if (Math.abs(getScrollY()) == mTriggerDistance) {
                    ((TextView)headerView).setText("release to refresh");
                }
                if (mRefreshing && yOffset < 0) {

                } else {
                    scrollBy(0, -yOffset);
                    mLastY = mCurY;
                }
                break;
            case MotionEvent.ACTION_UP:
                rebound();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void refreshCompleted() {
        if (!mRefreshing) return;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshing = false;
                ((TextView)headerView).setText("pull to refresh");
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
                invalidate();
            }
        }, 100);
    }

    private void rebound() {
        int scrollY = getScrollY();
        int headerHeight = -headerView.getMeasuredHeight();

        if (mRefreshing) {
            mScroller.startScroll(0, scrollY, 0, -(scrollY - headerHeight), 300);
            invalidate();
            return;
        }

        if (Math.abs(scrollY) >= mTriggerDistance) {
            ((TextView) headerView).setText("refreshing");
            mRefreshing = true;
            mScroller.startScroll(0, scrollY, 0, -(scrollY - headerHeight), 300);
            invalidate();
            return;
        }

        mScroller.startScroll(0, scrollY, 0, -scrollY, 300);
        invalidate();
    }
}
