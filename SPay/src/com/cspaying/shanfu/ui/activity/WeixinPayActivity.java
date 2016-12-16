package com.cspaying.shanfu.ui.activity;


import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.utils.Util;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeixinPayActivity extends Activity {
	
	private IWXAPI api;   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		
		api = WXAPIFactory.createWXAPI(this, "wxb5d8ad7674532882");
		Button appayBtn = (Button) findViewById(R.id.appay_btn);
		appayBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
				Button payBtn = (Button) findViewById(R.id.appay_btn);
				payBtn.setEnabled(false);
				Toast.makeText(WeixinPayActivity.this, "��ȡ������...", Toast.LENGTH_SHORT).show();
		        try{
					byte[] buf = Util.httpGet(url);
					if (buf != null && buf.length > 0) {
						String content = new String(buf);
						Log.e("get server pay params:",content);
			        	JSONObject json = new JSONObject(content); 
						if(null != json && !json.has("retcode") ){
							PayReq req = new PayReq();
							//req.appId = "wxf8b4f85f3a794e77";  // ������appId
							req.appId			= "wxb5d8ad7674532882";
							req.partnerId		= "15233133";
							req.prepayId		= "wx20161017213134be5fbaacdc0215751058";
							req.nonceStr		= "Ho7nAFOALQpVqSM7";
							req.timeStamp		=  "1476718320";
							req.packageValue	=  "WXPay";
							req.sign			="4FB3D144B19BEEF48297873081A26E3C";
							req.extData			= "app data"; 
							/*Log.e("+++++++++++++++++++++++++++json", json.toString());
							Log.e("++++++++++++++++++++++++",
									json.getString("appid")+","+json.getString("partnerid")+","
									+json.getString("prepayid")+","+json.getString("noncestr")+","
									+json.getString("timestamp")+","+json.getString("package")+","
									+json.getString("sign"));*/
							Toast.makeText(WeixinPayActivity.this, "�����֧��", Toast.LENGTH_SHORT).show();
							// ��֧��֮ǰ�����Ӧ��û��ע�ᵽ΢�ţ�Ӧ���ȵ���IWXMsg.registerApp��Ӧ��ע�ᵽ΢��
							api.sendReq(req);
						}else{
				        	Log.d("PAY_GET", "���ش���"+json.getString("retmsg"));
				        	Toast.makeText(WeixinPayActivity.this, "���ش���"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
						}
					}else{
			        	Log.d("PAY_GET", "�������������");
			        	Toast.makeText(WeixinPayActivity.this, "�������������", Toast.LENGTH_SHORT).show();
			        }
		        }catch(Exception e){
		        	Log.e("PAY_GET", "�쳣��"+e.getMessage());
		        	Toast.makeText(WeixinPayActivity.this, "�쳣��"+e.getMessage(), Toast.LENGTH_SHORT).show();
		        }
		        payBtn.setEnabled(true);
			}
		});		
		Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
		checkPayBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
				Toast.makeText(WeixinPayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
