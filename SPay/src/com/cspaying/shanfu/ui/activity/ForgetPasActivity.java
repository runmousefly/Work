package com.cspaying.shanfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;

public class ForgetPasActivity extends BaseActivity {

	private RelativeLayout layTitle;
	private TextView tvTitle, tvBack;
	private Button btnReg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_pas_main);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

		layTitle = (RelativeLayout) findViewById(R.id.lay_title);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("忘记密码");
		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);

		btnReg = (Button) findViewById(R.id.btn_register);
		btnReg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_register:
			Intent intent = new Intent(ForgetPasActivity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_left_back:
			finish();
			break;
		default:
			break;
		}
	}
}
