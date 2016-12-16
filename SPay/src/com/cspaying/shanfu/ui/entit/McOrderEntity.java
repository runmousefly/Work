package com.cspaying.shanfu.ui.entit;

import android.R.string;

public class McOrderEntity {
	
	private String outTradeNo;//订单号
	private String totalNum;//总订单数
	private String totalAmount;//总金额
	private String logOnInfo;//登陆信息
	private String transTime;//交易时间
	private String postscript;//附言
	private String status;//支付支付状态
	private String outChannelNo;//支付类型
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getLogOnInfo() {
		return logOnInfo;
	}
	public void setLogOnInfo(String logOnInfo) {
		this.logOnInfo = logOnInfo;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOutChannelNo() {
		return outChannelNo;
	}
	public void setOutChannelNo(String outChannelNo) {
		this.outChannelNo = outChannelNo;
	}
	
	
	

}
