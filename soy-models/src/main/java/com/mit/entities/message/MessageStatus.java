package com.mit.entities.message;

import org.bson.Document;

public class MessageStatus {
	private long id;
	private byte status;

	public MessageStatus() {}

	public MessageStatus(long id, byte status) {
		super();
		this.id = id;
		this.status = status;
	}

	public MessageStatus(Document doc) {
		this.id = doc.getLong("id");
		this.status = doc.getInteger("status").byteValue();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Document toDocument() {
		return new Document("id", id).append("status", status);
	}

}
