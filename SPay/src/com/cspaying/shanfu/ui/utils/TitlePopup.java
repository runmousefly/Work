package com.cspaying.shanfu.ui.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.adapter.PoplistAdapter;


public class TitlePopup extends PopupWindow {

	//private TextView comment;

	private Context mContext;
	
	private ListView pay_list;

	// �б?���ļ��
	protected final int LIST_PADDING = 10;

	// ʵ��һ������
	private Rect mRect = new Rect();

	// ����λ�ã�x��y��
	private final int[] mLocation = new int[2];

	// ��Ļ�Ŀ�Ⱥ͸߶�
	private int mScreenWidth, mScreenHeight;

	// �ж��Ƿ���Ҫ��ӻ�����б�������
	private boolean mIsDirty;

	// λ�ò�������
	private int popupGravity = Gravity.NO_GRAVITY;

	// ����������ѡ��ʱ�ļ���
	//private OnItemOnClickListener mItemOnClickListener;

	// ���嵯���������б�
	private ArrayList<String> mActionItems = new ArrayList<String>();
	private PoplistAdapter listAdapter;
	private OnItemOnClickListener mItemOnClickListener;
	public TitlePopup(Context context) {
		// ���ò��ֵĲ���
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public TitlePopup(Context context, int width, int height) {
		this.mContext = context;

		// ���ÿ��Ի�ý���
		setFocusable(true);
		// ���õ����ڿɵ��
		setTouchable(true);
		// ���õ�����ɵ��
		setOutsideTouchable(true);

		// �����Ļ�Ŀ�Ⱥ͸߶�
//		mScreenWidth = Util.getScreenWidth(mContext);
//		mScreenHeight = Util.getScreenHeight(mContext);

		// ���õ����Ŀ�Ⱥ͸߶�
		setWidth(width);
		setHeight(height);

		setBackgroundDrawable(new BitmapDrawable());

		// ���õ����Ĳ��ֽ���
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.comment_popu, null);
		setContentView(view);
		
		pay_list = (ListView) view.findViewById(R.id.paylist);
		//comment = (TextView) view.findViewById(R.id.popu_comment);
		//comment.setOnClickListener(onclick);
	}
	
	public void initListView(){
		listAdapter = new PoplistAdapter(mActionItems, mContext);
		pay_list.setAdapter(listAdapter);
		pay_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				mItemOnClickListener.onItemClick(mActionItems.get(arg2), arg2);
			}
		});
	}

	/**
	 * ��ʾ�����б����
	 */
	public void show(final View c) {
		initListView();
		//comment.setText(mActionItems.get(0));
		// ��õ����Ļ��λ�����
		c.getLocationOnScreen(mLocation);
		// ���þ��εĴ�С
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + c.getWidth(),
				mLocation[1] + c.getHeight());
		// �ж��Ƿ���Ҫ��ӻ�����б�������
		
		//- this.getWidth()
		showAtLocation(c, Gravity.NO_GRAVITY, mLocation[0]-20
				, mLocation[1]+c.getHeight()+10);// +((this.getHeight() -c.getHeight()))
	}

	OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			switch (v.getId()) {
			/*case R.id.popu_comment:
				//mItemOnClickListener.onItemClick(mActionItems.get(1), 1);
				break;
			*/
			}
		}

	};

	/**
	 * ���������
	 */
	public void addAction(ArrayList<String> Items) {
		if (Items != null) {
			mActionItems = Items;
		}
	}

	/**
	 * ���������
	 */
	public void cleanAction() {
		if (mActionItems.isEmpty()) {
			mActionItems.clear();
		}
	}

	/**
	 * ���λ�õõ�������
	 */
	public String getAction(int position) {
		if (position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}
	
	/**
	 * 设置监听事件
	 */
	public void setItemOnClickListener(
			OnItemOnClickListener onItemOnClickListener) {
		this.mItemOnClickListener = onItemOnClickListener;
	}

	/**
	 * @author yangyu 功能描述：弹窗子类项按钮监听事件
	 */
	public static interface OnItemOnClickListener {
		public void onItemClick(String item, int position);
	}

	

	
}
