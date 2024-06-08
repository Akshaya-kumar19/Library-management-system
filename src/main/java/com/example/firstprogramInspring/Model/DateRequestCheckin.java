package com.example.firstprogramInspring.Model;

public class DateRequestCheckin {
	private String min_date;
	private String max_date;
	public String getMin_date() {
		return min_date;
	}
	public void setMin_date(String min_date) {
		this.min_date = min_date;
	}
	public String getMax_date() {
		return max_date;
	}
	public void setMax_date(String max_date) {
		this.max_date = max_date;
	}
	@Override
	public String toString() {
		return "Date_Request_checkin [min_date=" + min_date + ", max_date=" + max_date + "]";
	}
	
	
	
}
