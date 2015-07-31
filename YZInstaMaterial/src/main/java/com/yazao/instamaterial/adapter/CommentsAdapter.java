package com.yazao.instamaterial.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yazao.instamaterial.R;
import com.yazao.instamaterial.util.RoundedTransformation;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by shaopingzhai on 15/7/30.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    private int avatarSize;
    private int itemsCount = 0;

    private boolean animationsLocked=false;
    private boolean delayEnterAnimation=true;
    private int lastAnimatedPosition=-1;

    public CommentsAdapter(Activity activity) {
        this.activity = activity;
        avatarSize = activity.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_comment, parent, false);

        return new CommentViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        CommentViewHolder commentHolder = (CommentViewHolder) holder;

        commentHolder.tvComment.setText("yueliangrensheng " + position);

        Picasso.with(activity).load(R.mipmap.ic_launcher).centerCrop().resize(avatarSize, avatarSize).transform(new RoundedTransformation()).into(commentHolder.ivUserAvatar);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void runEnterAnimation(View itemView, int position) {
        if(animationsLocked) return;

        if(position>lastAnimatedPosition){
            lastAnimatedPosition=position;
            itemView.setTranslationY(100);
            itemView.setAlpha(0.f);
            itemView.animate().translationY(0).alpha(1.0f).setStartDelay(delayEnterAnimation?20 *position:0).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animationsLocked=true;
                }
            }).start();
        }

    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public void updateItems() {
        itemsCount=10;
        notifyDataSetChanged();
    }

    public void addItem(){
        itemsCount++;
        //利用了 RecyclerView 的notifyItemInserted方法实现了添加一个item的动画效果
        notifyItemInserted(itemsCount-1);
        
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        @InjectView(R.id.tvComment)
        TextView tvComment;

        public CommentViewHolder(View inflate) {
            super(inflate);
            ButterKnife.inject(this, inflate);
        }
    }
}