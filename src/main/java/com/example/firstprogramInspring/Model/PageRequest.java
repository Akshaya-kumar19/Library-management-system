package com.example.firstprogramInspring.Model;

public class PageRequest {
	
	private int pageNo;
	private int pageLimit;
	private String searchUsingColumn;
	private String searchelement;
	private String sortingOrder;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}
	
	public String getSearchUsingColumn() {
		return searchUsingColumn;
	}
	public void setSearchUsingColumn(String searchUsingColumn) {
		this.searchUsingColumn = searchUsingColumn;
	}
	public String getSearchelement() {
		return searchelement;
	}
	public void setSearchelement(String searchelement) {
		this.searchelement = searchelement;
	}
	public String getSortingOrder() {
		return sortingOrder;
	}
	public void setSortingOrder(String sortingOrder) {
		this.sortingOrder = sortingOrder;
	}
	
	@Override
	public String toString() {
		return "PageRequest [pageNo=" + pageNo + ", pageLimit=" + pageLimit + ", searchUsingColumn=" + searchUsingColumn
				+ ", searchelement=" + searchelement + ", sortingOrder=" + sortingOrder + "]";
	}
	
}
