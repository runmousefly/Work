package com.cspaying.shanfu.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.entit.Re_jd_pay;
import com.cspaying.shanfu.ui.entit.Re_weixin_pay;
import com.cspaying.shanfu.ui.entit.Re_zf_pay;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.InitSignString;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.scan.CaptureActivity;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.CustomProgressDialog;
import com.cspaying.shanfu.ui.utils.LoginUtils;
import com.cspaying.shanfu.zxing.encoding.EncodingHandler;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.tencent.mm.sdk.openapi.IWXAPI;

public class PayActivity extends BaseActivity {

	private final static String TAG = "PayActivity";
	private RelativeLayout layTitle;
	private LinearLayout layScaCode;
	private TextView tvTitle, tvAmount, tvPayType;
	private Button btnBack;
	private TextView tvBack;
	private EditText etWeb;
	private Bitmap logoBmp;
	private ImageView ivScanCodePay;
	private LinearLayout save_code;
	private static final int IMAGE_HALFWIDTH = 40;// 宽度值，影响中间图片大小
	
	public static String Table_Type = "Table_Type";
	public static String Amount = "amount";
	
	private String  strAmount;
	private int currentType = 0;
	private CustomProgressDialog progressDialog;
	private IWXAPI api; 
	private Bitmap viewbitmap;
	private Context mContext = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_code_pay_main);
		MyApplication.getInstance().addActivity(this);
		mContext = this;
		getIntents();
		initView();
		
		initTitle();
		//initData();
	}
	
	public void getIntents(){
		currentType = getIntent().getIntExtra(Table_Type, 0);
		strAmount = getIntent().getStringExtra(Amount);
	}

	private void initView() {
		
		ivScanCodePay = (ImageView) findViewById(R.id.iv_ScanCodePay);

		tvAmount = (TextView) findViewById(R.id.tv_amount);
		tvPayType = (TextView) findViewById(R.id.tv_scanCodePayType);

		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);
		layScaCode = (LinearLayout) findViewById(R.id.lay_scanCode);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		

		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);
		
		save_code = (LinearLayout) findViewById(R.id.save_code);
		save_code.setOnClickListener(this);
		
		//layScaCode.setVisibility(View.VISIBLE);
		//layScaCode.setOnClickListener(this);
		
		ivScanCodePay.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				showShare(mContext);
				return false;
			}
		});
	}
	
	private com.google.zxing.Result  parseQRcodeBitmap(Bitmap obmp){  
         int width = obmp.getWidth();  
         int height = obmp.getHeight();  
         int[] data = new int[width * height];  
         obmp.getPixels(data, 0, width, 0, 0, width, height);  
         RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);  
         BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));  
         QRCodeReader reader = new QRCodeReader();  
         Result re = null;  
         try {  
             re = reader.decode(bitmap1);  
         } catch (NotFoundException e) {  
             e.printStackTrace();  
         } catch (ChecksumException e) {  
             e.printStackTrace();  
         } catch (FormatException e) {  
             e.printStackTrace();  
         }  
         return re;
	}  
	
	/**
	 * 加载头部
	 */
	public void initTitle(){
		tvAmount.setText(strAmount);
		if (currentType == 0) {
			tvPayType.setText("扫一扫完成支付");
			tvTitle.setText("支付宝支付");
			logoBmp = BitmapFactory.decodeResource(super.getResources(),
					R.drawable.zfb_scan_logo);
			startProgressDialog();
			postzhifubaoPay();
		} else if (currentType == 1) {
			tvPayType.setText("扫一扫完成支付");
			tvTitle.setText("微信支付");
			logoBmp = BitmapFactory.decodeResource(super.getResources(),
					R.drawable.wx_scan_logo);
			startProgressDialog();
			postWeixinPay();
			
		} else if (currentType == 2) {
			tvPayType.setText("扫一扫完成支付");  
			tvTitle.setText("京东钱包支付");
			logoBmp = BitmapFactory.decodeResource(super.getResources(),
					R.drawable.jd_san_logo);
			startProgressDialog();
			postJingDongPay();
		}
	}
	
	/**
	 * 加载二维码
	 */
	public void initData(String codeUrl){
		
		try {
			final Resources r = getResources();
			// Bitmap bm = Create2DCode(strAmount);
//			Bitmap bm = createCode(strAmount, logo, BarcodeFormat.QR_CODE);
			//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（600*600）
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(codeUrl, 600);
			
            //二维码和logo合并
			 viewbitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(), qrCodeBitmap
	                .getHeight(), qrCodeBitmap.getConfig());
	        Canvas canvas = new Canvas(viewbitmap);
	        //二维码
	        canvas.drawBitmap(qrCodeBitmap, 0,0, null);
	        //logo绘制在二维码中央
			canvas.drawBitmap(logoBmp, qrCodeBitmap.getWidth() / 2
					- logoBmp.getWidth() / 2, qrCodeBitmap.getHeight()
					/ 2 - logoBmp.getHeight() / 2, null);
			
			cacheReceCode(mContext);
			
			//------------------添加logo部分------------------//
			ivScanCodePay.setImageBitmap(viewbitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 京东支付接口
	 */
	public void postJingDongPay(){  
		    String outTradeNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
			String loginInfo = LoginUtils.getLoginName(PayActivity.this);
			String token = LoginUtils.getLoginToken(PayActivity.this);
			String mchId = loginInfo;//.split("@")[1];
			String jsonData = InitJson.getInstance(PayActivity.this)
				.JDpay(strAmount, "京东支付", "jdPay", "京东支付", "2",mchId, outTradeNo
						, "cs.pay.submit.inner", "CNY", "1.0");
			
			HttpUtil.getInstance(PayActivity.this).reques(jsonData,HttpUtil.baseUrl, 
					new OnRequestListener() {     
				
				@Override
				public void onResult(int statusCode, String str) {
					// TODO Auto-generated method stub
					Message message = new Message();
					if (statusCode == 0) {
						message.what =1;  
						message.obj = str;
					}else{
						message.what = 2;
					}
					handler.sendMessage(message);
				}
			});
	}
	
	
	
	
	/**
	 * 微信支付接口//wxPubQR
	 */
	public void postWeixinPay(){
		    String outTradeNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
			String loginInfo = LoginUtils.getLoginName(PayActivity.this);
			String token = LoginUtils.getLoginToken(PayActivity.this);
			String mchId = loginInfo;///.split("@")[1];
			String jsonData = InitJson.getInstance(PayActivity.this)
				.wxpay(strAmount, "微信支付", "wxPubQR", "微信支付", "2", mchId, outTradeNo
						, "cs.pay.submit.inner", "CNY", "1.0");
			
			HttpUtil.getInstance(PayActivity.this).reques(jsonData,HttpUtil.baseUrl, 
					new OnRequestListener() {
				
				@Override
				public void onResult(int statusCode, String str) {
					// TODO Auto-generated method stub
					Message message = new Message();
					if (statusCode == 0) {
						message.what = 2;
						message.obj = str;
					}else{
						message.what = 4;
					}
					handler.sendMessage(message);
				}
			});
	}
	
	
	/**
	 * 支付宝支付接口
	 */
	public void postzhifubaoPay(){
		    String outTradeNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
			String loginInfo = LoginUtils.getLoginName(PayActivity.this);
			String token = LoginUtils.getLoginToken(PayActivity.this);
			String mchId = loginInfo;//.split("@")[1]
			String jsonData = InitJson.getInstance(PayActivity.this)
				.wxpay(strAmount, "支付宝支付", "alipayQR", "支付宝支付", "2",mchId, outTradeNo
						, "cs.pay.submit.inner", "CNY", "1.0");
			
			HttpUtil.getInstance(PayActivity.this).reques(jsonData,HttpUtil.baseUrl, 
					new OnRequestListener() {
				
				@Override
				public void onResult(int statusCode, String str) {
					// TODO Auto-generated method stub
					Message message = new Message();
					if (statusCode == 0) {
						message.what = 3;
						message.obj = str;
					}else{
						message.what = 4;
					}
					handler.sendMessage(message);
				}
			});
	}
	
	/**
	 * 解析微信订单支付返回JSON字符串
	 * @param jsonData
	 */
	public void ParseReSWeixinPay(String jsonData){
		Re_weixin_pay reSweixinpayEntity = ParaseJson.getInstance(PayActivity.this)
				    .ParaseWxxinPay(jsonData);
		if (reSweixinpayEntity != null) {
			Message message = new Message();
			if(reSweixinpayEntity.getResultCode().equals("0")){
				HashMap<String, String> hashMap = new HashMap<String, String>();
				
				
				hashMap.put("outChannelNo", reSweixinpayEntity.getOutChannelNo());
				hashMap.put("codeUrl", reSweixinpayEntity.getCodeUrl());
				hashMap.put("resultCode", reSweixinpayEntity.getResultCode());
				hashMap.put("returnCode", reSweixinpayEntity.getReturnCode());
				hashMap.put("outTradeNo", reSweixinpayEntity.getOutTradeNo());
				
			  
				
				if (reSweixinpayEntity.getReturnMsg() != null) {
					hashMap.put("returnMsg", reSweixinpayEntity.getReturnMsg());
				}
				String Loginkey = LoginUtils.getSignKey(MyApplication.getContext());
				String sign = InitSignString.getSign(hashMap,Loginkey);
				Log.e("++++++++++++++++++++++++++sign", sign);
				if (true) {//)sign.equals(reSweixinpayEntity.getSign())
					message.what = 8;
					message.obj = reSweixinpayEntity.getCodeUrl();
				}else {
					message.what = 5;
				}
			}else {
				message.what = 4;
				
			}
			handler.sendMessage(message);
			
		}else {
			handler.sendEmptyMessage(6);
		}
	}
	
	/**
	 * 解析微信订单支付返回JSON字符串
	 * @param jsonData
	 */
	public void ParseReSWeixinPay2(String jsonData){
		Re_zf_pay reSweixinpayEntity = ParaseJson.getInstance(PayActivity.this)
				    .Parasezhifubao(jsonData);
		if (reSweixinpayEntity != null) {
			Message message = new Message();
			if(reSweixinpayEntity.getResultCode().equals("0")){
				HashMap<String, String> hashMap = new HashMap<String, String>();
				
				
				hashMap.put("outChannelNo", reSweixinpayEntity.getOutChannelNo());
				hashMap.put("codeUrl", reSweixinpayEntity.getCodeUrl());
				hashMap.put("resultCode", reSweixinpayEntity.getResultCode());
				hashMap.put("returnCode", reSweixinpayEntity.getReturnCode());
				hashMap.put("outTradeNo", reSweixinpayEntity.getOutTradeNo());
				
				if (reSweixinpayEntity.getReturnMsg() != null) {
					hashMap.put("returnMsg", reSweixinpayEntity.getReturnMsg());
				}
				String Loginkey = LoginUtils.getSignKey(MyApplication.getContext());
				String sign = InitSignString.getSign(hashMap,Loginkey);
				Log.e("++++++++++++++++++++++++++sign", sign);
				if (true) {
					message.what = 8;
					message.obj = reSweixinpayEntity.getCodeUrl();
				}else {
					message.what = 5;
				}
			}else {
				message.what = 4;
				
			}
			handler.sendMessage(message);
			
		}else {
			handler.sendEmptyMessage(6);
		}
	}
	
	
	/**
	 * 解析京东订单支付返回JSON字符串
	 * @param jsonData
	 */
	public void ParseReJdPay(String jsonData){
		Re_jd_pay reSjdpayEntity = ParaseJson.getInstance(PayActivity.this)
				    .ParaseJdPay(jsonData);
		if (reSjdpayEntity != null) {
			Message message = new Message();
			if(reSjdpayEntity.getResultCode().equals("0")){
				HashMap<String, String> hashMap = new HashMap<String, String>();
				
				  
				hashMap.put("outTradeNo", reSjdpayEntity.getOutTradeNo());
				hashMap.put("outChannelNo", reSjdpayEntity.getOutChannelNo());
				hashMap.put("payCode", reSjdpayEntity.getPayCode());
				hashMap.put("resultCode", reSjdpayEntity.getResultCode());
				hashMap.put("returnCode", reSjdpayEntity.getReturnCode());
				hashMap.put("transTime", reSjdpayEntity.getTransTime());
				if (reSjdpayEntity.getReturnMsg() != null) {
					hashMap.put("returnMsg", reSjdpayEntity.getReturnCode());
				}
				
				String Loginkey = LoginUtils.getSignKey(MyApplication.getContext());
				String sign = InitSignString.getSign(hashMap,Loginkey);
				Log.e("++++++++++++++++++++++++++sign", sign);
				Log.e("++++++++++++++++++++++++++reSjdpayEntity.getSign()", sign);
				if (true) {//sign.equals(reSjdpayEntity.getSign())
					message.what = 8;
					message.obj = reSjdpayEntity.getPayCode();
					
				}else {  
					message.what = 5;
				}
			}else {
				message.what = 4;
				
			}
			handler.sendMessage(message);
			
		}else {
			handler.sendEmptyMessage(6);
		}
	}
	
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 定义一个Handler，用于处理下载线程与UI间通讯
			switch (msg.what) {
			case 1 :
				String reJd = (String) msg.obj;
				if (reJd != null) {
					ParseReJdPay(reJd);
				}
				
				break;
			case 2:
				String wxJd = (String) msg.obj;
				if (wxJd != null) {
					ParseReSWeixinPay(wxJd);  
				}
				
				break;
			case 3 :
				String zhifubaoData = (String) msg.obj;
				if (zhifubaoData != null) {
					ParseReSWeixinPay2(zhifubaoData);  
				}
			   break;
			case 4:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				break;
				
			case 5:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				
				break;
			case 6:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				break;
			case 7:
			    String wxcodeUrl = (String) msg.obj;
			    if (wxcodeUrl != null) {
			    	initData(wxcodeUrl);
				}
			break;
			case 8:
			    String jdcodeUrl = (String) msg.obj;
			    if (jdcodeUrl != null) {
			    	initData(jdcodeUrl);
				}
			break;
			case 9:
				String codeUrl = msg.toString();
				if (codeUrl != null) {
					if (currentType == 0) {
						
					} else if (currentType == 1) {
						
					} else if (currentType == 2) {
						
					}
				}
				break;
			
			
			default:
				
				break;
			}
			stopProgressDialog();
		}
		
		
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		case R.id.tv_left_back:
			finish();
			break;
		case R.id.lay_scanCode :
		    Intent intent = new Intent(PayActivity.this,CaptureActivity.class);
		    startActivity(intent);
		   break;
		case R.id.save_code :
			startProgressDialog();
			saveImageToGallery(PayActivity.this);
			break;
		default:
			break;
		}
	}
	
	public  void saveImageToGallery(Context context) {
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "QPay");
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        viewbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	    
	    // 其次把文件插入到系统图库 
	    try {
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    // 最后通知图库更新
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
	    		, Uri.parse("file://" + fileName)));
	    Toast.makeText(getApplicationContext(),MyApplication.getContext()
				.getString(R.string.save_code_sucess)
				,Toast.LENGTH_SHORT).show();
	    stopProgressDialog();
	}
	
	public  void cacheReceCode(Context context) {
	    File appDir = new File(context.getCacheDir(), "QPay");
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = "tmpPayCode.png";
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        viewbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	}
	
	//二维码分享
    private void showShare(Context context) 
    {
    	 OnekeyShare oks = new OnekeyShare();
    	 //关闭sso授权
    	 oks.disableSSOWhenAuthorize(); 
    	 // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
    	 //oks.setTitle("QPay固定收款二维码");
    	 // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
    	// oks.setTitleUrl("http://www.cspaying.com");
    	 // text是分享文本，所有平台都需要这个字段
    	// oks.setText("QPay固定收款二维码");
    	 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
    	 String shareImgPath = context.getCacheDir().getAbsolutePath()+"/QPay/tmpPayCode.png";
    	 File shareFile = new File(shareImgPath);
    	 if(!shareFile.exists())
    	 {
    		 Log.e(TAG, "share failed!");
    		 return;
    	 }
 	     oks.setImagePath(shareImgPath);
    	 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    	 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
    	 // url仅在微信（包括好友和朋友圈）中使用
    	 //oks.setUrl(mRecvCodeUrl);
    	 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
    	 oks.setComment("深圳前海乘势科技有限公司");
    	 // site是分享此内容的网站名称，仅在QQ空间使用
    	// oks.setSite("深圳前海乘势科技有限公司");
    	 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
    	 //oks.setSiteUrl("http://www.cspaying.com");

    	// 启动分享GUI
    	 oks.show(this);
    	 }
    
	 /**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(this);
    	   progressDialog.setMessage(MyApplication.getContext()
    			   .getResources().getString(R.string.intalling));
    	}
    	progressDialog.show();
    }
    
    /**
     * 结束ProgressDialog()
     */
    private void stopProgressDialog(){
	  if (progressDialog != null && progressDialog.isShowing()){
    	progressDialog.dismiss();
    	progressDialog = null;
      }
    }
    
}
