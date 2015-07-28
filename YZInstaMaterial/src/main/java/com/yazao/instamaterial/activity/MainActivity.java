package com.yazao.instamaterial.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.yazao.instamaterial.R;
import com.yazao.instamaterial.adapter.FeedAdapter;
import com.yazao.instamaterial.util.Utils;

import butterknife.InjectView;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.rvFeed)
    RecyclerView rvFeed;

    @InjectView(R.id.btnCreate)
    ImageView btnCreate;

    FeedAdapter adapter = null;
    /**
     * 动画加载的标记
     */
    private boolean pendingInroAnimation = false;
    private long ANIM_DURATION_FAB=400;
    private long ANIM_DURATION_TOOLBAR = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFeed();

        if (savedInstanceState == null) {
            pendingInroAnimation = true;
        } else {

        }

    }

    private void setupFeed() {

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(layout);

        adapter = new FeedAdapter(this);
        rvFeed.setAdapter(adapter);

        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (pendingInroAnimation) {
            pendingInroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    /**
     * 开始动画
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void startIntroAnimation() {
        //将FAB按钮 隐藏到界面之外
        btnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        //隐藏 toolbar、menuItem以及ivlogo
        toolbar.setTranslationY(-actionbarSize);
        ivLogo.setTranslationY(-actionbarSize);
        menuItem.getActionView().setTranslationY(-actionbarSize);

        //恢复位置，并设置 view 的 属性动画
        toolbar.animate().translationY(0).setDuration(ANIM_DURATION_TOOLBAR).setStartDelay(300);
        ivLogo.animate().translationY(0).setDuration(ANIM_DURATION_TOOLBAR).setStartDelay(400);
        menuItem.getActionView().animate().translationY(0).setDuration(ANIM_DURATION_TOOLBAR).setStartDelay(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                startContentAnimation();
            }
        }).start();

    }

    /**
     * 开始 内容 动画
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void startContentAnimation() {
        btnCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        adapter.updateItems();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
