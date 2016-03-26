package com.epsilon.LeakHawk.db;

import java.util.List;

public class DBTransaction<T> {

	public static int SUCCESS = 1;

	public static int FAILED = -1;

	private List<T> resultList;

	private int status;
	private String message;

	public DBTransaction() {

	}

	public DBTransaction(int status, String msg) {
		this.status = status;
		this.message = msg;
	}

	public boolean isSuccessful() {
		if (this.status > 0) {
			return true;
		}
		return false;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

}
