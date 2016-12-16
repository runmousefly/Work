package com.cspaying.shanfu.ui.entit;


public class SubStatisticsEntity {
	
	private String itemCode;//支付类型代码
	private String itemName;//支付类型名称
	private int totalNumByItem;//笔数
	private double totalAmountByItem;//金额
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getTotalNumByItem() {
		return totalNumByItem;
	}
	public void setTotalNumByItem(int totalNumByItem) {
		this.totalNumByItem = totalNumByItem;
	}
	public double getTotalAmountByItem() {
		return totalAmountByItem;
	}
	public void setTotalAmountByItem(double totalAmountByItem) {
		this.totalAmountByItem = totalAmountByItem;
	}

}
