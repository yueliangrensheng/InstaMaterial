package com.yazao.instamaterial.activity;

import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.yazao.instamaterial.R;
import com.yazao.instamaterial.config.GlobalParams;
import com.yazao.instamaterial.util.DrawerLayoutInstaller;
import com.yazao.instamaterial.util.HideSoftInputUtil;
import com.yazao.instamaterial.util.Utils;
import com.yazao.instamaterial.view.GlobalMenuView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BaseActivity extends ActionBarActivity implements GlobalMenuView.OnHeaderClickListener {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.ivLogo)
    ImageView ivLogo;

    MenuItem menuItem;
    private DrawerLayout drawerLayout;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);

        //重置Toolbar
        setupToolbar();

        if (shouldInstallDrawer()) {
            setupDrawer();
        }
    }

    private void setupDrawer() {
        {
            GlobalMenuView menuView = new GlobalMenuView(this);
            menuView.setOnHeaderClickListener(this);

            drawerLayout = DrawerLayoutInstaller.from(this)
                    .drawerRoot(R.layout.drawer_root)
                    .drawerLeftView(menuView)
                    .drawerLeftWidth(Utils.dpToPx(300))
                    .withNavigationIconToggler(getToolbar())
                    .build();
        }
    }

    private boolean shouldInstallDrawer() {
        return true;
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //自定义 menu菜单
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuItem = menu.findItem(R.id.action_inbox);
        menuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    /**
     * 处理键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (HideSoftInputUtil.getInstance(this).isShouldHideInput(ev)) {
                HideSoftInputUtil.getInstance(this).hideSoftInput();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (!GlobalParams.IS_OPEN_PROFILE_ACTIVITY) {
            GlobalParams.IS_OPEN_PROFILE_ACTIVITY = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int[] startingLocation = new int[2];
                    v.getLocationOnScreen(startingLocation);
                    startingLocation[0] += v.getWidth() / 2;
                    UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseActivity.this);
                    overridePendingTransition(0, 0);
                }
            }, 200);
        }
    }
}
