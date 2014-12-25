package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.util.SpUtils;

/**
 * 通过触摸移动设置归属地在屏幕上显示的位置
 * 
 * @author xfzhang
 * 
 */
public class DragViewActivity extends Activity implements OnTouchListener {
	private LinearLayout ll_drag;

	private int firstX;// down下时, 事件发送的x坐标
	private int firstY;// down下时, 事件发送的y坐标

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_drag);

		init();
	}

	private void init() {
		ll_drag = (LinearLayout) findViewById(R.id.ll_drag);
		ll_drag.setOnTouchListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		int locationX = SpUtils.getInstance(this).getInt(SpUtils.LOCATION_X, -1);
		int locationY = SpUtils.getInstance(this).getInt(SpUtils.LOCATION_Y, -1);
		if (locationX != -1) {
			// iv_drag_location.setX(locationX);
			// iv_drag_location.setY(locationY);
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) ll_drag.getLayoutParams();
			layoutParams.leftMargin = locationX;
			layoutParams.topMargin = locationY;
			ll_drag.setLayoutParams(layoutParams);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			firstX = (int) event.getRawX();
			firstY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getRawX();
			int moveY = (int) event.getRawY();
			int distanceX = moveX - firstX;
			int distanceY = moveY - firstY;

			int newLeft = ll_drag.getLeft() + distanceX;
			int newTop = ll_drag.getTop() + distanceY;
			int newRight = ll_drag.getRight() + distanceX;
			int newBottom = ll_drag.getBottom() + distanceY;

			ll_drag.layout(newLeft, newTop, newRight, newBottom);

			firstX = moveX;
			firstY = moveY;

			break;
		case MotionEvent.ACTION_UP:
			int x = ll_drag.getLeft();
			int y = ll_drag.getTop();

			SpUtils.getInstance(this).putInt(SpUtils.LOCATION_X, x);
			SpUtils.getInstance(this).putInt(SpUtils.LOCATION_Y, y);
			break;
		default:
			break;
		}
		return true;
	}
}
