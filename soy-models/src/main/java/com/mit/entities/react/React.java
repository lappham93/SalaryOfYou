package com.mit.entities.react;

public class React {
	private int objectType; // refer ObjectType enum
	private long objectId;  // id of object (product, booth, comment)
	
	public React(int objectType, long objectId) {
		super();
		this.objectType = objectType;
		this.objectId = objectId;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	
}
