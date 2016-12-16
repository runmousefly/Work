package com.cspaying.shanfu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cspaying.shanfu.R;

public class GridViewNumAdapter extends BaseAdapter {
	// 上下文对象
	private Context context;
	// 图片数组
	private Integer[] imgs = { R.drawable.icon_digital_01,
			R.drawable.icon_digital_02, R.drawable.icon_digital_03,
			R.drawable.icon_digital_04, R.drawable.icon_digital_05,
			R.drawable.icon_digital_06, R.drawable.icon_digital_07,
			R.drawable.icon_digital_08, R.drawable.icon_digital_09,
			R.drawable.icon_digital_0x, R.drawable.icon_digital_00,
			R.drawable.icon_digital_delete };

	public GridViewNumAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		return imgs.length;
	}

	public Object getItem(int item) {
		return item;
	}

	public long getItemId(int id) {
		return id;
	}

	// 创建View方法
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(140, 140));// 设置ImageView对象布局
			imageView.setAdjustViewBounds(false);// 设置边界对齐
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 设置刻度的类型
			imageView.setPadding(1, 1, 1, 1);// 设置间距
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(imgs[position]);// 为ImageView设置图片资源
		return imageView;
	}
}
