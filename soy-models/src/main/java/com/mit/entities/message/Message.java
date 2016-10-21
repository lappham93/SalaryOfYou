package com.mit.entities.message;

import com.mit.entities.user.UserInfo;
import com.mit.utils.LinkBuilder;

public class Message {
	private long id;
	private int srcId;
	private int type;
	private String content;
	private long createTime;
	private long updateTime;

	public Message(long id, int srcId, int type, String content) {
		this.id = id;
		this.srcId = srcId;
		this.type = type;
		this.content = content;
	}

	public Message(long id, int srcId, int type, String content,
			long createTime, long updateTime) {
		this.id = id;
		this.srcId = srcId;
		this.type = type;
		this.content = content;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Message(Message msg) {
		this.id = msg.getId();
		this.srcId = msg.getSrcId();
		this.type = msg.getType();
		this.content = msg.getContent();
		this.createTime = msg.getCreateTime();
		this.updateTime = msg.getUpdateTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSrcId() {
		return srcId;
	}

	public void setSrcId(int srcId) {
		this.srcId = srcId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public MessageClient buildMessageClient() {
		return new MessageClient(this);
	}

	public class MessageClient extends Message {
		private String firstName;
		private String lastName;

		public MessageClient(Message msg) {
			super(msg);
		}

		public String getFirstName() {
			return firstName;
		}
		
		public String getLastName() {
			return lastName;
		}

		public MessageClient addInfo(UserInfo info) {
			firstName = info.getFirstName();
			lastName = info.getLastName();
			return this;
		}
	}
}
