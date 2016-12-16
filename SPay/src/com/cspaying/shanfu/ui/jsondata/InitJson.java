package com.cspaying.shanfu.ui.jsondata;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.json.Json;

import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.activity.LoginActivity;
import com.cspaying.shanfu.ui.entit.ExcraEntity;
import com.cspaying.shanfu.ui.entit.JdExcraEntity;
import com.cspaying.shanfu.ui.entit.Jd_pay_entity;
import com.cspaying.shanfu.ui.entit.OrderEntity;
import com.cspaying.shanfu.ui.entit.ReRefund;
import com.cspaying.shanfu.ui.entit.TransStatus;
import com.cspaying.shanfu.ui.utils.LoginUtils;

import android.content.Context;
import android.util.Log;


public class InitJson {
	
	private static InitJson instance;
	private Context context;
	public final static String GetHomeSlide = "GetHomeSlide";
	public final static String GetDiamondRingSeries = "GetDiamondRingSeries";
	private static String longinkey;
	public InitJson(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public static InitJson getInstance(Context context){
		if(instance == null){
			instance = new InitJson(context);
		}
		longinkey = LoginUtils.getSignKey(MyApplication.getContext());
		return instance;
	}
	
	
	public String initExtra(String key ,String value){
		 String data = null;
		 JSONObject jsonObject=new JSONObject() ;
		 try {
			jsonObject.put(key,value);
			data = jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return data;
		 
	}
	/**
	 * 生成登陆JsonData
	 * @param apiname
	 * @return
	 */
	public String initLogin(String tradeType,String version,String logOnInfo
			,String logOnPassword,String deviceNum){
		 String data = null;
		 String password = InitSignString.MD5(logOnPassword);
		 String deviceId = MyApplication.getImei();
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("logOnInfo",logOnInfo);
		 hashMap.put("logOnPassword",password);
		 hashMap.put("deviceNum"," "+ deviceId);            
		 
		 //key = "91be991a7491481ab43a89657a780b69"
		 //String sign = InitSignString.getSign(hashMap,longinkey);
		
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			    jsonObject.put("tradeType",tradeType);  
			    jsonObject.put("version",version);
			    jsonObject.put("logOnInfo",logOnInfo);
			    jsonObject.put("logOnPassword",password);
			    jsonObject.put("deviceNum",deviceId);
			    //jsonObject.put("sign",sign);
                data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "initLeftList ERROR");
        }
		
		return data;
	}
	
	
	/**
	 * 生成微信扫描支付
	 * @param apiname
	 * @return
	 */
	public String initScanPayJson(
			 String amount,String body,String channel,String currency,String description
			,String goodsTag,String limitPay,String mchId,String notifyUrl,String outTradeNo
			,String productId,String timePaid,String tradeType,String version
			){
		 String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("amount",amount);
		 hashMap.put("body",body);
		 hashMap.put("channel",channel);
		 hashMap.put("currency",currency);
		 hashMap.put("description",description);
		 
		 hashMap.put("goodsTag",goodsTag);
		 hashMap.put("limitPay",limitPay);
		 hashMap.put("mchId",mchId);
		 hashMap.put("notifyUrl",notifyUrl);
		 hashMap.put("outTradeNo",outTradeNo);
		    
		 hashMap.put("productId",productId);
		 hashMap.put("timePaid",timePaid);
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 
		 //key = "91be991a7491481ab43a89657a780b69"
		 String Loginkey = LoginUtils.getSignKey(MyApplication.getContext());
		 String sign = InitSignString.getSign(hashMap,Loginkey);
		
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			    jsonObject.put("amount",amount);
			    jsonObject.put("body",body);
			    jsonObject.put("channel",channel);
			    jsonObject.put("currency",currency);
			    jsonObject.put("description",description);
			    
			    
			    jsonObject.put("goodsTag",goodsTag);
			    jsonObject.put("limitPay",limitPay);
			    jsonObject.put("mchId",mchId);
			    jsonObject.put("notifyUrl",notifyUrl);
			    jsonObject.put("outTradeNo",outTradeNo);
			    
			    
			    jsonObject.put("productId",productId);
			    jsonObject.put("timePaid",timePaid);
			    jsonObject.put("tradeType",tradeType);
			    jsonObject.put("version",version);
			    jsonObject.put("sign",sign);
			    
			    
                data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "initLeftList ERROR");
        }
		
		return data;
	}
	
	
	/**
	 * 商户信息查询
	 * @param apiname
	 * @return
	 */
	public String initMerchantDetail(String logOnInfo,String token,String tradeType,String version){
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("logOnInfo",logOnInfo);
		 hashMap.put("token",token);
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 
		 //key = "91be991a7491481ab43a89657a780b69"
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			    jsonObject.put("logOnInfo",logOnInfo);
			    jsonObject.put("token",token);
			    jsonObject.put("tradeType",tradeType);
			    jsonObject.put("version",version);
			    jsonObject.put("sign",sign);
			    
			    data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "initDiamonsDetai ERROR");
        }
		
		return data;
	}
	
	
	
	/**
	 * 交易流水查询
	 * @param apiname
	 * @return
	 */
	public String initTransactionFlow(String logOnInfo,String token,String queryType
			,String tradeType,String transDate
			,String transEndTime,String transStartTime,String transStatus,String version){
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("logOnInfo",logOnInfo);
		 hashMap.put("token",token);
		 hashMap.put("queryType",queryType);
		 hashMap.put("tradeType",tradeType);
		 
		 hashMap.put("transDate",transDate);
		// hashMap.put("transEndTime",transEndTime);
		// hashMap.put("transStartTime",transStartTime);
		 hashMap.put("transStatus",transStatus);
		 hashMap.put("version",version);
		 
		 //key = "91be991a7491481ab43a89657a780b69"
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			
			
			jsonObject.put("logOnInfo",logOnInfo);
			jsonObject.put("token",token);
			jsonObject.put("queryType",queryType);
			jsonObject.put("tradeType",tradeType);
			 
			jsonObject.put("transDate",transDate);
			//jsonObject.put("transEndTime",transEndTime);
			//jsonObject.put("transStartTime",transStartTime);
			jsonObject.put("transStatus",transStatus);
			jsonObject.put("version",version);
			
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "initDiamonsDetai ERROR");
        }
		
		return data;
	}
	
	
	/**
	 * 退款流水查询
	 * @param apiname
	 * @return
	 */
	public String initRefundFlow(String logOnInfo,String token,String queryType
			,String tradeType,String transDate
			,String transEndTime,String transStartTime,String transStatus,String version){
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("logOnInfo",logOnInfo);
		 hashMap.put("token",token);
		 hashMap.put("queryType",queryType);
		 hashMap.put("tradeType",tradeType);
		 
		 hashMap.put("transDate",transDate);
		
		 hashMap.put("transStatus",transStatus);
		 hashMap.put("version",version);
		 
		 //key = "91be991a7491481ab43a89657a780b69"
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			
			
			jsonObject.put("logOnInfo",logOnInfo);
			jsonObject.put("token",token);
			jsonObject.put("queryType",queryType);
			jsonObject.put("tradeType",tradeType);
			 
			jsonObject.put("transDate",transDate);
		    jsonObject.put("transStatus",transStatus);
			jsonObject.put("version",version);
			
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "initDiamonsDetai ERROR");
        }
		
		return data;
	}
	
	
	
	/**
	 * 退款申请
	 * @param apiname
	 * @return
	 */
	public String Refund(String tradeType,String version,String token
			,String mchId,String channel,String outTradeNo,String outRefundNo
			,String amount,String description){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		   
		 hashMap.put("mchId",mchId);
		 hashMap.put("channel",channel);
		 hashMap.put("outTradeNo",outTradeNo);
		 hashMap.put("outRefundNo",outRefundNo);
		 
		 hashMap.put("amount",amount);
		 hashMap.put("description",description);
		 //hashMap.put("extra",extra);
		 
		 
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			jsonObject.put("tradeType",tradeType);
			 jsonObject.put("version",version);
			 jsonObject.put("token",token);
			 
			 jsonObject.put("mchId",mchId);
			 jsonObject.put("channel",channel);
			 jsonObject.put("outTradeNo",outTradeNo);
			 jsonObject.put("outRefundNo",outRefundNo);
			 
			 jsonObject.put("amount",amount);
			 jsonObject.put("description",description);
			 jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initRefund", "initRefund ERROR");
        }
		
		return data;
	}
	
	/**
	 * 增加收银员
	 * @param apiname
	 * @return
	 */
	public String initAddCashier(String tradeType,String version
		,String token,String cashierName,String cashierPhone,String store,String refundAut
		,String queryDtlAut,String queryStatAut,String startupAut){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		 hashMap.put("cashierName",cashierName);
		 hashMap.put("cashierPhone",cashierPhone);
		 hashMap.put("store",store);
		 hashMap.put("refundAut",refundAut);
		 hashMap.put("queryDtlAut",queryDtlAut);
		 hashMap.put("queryStatAut",queryStatAut);
		 hashMap.put("startupAut",startupAut);
		 

		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			 
			jsonObject.put("tradeType",tradeType);
			jsonObject.put("version",version);
			jsonObject.put("token",token);
			jsonObject.put("cashierName",cashierName);
			jsonObject.put("cashierPhone",cashierPhone);
			jsonObject.put("store",store);
			jsonObject.put("refundAut",refundAut);
			jsonObject.put("queryDtlAut",queryDtlAut);
			jsonObject.put("queryStatAut",queryStatAut);
			jsonObject.put("startupAut",startupAut);
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	
	/**
	 * 查询收银员
	 * @param apiname
	 * @return
	 */
	public String initQureyCashier(String tradeType,String version,String token){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		
		 

		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			 
			jsonObject.put("tradeType",tradeType);
			jsonObject.put("version",version);
			jsonObject.put("token",token);
			
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	/**
	 * 增加收银员
	 * @param apiname
	 * @return
	 */
	public String initDeleteCashier(String tradeType,String version,String token,String cashierNo){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		 hashMap.put("cashierNo",cashierNo);
		
		String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			 
			jsonObject.put("tradeType",tradeType);
			jsonObject.put("version",version);
			jsonObject.put("token",token);
			jsonObject.put("cashierNo",cashierNo);
			
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	
	
	/**
	 * 增加收银员
	 * @param apiname
	 * @return
	 */
	public String initUpdateCashier(String tradeType,String version,String token,String cashierNo
			,String cashierName,String cashierPhone,String store,String refundAut,
			String queryDtlAut,String queryStatAut,String startupAut){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		 hashMap.put("cashierNo",cashierNo);
		 hashMap.put("cashierName",cashierName);
		 hashMap.put("cashierPhone",cashierPhone);
		 hashMap.put("store",store);
		 hashMap.put("refundAut",refundAut);
		 hashMap.put("queryDtlAut",queryDtlAut);
		 hashMap.put("queryStatAut",queryStatAut);
		 hashMap.put("startupAut",startupAut);
		
		String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			 
			jsonObject.put("tradeType",tradeType);
			jsonObject.put("version",version);
			jsonObject.put("token",token);
			jsonObject.put("cashierNo",cashierNo);
			jsonObject.put("cashierName",cashierName);
			jsonObject.put("cashierPhone",cashierPhone);
			jsonObject.put("store",store);
			jsonObject.put("refundAut",refundAut);
			jsonObject.put("queryDtlAut",queryDtlAut);
			jsonObject.put("queryStatAut",queryStatAut);
			jsonObject.put("startupAut",startupAut);
			
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	
	
	/**
	 * 注销登陆
	 * @param apiname
	 * @return
	 */
	public String Cancellation_login(String tradeType,String version
			,String logOnInfo,String token){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("logOnInfo",logOnInfo);
		 hashMap.put("token",token);
		 
		 
        String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			 
			jsonObject.put("tradeType",tradeType);
			jsonObject.put("version",version);
			jsonObject.put("logOnInfo",logOnInfo);
			jsonObject.put("token",token);
			
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	
	/**
	 * 修改密码
	 * @param apiname
	 * @return
	 */
	public String Modify_Password(String tradeType,String version
			,String token,String logOnInfo,String oldPass,String newPass){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",logOnInfo);
		 hashMap.put("logOnInfo",token);
		 hashMap.put("oldPass",oldPass);
		 hashMap.put("newPass",newPass);
		 
		String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject() ;
		try {   
			 
			jsonObject.put("tradeType",tradeType);
			 jsonObject.put("version",version);
			 jsonObject.put("token",logOnInfo);
			 jsonObject.put("logOnInfo",token);
			 jsonObject.put("oldPass",oldPass);
			 jsonObject.put("newPass",newPass);
			
			jsonObject.put("sign",sign);
			    
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	
	/**
	 * 订单接口
	 * @param apiname
	 * @return
	 */
	public String InitOrder(String tradeType,String version,String mchId
		   ,String channel,String amount,String amountSettle
		   ,String currency,String subject,String body ,ExcraEntity extra,String timePaid,String timeExpire
		   ,String description,String cashierNo,String terminalType,String extraType,String ScanId){
		
		 String data = null;
		 
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("mchId",mchId);
		 hashMap.put("channel",channel);
		 
		
		 hashMap.put("amount",amount);
		 hashMap.put("amountSettle",amountSettle);
		 hashMap.put("currency",currency);
		 hashMap.put("subject",subject);   
		 hashMap.put("body",body);
		 hashMap.put(extraType,ScanId);
		 
		
		 hashMap.put("description",description);
		 hashMap.put("cashierNo",cashierNo);
		 hashMap.put("terminalType",terminalType);
		 hashMap.put("timePaid",timePaid);
		 //hashMap.put("timeExpire",timeExpire);
		 
		String sign = InitSignString.getSign(hashMap,longinkey);
		JSONObject jsonObject=new JSONObject() ;
		OrderEntity orderEntity = new OrderEntity();
		try {   
			orderEntity.setTradeType(tradeType);
			orderEntity.setVersion(version);
			orderEntity.setMchId(mchId);
			orderEntity.setChannel(channel);
			orderEntity.setAmount(amount);
			orderEntity.setAmountSettle(amountSettle);
			orderEntity.setCurrency(currency);
			orderEntity.setSubject(subject);
			orderEntity.setBody(body);
			orderEntity.setExtra(extra);
			orderEntity.setTimePaid(timePaid);
			//orderEntity.setTimeExpire(timeExpire);
			orderEntity.setDescription(description);
			orderEntity.setCashierNo(cashierNo);
			orderEntity.setTerminalType(terminalType);
			orderEntity.setSign(sign);
			 
			    
			data = Json.toJson(orderEntity);
			
        } catch (Exception e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	
	/**
	 * 支付
	 * @param apiname
	 * @return
	 */
	public String JDpay(String amount,String body,String channel,String description
		,String accessType,String mchId,String outTradeNo,String tradeType,
		String currency,String version ){
		
		 String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("amount",amount);
		 hashMap.put("body",body);
		 hashMap.put("channel",channel);
		 hashMap.put("description",description);
		 hashMap.put("accessType",accessType);
		 hashMap.put("mchId",mchId);
		 
		// hashMap.put("outTradeNo",outTradeNo);
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("currency",currency); 
		 hashMap.put("version",version);
		 
		String sign = InitSignString.getSign(hashMap,longinkey);
		JSONObject jsonObject=new JSONObject() ;
		Jd_pay_entity JdEntity = new Jd_pay_entity();
		JdExcraEntity jdExcraEntity = new JdExcraEntity();
		jdExcraEntity.setAccessType(accessType);
		try {   
			 
			JdEntity.setAmount(amount);
			JdEntity.setBody(body);
			JdEntity.setChannel(channel);
			JdEntity.setCurrency(currency);
			JdEntity.setDescription(description);
			JdEntity.setExtra(jdExcraEntity);
			JdEntity.setMchId(mchId);
			//JdEntity.setOutTradeNo(outTradeNo);
			JdEntity.setSign(sign);
			JdEntity.setTradeType(tradeType);
			JdEntity.setVersion(version);
			
			data =  Json.toJson(JdEntity);
        } catch (Exception e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	/**
	 * 支付
	 * @param apiname
	 * @return
	 */
	public String wxpay(String amount,String body,String channel,String description
		,String accessType,String mchId,String outTradeNo,String tradeType,
		String currency,String version ){
		
		 String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("amount",amount);
		 hashMap.put("body",body);
		 hashMap.put("channel",channel);
		 hashMap.put("description",description);
		 //hashMap.put("accessType",accessType);
		 hashMap.put("mchId",mchId);
		 
		// hashMap.put("outTradeNo",outTradeNo);
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("currency",currency); 
		 hashMap.put("version",version);
		 
		 
		  
		 
		String sign = InitSignString.getSign(hashMap,longinkey);
		JSONObject jsonObject=new JSONObject() ;
		Jd_pay_entity JdEntity = new Jd_pay_entity();
		JdExcraEntity jdExcraEntity = new JdExcraEntity();
		jdExcraEntity.setAccessType(accessType);
		try {   
			 
			JdEntity.setAmount(amount);
			JdEntity.setBody(body);
			JdEntity.setChannel(channel);
			JdEntity.setCurrency(currency);
			JdEntity.setDescription(description);
			//JdEntity.setExtra(jdExcraEntity);
			JdEntity.setMchId(mchId);
			//JdEntity.setOutTradeNo(outTradeNo);
			JdEntity.setSign(sign);
			JdEntity.setTradeType(tradeType);
			JdEntity.setVersion(version);
			
			data =  Json.toJson(JdEntity);
        } catch (Exception e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	
	
	
	/**
	 * 修改密码
	 * @param apiname
	 * @return
	 */
	public String yufu(){
		
		 String data = null;
		 

		 JSONObject jsonObject=new JSONObject() ;
		try {   
			 
			jsonObject.put("uname","13266895300");
			 jsonObject.put("pwd","b2069e887382dea69b7ca9b68b681583");
			 jsonObject.put("tamp","1469864494495");
			 jsonObject.put("sign","d07d0ed2290fb17050450812f8e4db13");
			
			data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initAddCashier", "initAddCashier ERROR");
        }
		
		return data;
	}
	/**
	 * 查询收银员
	 * @param apiname
	 * @return
	 */
	public String queryCashier(String tradeType,String version
			,String token){
		 String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject();
		try {   
			
			jsonObject.put("tradeType",tradeType);
			 jsonObject.put("version",version);
			 jsonObject.put("token",token);
			 jsonObject.put("sign",sign);
			 data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========queryCashier", "queryCashier ERROR");
        }
		return data;
	}
	
	
	/**
	 * 常见问题
	 * @param apiname
	 * @return
	 */
	public String initProblem(String tradeType,String version){
		 String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject();
		try {   
			
			 jsonObject.put("tradeType",tradeType);
			 jsonObject.put("version",version);
			 jsonObject.put("sign",sign);
			 data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========queryCashier", "queryCashier ERROR");
        }
		return data;
	}
	
	/**
	 * 修改密码
	 * @param apiname
	 * @return
	 */
	public String initChangePass(String tradeType,String version,String token
			,String logOnInfo,String oldPass,String newPass){
		 String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		 hashMap.put("logOnInfo",logOnInfo);
		 hashMap.put("oldPass",oldPass);
		 hashMap.put("newPass",newPass);
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject();
		try {   
			
			 jsonObject.put("tradeType",tradeType);
			 jsonObject.put("version",version);
			 jsonObject.put("token",token);
			 jsonObject.put("logOnInfo",logOnInfo);
			 jsonObject.put("oldPass",oldPass);
			 jsonObject.put("newPass",newPass);
			 
			 jsonObject.put("sign",sign);
			 data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initChangePass", "initChangePass ERROR");
        }
		return data;
	}
	
	
	
	/**
	 * 加载详细订单
	 * @param apiname
	 * @return
	 */
	public String initDetailOrder(String tradeType,String version,String mchId
			,String outTradeNo,String queryType){
		 String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("mchId",mchId);
		 hashMap.put("outTradeNo",outTradeNo);
		 hashMap.put("queryType",queryType);
		 
		

		 String sign = InitSignString.getSign(hashMap,longinkey);
		 JSONObject jsonObject=new JSONObject();
		try {   
			
			 jsonObject.put("tradeType",tradeType);
			 jsonObject.put("version",version);
			 jsonObject.put("mchId",mchId);
			 jsonObject.put("outTradeNo",outTradeNo);
			 jsonObject.put("queryType",queryType);
			 jsonObject.put("sign",sign);
			 data = jsonObject.toString();
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initChangePass", "initChangePass ERROR");
        }
		return data;
	}
	
	/**
	 * 退款
	 * @param apiname
	 * @return
	 */
	public String initRefund(String tradeType,String version,String token,String mchId
		,String channel,String outTradeNo,String outRefundNo,String amount,String 
			description){
		
		String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("token",token);
		 hashMap.put("mchId",mchId);
		 
		 hashMap.put("channel",channel);
		 hashMap.put("outTradeNo",outTradeNo);
		 hashMap.put("outRefundNo",outRefundNo);
		 hashMap.put("amount",amount);
		 hashMap.put("description",description);
		 
		 
		

		 String sign = InitSignString.getSign(hashMap,longinkey);
		 ReRefund jsonObject= new ReRefund();
			try {   
				
				jsonObject.setTradeType(tradeType);
				jsonObject.setVersion(version);
				jsonObject.setToken(token);
				jsonObject.setMchId(mchId);
				 
				jsonObject.setChannel(channel);
				jsonObject.setOutTradeNo(outTradeNo);
				jsonObject.setOutRefundNo(outRefundNo);
				jsonObject.setAmount(amount);
				jsonObject.setDescription(description);
				 jsonObject.setSign(sign);
				 data = jsonObject.toString();
        } catch (Exception e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initChangePass", "initChangePass ERROR");
        }
		return data;
	}
	
	/**
	 * 退款
	 * @param apiname  
	 * @return
	 */
	public String Refund(String tradeType,String version,String token,String mchId
		,String channel,String outTradeNo,String outRefundNo,String amount,String 
			description,TransStatus transStatus){
		
		String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		// hashMap.put("token",token);
		 hashMap.put("mchId",mchId);
		 
		 hashMap.put("channel",channel);
		 hashMap.put("outTradeNo",outTradeNo);
		 hashMap.put("outRefundNo",outRefundNo);
		 hashMap.put("amount",amount);
		 hashMap.put("description",description);
		 
		 
		

		 String sign = InitSignString.getSign(hashMap,longinkey);
		 ReRefund jsonObject= new ReRefund();
			try {   
				
				jsonObject.setTradeType(tradeType);
				jsonObject.setVersion(version);
				//jsonObject.setToken(token);
				jsonObject.setMchId(mchId);
				 
				jsonObject.setChannel(channel);
				jsonObject.setOutTradeNo(outTradeNo);
				jsonObject.setOutRefundNo(outRefundNo);
				jsonObject.setAmount(amount);
				jsonObject.setDescription(description);
				jsonObject.setExtra(transStatus);
				 jsonObject.setSign(sign);
				 data = Json.toJson(jsonObject);
        } catch (Exception e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initChangePass", "initChangePass ERROR");
        }
		return data;
	}
	
	
	/**
	 * 退款
	 * @param apiname  
	 * @return
	 */
	public String ReceCode(String tradeType,String version,String mchId){
		
		String data = null;
		 HashMap<String, String> hashMap = new HashMap<String, String>();
		 hashMap.put("tradeType",tradeType);
		 hashMap.put("version",version);
		 hashMap.put("mchId",mchId);
		 
		
		 String sign = InitSignString.getSign(hashMap,longinkey);
		 ReRefund jsonObject= new ReRefund();
			try {   
				
				jsonObject.setTradeType(tradeType);
				jsonObject.setVersion(version);
				jsonObject.setMchId(mchId);
				 
				
				 jsonObject.setSign(sign);
				 data = Json.toJson(jsonObject);
        } catch (Exception e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========initChangePass", "initChangePass ERROR");
        }
		return data;
	}

}
