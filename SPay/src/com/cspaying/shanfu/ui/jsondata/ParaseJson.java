package com.cspaying.shanfu.ui.jsondata;

import java.util.List;

import org.nutz.json.Json;

import android.content.Context;
import android.util.Log;

import com.cspaying.shanfu.ui.activity.ReAddCashier;
import com.cspaying.shanfu.ui.entit.CashierEntity;
import com.cspaying.shanfu.ui.entit.MerchantInformation;
import com.cspaying.shanfu.ui.entit.Orderinquiry;
import com.cspaying.shanfu.ui.entit.PasswordEntity;
import com.cspaying.shanfu.ui.entit.PostOrderDetails;
import com.cspaying.shanfu.ui.entit.ProblemListEntity;
import com.cspaying.shanfu.ui.entit.Re_LoginEntity;
import com.cspaying.shanfu.ui.entit.Re_Pay_Sweixin_ntity;
import com.cspaying.shanfu.ui.entit.Re_jd_pay;
import com.cspaying.shanfu.ui.entit.Re_weixin_pay;
import com.cspaying.shanfu.ui.entit.Re_zf_pay;
import com.cspaying.shanfu.ui.entit.ResultStatistics1;
import com.cspaying.shanfu.ui.entit.ResultStatistics2;
import com.cspaying.shanfu.ui.entit.Statistics1Entity;
import com.cspaying.shanfu.ui.entit.Statistics2Entity;
import com.cspaying.shanfu.ui.entit.rece_code_entity;

public class ParaseJson {
	private static ParaseJson instance;
	private Context context;

