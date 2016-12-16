package com.cspaying.shanfu.ui.entit;

import java.util.List;

public class ResultStatistics2 {
	public String returnCode;
	public String resultCode;
	public String errCodeDes;
	public String sign;
	public List<Statistics2Entity> detail;
	
	
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
	public List<Statistics2Entity> getDetail() {
		return detail;
	}
	public void setDetail(List<Statistics2Entity> detail) {
		this.detail = detail;
	}
	public String getErrCodeDes() {
		return errCodeDes;
	}
	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

}
