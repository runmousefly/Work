package com.terris.mobileguard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView
{
	public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isFocused()
	{
		// TODO Auto-generated method stub
		return true;
	}
	

}
