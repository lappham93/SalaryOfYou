package com.mit.entities.social;

import java.util.ArrayList;
import java.util.List;

public class FeedReport {
	
	public static enum ReportStatus{
		NEW(1),
		REVIEWED(0);
		
		private int value;
		
		private ReportStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public static ReportStatus getReportStatusType(int value) {
			for (ReportStatus status : ReportStatus.values()) {
				if (status.getValue() == value) {
					return status;
				}
			}
			return null;
		}
	}
	
	private long id;
	private long feedId;
	private int reports;
	private List<Long> userReportIds;
	private int status;
	private long createTime;
	private long updateTime;
	
	public FeedReport(long id, long feedId, int reports, List<Long> userReportIds, int status, long createTime, long updateTime) {
		this.id = id;
		this.feedId = feedId;
		this.reports = reports;
		this.userReportIds = userReportIds;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
	public FeedReport(long id, long feedId) {
		this.id = id;
		this.feedId = feedId;
		this.reports = 0;
		this.status = ReportStatus.NEW.getValue();
	}
	
	public void addReport(long userId) {
		if (this.userReportIds == null) {
			this.userReportIds = new ArrayList<Long>();
		}
		if (!this.userReportIds.contains(userId)) {
			this.userReportIds.add(userId);
			this.reports ++;
		}
	}
	
	public void changeStatus(int status) {
		if (ReportStatus.getReportStatusType(status) != null) {
			this.status = status;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFeedId() {
		return feedId;
	}

	public void setFeedId(long feedId) {
		this.feedId = feedId;
	}

	public int getReports() {
		return reports;
	}

	public void setReports(int reports) {
		this.reports = reports;
	}

	public List<Long> getUserReportIds() {
		return userReportIds;
	}

	public void setUserReportIds(List<Long> userReportIds) {
		this.userReportIds = userReportIds;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
