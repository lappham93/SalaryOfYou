package com.mit.entities.react;

public class Rating extends React{
	private long id;
	private long userId;
	private double point;
	private int status;
	
	public Rating(int objectType, long objectId, long id, long userId, double point) {
		super(objectType, objectId);
		this.id = id;
		this.userId = userId;
		this.point = point;
		this.status = 1;
	}
	
	public Rating(int objectType, long objectId, long id, long userId, double point, int status) {
		super(objectType, objectId);
		this.id = id;
		this.userId = userId;
		this.point = point;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
