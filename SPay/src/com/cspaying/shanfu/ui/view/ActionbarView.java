package com.cspaying.shanfu.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cspaying.shanfu.R;

//import com.yindd.ui.base.BaseSlidingActivity;

public class ActionbarView extends RelativeLayout {

	private Context context;

	private TextView titleText;

	private ImageButton leftBtn;

	private ImageButton rightBtn;

	private ImageButton rightSecondBtn;

	private TextView rightText,leftText;

	private OnActionBtnClickListener onActionBtnClickListener;

	public ActionbarView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public ActionbarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ActionbarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.actionbar_view, this);
		leftBtn = (ImageButton) view.findViewById(R.id.actionbar_left_btn);
		leftBtn.setOnClickListener(onClick);
		titleText = (TextView) view.findViewById(R.id.actionbar_title);
		rightText = (TextView) view.findViewById(R.id.actionbar_right_btnText);
		leftText = (TextView) view.findViewById(R.id.actionbar_left_tv);
		
		
		rightText.setOnClickListener(onClick);
		leftText.setOnClickListener(onClick);
		rightBtn = (ImageButton) view.findViewById(R.id.actionbar_right_btn);
		rightBtn.setOnClickListener(onClick);
		rightSecondBtn = (ImageButton) view
				.findViewById(R.id.actionbar_right_second_btn);
		rightSecondBtn.setOnClickListener(onClick);
	}

	/**
	 * ���ñ���
	 * 
	 * @param resText
	 *            ��Դ
	 */
	public void setTitle(int resText) {
		titleText.setText(resText);
	}

	/**
	 * ���ñ���
	 * 
	 * @param text
	 *            ����
	 */
	public void setTitle(CharSequence text) {
		titleText.setText(text);
	}

	public void setRightText(CharSequence text) {
		rightText.setText(text);
		rightText.setTextSize(18);
		rightText.setVisibility(View.VISIBLE);
	}
	public void setLeftText(CharSequence text) {
		leftText.setText(text);
		leftText.setTextSize(18);
		leftText.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.GONE);
	}
	public String getRightText() {
		return rightText.getText().toString();
	}
	/**
	 * ���ñ���������?
	 * 
	 * @param text
	 *            ����
	 */
	public void setTitleSize(int size) {
		titleText.setTextSize(size);
	}

	/**
	 * ���ñ���������ɫ
	 * 
	 * @param text
	 *            ����
	 */
	public void setTitleColor(int size) {
		titleText.setTextColor(size);
	}

	/**
	 * ����leftbutton�ı���
	 * 
	 */
	public void setLeftbunttonImage(int resText) {
		leftBtn.setBackgroundResource(resText);
	}
	public void setRightbunttonImage(int resText) {
		rightBtn.setBackgroundResource(resText);
		rightBtn.setVisibility(View.VISIBLE);
		invalidate();
	}

	public void setConnecting(int resText) {
		// rightBtn.setBackgroundResource(R.anim.animation_list);
		// rightBtn.setVisibility(View.VISIBLE);
		// //invalidate();
		// AnimationDrawable aa=(AnimationDrawable)rightBtn.getBackground();
		// aa.setOneShot(false);
		// if(resText == 0){//传让参数让动画自动判断启动和关闭 xing.lv 14.12.22
		// aa.stop();
		// }else{
		// aa.start();
		// }
	}

	public void setRightSecondbunttonImage(int resText) {
		rightSecondBtn.setBackgroundResource(resText);
		rightSecondBtn.setVisibility(View.VISIBLE);
		invalidate();
	}

	/**
	 * �����ұ�button
	 * 
	 * @param isHidden
	 *            �Ƿ�����
	 */
	public void hiddenRightBtn(boolean isHidden) {
		if (isHidden) {
			rightBtn.setVisibility(View.GONE);
		} else {
			rightBtn.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * �����ұߵڶ���button
	 * 
	 * @param isHidden
	 *            �Ƿ�����
	 */
	public void hiddenRightSecondBtn(boolean isHidden) {
		if (isHidden) {
			rightSecondBtn.setVisibility(View.GONE);
		} else {
			rightSecondBtn.setVisibility(View.VISIBLE);
		}
	}

	public OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.actionbar_left_btn:

				if (onActionBtnClickListener == null) {
//					FragmentActivity activity = (FragmentActivity) context;
//					if (activity instanceof BaseSlidingActivity) {
//						BaseSlidingActivity slidingActivity = (BaseSlidingActivity) activity;
//						slidingActivity.getSlidingMenu().toggle();
//					}
				}
				if (onActionBtnClickListener != null) {
					onActionBtnClickListener.onLeftBtnClick(v);
				}
				break;
			case R.id.actionbar_right_btn:
				if (onActionBtnClickListener != null) {
					int versions = Integer.parseInt(Build.VERSION.SDK);
					if (versions < 18) {
						// Dialog alertDialog =new AlertDialog.Builder(context)
						// .setTitle(getResources().getString(R.string.commonality_versions_prompt))
						// .setMessage(getResources().getString(R.string.mainband_alertDialog))
						// .setPositiveButton(getResources().getString(R.string.commonality_confirm),
						// null)
						// .show();
					} else {
						onActionBtnClickListener.onRightBtnClick(v);
					}
				}
				break;
			case R.id.actionbar_right_second_btn:
				if (onActionBtnClickListener != null) {
					onActionBtnClickListener.onRightSecondBtnClick(v);
				}
				break;
				
			case R.id.actionbar_right_btnText:
				if (onActionBtnClickListener != null) {
					onActionBtnClickListener.onRightTextClick(v);
				}
				break;
				
			case R.id.actionbar_left_tv:
				if (onActionBtnClickListener != null) {
					onActionBtnClickListener.onLeftTextClick(v);
				}
				break;
			default:
				break;
			}
		}
	};

	public void setOnActionBtnClickListener(
			OnActionBtnClickListener onActionBtnClickListener) {
		this.onActionBtnClickListener = onActionBtnClickListener;
	}

	/**
	 * actionbar����¼�?
	 */
	public interface OnActionBtnClickListener {
		/**
		 * ��button��ť���?
		 * 
		 * @param view
		 *            ��button
		 */
		void onLeftBtnClick(View view);

		/**
		 * ��button���?
		 * 
		 * @param view
		 *            ��button
		 */
		void onRightBtnClick(View view);

		/**
		 * �Ҷ�button���?
		 * 
		 * @param view
		 *            �Ҷ�button
		 */
		void onRightSecondBtnClick(View view);
		
		void onRightTextClick(View view);
		
		void onLeftTextClick(View view);
	}
}
