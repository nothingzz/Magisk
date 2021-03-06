package com.topjohnwu.magisk.uicomponents;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class ExpandableViewHolder {

    private ViewGroup expandLayout;
    private ValueAnimator expandAnimator, collapseAnimator;
    private boolean mExpanded = false;
    private int expandHeight = 0;

    public ExpandableViewHolder(ViewGroup viewGroup) {
        expandLayout = viewGroup;
        setExpanded(false);
        expandLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        if (expandHeight == 0) {
                            expandLayout.measure(0, 0);
                            expandHeight = expandLayout.getMeasuredHeight();
                        }

                        expandLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        expandAnimator = slideAnimator(0, expandHeight);
                        collapseAnimator = slideAnimator(expandHeight, 0);
                        return true;
                    }

                });
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
        ViewGroup.LayoutParams layoutParams = expandLayout.getLayoutParams();
        layoutParams.height = expanded ? expandHeight : 0;
        expandLayout.setLayoutParams(layoutParams);
    }

    public void expand() {
        if (mExpanded) return;
        expandLayout.setVisibility(View.VISIBLE);
        expandAnimator.start();
        mExpanded = true;
    }

    public void collapse() {
        if (!mExpanded) return;
        collapseAnimator.start();
        mExpanded = false;
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(valueAnimator -> {
            int value = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = expandLayout.getLayoutParams();
            layoutParams.height = value;
            expandLayout.setLayoutParams(layoutParams);
        });
        return animator;
    }
}
