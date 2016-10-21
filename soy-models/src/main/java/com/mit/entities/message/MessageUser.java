package com.mit.entities.message;

import java.util.LinkedList;
import java.util.List;

public class MessageUser {
	private int uid;
	private List<MessageStatus> msgIds;

	public MessageUser() {
		super();
	}

	public MessageUser(int uid, List<MessageStatus> msgIds) {
		super();
		this.uid = uid;
		this.msgIds = msgIds;
	}

	public MessageUser(int uid) {
		super();
		this.uid = uid;
		this.msgIds = new LinkedList<MessageStatus>();
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public List<MessageStatus> getMsgIds() {
		return msgIds;
	}

	public void setMsgIds(List<MessageStatus> msgIds) {
		this.msgIds = msgIds;
	}

	public void addMessage(MessageStatus msgStt) {
		if(msgIds == null) {
			msgIds = new LinkedList<MessageStatus>();
		}

		msgIds.add(msgStt);
	}

}
