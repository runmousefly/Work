package com.cspaying.shanfu.ui.entit;

import java.util.ArrayList;

public class ProblemListEntity {
	
	private String returnCode;
	private String returnMsg;
	private String errCode;
	private String errCodeDes;
	private ArrayList<problemEntity> detail;
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
	public ArrayList<problemEntity> getDetail() {
		return detail;
	}
	public void setDetail(ArrayList<problemEntity> detail) {
		this.detail = detail;
	}

	

}
