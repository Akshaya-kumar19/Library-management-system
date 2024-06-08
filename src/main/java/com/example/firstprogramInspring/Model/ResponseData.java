package com.example.firstprogramInspring.Model;

public class ResponseData<T> {
	private String msg;
	private String status;
	private int count;
	private T data;
	@Override
	public String toString() {
		return "ResponseData [msg=" + msg + ", status=" + status + ", data=" + data + "]";
	}
	public ResponseData(String msg, String status,T data, int count) {
		super();
		this.msg = msg;
		this.status = status;
		this.data = data;
		this.count = count;
	}
	public ResponseData(String msg, String status) {
		super();
		this.msg = msg;
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
