package com.qqpractice.ui.ec;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.qqpractice.activity.R;
import com.qqpractice.ui.ExImageButton;

public class ToolsBar implements View.OnClickListener {
	private static final int SLIDE_TO_RIGHT = 1;
	private static final int SLIDE_TO_LEFT = 2;
	
	private static final int ANIM_ENTER_RIGHT = 0;
	private static final int ANIM_ENTER_LEFT = 1;
	private static final int ANIM_EXIT_RIGHT = 2;
	private static final int ANIM_EXIT_LEFT = 3;
	
	private Context mContext;
	
	private ExImageButton btnNext = null;
	private ExImageButton btnPrev = null;
	
	private LinearLayout lyFornt = null;
	private LinearLayout lyBack = null;
	
	private Animation animIn;
	private Animation animOut;
	
	private Animation AnimTemple[] = null;
	
	public ToolsBar(Context context, View home) {
		mContext = context;
		
		btnNext = (ExImageButton) home.findViewById(R.id.home_switchbutton_next);
		btnPrev = (ExImageButton) home.findViewById(R.id.home_switchbutton_previous);
		lyFornt = (LinearLayout) home.findViewById(R.id.home_switchbutton_front);
		lyBack = (LinearLayout) home.findViewById(R.id.home_switchbutton_back);
		
		btnNext.setOnClickListener(this);
		btnPrev.setOnClickListener(this);
		
		AnimTemple = new Animation[]{
			AnimationUtils.loadAnimation(mContext, R.anim.enter_right),
			AnimationUtils.loadAnimation(mContext, R.anim.enter_left),
			AnimationUtils.loadAnimation(mContext, R.anim.exit_right),
			AnimationUtils.loadAnimation(mContext, R.anim.exit_left),
		};
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.home_switchbutton_next) {
			this.slide(SLIDE_TO_RIGHT);
		}else if(id == R.id.home_switchbutton_previous) {
			this.slide(SLIDE_TO_LEFT);
		}
		
	}
	
	private void slide(final int mode) {
		final View v_show, v_hide;
		if(mode == SLIDE_TO_RIGHT) {
			animIn = AnimTemple[ANIM_ENTER_RIGHT];
			animOut = AnimTemple[ANIM_EXIT_LEFT];
			v_show = lyBack;
			v_hide = lyFornt;
		}else {
			animIn = AnimTemple[ANIM_ENTER_LEFT];
			animOut = AnimTemple[ANIM_EXIT_RIGHT];
			v_show = lyFornt;
			v_hide = lyBack;
		}
		animOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				v_hide.setVisibility(View.GONE);
				enableWhenEndOfAnimation();
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				disableWhenStartAnimation(false);
				
			}
			
		});
		v_show.setVisibility(View.VISIBLE);
		v_show.startAnimation(animIn);
		v_hide.startAnimation(animOut);
	}
	
	/**
	 * Disable the tools bar when the animation has been starting, which to avoid the onClick event is repeated.
	 * @param flag enable or not, this param should be set <b>false</b> always.
	 * */
	private void disableWhenStartAnimation(boolean flag) {
		btnNext.setEnabled(flag);
		btnPrev.setEnabled(flag);
	}
	
	private void enableWhenEndOfAnimation() {
		disableWhenStartAnimation(true);
	}

}
