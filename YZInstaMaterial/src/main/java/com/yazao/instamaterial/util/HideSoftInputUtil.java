package com.yazao.instamaterial.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * 
 * com.anguotech.android.util.HideSoftInputUtil
 * @description: 隐藏软键盘工具类
 * @author yueliangrensheng create at 2015年1月13日下午2:38:13
 */
public class HideSoftInputUtil {
	private static Activity context;
	private static HideSoftInputUtil instance;
	private View v;
	private IBinder token;
	private HideSoftInputUtil(){}
	public static HideSoftInputUtil getInstance(Context mcontext){
		synchronized (HideSoftInputUtil.class) {
			context=(Activity) mcontext;
			if(instance==null){
				instance=new HideSoftInputUtil();
			}
			return instance;
		}
	}
	

	/**
	 * 
	 * @description 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏 
	 * @return
	 * boolean
	 * @author Administrator create at 2014-8-25上午9:47:45
	 */
	public  boolean isShouldHideInput( MotionEvent event) {
		v = context.getCurrentFocus();
		
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getRawX() > left && event.getRawX() < right
					&& event.getRawY() > top && event.getRawY() < bottom) {
				// if (event.getX() > left && event.getX() < right
				// && event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	public  boolean isShouldHideInput2( MotionEvent event, FrameLayout fl_username) {
		v = fl_username;
		
		if (v != null && (v instanceof FrameLayout)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getRawX() > left && event.getRawX() < right
					&& event.getRawY() > top && event.getRawY() < bottom) {
				// if (event.getX() > left && event.getX() < right
				// && event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}
	/**
	 * 
	 * @description 多种隐藏软件盘方法的其中一种 
	 * void
	 * @author Administrator create at 2014-8-25上午9:48:00
	 */
	public  void hideSoftInput() {
		
		if(v!=null){
			token=v.getWindowToken();
		}
		if (token != null) {
			InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
