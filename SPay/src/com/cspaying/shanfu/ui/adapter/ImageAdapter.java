package com.cspaying.shanfu.ui.adapter;

import com.cspaying.shanfu.ui.view.GalleryFlow;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter
{
	private Context mContext;
	private int[] mImageIds;
	private int[] mselectedImageIds;

	private int width;
	private int selectedPosition;
	private int currentItem;
	//public ImageView[] mImages;

	public ImageAdapter(Context c, int[] ImageIds,int[] selectedImageIds,int width)
	{
		mContext = c;
		mImageIds = ImageIds;
		mselectedImageIds = selectedImageIds;
		this.width = width;
		
		//mImages = new ImageView[mImageIds.length];
	}

	public int getCount()
	{
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if (position == selectedPosition) {
			currentItem = position;
			changeImage();
		}
		return position;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param position
	 * @return
	 */
	private ImageView getImageView(int position)
	{
		

		ImageView imageView = new ImageView(mContext);
		
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageResource(mselectedImageIds[position % mselectedImageIds.length]);
		imageView.setLayoutParams(new GalleryFlow.LayoutParams(width
				, LayoutParams.MATCH_PARENT));
		/*if (position != currentItem) {
			imageView.setLayoutParams(new GalleryFlow.LayoutParams(width
					, LayoutParams.MATCH_PARENT));
		} else {
			imageView.setLayoutParams(new GalleryFlow.LayoutParams(width*3
					, LayoutParams.MATCH_PARENT));
			imageView.setImageResource(mselectedImageIds[position % mselectedImageIds.length]);
		}*/
		
		
		/*if(position == currentItem || position == currentItem-1 || position ==currentItem+1 ){
			imageView.setVisibility(View.VISIBLE);
		}else {
			imageView.setVisibility(View.GONE);
		}*/
		
		return imageView;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getImageView(position);
	}

	public float getScale(boolean focused, int offset)
	{
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
	
	public void setSelltion(int selectedPosition){
		this.selectedPosition = selectedPosition;
	}
	
	public void changeImage() {
		this.notifyDataSetChanged();
	}
}
