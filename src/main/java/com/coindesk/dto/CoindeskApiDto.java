package com.coindesk.dto;

import java.util.Map;

import lombok.Data;

@Data
public class CoindeskApiDto {

	private Time time;

	private String disclaimer;

	private String chartName;

	private Map<String, BpiInfo> bpi;

	@Data
	public static class Time {
		private String updated;
		private String updatedISO;
		private String updateduk;
	}

	@Data
	public static class BpiInfo {
		private String code;
		private String symbol;
		private String rate;
		private String description;
		private Double rate_float;
	}
}
