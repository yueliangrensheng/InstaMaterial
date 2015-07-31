package com.yazao.instamaterial.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yazao.instamaterial.R;
import com.yazao.instamaterial.adapter.CommentsAdapter;
import com.yazao.instamaterial.util.Utils;
import com.yazao.instamaterial.view.SendCommentButton;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by shaopingzhai on 15/7/25.
 */
public class CommentsActivity extends BaseActivity implements SendCommentButton.OnSendClickListener {

    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @InjectView(R.id.contentRoot)
    LinearLayout contentRoot;
    @InjectView(R.id.rvComments)
    RecyclerView rvComments;
    @InjectView(R.id.llAddComment)
    LinearLayout llAddComment;
    @InjectView(R.id.etComment)
    EditText etComment;
    @InjectView(R.id.btnSendComment)
    SendCommentButton btnSendComment;

    private CommentsAdapter adater;
    private int drawingStartLoction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comments);
        
        setupComments();
        setupSendCommentButton();

        drawingStartLoction=getIntent().getIntExtra(ARG_DRAWING_START_LOCATION,0);

        if (savedInstanceState==null){
            // 该监听 作用： 在view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }



    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void startIntroAnimation() {
        contentRoot.setScaleY(0.1f);
        //设置初始位置
        contentRoot.setPivotY(drawingStartLoction);

        llAddComment.setTranslationY(100);
        contentRoot.animate().scaleY(1).setDuration(200).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animateContent();
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void animateContent() {
        adater.updateItems();

        llAddComment.animate().translationY(0).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void setupSendCommentButton() {

        btnSendComment.setOnSendClickListener(this);
    }



    private void setupComments() {
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        rvComments.setLayoutManager(layoutManager);

        rvComments.setHasFixedSize(true);

         adater=new CommentsAdapter(this);
        rvComments.setAdapter(adater);

        rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //在 滚动时，item没有动画
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    adater.setAnimationsLocked(true);
                }
            }
        });
    }

    @Override
    public void onSendClickListener(View v) {
        adater.addItem();
        adater.setAnimationsLocked(false);
        adater.setDelayEnterAnimation(false);
        rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * adater.getItemCount());

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBackPressed() {
        //在退出 时的动画，toolbar不动，
        contentRoot.animate().translationY(Utils.getScreenHeight(this)).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                CommentsActivity.super.onBackPressed();
                overridePendingTransition(0,0);
            }
        }).start();
    }
}
