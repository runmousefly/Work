package com.cspaying.shanfu.ui.entit;

import java.util.ArrayList;

public class CashierEntity {
	private String returnCode;
	private String returnMsg;
	private String resultCode;
	private String errCode;
	private String errCodeDes;
	private ArrayList<CashierDetailEntity> detail; 
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrCodeDes() {
		return errCodeDes;
	}
	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}
	public ArrayList<CashierDetailEntity> getDetail() {
		return detail;
	}
	public void setDetail(ArrayList<CashierDetailEntity> detail) {
		this.detail = detail;
	}
	
	
}
