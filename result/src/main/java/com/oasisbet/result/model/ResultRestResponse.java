package com.oasisbet.result.model;

import java.util.List;

public class ResultRestResponse extends StatusResponse {
	private List<ResultEvent> resultEvent;

	public List<ResultEvent> getResultEvent() {
		return resultEvent;
	}

	public void setResultEvent(List<ResultEvent> resultEvent) {
		this.resultEvent = resultEvent;
	}
}
