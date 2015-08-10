package com.yazao.instamaterial.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.yazao.instamaterial.util.Utils;

/**
 * Created by shaopingzhai on 15/8/4.
 *<p>
 * 有了上下文菜单的整个布局，现在可以开始逻辑部分了。为了方便使用，我们用FeedContextMenuManager来管理这些逻辑。
 * 下面是FeedContextMenuManager需要满足的需求：



 1.一次只能有一个菜单显示在屏幕上

 2.点击按钮“more”之后显示菜单，再次点击则菜单消失。

 3.菜单需要显示在被点击按钮的正上方。

 4.在滚动新闻列表的时候菜单消失。

 实现第一点最简单的方式是FeedContextMenuManager作为单例模式来实现。

 manager需要处理菜单视图，其中最重要的就是在恰当的时候将其移除。我们使用OnAttachStateChangeListener以及
 它的onViewDetachedFromWindow(View v)方法来达到此目的，利用它们，就可以实现在菜单dismiss或者activity destroyed的
 时候菜单视图被移除。</p>
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class FeedContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener {

    private static FeedContextMenuManager instance;

    private FeedContextMenu contextMenuView;

    private boolean isContextMenuDismissing;
    private boolean isContextMenuShowing;

    public static FeedContextMenuManager getInstance() {
        if (instance == null) {
            instance = new FeedContextMenuManager();
        }
        return instance;
    }

    private FeedContextMenuManager() {

    }

    public void toggleContextMenuFromView(View openingView, int feedItem, FeedContextMenu.OnFeedContextMenuItemClickListener listener) {
        if (contextMenuView == null) {
            showContextMenuFromView(openingView, feedItem, listener);
        } else {
            hideContextMenu();
        }
    }

    private void showContextMenuFromView(final View openingView, int feedItem, FeedContextMenu.OnFeedContextMenuItemClickListener listener) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            contextMenuView = new FeedContextMenu(openingView.getContext());
            contextMenuView.bindToItem(feedItem);
            contextMenuView.addOnAttachStateChangeListener(this);
            contextMenuView.setOnFeedMenuItemClickListener(listener);

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(contextMenuView);

            contextMenuView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contextMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition(openingView);
                    performShowAnimation();
                    return false;
                }
            });
        }
    }

    private void setupContextMenuInitialPosition(View openingView) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = Utils.dpToPx(16);
        contextMenuView.setTranslationX(openingViewLocation[0] - contextMenuView.getWidth() / 3);
        contextMenuView.setTranslationY(openingViewLocation[1] - contextMenuView.getHeight() - additionalBottomMargin);
    }

    private void performShowAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(contextMenuView.getHeight());
        contextMenuView.setScaleX(0.1f);
        contextMenuView.setScaleY(0.1f);
        contextMenuView.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnimation();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void performDismissAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(contextMenuView.getHeight());
        contextMenuView.animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (contextMenuView != null) {
                            contextMenuView.dismiss();
                        }
                        isContextMenuDismissing = false;
                    }
                });
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (contextMenuView != null) {
            hideContextMenu();
            contextMenuView.setTranslationY(contextMenuView.getTranslationY() - dy);
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contextMenuView = null;
    }
}