	public ParaseJson(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public static ParaseJson getInstance(Context context) {
		if (instance == null) {
			instance = new ParaseJson(context);
		}

		return instance;

	}

	/**
	 * 解析登陆JsonData
	 * 
	 * @param data
	 * @return
	 */
	public Re_LoginEntity ParaseLoginGetData(String data) {
		Re_LoginEntity entity = null;
		try {
			entity = Json.fromJson(Re_LoginEntity.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "ParaseLoginGetData  ERROR");
		}
		return entity;
	}

	/**
	 * 解析微信支付返回字符串
	 * 
	 * @param data
	 * @return
	 */
	public Re_Pay_Sweixin_ntity ParaseWeixinPay(String data) {
		Re_Pay_Sweixin_ntity entity = null;
		try {
			entity = Json.fromJson(Re_Pay_Sweixin_ntity.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "Re_Pay_Sweixin_ntity  ERROR");
		}
		return entity;
	}

	/**
	 * 解析微信支付返回字符串
	 * 
	 * @param data
	 * @return
	 */
	public Re_zf_pay Parasezhifubao(String data) {
		Re_zf_pay entity = null;
		try {
			entity = Json.fromJson(Re_zf_pay.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "Re_Pay_Sweixin_ntity  ERROR");
		}
		return entity;
	}
	
	/**
	 * 解析微信支付返回字符串
	 * 
	 * @param data
	 * @return
	 */
	public rece_code_entity ParaseRece_code_entity(String data) {
		rece_code_entity entity = null;
		try {
			entity = Json.fromJson(rece_code_entity.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "Re_Pay_Sweixin_ntity  ERROR");
		}
		return entity;
	}
	
	/**
	 * 解析微信支付返回字符串
	 * 
	 * @param data
	 * @return
	 */
	public Re_weixin_pay ParaseWxxinPay(String data) {
		Re_weixin_pay entity = null;
		try {
			entity = Json.fromJson(Re_weixin_pay.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "Re_Pay_Sweixin_ntity  ERROR");
		}
		return entity;
	}

	/**
	 * 解析京东支付返回字符串
	 * 
	 * @param data
	 * @return
	 */
	public Re_jd_pay ParaseJdPay(String data) {
		Re_jd_pay entity = null;
		try {
			entity = Json.fromJson(Re_jd_pay.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "Re_Pay_Sweixin_ntity  ERROR");
		}
		return entity;
	}

	/**
	 * 解析商户信息
	 * 
	 * @param data
	 * @return
	 */
	public MerchantInformation ParaseMerchantInfomation(String data) {
		MerchantInformation entity = null;
		try {
			entity = Json.fromJson(MerchantInformation.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "MerchantInformation  ERROR");
		}
		return entity;
	}

	/**
	 * 解析定单信息
	 * 
	 * @param data
	 * @return
	 */
	public Orderinquiry ParaseOrderInquiry(String data) {
		Orderinquiry entity = null;
		// try{
		entity = Json.fromJson(Orderinquiry.class, data);
		// }catch(Exception e){
		// Log.e("Tag:ParsedJsonData", "MerchantInformation  ERROR");
		// }
		return entity;
	}

	/**
	 * 解析商户信息
	 * 
	 * @param data
	 * @return
	 */
	public CashierEntity ParaseCashier(String data) {
		CashierEntity entity = null;
		// try{
		entity = Json.fromJson(CashierEntity.class, data);
		// }catch(Exception e){
		// Log.e("Tag:ParsedJsonData", "MerchantInformation  ERROR");
		// }
		return entity;
	}

	/**
	 * 解析商户信息
	 * 
	 * @param data
	 * @return
	 */
	public ReAddCashier ParaseReAddCashier(String data) {
		ReAddCashier entity = null;
		// try{
		entity = Json.fromJson(ReAddCashier.class, data);
		// }catch(Exception e){
		// Log.e("Tag:ParsedJsonData", "MerchantInformation  ERROR");
		// }
		return entity;
	}

	/**
	 * 解析常见问题
	 * 
	 * @param data
	 * @return
	 */
	public ProblemListEntity ParaseProblemListEntity(String data) {
		ProblemListEntity entity = null;
		// try{
		entity = Json.fromJson(ProblemListEntity.class, data);
		// }catch(Exception e){
		// Log.e("Tag:ParsedJsonData", "MerchantInformation  ERROR");
		// }
		return entity;
	}

	/**
	 * 解析修改密码
	 * 
	 * @param data
	 * @return
	 */
	public PasswordEntity Parasechangpass(String data) {
		PasswordEntity entity = null;
		try {
			entity = Json.fromJson(PasswordEntity.class, data);
		} catch (Exception e) {
			Log.e("Tag:ParsedJsonData", "MerchantInformation  ERROR");
		}
		return entity;
	}

	/**
	 * 解析订单详情
	 * 
	 * @param data
	 * @return
	 */
	public PostOrderDetails ParasepostDetail(String data) {
		PostOrderDetails entity = null;
		try {
			entity = Json.fromJson(PostOrderDetails.class, data);
		} catch (Exception e) {
			Log.e("ParasepostDetail", e.getLocalizedMessage());
		}
		return entity;
	} 

	/**
	 * 解析定期交易统计查询
	 * 
	 * @param data
	 * @return
	 */
	public List<Statistics1Entity> ParasepostStatistics(String data) {
		List<Statistics1Entity> list = null;
		try {
			ResultStatistics1 jsonObject = Json.fromJson(ResultStatistics1.class,data);
			Log.d("JSONObject",jsonObject.toString());
			if(jsonObject.getReturnCode().equals("0")){
				list = jsonObject.getDetail();
				Log.d("JSONArray",list.toString());
				//list = (List<Statistics1Entity>) 
			}else{
				Log.e("ParasepostStatistics returnMsg", jsonObject.getReturnMsg());
			}
			// list = Json.fromJson(StatisticsEntity.class, data);  
		} catch (Exception e) {
			Log.e("ParasepostStatistics", "ParasepostStatistics exception");
			e.printStackTrace();
		}
		return list;     
	}
	  
	/**
	 * 定期交易分布查询    
	 * 
	 * @param data  
	 * @return
	 */
	public List<Statistics2Entity> ParasepostStatistics2(String data) {
		List<Statistics2Entity> list = null;
		try {
			ResultStatistics2 jsonObject = Json.fromJson(ResultStatistics2.class,data);
			Log.d("JSONObject 2",jsonObject.toString());
			if(jsonObject.getReturnCode().equals("0") && jsonObject.getResultCode().equals("0")){
				list = jsonObject.getDetail();
				Log.d("JSONArray 2",list.toString());
				//list = (List<Statistics1Entity>) 
			}else{
				Log.e("ParasepostStatistics returnMsg", jsonObject.getErrCodeDes());
			}
			// list = Json.fromJson(StatisticsEntity.class, data);
		} catch (Exception e) {
			Log.e("ParasepostStatistics", ""+e.getLocalizedMessage());
		}
		return list;
	}
}
