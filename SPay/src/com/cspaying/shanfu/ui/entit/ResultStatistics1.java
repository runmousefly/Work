package com.cspaying.shanfu.ui.entit;

import java.util.List;

public class ResultStatistics1 {
	public String returnCode;
	public String resultCode;
	public String returnMsg;
	
	public String sign;
	public List<Statistics1Entity> detail;
	
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public List<Statistics1Entity> getDetail() {
		return detail;
	}
	public void setDetail(List<Statistics1Entity> detail) {
		this.detail = detail;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	
}
