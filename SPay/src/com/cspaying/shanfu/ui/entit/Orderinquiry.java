package com.cspaying.shanfu.ui.entit;

import java.util.ArrayList;

public class Orderinquiry {
	
	private String returnCode;
	private int totalNum;
	private float totalAmount;
	private String logOnInfo;
	private String resultCode;
	
	private ArrayList<OrderDetail> detail;
	
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public float getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getLogOnInfo() {
		return logOnInfo;
	}
	public void setLogOnInfo(String logOnInfo) {
		this.logOnInfo = logOnInfo;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public ArrayList<OrderDetail> getDetail() {
		return detail;
	}
	public void setDetail(ArrayList<OrderDetail> detail) {
		this.detail = detail;
	}
	
	

	
	


}
