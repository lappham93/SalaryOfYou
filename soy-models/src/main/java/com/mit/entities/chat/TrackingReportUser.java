/*
 * Copyright 2016 nghiatc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mit.entities.chat;

import java.util.ArrayList;
import java.util.List;

import com.mit.utils.JsonUtils;

/**
 *
 * @author nghiatc
 * @since Apr 28, 2016
 */
public class TrackingReportUser {
	public static final int NEW = 1;
    private long id;
    private int userId;
    private int reportId;
    private int roomId;
    private long timeReport;
    private int status;

    public TrackingReportUser() {
    }

    public TrackingReportUser(long id, int userId, int reportId, int roomId, long timeReport) {
        this.id = id;
        this.userId = userId;
        this.reportId = reportId;
        this.roomId = roomId;
        this.timeReport = timeReport;
        this.status = NEW;
    }
    
    public TrackingReportUser(long id, int userId, int reportId, int roomId, long timeReport, int status) {
        this.id = id;
        this.userId = userId;
        this.reportId = reportId;
        this.roomId = roomId;
        this.timeReport = timeReport;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public long getTimeReport() {
        return timeReport;
    }

    public void setTimeReport(long timeRepoprt) {
        this.timeReport = timeRepoprt;
    }
    
    public int getStatus() {
    	return status;
    }
    
    public void setStatus(int status) {
    	this.status = status;
    }

    @Override
    public String toString() {
        return "TrackingReportUser{" + "id=" + id + ", userId=" + userId + ", reportId=" + reportId + ", roomId=" + roomId + ", timeReport=" + timeReport + '}';
    }
    
    public JsonBuilder buildJson() {
		return new JsonBuilder(this);
	}
    
    public static class JsonBuilder {
        private long id;
        private int userId;
        private int reportId;
        private int roomId;
        private long timeReport;
        private int status;

        public JsonBuilder(TrackingReportUser t) {
            this.id = t.id;
            this.userId = t.userId;
            this.reportId = t.reportId;
            this.roomId = t.roomId;
            this.timeReport = t.timeReport;
            this.status = t.getStatus();
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getReportId() {
            return reportId;
        }

        public void setReportId(int reportId) {
            this.reportId = reportId;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public long getTimeReport() {
            return timeReport;
        }

        public void setTimeReport(long timeReport) {
            this.timeReport = timeReport;
        }
        
        public int getStatus() {
        	return status;
        }
        
        public void setStatus(int status) {
        	this.status = status;
        }

        @Override
        public String toString() {
            return "JsonBuilder{" + "id=" + id + ", userId=" + userId + ", reportId=" + reportId + ", roomId=" + roomId + ", timeReport=" + timeReport + '}';
        }
        
        
    }

    /* 
     * @params: trus has been sorted with userId field 
     */
    public static List<TrackingReportMerge> buildMerge(List<TrackingReportUser> trus) {
    	TrackingReportUser context = new TrackingReportUser();
		List<TrackingReportMerge> rs = new ArrayList<>();
		if (trus != null) {
			int reports = 0;
			TrackingReportMerge trm = null;
			for (int idx = 0; idx < trus.size(); idx++) {
				TrackingReportUser tru = trus.get(idx);
				if (idx - 1 < 0 || trus.get(idx - 1).getUserId() != tru.getUserId()) {
					trm = context.new TrackingReportMerge();
					trm.setUserId(tru.getUserId());
				}
				trm.addTracking(tru.getId());
				reports ++;
				
				if (idx + 1 >= trus.size() || trus.get(idx + 1).getUserId() != tru.getUserId()) {
					trm.setNumReport(reports);
					reports = 0;
					rs.add(trm);
				}
			}
		}
		return rs;
	}
    
    public class TrackingReportMerge{
    	private int userId;
    	private List<Long> trackingIds;
    	private int numReport;
    	
    	public TrackingReportMerge(){}
    	
    	public void setUserId(int userId) {
    		this.userId = userId;
    	}
    	
    	public void setNumReport(int numReport) {
    		this.numReport = numReport;
    	}
    	
    	public void addTracking(long trackingId) {
    		if (this.trackingIds == null) {
    			this.trackingIds = new ArrayList<>();
    		}
    		if (!this.trackingIds.contains(trackingId)) {
    			this.trackingIds.add(trackingId);
    		}
    	}
    	
    	public int getUserId() {
    		return userId;
    	}
    	
    	public List<Long> getTrackingIds() {
    		return trackingIds;
    	}
    	
    	public int getNumReport() {
    		return numReport;
    	}
    	
    	@Override
    	public String toString() {
    		return "userId=" + userId + ", trackingIds=" + JsonUtils.Instance.toJson(trackingIds) + ", numReport=" + numReport;
    	}
    	
    }
}
