package com.mit.entities;

import java.math.BigDecimal;


public class SMSHistory {
	private long id;
	private String provider;
	private String smsFrom;
	private String smsTo;
	private String content;
	private String type;
	private String msgId;
	private BigDecimal price;
	private long createTime;
	private long deliverTime;
	private int deliverStatus;

	public SMSHistory(long id, String provider, String smsFrom, String smsTo, String content,
			String type, String msgId, BigDecimal price, long createTime, long deliverTime,
			int deliverStatus) {
		this.id = id;
		this.provider = provider;
		this.smsFrom = smsFrom;
		this.smsTo = smsTo;
		this.content = content;
		this.type = type;
		this.msgId = msgId;
		this.price = price;
		this.createTime = createTime;
		this.deliverTime = deliverTime;
		this.deliverStatus = deliverStatus;
	}

	public SMSHistory(String provider, String smsFrom, String smsTo, String content,
			String type, String msgId, BigDecimal price) {
		this.provider = provider;
		this.smsFrom = smsFrom;
		this.smsTo = smsTo;
		this.content = content;
		this.type = type;
		this.msgId = msgId;
		this.price = price;
	}

	public SMSHistory(String provider, String smsFrom, String smsTo, String content, String type) {
		this.provider = provider;
		this.smsFrom = smsFrom;
		this.smsTo = smsTo;
		this.content = content;
		this.type = type;
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

	public String getSmsFrom() {
		return smsFrom;
	}

	public void setSmsFrom(String smsFrom) {
		this.smsFrom = smsFrom;
	}

	public String getSmsTo() {
		return smsTo;
	}

	public void setSmsTo(String smsTo) {
		this.smsTo = smsTo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}

	public int getDeliverStatus() {
		return deliverStatus;
	}

	public void setDeliverStatus(int deliverStatus) {
		this.deliverStatus = deliverStatus;
	}
}
