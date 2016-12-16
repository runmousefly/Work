package com.cspaying.shanfu.ui.entit;

import java.util.List;

public class Statistics1Entity {

	private String type;// 定期类型
	private int totalNum;// 总笔数
	private double totalAmount;// 总金额
	private List<SubStatisticsEntity> subDetail;
	
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SubStatisticsEntity> getSubDetail() {
		return subDetail;
	}

	public void setSubDetail(List<SubStatisticsEntity> subDetail) {
		this.subDetail = subDetail;
	}

}
