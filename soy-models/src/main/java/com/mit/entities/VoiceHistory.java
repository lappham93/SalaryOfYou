package com.mit.entities;

import java.math.BigDecimal;

public class VoiceHistory {
	private long id;
	private String provider;
	private String from;
	private String to;
	private String type;
	private String callId;
	private BigDecimal price;
	private int duration;
	private long requestTime;
	private long startTime;
	private long endTime;
	private String callStatus;
	private long createTime;
	private long updateTime;
	
	public VoiceHistory() {}
	
	public VoiceHistory(String provider, String from, String to, String type, String callId) {
		this.provider = provider;
		this.from = from;
		this.to = to;
		this.type = type;
		this.callId = callId;
	}

	public VoiceHistory(long id, String provider, String from, String to, String type, String callId, BigDecimal price,
			int duration, long requestTime, long startTime, long endTime, String callStatus, long createTime, long updateTime) {
		this.id = id;
		this.provider = provider;
		this.from = from;
		this.to = to;
		this.type = type;
		this.callId = callId;
		this.price = price;
		this.duration = duration;
		this.requestTime = requestTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.callStatus = callStatus;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	
}
